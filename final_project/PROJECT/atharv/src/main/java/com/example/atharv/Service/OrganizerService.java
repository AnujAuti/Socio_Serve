package com.example.atharv.Service;

import com.example.atharv.Entity.Organizer;
import com.example.atharv.Repository.OrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizerService {
    
    @Autowired
    private OrganizerRepository organizerRepository;
    
    public Organizer registerOrganizer(Organizer organizer) {
        System.out.println("=== REGISTERING ORGANIZER ===");
        System.out.println("Org Name: " + organizer.getOrganizationName());
        System.out.println("Contact: " + organizer.getContactPerson());
        System.out.println("Email: " + organizer.getEmail());
        System.out.println("Phone: " + organizer.getPhone());
        System.out.println("Password: " + organizer.getPassword());
        System.out.println("Cause: " + organizer.getCause());
        
        try {
            // Check if email already exists
            if (organizerRepository.existsByEmail(organizer.getEmail())) {
                System.out.println("❌ Email already exists: " + organizer.getEmail());
                throw new RuntimeException("Email already registered: " + organizer.getEmail());
            }
            
            // Save without password encryption for now
            Organizer saved = organizerRepository.save(organizer);
            System.out.println("✅ Organizer saved with ID: " + saved.getId());
            
            return saved;
            
        } catch (Exception e) {
            System.out.println("❌ ERROR in registerOrganizer: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }
    
    public Optional<Organizer> loginOrganizer(String email, String password) {
        System.out.println("=== ORGANIZER LOGIN ATTEMPT ===");
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        
        Optional<Organizer> organizer = organizerRepository.findByEmail(email);
        if (organizer.isPresent()) {
            // Simple password check (no encryption for now)
            if (organizer.get().getPassword().equals(password)) {
                System.out.println("✅ Organizer login successful");
                return organizer;
            } else {
                System.out.println("❌ Organizer password incorrect");
            }
        } else {
            System.out.println("❌ Organizer email not found");
        }
        return Optional.empty();
    }
    
    public List<Organizer> getAllOrganizers() {
        return organizerRepository.findAll();
    }
}