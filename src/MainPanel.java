import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;

/**@author Richard Luby, Copyright 2013*/
/** this class holds the table of movies on the left, and the movie information
 * on the right */
public class MainPanel extends JPanel {
    /** the main application window */
    private MainFrame mainFrame;
    /** the current controller for the progra */
    private Controller controller;
    /** the table containing the data */
    private JTable movieTable;
    /** the current movie being displayed */
    private Movie currentMovie;
    /** the JPanel in charge of displaying information about each movie */
    private InfoPanel infoPanel;
    /** panel containing the table of books */
    private TablePanel tablePanel;
    /** returns a main panel with a table on the left and an info panel on the
     * right Create the application. */
    public MainPanel(MainFrame mf) {
        mainFrame = mf;
        controller = mainFrame.getController();
        currentMovie = controller.getMovie(0);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JSplitPane splitPane = new JSplitPane();
        splitPane.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        add(splitPane);
        add(Box.createRigidArea(new Dimension(10, 10)));

        JPanel tablePanel = setupTablePanel();
        splitPane.setLeftComponent(tablePanel);

        infoPanel = new InfoPanel(mainFrame);
        JScrollPane infoScroller = new JScrollPane(infoPanel);
        infoScroller.setViewportView(infoPanel);
        splitPane.setRightComponent(infoScroller);

    }
    /** sets up the table panel on the left side of the screen
     * @return returns a formatted table panel */
    private JPanel setupTablePanel(){
        tablePanel = new TablePanel(mainFrame);
        movieTable = tablePanel.getMovieTable();
        //JScrollPane scroller = new JScrollPane(movieTable);
        //tablePanel.add(scroller);
        return tablePanel;
    }
    /** sets the mainFrame for this panel; should be used to ensure up to date
     * data
     * @param mf the mainFrame to set */
    public void setMainFrame(MainFrame mf){
        mainFrame = mf;
        controller = mainFrame.getController();
        infoPanel.setMainFrame(mainFrame);
        tablePanel.setMainFrame(mainFrame);
    }
    /** returns the data table for this class
     * @return returns the table containing the movies */
    public JTable getMovieTable(){
        return tablePanel.getMovieTable();
    }
    /** sets the current movie and updates the display */
    public void updateCurrentMovie(){
        infoPanel.setCurrentMovie(mainFrame.getController().getSelectedMovie());
    }
}
