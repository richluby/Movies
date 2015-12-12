import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 @author Richard Luby, Copyright 2013 */

/** this class contains data pertinent to each movie object */
public class Movie{
	/**
	 tag to determine what this line is in the file
	 */
	protected enum TAG{
		TITLE, MPAA_RATING, USER_RATING, SYNOPSIS, CERT_RATINGS, NOTES, FILE_PATH, SERIES, SEASON, SERIAL_ORDER
	}

	/**
	 the list of available ratings for movies
	 */
	final public static String[] MPAA_RATINGS = new String[]{"G", "PG", "PG-13", "R", "NC-17"};
	/**
	 the title of the movie
	 */
	private String title;
	/**
	 the official MPAA rating of the movie
	 */
	private String ageRating;
	/**
	 the integer representation of the MPAA rating for the movie
	 */
	private int intAgeRating;
	/**
	 the user rating (1-5) of the movie
	 */
	private double userRating;
	/**
	 the synopsis of the movie
	 */
	private String synopsis;
	/**
	 the certifications for why it is rated this way by the MPAA
	 */
	private String ratingCertifications;
	/**
	 miscellaneous notes the user may want to use
	 */
	private String userNotes;
	/**
	 file to contain the movie
	 */
	private File movieFile;
	/**
	 file to contain the data relevant to this movie
	 */
	private File dataFile;
	/**
	 the series of this movie
	 */
	private String series;
	/**
	 the season of the this movie
	 */
	private String season;
	/**
	 the 0-based serial order of this movie in the series or season
	 */
	private int serialOrder;
	/**
	 boolean to determine if the user changed this movie file
	 */
	private boolean movieWasChanged;

	/**
	 initializes the title of the movie to the user input; everything else is
	 put at blank.
	 @param t the title of the movie*/
	public Movie(String t){
		this();
		title = t;
	}

	/**
	 returns a blank movie, with all values initiated to "" or 0
	 */
	public Movie(){
		movieFile = null;
		dataFile = null;
		title = "";
		ageRating = "";
		intAgeRating = 0;
		userRating = 0;
		synopsis = "";
		ratingCertifications = "";
		userNotes = "";
		movieWasChanged = false;
		season = "";
		series = "";
		serialOrder = 0;
	}

	/**
	 @return returns the serial order of the movie in the season or series */
	public int getSerialOrder(){
		return serialOrder;
	}

	/**
	 @param serialOrder the 0-based index of this movie in the series or season of this movie */
	public void setSerialOrder(int serialOrder){
		this.serialOrder = serialOrder;
	}

	/**
	 @return returns the name of the season to which this movie belongs */
	public String getSeason(){
		return season;
	}

	/**
	 @param season the season in which this movie belongs */
	public void setSeason(String season){
		this.season = season;
	}

	/**
	 get the title of the movie
	 @return the title of the movie */
	public String getTitle(){
		return title;
	}

	/**
	 set the title of the movie
	 @param title the title to set */
	public void setTitle(String title){
		this.title = title;
		//movieWasChanged(true);
	}

	/**
	 @return returns the name of the series to which this movie belongs */
	public String getSeries(){
		return series;
	}

	/**
	 @param series the series in which this movie belongs */
	public void setSeries(String series){
		this.series = series;
	}

	/**
	 returns the MPAA rating of the movie
	 @return the MPAA rating */
	public String getAgeRating(){
		return ageRating;
	}

	/**
	 sets the MPAA rating of the movie
	 @param ar the MPAA rating to set
	 */
	public void setAgeRating(String ar){
		ageRating = ar;
		for (int i = 0; i < MPAA_RATINGS.length; i++){
			if (ageRating.equals(MPAA_RATINGS[i])){
				intAgeRating = i; //also set the integer MPAA age rating
				break;
			}
		}
		//movieWasChanged(true);
	}

	/**
	 get the int representation of the MPAA ratings
	 @return returns the integer representation of the MPAA ratings */
	public int getIntMPAARating(){
		return intAgeRating;
	}

	/**
	 get the user rating
	 @return the user rating
	 */
	public double getUserRating(){
		return userRating;
	}

	/**
	 set the user rating
	 @param userRating the userRating to set */
	public void setUserRating(double userRating){
		this.userRating = userRating;
		//movieWasChanged(true);
	}

