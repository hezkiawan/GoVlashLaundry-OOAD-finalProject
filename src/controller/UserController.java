package controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.Vector;

import database.UserDAO;
import model.Admin;
import model.Customer;
import model.Employee;
import model.LaundryStaff;
import model.Receptionist;
import model.User;

public class UserController {
	private UserDAO userDAO = new UserDAO();
	

    // LOGIN
    public User login(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) return null;
        return userDAO.login(email, password);
    }

    // REGISTER / ADD USER
    // Validates inputs based on Source 16, 20, 30, 33
    public String register(String name, String email, String password, String confirmPass, String gender, LocalDate dobLocal, String role) {
        
        // 1. Check Empty Fields (General)
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || gender == null || dobLocal == null) {
            return "All fields must be filled.";
        }

        // 2. Validate Password (Source 20, 33: "Must at least 6 characters long")
        if (password.length() < 6) {
            return "Password must be at least 6 characters long.";
        }
        
        // 3. Validate Confirm Password (Source 20, 33: "Must be equal to the password")
        if (!password.equals(confirmPass)) {
            return "Confirm Password must match Password.";
        }

        // 4. Validate Email Suffix (Source 16: Employee ends with @govlash.com, Source 33: Customer ends with @email.com)
        if (role.equals("Customer")) {
            if (!email.endsWith("@email.com")) return "Customer email must end with '@email.com'.";
        } else {
            // It is an employee
            if (!email.endsWith("@govlash.com")) return "Employee email must end with '@govlash.com'.";
        }
        
        // 5. Validate email
        if (userDAO.emailExists(email)) {
            return "Email already exists.";
        }
        
        // 6. validate username
        if (userDAO.usernameExists(name)) {
            return "Username already exists.";
        }



        // 5. Validate Age
        // Source 20: Employee > 17 years old
        // Source 33: Customer > 12 years old
        int age = Period.between(dobLocal, LocalDate.now()).getYears();
        
        if (role.equals("Customer")) {
            if (age < 12) return "Customer must be at least 12 years old.";
        } else {
            if (age < 17) return "Employee must be at least 17 years old.";
        }

        // 6. Validate Unique Username/Email?
        // Usually handled by DB constraint (SQLException), but we can try to register:
        
        Date dob = Date.valueOf(dobLocal); // Convert LocalDate to SQL Date
        
        User newUser;
        switch(role) {
        case "Customer":
            newUser = new Customer(0, name, email, password, gender, dob);
            break;

        case "Receptionist":
            newUser = new Receptionist(0, name, email, password, gender, dob);
            break;

        case "Laundry Staff":
            newUser = new LaundryStaff(0, name, email, password, gender, dob);
            break;

        case "Admin":
            newUser = new Admin(0, name, email, password, gender, dob);
            break;

        default:
            return "Invalid role.";
    }
        
        boolean success = userDAO.register(newUser);
        if (!success) {
            return "Registration failed.";
        }

        return "Success";
    }

    // VIEW EMPLOYEES (Admin only)
    public Vector<Employee> getAllEmployees() {
        return userDAO.getAllEmployees();
    }
}
