import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

/**@author Richard Luby, Copyright 2013*/
/** this class contains the preferences for the application */
public class Preferences {
/** prefix to be used for the preferences file tag */
final static private String LAST_OS_TAG = "Last OS: ", DATA_DIRECTORY_TAG = "DataDir: ";
	/** the directory wherein the items are contained */
	//private String mainDataDirectory;
/** the location of the preferences file */
private String prefsFileLocation;
/**
 the file representation of the data directory. this folder contains the actual
 movie files
 */
	private File mainDataFolder;
/** boolean to determine the current OS */
private boolean currentOSisWindows;
	/** the current controller object */
	private Controller controller;
	/** boolean to determine if the preferences were modified */
	private boolean prefsChanged;
	/** returns a preferences object with data initiated using the default
	 * location and settings unless a prefs file already exists
	 * @param c the current controller for this object */
	public Preferences(Controller c) {
		controller = c;
		prefsFileLocation = "";
		setMainDataDirectory("");
		prefsChanged = false;
		//test for windows
		currentOSisWindows = System.getProperty("os.name").toLowerCase().contains("windows");
		readPrefsFile();
	}

	/** reads the preferences file from the last session; if no file exists, a
	 * new one is made with default settings */
	public void readPrefsFile(){
		File           prefsFile = null;
		BufferedReader reader    = null;
		try{
			if (!prefsFileLocation.equals("")){//in case a future implementation allows a choice
				prefsFile = new File(prefsFileLocation);
			} else {
				String pathToLocation = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()
				                            .getPath();
				prefsFile = new File((pathToLocation.substring(0, pathToLocation
						                                                  .lastIndexOf(File.separator) + 1)) + buildFileDirectory(
								                                                                                                         new String[]{"bin",
								                                                                                                                      "prefs"},
								                                                                                                         false,
								                                                                                                         false));
			}
			//System.out.println(prefsFile.getAbsolutePath());
			String tempLine = "";
			boolean lastOSwasWindows = false;
			reader = new BufferedReader(new FileReader(prefsFile));
			while (reader.ready()){//loop to parse through preferences
				tempLine = reader.readLine();
				if (tempLine.startsWith(LAST_OS_TAG)){
					if (tempLine.toLowerCase().contains("windows")){
						lastOSwasWindows = true;
					}
				} else if (tempLine.startsWith(DATA_DIRECTORY_TAG)){
					String temp = tempLine.substring(DATA_DIRECTORY_TAG.length());
					if (lastOSwasWindows != currentOSisWindows){
						mainDataFolder = new File(switchSlashStyle(temp, lastOSwasWindows));
					} else {
						mainDataFolder = new File(temp);
					}
				}
			}
		} catch (FileNotFoundException e){
			if (!e.getMessage().contains("Access Denied")){
				chooseMovieFolder();
			}
			System.out.println("Caught");
			e.printStackTrace();
		} catch (IOException | URISyntaxException e){
			e.printStackTrace();
		} finally{
			try{
				if (reader != null){
					reader.close();

				}
			} catch (IOException e){
				System.out.println("Caught");
				e.printStackTrace();//occurs when the file is not found
			}
		}
	}

/** prompts the user to select the folder containing the movies */
public void chooseMovieFolder(){
	JOptionPane.showMessageDialog(null, "Please select the folder for your movies.",
	                              "Select Directory", JOptionPane.PLAIN_MESSAGE);
	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//show only directories
	if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
		mainDataFolder = fileChooser.getSelectedFile();
		prefsChanged = true;
	} else {
		JOptionPane.showMessageDialog(null, "No folders found. Exiting program", "Exiting Program",
		                              JOptionPane.PLAIN_MESSAGE);
		System.exit(0);
	}
}

/** this function adjusts the slashes in file directories to ensure they are
 * correct
 * @param string the string in which to switch the slashes
 * @param useWindowsSlash boolean to determine if the Windows style slash
 *            should be used; the assumption is made that the string has the
 *            opposite answer
 * @return returns the string with the other type of slash */
private String switchSlashStyle(String string, boolean useWindowsSlash){
	if (useWindowsSlash){
		string.replaceAll(Controller.NORMAL_SLASH,
		                  Controller.WINDOWS_SLASH);//change the slashes if the directories are wrong
	} else {
		string.replaceAll("\\" + Controller.WINDOWS_SLASH,
		                  Controller.NORMAL_SLASH);//change the slashes if the directories are wrong
	}
	return string;
}

/** function to build a string with the correct slash style for the OS
 * @param strings the array of strings to be used
 * @param useLeadingSlash boolean to determine if a leading slash should be
 *            used
 * @param useTrailingSlash boolean to determine if a trailing slash should
 *            be used
 * @return returns a string with a slash between each item in the array */
private String buildFileDirectory(String[] strings, boolean useLeadingSlash, boolean useTrailingSlash){
	StringBuilder result = new StringBuilder();
	String        slash  = File.separator;
	if (useLeadingSlash){
		result.append(slash);
	}
	for (int i = 0; i < (strings.length); i++){
		result.append(strings[i]).append(slash);
	}
	if (useTrailingSlash){
		result.append(slash);
	}
	return result.toString();
}

/** returns the main Data Directory, ie where the primary user data is stored
 * @return the main Data Directory */
public File getMainDataDirectory(){
	return mainDataFolder;
}

/** sets the main Data Directory
 * @param mainDataDirectory the main Data Directory to set */
public void setMainDataDirectory(String mainDataDirectory){
	mainDataFolder = new File(mainDataDirectory);
	mainDataFolder.setReadable(true);
	prefsChanged = true;
}

/** reads the preferences file from the last session; if no file exists, a
 * new one is made with default settings
 * @param file the preferences file to read; this file must be in the
 *            correct OS in order not to throw an exception */
public void readPrefsFile(String file){
	prefsFileLocation = file;
	readPrefsFile();
}

/** writes the preferences file to the default location */
	public void writePrefsFile(){
		BufferedWriter writer = null;
		try {
			File binDirectory = new File("bin");
			if (!binDirectory.exists() && !binDirectory.mkdir()) {
				JOptionPane.showMessageDialog(null, "There was an error saving your preferences", "Error",
				                              JOptionPane.INFORMATION_MESSAGE);
				//throw new IOException("Could not write prefs file");
			}
			File prefsFile = new File("bin", "prefs");//buildFileDirectory(new String[]{"bin", "prefs"}, false, true));
			if (!prefsFile.exists() && !prefsFile.createNewFile()){
				JOptionPane.showMessageDialog(null, "There was an error saving your preferences", "Error",
				                              JOptionPane.INFORMATION_MESSAGE);
			}
			writer = new BufferedWriter(new FileWriter(prefsFile));//may have to get directory to current jar in order to make it work correctly
			writer.write(LAST_OS_TAG + System.getProperty("os.name") + "\n");
			writer.write(DATA_DIRECTORY_TAG + mainDataFolder.getCanonicalPath() + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/** returns true if settings were changed, false otherwise
	 * @return returns true if settings were changed, false otherwise */
	public boolean changed(){
		// TODO Auto-generated method stub
		return prefsChanged;
	}
}
