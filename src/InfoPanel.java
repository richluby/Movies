import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**@author Richard Luby, Copyright 2013*/
/**extends a JPanel in order to present information for the current movie*/
public class InfoPanel extends JPanel {
	/**the movie currently being displayed*/
	private Movie currentMovie;
	/**the main application window*/
	private MainFrame mainFrame;
	/** the current controller of the program */
	private Controller controller;
	/** the JLabel in charge of the title of the movie */
	private JLabel titleLabel;
	/** the font to be used with the title */
	private Font titleFont;
	/** the field containing the synopsis for the movie */
	private JTextArea synopsisArea;
	/** contains the rating information for the movie */
	private JTextArea certificationsTextArea;
	/** dislpays the MPAA rating for the movie */
	private JLabel MPAARating;
	/** JComboBox to hold the MPAA rating options */
	private JComboBox ratingOptions;
	/** JList to hold the user rating */
	private JComboBox userRatingList;
	/** set up the notes from the user */
	private JTextArea userNotes;
	/** JPanel to contain buttons that are specific to the movie information */
	private JPanel buttonPanel;
	/** JLabel to hold the user rating from the movie */
	private JLabel userRatingLabel;
	/** data font */
	private Font dataFont;
	/** button used to play the currently selected movie */
	private JButton playButton;
	/** button used to edit the currently selected movie */
	private JButton editButton;
	/** button used to save the currently selected movie */
	private JButton saveButton;
	/** button used to delete the currently selected movie */
	private JButton deleteButton;
	/**creates a new InfoPanel with the application window
	 * @param mf the main application window*/
	public InfoPanel(MainFrame mf) {
		//set initial values
		mainFrame = mf;
		controller = mainFrame.getController();
		currentMovie = controller.getSelectedMovie();
		Dimension preferredDim = mainFrame.getSize();
		preferredDim.width = (preferredDim.width / 2) - 20;
		preferredDim.height -= 60;
		setPreferredSize(preferredDim);
		//set fonts
		titleFont = new Font(Font.SERIF, Font.BOLD, 30);
		dataFont = new Font(Font.SERIF, Font.ITALIC, 18);
		//set the layout
		GridBagLayout layout = new GridBagLayout();
		//layout.preferredLayoutSize(this);
		//layout.preferredLayoutSize(this);
		setLayout(layout);
		//set up rating list
		Double[] ratings = new Double[11];
		//double counter = 0.0;
		for (int i = 0; i < (ratings.length); i++) {
			ratings[i] = i / 2.0;
		}
		userRatingList = new JComboBox(ratings);
		userRatingList.setSelectedItem(currentMovie.getUserRating());
		//userRatingList.set
		//set up the display
		initializePanel(currentMovie);
	}
	/** used to initialize the displays of the JPanel to the current movie
	 * @param mov the movie to update for the JPanel */
	public void initializePanel(Movie mov) {
		removeAll();
		int col = 1, row = 0, tGWidth = 1, tGHeight = 1, tAnchor = GridBagConstraints.CENTER, fill = GridBagConstraints.VERTICAL, txPad = 10, tyPad = 0, inset = 20;
		final int defaultTextAreaColumns = 30;
		Insets insets = new Insets(inset, 0, 0, 0);
		double tXWeight = .15, tYWeight = 0;
		GridBagConstraints gbConstraints = new GridBagConstraints(col, row, tGWidth, tGHeight, tXWeight, tYWeight, tAnchor, fill, insets,
				txPad, tyPad);
		//gbConstraints.fill = GridBagConstraints.BOTH;
		currentMovie = mov;
		//set the title label
		titleLabel = new JLabel(currentMovie.getTitle());
		titleLabel.setFont(titleFont);
		titleLabel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		add(titleLabel, gbConstraints);
		//set the synopsis section
		synopsisArea = new JTextArea(currentMovie.getSynopsis());
		synopsisArea.setColumns(defaultTextAreaColumns);
		synopsisArea.setWrapStyleWord(true);
		synopsisArea.setLineWrap(true);
		synopsisArea.setEditable(false);
		synopsisArea.setFont(dataFont);
		synopsisArea.setToolTipText("Overview of the movie");
		JScrollPane synScroller = new JScrollPane(synopsisArea);
		row = 1;//set in row 2
		col = 0;
		tGWidth = 3;
		tGHeight = 2;//extended to row 3
		tYWeight = .15;
		tXWeight = 0;
		fill = GridBagConstraints.BOTH;
		gbConstraints = new GridBagConstraints(col, row, tGWidth, tGHeight, tXWeight, tYWeight, tAnchor, fill, insets, txPad, tyPad);
		add(synScroller, gbConstraints);
		//set the ratings display
		certificationsTextArea = new JTextArea(currentMovie.getRatingCertifications());
		certificationsTextArea.setFont(dataFont);//TODO improve rating display
		certificationsTextArea.setLineWrap(true);
		certificationsTextArea.setWrapStyleWord(true);
		certificationsTextArea.setEditable(false);
		certificationsTextArea.setColumns(defaultTextAreaColumns);
		certificationsTextArea.setRows(3);
		certificationsTextArea.setToolTipText("Rating as determined by the MPAA");
		row = 4;//set in 4th row
		tGHeight = 1;
		tGWidth = 2;
		tYWeight = 0;
		col = 1;
		fill = GridBagConstraints.VERTICAL;
		//tXWeight = 0;
		gbConstraints = new GridBagConstraints(col, row, tGWidth, tGHeight, tXWeight, tYWeight, tAnchor, fill, insets, txPad, tyPad);
		add(certificationsTextArea, gbConstraints);
		//set up MPAA ratings
		ratingOptions = new JComboBox(Movie.MPAA_RATINGS);
		ratingOptions.setSelectedItem(currentMovie.getAgeRating());
		ratingOptions.setVisible(false);
		col = 0;
		tGWidth = 1;
		gbConstraints = new GridBagConstraints(col, row, tGWidth, tGHeight, tXWeight, tYWeight, tAnchor, fill, insets, txPad, tyPad);
		add(ratingOptions, gbConstraints);
		//set up MPAA label
		MPAARating = new JLabel("Rated " + currentMovie.getAgeRating());
		MPAARating.setFont(dataFont);
		add(MPAARating, gbConstraints);
		//JLabel for the user rating
		JLabel ratingLabel = new JLabel("Rating: ");
		ratingLabel.setFont(dataFont);
		ratingLabel.setToolTipText("User rating out of 5");
		row = 5; //set in 5th row
		col = 0;
		tGWidth = 1;
		gbConstraints = new GridBagConstraints(col, row, tGWidth, tGHeight, tXWeight, tYWeight, tAnchor, fill, insets, txPad, tyPad);
		add(ratingLabel, gbConstraints);
		//place in the jlist containing rating options
		col = 1;
		fill = GridBagConstraints.VERTICAL;
		tAnchor = GridBagConstraints.LINE_START;
		gbConstraints = new GridBagConstraints(col, row, tGWidth, tGHeight, tXWeight, tYWeight, tAnchor, fill, insets, txPad, tyPad);
		userRatingList.setSelectedItem(currentMovie.getUserRating());
		userRatingList.setVisible(false);
		add(userRatingList, gbConstraints);
		//place the label that contains the rating
		userRatingLabel = new JLabel(currentMovie.getUserRating() + "");
		userRatingLabel.setFont(dataFont);
		add(userRatingLabel, gbConstraints);
		//set up user notes
		userNotes = new JTextArea(currentMovie.getUserNotes());
		userNotes.setColumns(defaultTextAreaColumns);
		userNotes.setWrapStyleWord(true);
		userNotes.setLineWrap(true);
		userNotes.setEditable(false);
		userNotes.setFont(dataFont);
		userNotes.setToolTipText("General notes or opinions");
		JScrollPane noteScroller = new JScrollPane(userNotes);
		row = 6;//set in row 6
		col = 0;
		tGWidth = 3;
		tGHeight = 2;
		tYWeight = .1;
		fill = GridBagConstraints.BOTH;
		gbConstraints = new GridBagConstraints(col, row, tGWidth, tGHeight, tXWeight, tYWeight, tAnchor, fill, insets, txPad, tyPad);
		add(noteScroller, gbConstraints);
		//set up the button panel
		buttonPanel = new JPanel();
		playButton = new JButton("Play");
		playButton.addActionListener(MyActionListeners.createNewPlayButtonListener());
		buttonPanel.add(playButton, BorderLayout.EAST);
		editButton = new JButton("Edit Movie");
		editButton.addActionListener(MyActionListeners.createNewEditButtonListener(this));
		buttonPanel.add(editButton, BorderLayout.WEST);
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new MyActionListeners.DeleteMovie(this));
		buttonPanel.add(deleteButton, BorderLayout.WEST);
		row = 8;//set in row 7
		col = 0;
		tGHeight = 1;
		tYWeight = 0;
		tGWidth = 3;
		tAnchor = GridBagConstraints.CENTER;
		gbConstraints = new GridBagConstraints(col, row, tGWidth, tGHeight, tXWeight, tYWeight, tAnchor, fill, insets, txPad, tyPad);
		add(buttonPanel, gbConstraints);
		validate();
		repaint();
	}
	/** allows the user to edit the data displayed in the panel */
	public void editDisplayedData(){
		synopsisArea.setEditable(true);
		synopsisArea.setBackground(Color.GREEN);
		userRatingList.setVisible(true);
		userRatingLabel.setVisible(false);
		certificationsTextArea.setEditable(true);
		certificationsTextArea.setBackground(Color.GREEN);
		userNotes.setEditable(true);
		userNotes.setBackground(Color.GREEN);
		ratingOptions.setVisible(true);
		playButton.setVisible(false);
		editButton.setVisible(false);
		saveButton = new JButton("Save Changes");
		saveButton.addActionListener(MyActionListeners.createSaveMovieChangesListener(this));
		buttonPanel.add(saveButton, BorderLayout.CENTER);
	}
	/** updates the current movie with the new information, and then reverts to
	 * being uneditable */
	public void saveMovieInformation(){
		//set synopsis
		synopsisArea.setEditable(false);
		synopsisArea.setBackground(Color.WHITE);
		currentMovie.setSynopsis(synopsisArea.getText());
		//set user rating
		currentMovie.setUserRating((Double) userRatingList.getSelectedItem());
		userRatingList.setVisible(false);
		userRatingLabel.setText(currentMovie.getUserRating() + "");
		userRatingLabel.setVisible(true);
		//change certifications
		certificationsTextArea.setEditable(false);
		certificationsTextArea.setBackground(Color.WHITE);
		currentMovie.setRatingCertifications(certificationsTextArea.getText());
		//change MPAA rating
		currentMovie.setAgeRating((String) ratingOptions.getSelectedItem());
		ratingOptions.setVisible(false);
		MPAARating.setText("Rated " + currentMovie.getAgeRating());
		//change user notes
		currentMovie.setUserNotes(userNotes.getText());
		userNotes.setEditable(false);
		userNotes.setBackground(Color.WHITE);
		//switch buttons back
		saveButton.setVisible(false);
		playButton.setVisible(true);
		editButton.setVisible(true);
		//this movie has been modified
		currentMovie.wasChanged(true);
	}

	/**
	 sets the current mainFrame and controller
	 @param mf the application window to use
	 */
	public void setMainFrame(MainFrame mf){
		mainFrame = mf;
		controller = mainFrame.getController();
		currentMovie = controller.getSelectedMovie();
		setCurrentMovie(currentMovie);
	}

	/** allows the current movie to be changed and the display updated
	 * @param mov the movie to set as the current movie */
	public void setCurrentMovie(Movie mov){
		currentMovie = mov;
		titleLabel.setText(currentMovie.getTitle());
		synopsisArea.setText(currentMovie.getSynopsis());
		MPAARating.setText("Rated " + currentMovie.getAgeRating());
		ratingOptions.setSelectedItem(currentMovie.getAgeRating());
		certificationsTextArea.setText(currentMovie.getRatingCertifications());
		userRatingLabel.setText(currentMovie.getUserRating() + "");
		userRatingList.setSelectedItem(currentMovie.getUserRating());
		userNotes.setText(currentMovie.getUserNotes());
	}

	/** deletes the currently active movie in the infopanel */
	public void deleteMovie(){
		int response = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you wish to delete " + currentMovie.getTitle() + "?",
				"****************Confirm Delete****************",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if (response == JOptionPane.OK_OPTION) {
			controller.deleteFromDisk(currentMovie);
		}
	}
}
