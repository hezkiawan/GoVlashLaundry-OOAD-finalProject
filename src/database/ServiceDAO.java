package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import model.Service;

public class ServiceDAO {
	private Connect connect = Connect.getInstance();

    public Vector<Service> getAllServices() {
        Vector<Service> services = new Vector<>();
        String query = "SELECT * FROM ms_service WHERE IsDeleted = 0";
        ResultSet rs = connect.execQuery(query);
        try {
            while(rs.next()) {
                services.add(new Service(
                    rs.getInt("ServiceId"),
                    rs.getString("ServiceName"),
                    rs.getString("ServiceDescription"),
                    rs.getInt("ServicePrice"),
                    rs.getInt("ServiceDuration"),
                    rs.getBoolean("IsDeleted")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return services;
    }

    public boolean  insertService(Service s) {
        String query = "INSERT INTO ms_service (ServiceName, ServiceDescription, ServicePrice, ServiceDuration) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = connect.prepareStatement(query);
        try {
            if (ps == null) return false;
            ps.setString(1, s.getServiceName());
            ps.setString(2, s.getServiceDesc());
            ps.setInt(3, s.getServicePrice());
            ps.setInt(4, s.getServiceDuration());
            int rows = ps.executeUpdate(); //returns number of affected rows
            return rows > 0;                 // true = success, false = failed
        } catch (SQLException e) { 
        	e.printStackTrace(); 
        	return false;
        }
    }

    public boolean updateService(Service s) {
        String query = "UPDATE ms_service SET ServiceName=?, ServiceDescription=?, ServicePrice=?, ServiceDuration=? WHERE ServiceId=?";
        PreparedStatement ps = connect.prepareStatement(query);
        try {
            if (ps == null) return false;
            ps.setString(1, s.getServiceName());
            ps.setString(2, s.getServiceDesc());
            ps.setInt(3, s.getServicePrice());
            ps.setInt(4, s.getServiceDuration());
            ps.setInt(5, s.getServiceId());
            int rows = ps.executeUpdate();   // number of rows updated
            return rows > 0;
        } catch (SQLException e) { 
        	e.printStackTrace(); 
        	return false;
        }
    }

    public boolean deleteService(int id) {
        String query = "UPDATE ms_service SET IsDeleted = 1 WHERE ServiceId=?";
        PreparedStatement ps = connect.prepareStatement(query);
        try {
            if (ps == null) return false;
            ps.setInt(1, id);
            int rows = ps.executeUpdate();  // returns number of rows deleted
            return rows > 0;
        } catch (SQLException e) { 
        	e.printStackTrace(); 
        	return false;
        }
    }
}
