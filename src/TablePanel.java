import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

/**@author Richard Luby, Copyright 2013*/
/** this class contains the table panel, and a searchbar at the top */
public class TablePanel extends JPanel {
    /** MainFrame of the application */
    private MainFrame mainFrame;
    /** the controller object for the application */
    private Controller controller;
    /** the search bar at the top */
    private JTextField searchbar;
    /** the table being displayed */
    private JTable movieTable;
    /** creates and initializes a table with the data
     * @param mf the main application window */
    public TablePanel(MainFrame mf) {
        mainFrame = mf;
        controller = mainFrame.getController();
        //set up layout
        Dimension preferredDim = mainFrame.getSize();
        //preferredDim.width = (preferredDim.width / 2);
        //preferredDim.height -= 60;
        //setPreferredSize(preferredDim);
        setLayout(new GridBagLayout());
        int col = 0, row = 0, tGWidth = 1, tGHeight = 1, tAnchor = GridBagConstraints.CENTER, fill = GridBagConstraints.BOTH, txPad = 10, tyPad = 0, inset = 20;
        Insets insets = new Insets(inset, 0, 0, 0);
        double tXWeight = .2, tYWeight = 0;
        GridBagConstraints gbConstraints = new GridBagConstraints(col, row, tGWidth, tGHeight, tXWeight, tYWeight, tAnchor, fill, insets,
                txPad, tyPad);
        //set up search bar
        searchbar = new JTextField("Search...");
        searchbar.setForeground(Color.LIGHT_GRAY);
        searchbar.addFocusListener(MyActionListeners.createSearchBarUITweakListener());
        searchbar.addKeyListener(MyActionListeners.createKeyFilterListener());
        add(searchbar, gbConstraints);
        //setup table
        setupTable();
        movieTable.addMouseListener(MyActionListeners.createUpdateCurrentMovieMouseListener());
        movieTable.addKeyListener(MyActionListeners.createUpdateCurrntMovieKeyListener());
        JScrollPane tableScroller = new JScrollPane(movieTable);
        gbConstraints.gridy = 1;
        gbConstraints.weighty = .2;
        add(tableScroller, gbConstraints);
    }

    /** this method sets up the table with the correct data */
    private void setupTable(){
        movieTable = new JTable(new MovieTable(mainFrame)) {
            /** Create alternating, colored rows, with the active row a different
             * color
             * @return returns a renderer that creates alternating row colors,
             *         with the active row a third color */
            @Override public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
                Component c = super.prepareRenderer(renderer, row, column);

                //  Alternate row color, set active row to med Gray

                if (!isRowSelected(row)) {
                    c.setBackground((row % 2) == 0 ? getBackground() : Color.LIGHT_GRAY);
                } else {
                    c.setBackground(Color.GRAY);
                }
                return c;
            }
        };
        //set column model renderers
        TableColumnModel cols = movieTable.getColumnModel();
        String[] titles = ((MovieTable) movieTable.getModel()).getColumnTitles();
        for (int i = 0; i < cols.getColumnCount(); i++) {
            if (titles[i].contains("Price")) {
                cols.getColumn(i).setCellRenderer(NumberRenderer.getCurrencyRenderer());
            }
        }

        movieTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //movieTable.setCellSelectionEnabled(true);
        movieTable.setRowSelectionAllowed(true);
        movieTable.setUpdateSelectionOnSort(true);
        movieTable.setFocusable(true);
        movieTable.setShowGrid(true);
        movieTable.setFillsViewportHeight(true);
        movieTable.setColumnSelectionAllowed(false);
        movieTable.setAutoCreateRowSorter(true);
        movieTable.setRowSelectionInterval(0, 0); //set selection to origin
        //movieTable.setColumnSelectionInterval(0, 0);
        TableRowSorter<MovieTable> sorter = new TableRowSorter<MovieTable>((MovieTable) movieTable.getModel());
        movieTable.setRowSorter(sorter);
        //bookTable.
    }

    /** returns the data table for this class
     * @return returns the table containing the movies */
    public JTable getMovieTable(){
        return movieTable;
    }
    /** sets the current mainFrame and controller
     * @param mf the application window to use */
    public void setMainFrame(MainFrame mf){
        mainFrame = mf;
        controller = mainFrame.getController();
        remove(movieTable);
        setupTable();
    }
}
