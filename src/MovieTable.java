import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**@author Richard Luby, Copyright 2013*/
public class MovieTable extends DefaultTableModel {
	/** variable to store the controller */
	private Controller controller;
	/** variable to store the main window */
	MainFrame mainFrame;
	/** variable to store the dataTable */
	JTable dataTable;
	
	/** array to store the names of the columns */
	private String[] columnNames = {"Title", "MPAA Rating", "Rating"};
	//must also change getValueAt(int,int) when adding ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^, and setValue(...)
	//in addition to the input reader, and the Book.toString() methods, and the View menu
	
	/** hashmap to map the column titles to the proper column index */
	private HashMap<Integer, String> titleMap;
	
	/** initializes the necessary variables
	 * @param mf the mainframe of the application */
	public MovieTable(MainFrame mf) {
		mainFrame = mf;
		controller = mainFrame.getController();
		titleMap = new HashMap<Integer, String>();//map to store the titles with the proper index
		for (int i = 0; i < columnNames.length; i++) {
			titleMap.put(i, columnNames[i]);
		}
		//controller.setTitleMap(columnNames);
	}
	
	/** returns the number of columns in the table
	 * @return returns the number of columns in the table */
	@Override public int getColumnCount(){
		return columnNames.length;
	}
	/** returns the name of the column at the index
	 * @param col the index of the column
	 * @return returns the name of the column at col */
	@Override public String getColumnName(int col){
		return columnNames[col];
	}
	
	/** returns the number of rows in the table
	 * @return returns the number of rows in the table */
	@Override public int getRowCount(){
		if (controller == null) { return 0; }
		return controller.getNumMovies();
	}
	
	/** returns the class for this cell
	 * @return the class type for this cell */
	//@Override public Class<?> getColumnClass(int col){
	//    if (columnNames[controller.getMainFrame().getDataTable()
	//                    .convertColumnIndexToModel(col)].equals("Rented")) { return Boolean.class; }
	//    return Object.class;
	//}
	
	/** function to disallow editing of all table contents */
	@Override public boolean isCellEditable(int row, int col)
	{
		return false;
	}
	
	/* /** function to change table contents //
	 * @Override public void setValueAt(Object in, int row, int col) { Movie
	 * movie = controller.getMovie(row); String input = ""; try { input
	 * =(String) in; if(in != null) { //String colName =
	 * titleMap.get(controller.getMainFrame().getDataTable() //
	 * .convertColumnIndexToModel(col)); if
	 * (titleMap.get(col).equalsIgnoreCase("Title")) { book.setSubject(input); }
	 * else if (colName.equalsIgnoreCase("Semester")) { book.setSemester(input);
	 * } else if (colName.equalsIgnoreCase("Purchase Price")) {
	 * book.setPurchasePrice(Listeners.parsePrice(input));
	 * controller.getMainFrame().setMutableLabels(); //return
	 * book.getPurchasePrice(); } else if
	 * (colName.equalsIgnoreCase("Sell Price")) {
	 * book.setSellPrice(Listeners.parsePrice(input));
	 * controller.getMainFrame().setMutableLabels(); //return
	 * book.getSellPrice(); } else if (colName.equalsIgnoreCase("Buyer")) {
	 * book.setBuyer(input); } else if (colName.equalsIgnoreCase("Seller")) {
	 * book.setSeller(input); } } }
	 * //controller.getMainFrame().setMutableLabels(); } */
	/** returns the object at (row,column)
	 * @return returns the object at (row, column) */
	@Override public Object getValueAt(int row, int column){
		if (row > (controller.getNumMovies())) {
			return null;
		} else {
			Movie movie = controller.getMovie(row);
			if (movie != null) {
				String colName = titleMap.get(column);
				if (colName.equalsIgnoreCase("Title")) {
					return movie.getTitle();
				} else if (colName.equalsIgnoreCase("MPAA Rating")) {
					return movie.getAgeRating();
				} else if (colName.equalsIgnoreCase("Rating")) { return movie.getUserRating(); }
			}
		}
		return null;
	}
	/** function to return the hashmap of titles and columns
	 * @return returns HashMap<Integer, String> of column indices to column
	 *         titles */
	public String[] getColumnTitles(){
		return columnNames;
	}
	/** set the dataTable for this model
	 * @param table the dataTable for this model */
	public void setTable(JTable table){
		dataTable = table;
	}
}
