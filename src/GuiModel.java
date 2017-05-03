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
            albumIDs = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString(1));
                row.add(rs.getString(2));
                row.add(rs.getString(3));
                dataVector.add(row);
                albumIDs.add(rs.getInt(4));
            }
            this.fireTableDataChanged();
        }
    }
    private ResultSet searchLA(String by, String val) throws SQLException{
    	System.out.println("searching la");
    	String query =
    			"SELECT crimeDate, description, address, zip " +
    					"FROM LACrimeData " +
    					"WHERE lower(" + by + ") LIKE lower(?) " +
    					"ORDER BY crimeDate, description, address";

    	PreparedStatement ps = db.prepareStatement(query);
    	ps.setString(1, "%" + val + "%");
    	return ps.executeQuery();
    }
    private ResultSet searchDenver(String by, String val){
    	return null;
    }
    private ResultSet searchChicago(String by, String val){
    	return null;
    }

//    public ResultSet searchByArtist(String val) throws SQLException {
//
//        // Note ResultSet *must* contain artist name, album title, album year, and album id,
//        // in that order for the rest of the code to work properly!
//        String query =
//            "SELECT ar.name, al.title, al.year, al.id " +
//            "FROM artist AS ar, album AS al " +
//            "WHERE lower(ar.name) LIKE lower(?) " +
//            "AND ar.id = al.artist_id " +
//            "ORDER BY ar.name, al.year, al.title";
//
//        PreparedStatement ps = db.prepareStatement(query);
//        ps.setString(1, "%" + val + "%");
//        return ps.executeQuery();
//    }
//
//    public ResultSet searchByAlbum(String val) throws SQLException {
//        String query =
//                "SELECT ar.name, al.title, al.year, al.id " +
//                "FROM artist AS ar, album AS al " +
//                "WHERE lower(al.title) LIKE lower(?) " +
//                "AND ar.id = al.artist_id " +
//                "ORDER BY ar.name, al.year, al.title";
//
//        PreparedStatement ps = db.prepareStatement(query);
//        ps.setString(1, "%" + val + "%");
//        return ps.executeQuery();
//    }

//    public Vector<String> getArtists() throws SQLException {
//        Vector<String> list = new Vector<>();
////        list.add("Chris Thile");
////        list.add("Hiromi");
////        list.add("Jethro Tull");
////        return list;
//    	String query = "select name from artist order by name";
//    	PreparedStatement ps = db.prepareStatement(query);
//    	ResultSet r = ps.executeQuery();
//    	while(r.next()){
//    		list.add(r.getString("name"));
//    	}
//    	//System.out.println(list);
//    	return list;
//    }

//    public void insertArtist(String artist) throws SQLException {
//        //System.out.println("Inserting new artist: " + artist + " (Not yet implemented)");
//        String query = "insert into artist (name) values (?)";
//        
//        PreparedStatement ps = db.prepareStatement(query);
//        ps.setString(1, artist);
//        //System.out.println(ps.toString());
//        int x = ps.executeUpdate();
//        //System.out.println(x);
//    }
//
//    public void insertAlbum(String artist, String title, String year) throws SQLException {
//       // System.out.println("Inserting new album: " + artist + " - " + title + "(" + year + ") " + " (Not yet implemented)");
//        String query = "insert into album (artist_id, title, year) " + 
//        	"values ( ( select id from artist where name = ? ), "
//        	+ "?, "
//        	+ "? )";
//        PreparedStatement ps = db.prepareStatement(query);
//        ps.setString(1, artist);
//        ps.setString(2, title);
//        ps.setInt(3, Integer.parseInt(year));
//        //System.out.println(ps.toString());
//        ps.executeUpdate();
//        
//    }

//    public void updateAlbum(int albumID, String title, String year) throws SQLException {
//        //System.out.println("Updating album id " + albumID + " to " + title + "(" + year + ") " + " (Not yet implemented)");
//        setValueAt(title, selectedRow, 1);
//        setValueAt(year, selectedRow, 2);
//        String query = "update album set title = ?, year = ? where id = ?";
//        PreparedStatement ps = db.prepareStatement(query);
//        ps.setString(1, title);
//        ps.setInt(2, Integer.parseInt(year));
//        ps.setInt(3, albumID);
//        //System.out.println(ps.toString());
//        ps.executeUpdate();
//        
//    }

//    public void deleteAlbum(int albumID) throws SQLException {
//        //System.out.println("Deleting album id: " + albumID + " (Not yet implemented)");
//        String query = "delete from album where id = ?";
//        PreparedStatement ps = db.prepareStatement(query);
//        ps.setInt(1, albumID);
//        ps.executeUpdate();
//    }
}