	/**
	 get the synopsis
	 @return the synopsis
	 */
	public String getSynopsis(){
		return synopsis;
	}

	/** set the synopsis
	 * @param synopsis the synopsis to set */
	public void setSynopsis(String synopsis){
		this.synopsis = synopsis;
		//movieWasChanged(true);
	}

	/** get the rating certifications
	 * @return the ratingCertifications */
	public String getRatingCertifications(){
		return ratingCertifications;
	}

	/**
	 set the rating certifications
	 @param ratingCertifications the ratingCertifications to set
	 */
	public void setRatingCertifications(String ratingCertifications){
		this.ratingCertifications = ratingCertifications;
		//movieWasChanged(true);
	}

	/**
	 get the notes submitted by the user
	 @return the notes submitted by the user
	 */
	public String getUserNotes(){
		return userNotes;
	}

	/**set the notes submitted by the user
	 * @param userNotes the notes submitted by the user
	 */
	public void setUserNotes(String userNotes){
		this.userNotes = userNotes;
		//movieWasChanged(true);
	}

	/**
	 gets the movie file
	 @return the movie File
	 */
	public File getMovieFile(){
		return movieFile;
	}

	/** sets the movie file
	 * @param movieFile the movieFile to set */
	public void setMovieFile(File movieFile){
		this.movieFile = movieFile;
		//movieWasChanged(true);
	}

	/**
	 @returns returns true if this movie is a member of a series, false otherwise
	 */
	public boolean isSeriesMember(){
		return !series.equals("");
	}

	/**
	 returns true if the movie was changed
	 @return the movieWasChanged
	 */
	public boolean wasChanged(){
		return movieWasChanged;
	}

	/**
	 set if this movie was modified
	 @param changed the movieWasChanged to set
	 */
	public void wasChanged(boolean changed){
		movieWasChanged = changed;
	}

	/**
	 returns the data file relevant to this movie, or null otherwise
	 @return the dataFile
	 */
	public File getDataFile(){
		return dataFile;
	}

	/**
	 sets the data file relevant to this movie
	 @param dataFile the dataFile to set
	 */
	public void setDataFile(File dataFile){
		this.dataFile = dataFile;
	}

	/** writes this movie to the disk in the specified location the file into
	 * which to write this movie is assumed to have been provided earlier; the
	 * file's contents are replaced, not appended */
	public boolean writeMovieToDisk(){
		boolean        writeSuccessful = true;
		BufferedWriter writer          = null;
		try{
			writer = new BufferedWriter(new FileWriter(dataFile));
			if (!title.equals("")){
				writer.append(TAG.TITLE + "" + title + "\n");
			}
			if (!ageRating.equals("")){
				writer.append(TAG.MPAA_RATING + "" + ageRating + "\n");
			}
			if (userRating != 0){
				writer.append(TAG.USER_RATING + "" + userRating + "\n");
			}
			if (!synopsis.equals("")){
				writer.append(TAG.SYNOPSIS + "" + synopsis + "\n");
			}
			if (!ratingCertifications.equals("")){
				writer.append(TAG.CERT_RATINGS + "" + ratingCertifications + "\n");
			}
			if (!series.equals("")){
				writer.append(TAG.SERIES + "" + series + "\n");
			}
			if (!season.equals("")){
				writer.append(TAG.SEASON + "" + season + "\n");
			}
			if (serialOrder != 0){
				writer.append(TAG.SERIAL_ORDER + "" + serialOrder + "\n");
			}
			if (!userNotes.equals("")){
				writer.append(TAG.NOTES + "" + userNotes + "\n");
			}
			if (!movieFile.getCanonicalPath().equals("")){
				writer.append(TAG.FILE_PATH + "" + movieFile + "\n");
			}
		} catch (IOException e){
			Logger.getLogger("Movies.Movie").warning("Failed to write information: " + e.getMessage());
			writeSuccessful = false;
		} finally{
			try{
				if (writer != null){
					writer.close();
				}
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		return writeSuccessful;
	}

	/** deletes the data file and associated data from the disk */
	public void deleteFromDisk(){
		dataFile.deleteOnExit();
		movieFile.deleteOnExit();
		String filePath = movieFile.getAbsolutePath();
		new File(filePath.substring(0, filePath.lastIndexOf("/"))).deleteOnExit();
	}
}
