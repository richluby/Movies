import java.awt.Toolkit;
import java.util.InputMismatchException;
import java.util.regex.PatternSyntaxException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

/**@author Richard Luby, Copyright 2013*/
/** this class hold the application window, which is mainly responsible for the
 * view */
public class MainFrame extends JFrame {
    /** the controller for the program */
    private Controller controller;
    /** the main panel of the application */
    private MainPanel mainPanel;
    /** instantiates the controller and sets up the application window */
    public MainFrame() {
        try {
            controller = new Controller(this);
        } catch (InputMismatchException e) {
            JOptionPane.showConfirmDialog(null,
                    "There was a critical program error. Please connect any necessary external drives and try again.",
                    "Initialization Error", JOptionPane.PLAIN_MESSAGE);
            System.exit(1);
        }
        MyActionListeners.initializeStaticVariables(this);
        addWindowListener(MyActionListeners.createMainFrameListener());
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        mainPanel = new MainPanel(this);
        //Dimension windowSize = getSize();
        //windowSize.height -= 50; //set mainpanel to be smaller than the main window
        //mainPanel.setPreferredSize(windowSize);
        add(mainPanel);
        //add(Box.createRigidArea(new Dimension(10, 10)));
        //add(new JPanel());
        setTitle("Movie Organizer");
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /** utility method to reset the application */
    public void resestApp(){
        controller = new Controller(this);
        MyActionListeners.initializeStaticVariables(this);
        mainPanel.setMainFrame(this);
        validate();
        repaint();
    }
    /** returns the current controller for the program
     * @return returns the current controller for the program */
    public Controller getController(){
        return controller;
    }
    /** returns the data table for this class
     * @return returns the table containing the movies */
    public JTable getMovieTable(){
        return mainPanel.getMovieTable();
    }
    /** updates the display for the current movie information */
    public void updateSelectedMovie(){
        mainPanel.updateCurrentMovie();
    }
    /** function to filter the table rows to help find what the user is typing */
    public void updateFilter(final String text){
        JTable table = getMovieTable();
        @SuppressWarnings("unchecked") TableRowSorter<MovieTable> sorter = (TableRowSorter<MovieTable>) table.getRowSorter();
        RowFilter<MovieTable, Object> filter = null;
        //set the filter
        try {
            /** filter to display only the items contained in the user's text */
            filter = new RowFilter<MovieTable, Object>() {
                @Override public boolean include(RowFilter.Entry<? extends MovieTable, ? extends Object> entry){
                    for (int i = entry.getValueCount() - 1; i >= 0; i--) {
                        if (entry.getStringValue(i).toLowerCase().contains(text.toLowerCase())) { return true; }
                    }
                    return false;
                }
            };
        } catch (PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(filter);
    }
}
