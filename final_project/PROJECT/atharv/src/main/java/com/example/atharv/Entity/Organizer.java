package com.example.atharv.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "organizers")
public class Organizer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String organizationName;
    
    @Column(nullable = false)
    private String contactPerson;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String phone;
    
    @Column(nullable = false)
    private String password;
    
    private String cause;
    
    // Constructors
    public Organizer() {}
    
    public Organizer(String organizationName, String contactPerson, String email, 
                     String phone, String password, String cause) {
        this.organizationName = organizationName;
        this.contactPerson = contactPerson;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.cause = cause;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
    
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getCause() { return cause; }
    public void setCause(String cause) { this.cause = cause; }
}