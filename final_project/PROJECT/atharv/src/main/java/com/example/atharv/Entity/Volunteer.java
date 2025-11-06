package com.example.atharv.Entity;



import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "volunteer", cascade = CascadeType.ALL)
@JsonIgnore
private List<VolunteerRegistration> registrations = new ArrayList<>();
    private String name;
    private String email;
    private String phone;
    private String skills;
    private String password;
    
   
    
    // Constructors
    public Volunteer() {}
    
    public Volunteer(String name, String email, String phone, String skills, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.skills = skills;
        this.password = password;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public List<VolunteerRegistration> getRegistrations() { return registrations; }
    public void setRegistrations(List<VolunteerRegistration> registrations) { this.registrations = registrations; }
}