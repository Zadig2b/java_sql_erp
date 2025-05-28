package com.erp.model;

public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private String city;
    private String email;

    public Customer(int id, String firstName, String lastName, String city, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.email = email;
    }

    // Getters
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getCity() { return city; }
    public String getEmail() { return email; }
}
