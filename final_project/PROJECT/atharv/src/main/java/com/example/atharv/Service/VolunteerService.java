package com.example.atharv.Service;





import com.example.atharv.Entity.Volunteer;
import com.example.atharv.Repository.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VolunteerService {
    
    @Autowired
    private VolunteerRepository volunteerRepository;
    
    public Volunteer registerVolunteer(Volunteer volunteer) {
        System.out.println("=== REGISTERING VOLUNTEER ===");
        System.out.println("Name: " + volunteer.getName());
        System.out.println("Email: " + volunteer.getEmail());
        System.out.println("Phone: " + volunteer.getPhone());
        System.out.println("Password: " + volunteer.getPassword());
        System.out.println("Skills: " + volunteer.getSkills());
        
        try {
            // Check if email already exists
            if (volunteerRepository.existsByEmail(volunteer.getEmail())) {
                System.out.println("❌ Email already exists: " + volunteer.getEmail());
                throw new RuntimeException("Email already registered: " + volunteer.getEmail());
            }
            
            // Save without password encryption for now
            Volunteer saved = volunteerRepository.save(volunteer);
            System.out.println("✅ Volunteer saved with ID: " + saved.getId());
            
            // Verify it was saved
            Optional<Volunteer> verified = volunteerRepository.findById(saved.getId());
            if (verified.isPresent()) {
                System.out.println("✅ Verified in database: " + verified.get().getEmail());
            }
            
            return saved;
            
        } catch (Exception e) {
            System.out.println("❌ ERROR in registerVolunteer: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }
    
    public Optional<Volunteer> loginVolunteer(String email, String password) {
        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        
        Optional<Volunteer> volunteer = volunteerRepository.findByEmail(email);
        if (volunteer.isPresent()) {
            // Simple password check (no encryption for now)
            if (volunteer.get().getPassword().equals(password)) {
                System.out.println("✅ Login successful");
                return volunteer;
            } else {
                System.out.println("❌ Password incorrect");
            }
        } else {
            System.out.println("❌ Email not found");
        }
        return Optional.empty();
    }
    
    public List<Volunteer> getAllVolunteers() {
        List<Volunteer> volunteers = volunteerRepository.findAll();
        System.out.println("Found " + volunteers.size() + " volunteers in database");
        return volunteers;
    }
    
    public Optional<Volunteer> getVolunteerById(Long id) {
        return volunteerRepository.findById(id);
    }
    
    public Optional<Volunteer> getVolunteerByEmail(String email) {
        return volunteerRepository.findByEmail(email);
    }
}