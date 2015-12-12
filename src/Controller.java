import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**@author Richard Luby, Copyright 2013*/
/** this class contains the data for the program to run; methods build and
 * maintain the used database */
public class Controller {
	/** string to determine OS slash style */
	final public static String WINDOWS_SLASH = "\\", NORMAL_SLASH = "/";
/** data path to the movie information, with a trailing separator */
final private String MOVIE_DATA_PATH;
	/** the main application window */
	private MainFrame mainFrame;
	/** an arraylist to hold the array of movies in memory */
	private ArrayList<Movie> movieList;
	/** the currently selected movie */
	private Movie selectedMovie;
/** the directory wherein the user's movies are stored, as read from the
	 * prefs file */
	private String movieDirectory;
	/** the preferences for this application */
	private Preferences prefs;
	/** the general movie formats */
	private HashSet<String> movieFormats;
	/** boolean to determine if the user wishes to automatically import movies
	 * into the movie directory */
	private boolean autoImport;
	
	/** creates a controller that is able to see the main application window *
	 * @param mf the main application window of the program
	 */
	public Controller(MainFrame mf) throws InputMismatchException {
		mainFrame = mf;
		autoImport = false;
		String pathToCurrentLocation = "";
		try {
			pathToCurrentLocation = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath() + "";
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		MOVIE_DATA_PATH = pathToCurrentLocation.substring(0, pathToCurrentLocation.lastIndexOf(File.separator) + 1) + "bin" + File.separator + "data" + File.separator;
		movieFormats = new HashSet<String>(40);//initial capacity of 40
		fillMovieFormats();
		checkFileSystem();
		int errorCounter = 0;
		while (!populateArrayList(movieList)) {
			errorCounter++;
			if (errorCounter >= 5) { throw new InputMismatchException("The data could not be initialized."); }
		}
	}
	/** fills the movie formats that most computers can read */
	private void fillMovieFormats(){
		BufferedReader fileStream = null;
		File directory = null;
		try {
			directory = new File(
					Preferences.class.getProtectionDomain().getCodeSource().getLocation()
					.toURI());

			directory.setReadable(true);
			directory.setWritable(true);
			fileStream = new BufferedReader((new FileReader(directory + File.separator + "fileExtensions")));
		
		//StringBuffer fileContents = new StringBuffer();
		
			String tempLine = "";
			while (fileStream.ready()) {
				tempLine = fileStream.readLine();
				movieFormats.add(tempLine.toLowerCase());
			}
		} catch (IOException | URISyntaxException e){
			e.printStackTrace();
		} finally {
			try {
				if (fileStream != null){
					fileStream.close();
				}
			} catch (IOException ex) {
				Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

/**
 this method checks the file system to see if the correct structure has
 been set up; if not, it creates the structure used by the program
 */
private void checkFileSystem(){
	prefs = new Preferences(this);
	File dataDirectory = new File("bin", "data");
	checkExistence(dataDirectory, true);//true is used to check for directory
}

/**
 checks if the directory exists; if not, creates the directory, or reports
 an error otherwise

 @param directory         the directory whose existence to check for
 @param checkForDirectory checks for a directory if true, checks for a
 file if false

 @return returns true if the file/directory now exists */
public boolean checkExistence(File directory, boolean checkForDirectory){
	boolean doesExist = true;
	try{
		if (checkForDirectory){
			if (!directory.exists() && !directory.mkdir()){
				//check if it exists; if not, make it; utilizes Java's shortcut boolean operations
				JOptionPane.showMessageDialog(null,
				                              "There was an error creating " + directory.getCanonicalPath() + ".",
				                              "Error",
				                              JOptionPane.ERROR_MESSAGE);
				doesExist = false;
			}
		} else {
			if (!directory.exists() && !directory.createNewFile()){//check if it exists; if not, make it
				JOptionPane.showMessageDialog(null,
				                              "There was an error creating " + directory.getCanonicalPath() + ".",
				                              "Error",
				                              JOptionPane.ERROR_MESSAGE);
				doesExist = false;
			}
		}
	} catch (IOException | NullPointerException e){
		e.printStackTrace();
	}
	return doesExist;
}

/**
 reads in the data from the disk for each movie and places it in the array
 list

 @return returns true if the operation was successful */
private boolean populateArrayList(ArrayList<Movie> ml){
	ml = new ArrayList<Movie>(50); //start with an initial capacity of 50
	File[] movieFiles = prefs.getMainDataDirectory().listFiles();
	Logger.getLogger("Movies.Controller.PopulateArrayList")
	      .config("Data Directory: " + prefs.getMainDataDirectory().getAbsolutePath());
	String movieNameWithExtension = "";
	Movie  movie                  = null;
	String extension              = "";
	String lastTag                = "";
	File   movieDataFile          = null;
	BufferedReader fileReader = null;
	try{
		for (int i = 0; i < movieFiles.length; i++){//loop to handle adding the file data for each movie file
			if (movieFiles[i].isFile() && !movieFiles[i].isHidden()){//make sure to only check actual files
				movieNameWithExtension = movieFiles[i].getName();
				extension = movieNameWithExtension.substring(movieNameWithExtension.lastIndexOf('.') + 1).toLowerCase();
				if (movieFormats.contains(extension)){//check if this format is in the movie list
					movie = new Movie(movieNameWithExtension.substring(0, movieNameWithExtension.lastIndexOf('.')));
					movie.setMovieFile(movieFiles[i]);
					movieDataFile = new File(MOVIE_DATA_PATH + movie.getTitle() + File.separator + movie.getTitle() + ".dat");//create file to movie information
					movie.setDataFile(movieDataFile);
					if (checkExistence(new File(MOVIE_DATA_PATH + movie.getTitle()),
					                   true) && checkExistence(movieDataFile, false)){
						//checks for the file path directories, and then checks for the file itself, creating them if necessary
						try{
							fileReader = new BufferedReader(new FileReader(movieDataFile));
							while (fileReader.ready()){
								lastTag = parseDataLine(fileReader.readLine(), movie,
								                        lastTag);//parses the line and updates the movie
							}
						} catch (IOException e){
							e.printStackTrace();
						} finally{
							try{
								fileReader.close();
							} catch (IOException e){
								e.printStackTrace();
							}
						}
					}
					ml.add(movie);
				}
			}
		}
	} catch (NullPointerException e){
		prefs.chooseMovieFolder();
		return false;
	}
	movieList = ml;
	selectedMovie = movieList.get(0);
	return true;
}

/**
 parses the data line passed in, applying it to the correct field in the
 movie

 @param dataLine the data to be parsed
 @param movie    the movie in which to place the information
 @param lastTag  the tag found from the previous run

 @return returns the last tag found */
private String parseDataLine(String dataLine, Movie movie, String lastTag){
	if (dataLine.startsWith(Movie.TAG.TITLE + "")){
		movie.setTitle(dataLine.substring((Movie.TAG.TITLE + "").length()));
		return Movie.TAG.TITLE + "";
	} else if (dataLine.startsWith(Movie.TAG.CERT_RATINGS + "")){
		movie.setRatingCertifications(dataLine.substring((Movie.TAG.CERT_RATINGS + "").length()));
		return Movie.TAG.CERT_RATINGS + "";
	} else if (dataLine.startsWith(Movie.TAG.FILE_PATH + "")){
		//movie.setMovieFile(new File(dataLine.substring((Movie.TAG.FILE_PATH + "").length())));
		return Movie.TAG.FILE_PATH + "";
	} else if (dataLine.startsWith(Movie.TAG.MPAA_RATING + "")){
		movie.setAgeRating(dataLine.substring((Movie.TAG.MPAA_RATING + "").length()));
		return Movie.TAG.MPAA_RATING + "";
	} else if (dataLine.startsWith(Movie.TAG.NOTES + "")){
		movie.setUserNotes(dataLine.substring((Movie.TAG.NOTES + "").length()));
		return Movie.TAG.NOTES + "";
	} else if (dataLine.startsWith(Movie.TAG.SYNOPSIS + "")){
		movie.setSynopsis(dataLine.substring((Movie.TAG.SYNOPSIS + "").length()));
		return Movie.TAG.SYNOPSIS + "";
	} else if (dataLine.startsWith(Movie.TAG.USER_RATING + "")){
		movie.setUserRating(Double.parseDouble(dataLine.substring((Movie.TAG.USER_RATING + "").length())));
		return Movie.TAG.USER_RATING + "";
	} else if (lastTag.equals(Movie.TAG.SYNOPSIS + "")){
		movie.setSynopsis(movie.getSynopsis() + "\n" + dataLine);//allows multi-line synopsis
	} else if (lastTag.equals(Movie.TAG.NOTES + "")){
		movie.setUserNotes(movie.getUserNotes() + "\n" + dataLine);//allows multi-line user notes
	}
	return "";
}
	
	/** returns the movie at the selected index
	 * @param i the index at which to get the movie
	 * @return returns the movie at the selected index */
	public Movie getMovie(int i){
		return movieList.get(i);
	}

	/** returns the movie with the selected title
	 * @param title the title of the movie to find
	 * @return returns the movie with the matching title, or null if no match
	 *         was found */
	public Movie getMovie(String title){
		for (Movie mov : movieList) {
			if (mov.getTitle().equals(title)) { return mov; }
		}
		return null;
	}

	/** returns the number of movies
	 * @return returns the current number of movies */
	public int getNumMovies(){
		return movieList.size();
	}

	/** replaces the movie at the specified index
	 * @param i the index at which to replace this movie
	 * @param mov the movie with which to replace the current one */
	public void replaceMovie(int i, Movie mov){
		movieList.remove(i);
		movieList.add(i, mov);
	}

/** writes relevant files to the disk, including updates to the database and
	 * prefs file */
	public void writeDataToDisk(){
		if (prefs.changed()) {
			prefs.writePrefsFile();
			
		}
		for (int i = 0; i < movieList.size(); i++) {
			if (movieList.get(i).wasChanged()) {
				writeMovie(i);
			}
		}
	}

	/** writes the selected movie to the disk; ************NOTE************ the
	 * line is commented out
	 * @param i the index at which to find the movie to write to disk */
	public void writeMovie(int i){
		movieList.get(i).writeMovieToDisk();
	}

/** returns the currently selected movie
	 * @return returns the currently selected movie */
	public Movie getSelectedMovie(){
		return selectedMovie;
	}

/**
 sets the currently selected movie

 @param mov the movie to set as the current selection
 */
public void setSelectedMovie(Movie mov){
	selectedMovie = mov;
}

	/** sets the selected movie to the chosen index
	 * @param i the index at which to set the movie */
	public void setSelectedMovie(int i){
		selectedMovie = movieList.get(i);
	}

	/** deletes the specified movie from the disk, and any associated files
	 * @param currentMovie the movie to be deleted from the disk */
	public void deleteFromDisk(Movie currentMovie){
		movieList.remove(currentMovie);
		currentMovie.deleteFromDisk();
	}
}
