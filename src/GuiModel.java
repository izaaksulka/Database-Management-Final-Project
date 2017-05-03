import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class GuiModel extends DefaultTableModel {

    Connection db;
    Vector<Integer> albumIDs;   // will hold album ID values corresponding to search results
    int selectedRow;            // current selected row

    public GuiModel() {
        Object[] a = {"Artist", "Album Title", "Year"};
        
        setColumnIdentifiers(a);
    }
    public void setColumns(Object[] newIDs){
    	setColumnIdentifiers(newIDs);
    }
    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }
    public void removeSelectedRow() {
        removeRow(selectedRow);
    }


    public int getAlbumID() {
        if (selectedRow != -1) {
            return albumIDs.elementAt(selectedRow);
        }
        else {
            return -1;
        }
    }

    public String getArtist() {
        if (selectedRow != -1) {
            return (String) getValueAt(selectedRow, 0);
        }
        else {
            return "";
        }
    }

    public String getAlbumTitle() {
        if (selectedRow != -1) {
            return (String) getValueAt(selectedRow, 1);
        }
        else {
            return "";
        }
    }

    public String getAlbumYear() {
        if (selectedRow != -1) {
            return (String) getValueAt(selectedRow, 2);
        }
        else {
            return "";
        }
    }

    public void login(String username, String password) throws SQLException, ClassNotFoundException {

        Class.forName("org.postgresql.Driver");
        String connectString = "jdbc:postgresql://flowers.mines.edu/csci403";

        db = DriverManager.getConnection(connectString, username, password);
    }

    public void search(City city, String by, String val) throws SQLException {
        ResultSet rs;
        switch(city){
		case Chicago:
			rs = searchChicago(by, val);
			break;
		case Denver:
			rs = searchDenver(by, val);
			break;
		case LA:
			rs = searchLA(by, val);
			break;
		default:
            throw new RuntimeException("Invalid search request");
        }
//        if (by.equals("Artist")) {
//            rs = searchByArtist(val);
//        }
//        else if (by.equals("Album")) {
//            rs = searchByAlbum(val);
//        }
//        else {
//            throw new RuntimeException("Invalid search request");
//        }
        if (rs == null) {
            setNumRows(1);
            setValueAt("Search by " + by + ":", 0, 0);
            setValueAt(val, 0, 1);
            setValueAt("(Not yet implemented)", 0, 2);
        }
        else {
            dataVector = new Vector<Vector<Object>>();
            //albumIDs = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                int colCount = GuiApplication.currentCityColumnCount(city);
                for (int i = 0; i < colCount; i++) {
					row.add(rs.getString(i + 1));
				}
//                row.add(rs.getString(1));
//                row.add(rs.getString(2));
//                row.add(rs.getString(3));
                dataVector.add(row);
                //albumIDs.add(rs.getInt(4));
            }
            this.fireTableDataChanged();
        }
    }
    private ResultSet searchLA(String by, String val) throws SQLException{
    	String query =
    			"SELECT date, description, address, zip " +
    					"FROM LACrimeData " +
    					"WHERE lower(" + by + ") LIKE lower(?) " +
    					"ORDER BY date, description, address";

    	PreparedStatement ps = db.prepareStatement(query);
    	ps.setString(1, "%" + val + "%");
    	return ps.executeQuery();
    }
    private ResultSet searchDenver(String by, String val) throws SQLException{
    	String query =
    			"SELECT date, description, address, neighborhood " +
    					"FROM denverCrimeData " +
    					"WHERE lower(" + by + ") LIKE lower(?) " +
    					"ORDER BY date, description, address";

    	PreparedStatement ps = db.prepareStatement(query);
    	ps.setString(1, "%" + val + "%");
    	return ps.executeQuery();
    }
    private ResultSet searchChicago(String by, String val) throws SQLException{
    	String query =
    			"SELECT date, description, block " +
    					"FROM chicagoCrimeData " +
    					"WHERE lower(" + by + ") LIKE lower(?) " +
    					"ORDER BY date, description";

    	PreparedStatement ps = db.prepareStatement(query);
    	ps.setString(1, "%" + val + "%");
    	return ps.executeQuery();
    }

}
