/**@author Richard Luby, Copyright 2013*/

import java.util.logging.Level;
import java.util.logging.Logger;

/** This program creates and maintains a database of movies and information
 * related to each item */
public class Movies {

    /** @param args command-line arguments passed into the program */
    public static void main(String[] args){
	    Logger.getLogger("Movies").setLevel(Level.CONFIG);
	    new MainFrame();
    }

}
