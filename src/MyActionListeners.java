import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableRowSorter;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

/**
 @author Richard Luby, Copyright 2013 */

/** this class contains all the action listeners for the program */
public class MyActionListeners{
	/** current mainFrame of the program */
	private static MainFrame mainFrame;

	/** initializes the static variables for the class */
	public static void initializeStaticVariables(MainFrame mf){
		mainFrame = mf;
	}

	/** creates a windowListener that calls controller to write the data to the
	 * disk */
	public static WindowListener createMainFrameListener(){
		return new WindowListener(){

			@Override public void windowOpened(WindowEvent e){}

			/** writes the contents to disk as the program is closing */
			@Override public void windowClosing(WindowEvent e){
				// TODO Auto-generated method stub
				mainFrame.getController().writeDataToDisk();
				System.exit(0);
			}

			@Override public void windowClosed(WindowEvent e){}

			@Override public void windowIconified(WindowEvent e){}

			@Override public void windowDeiconified(WindowEvent e){}

			@Override public void windowActivated(WindowEvent e){}

			@Override public void windowDeactivated(WindowEvent e){}
		};
	}

	/** creates an actionListener that plays the currently selected movie with
	 * the user's default program
	 * @return returns an actionListener that plays the currently selected movie
	 *         with the user's default program */
	public static ActionListener createNewPlayButtonListener(){
		return listener -> {
			try{
				Desktop.getDesktop().open(mainFrame.getController().getSelectedMovie().getMovieFile());
			} catch (IOException e){
				e.printStackTrace();
			}
		};
	}

	/** returns an ActionListener that allows the user to edit the selected movie
	 * @param infoPanel the panel that displays information about each movie
	 * @return returns an ActionListener that allows the user to edit the
	 *         selected movie */
	public static ActionListener createNewEditButtonListener(final InfoPanel infoPanel){
		return listener -> infoPanel.editDisplayedData();
	}

	/** creates an actionListener that allows the user to save any changes made
	 * to the movie in edit mode
	 * @param infoPanel the current panel holding the data */
	public static ActionListener createSaveMovieChangesListener(final InfoPanel infoPanel){
		return arg0 -> infoPanel.saveMovieInformation();
	}

	/** function to return a focus listener that updates the search field
	 * @return returns a FocusListener that sets the text and foreground color
	 *         of the search box */
	public static FocusListener createSearchBarUITweakListener(){
		return new FocusListener(){
			@Override public void focusGained(FocusEvent e){
				JTextField field = ((JTextField) e.getSource());
				if (field.getText().equals("Search...")){
					field.setForeground(Color.BLACK);
					field.setText("");
				}
			}


			@SuppressWarnings("unchecked") @Override public void focusLost(FocusEvent e){
				JTextField field = (JTextField) e.getSource();
				if (field.getText().equals("")){
					field.setText("Search...");
					field.setForeground(Color.LIGHT_GRAY);
					((TableRowSorter<MovieTable>) mainFrame.getMovieTable().getRowSorter())
							.setRowFilter(null); //reset the row sorter to show all rows
				}
			}
		};
	}

	/** function to return a KeyAdapter to filter the table
	 * @return returns a KeyAdapter to search through the titles column */
	public static KeyAdapter createKeyFilterListener(){
		return new KeyAdapter(){
			@SuppressWarnings("unchecked") @Override public void keyReleased(KeyEvent e){
				JTextField field = (JTextField) e.getSource();
				if (!field.getText().equals("") && field.hasFocus()){
					mainFrame.updateFilter(field.getText());
				} else if (field.getText().equals("")){
					((TableRowSorter<MovieTable>) mainFrame.getMovieTable().getRowSorter())
							.setRowFilter(null); //reset the row sorter to show all rows
				}
			}
		};
	}

	/** creates a mouse listener that updates the currently displayed movie
	 * @return returns a mouse listener that updates the currently displayed
	 *         movie */
	public static MouseListener createUpdateCurrentMovieMouseListener(){
		return new MouseListener(){

			@Override public void mouseClicked(MouseEvent arg0){
				JTable movieTable = mainFrame.getMovieTable();
				int    row        = movieTable.convertRowIndexToModel(movieTable.getSelectedRow());
				mainFrame.getController().setSelectedMovie(row);
				mainFrame.updateSelectedMovie();
			}

			@Override public void mousePressed(MouseEvent arg0){}

			@Override public void mouseReleased(MouseEvent arg0){}

			@Override public void mouseEntered(MouseEvent arg0){}

			@Override public void mouseExited(MouseEvent arg0){}
		};
	}

	/** creates a key listener that updates the currently displayed movie
	 * @return returns a key listener that updates the currently displayed movie */
	public static KeyListener createUpdateCurrentMovieKeyListener(){
		// TODO Auto-generated method stub
		return new KeyListener(){

			@Override public void keyTyped(KeyEvent arg0){}

			@Override public void keyPressed(KeyEvent arg0){}

			@Override public void keyReleased(KeyEvent arg0){
				JTable movieTable = mainFrame.getMovieTable();
				int row = movieTable.convertRowIndexToModel(movieTable.getSelectedRow());
				mainFrame.getController().setSelectedMovie(row);
				mainFrame.updateSelectedMovie();

			}
		};
	}

	/** this class deletes the currently active movie in the info panel */
	public static class DeleteMovie implements ActionListener{
		/** the panel that holds the movie information */
		private InfoPanel infoPanel;

		/** creates a new ActionListener that deletes a movie
		 * @param iF the panel that contains the movie information */
		public DeleteMovie(InfoPanel iF){
			infoPanel = iF;
		}

		/** @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent) */
		@Override public void actionPerformed(ActionEvent e){
			infoPanel.deleteMovie();
		}

	}
}
