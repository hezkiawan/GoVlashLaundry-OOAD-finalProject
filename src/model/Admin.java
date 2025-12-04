package model;

import java.sql.Date;

public class Admin extends Employee{

	public Admin(int id, String name, String email, String password, String gender, Date dob) {
		super(id, name, email, password, gender, dob, "Admin");
		// TODO Auto-generated constructor stub
	}
	
}
