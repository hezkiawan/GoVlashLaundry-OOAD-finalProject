package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import model.Admin;
import model.Employee;
import model.Customer;
import model.LaundryStaff;
import model.Receptionist;
import model.User;

public class UserDAO {
	private Connect connect = Connect.getInstance();

    // LOGIN METHOD
    public User login(String email, String InputPassword) {
        User user = null;
        String query = "SELECT * FROM ms_user WHERE UserEmail = ? AND UserPassword = ?";
        PreparedStatement ps = connect.prepareStatement(query);
        
        try {
            if (ps == null) return null; // Safety check if connection failed
            
            ps.setString(1, email);
            ps.setString(2, InputPassword);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int id = rs.getInt("UserId");
                String name = rs.getString("UserName");
                String gender = rs.getString("UserGender");
                java.sql.Date dob = rs.getDate("UserDOB");
                String role = rs.getString("UserRole");
                String pw = rs.getString("UserPassword");

                // Instantiate the correct subclass based on the role string
                if(role.equals("Admin")) {
                    user = new Admin(id, name, email, pw, gender, dob);
                } else if(role.equals("Customer")) {
                    user = new Customer(id, name, email, pw, gender, dob);
                } else if(role.equals("Receptionist")) {
                    user = new Receptionist(id, name, email, pw, gender, dob);
                } else if(role.equals("Laundry Staff")) {
                    user = new LaundryStaff(id, name, email, pw, gender, dob);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // REGISTER METHOD (For Customers & Employees)
    public boolean register(User user) {
        // We use user.getRole() which is populated by the Subclass constructor
        String query = "INSERT INTO ms_user (UserName, UserEmail, UserPassword, UserGender, UserDOB, UserRole) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connect.prepareStatement(query);
        
        try {
            if (ps == null) return false;
            
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getGender());
            ps.setDate(5, user.getDob());
            ps.setString(6, user.getRole()); // [cite: 28, 33]
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
 // VIEW ALL EMPLOYEES (For Admin)
    public Vector<Employee> getAllEmployees() {
        Vector<Employee> employees = new Vector<>();
        String query = "SELECT * FROM ms_user WHERE UserRole IN ('Admin', 'Laundry Staff', 'Receptionist')";
        ResultSet rs = connect.execQuery(query);
        
        try {
            while(rs.next()) {
                int id = rs.getInt("UserId");
                String name = rs.getString("UserName");
                String email = rs.getString("UserEmail");
                String pass = rs.getString("UserPassword");
                String gender = rs.getString("UserGender");
                java.sql.Date dob = rs.getDate("UserDOB");
                String role = rs.getString("UserRole");

                if(role.equals("Laundry Staff")) {
                    employees.add(new LaundryStaff(id, name, email, pass, gender, dob));
                } else if(role.equals("Receptionist")) {
                    employees.add(new Receptionist(id, name, email, pass, gender, dob));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public boolean emailExists(String email) {
        String q = "SELECT 1 FROM ms_user WHERE UserEmail = ?";
        PreparedStatement ps = connect.prepareStatement(q);
        try {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next(); 
        } catch (Exception e) { return true; }  
    }
    
    public boolean usernameExists(String username) {
        String q = "SELECT 1 FROM ms_user WHERE UserName = ?";
        PreparedStatement ps = connect.prepareStatement(q);
        try {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            return true; // assume exists to avoid duplicates
        }
    }
}
