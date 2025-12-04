package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {
    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private final String DATABASE = "govlash_laundry"; //name of the database
    private final String HOST = "localhost:3306"; //use the port (in xampp)
    private final String CONNECT = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
    
    private Connection con;
    private Statement st;
    private static Connect connect;
    
    public ResultSet rs;
    public ResultSetMetaData rsm;
    private PreparedStatement ps;
    
    //SINGLETON PATTERN
    public static Connect getInstance() { 
        if(connect == null) return new Connect();
        return connect;
    }
    
    private Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //return sebuah object dr com.mysql.cj.jdbc.Driver
            con = DriverManager.getConnection(CONNECT, USERNAME, PASSWORD); //establish connection
            st = con.createStatement(); //create Statement object
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    // BUAT SELECT QUERIES
    public ResultSet execQuery(String query) {
        try {
            rs = st.executeQuery(query); //execute query
            rsm = rs.getMetaData(); //get meta data from query result
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return rs;
    }
    
    
    // BUAT INSERT UPDATE DEL
    public PreparedStatement prepareStatement(String query) {
        try {
            ps = con.prepareStatement(query); //create PreparedStatement
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return ps;
    }
}
