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
    public String register(String name, String email, String password, String confirmPass, String gender, LocalDate dobLocal, String role) {
        
        // 1. Check empty fields
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || gender == null || dobLocal == null) {
            return "All fields must be filled.";
        }

        // 2. Validate password (must at least 6 characters long)
        if (password.length() < 6) {
            return "Password must be at least 6 characters long.";
        }
        
        // 3. Validate confirm password (must be equal to the password)
        if (!password.equals(confirmPass)) {
            return "Confirm Password must match Password.";
        }

        // 4. Validate email suffix (employee ends with @govlash.com, customer ends with @email.com)
        if (role.equals("Customer")) {
            if (!email.endsWith("@email.com")) return "Customer email must end with '@email.com'.";
        } else {
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



        // 5. Validate age
        // Employee > 17 years old
        // Customer > 12 years old
        int age = Period.between(dobLocal, LocalDate.now()).getYears();
        
        if (role.equals("Customer")) {
            if (age < 12) return "Customer must be at least 12 years old.";
        } else {
            if (age < 17) return "Employee must be at least 17 years old.";
        }
        
     // Convert localDate to SQL date
        Date dob = Date.valueOf(dobLocal); 
        
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
