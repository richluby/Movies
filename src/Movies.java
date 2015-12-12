/*******************************************************************************
 @author Richard Luby, Copyright (c) 2015.
 Permission is granted to modify or redistribute this code provided that the source is
 made available with the binaries, any contributing authors are mentioned, and no
  profit is earned.
 ******************************************************************************/

import java.util.logging.Level;
import java.util.logging.Logger;

/** This program creates and maintains a database of movies and information
 * related to each item */
public class Movies {

    /** @param args command-line arguments passed into the program */
    public static void main(String[] args){
	    if (args.length > 0){
		    if (args[0].equals("CONFIG")){
			    Logger.getLogger("Movies").setLevel(Level.CONFIG);
		    } else if (args[0].equals("INFO")){
			    Logger.getLogger("Movies").setLevel(Level.INFO);
		    } else if (args[0].equals("WARNING")){
			    Logger.getLogger("Movies").setLevel(Level.WARNING);
		    }
	    } else {
		    Logger.getLogger("Movies").setLevel(Level.SEVERE);
	    }
	    new MainFrame();
    }

}
