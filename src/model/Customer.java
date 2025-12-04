package model;

import java.sql.Date;

public class Customer extends User{

	public Customer(int id, String name, String email, String password, String gender, Date dob) {
		super(id, name, email, password, gender, dob, "Customer");
		// TODO Auto-generated constructor stub
	}
	
}
