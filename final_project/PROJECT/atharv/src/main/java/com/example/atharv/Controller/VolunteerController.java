package com.example.atharv.Controller;





import com.example.atharv.Entity.Volunteer;
import com.example.atharv.Service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;





@RestController
@RequestMapping("/api/volunteers")
@CrossOrigin(origins = "*")
public class VolunteerController {
    
    @Autowired
    private VolunteerService volunteerService;
    
    @PostMapping("/register")
    public ResponseEntity<?> registerVolunteer(@RequestBody Volunteer volunteer) {
        System.out.println("=== REGISTRATION REQUEST RECEIVED ===");
        System.out.println("Name: " + volunteer.getName());
        System.out.println("Email: " + volunteer.getEmail());
        System.out.println("Phone: " + volunteer.getPhone());
        System.out.println("Skills: " + volunteer.getSkills());
        System.out.println("Password: " + volunteer.getPassword());
        
        try {
            Volunteer savedVolunteer = volunteerService.registerVolunteer(volunteer);
            System.out.println("✅ REGISTRATION SUCCESS - ID: " + savedVolunteer.getId());
            return ResponseEntity.ok(savedVolunteer);
            
        } catch (RuntimeException e) {
            System.out.println("❌ REGISTRATION FAILED: " + e.getMessage());
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/login")
        public ResponseEntity<?> loginVolunteer(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("🔐 Volunteer login attempt for: " + loginRequest.getEmail());
            
            Optional<Volunteer> volunteer = volunteerService.loginVolunteer(
                loginRequest.getEmail(), loginRequest.getPassword());
            
            if (volunteer.isPresent()) {
                System.out.println("✅ Volunteer login successful: " + volunteer.get().getEmail());
                return ResponseEntity.ok(volunteer.get());
            } else
             {
                System.out.println("❌ Volunteer login failed: Invalid credentials");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
            }
        } catch (Exception e) {
            System.out.println("❌ Volunteer login error: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body("Login failed: " + e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Volunteer>> getAllVolunteers() {
        List<Volunteer> volunteers = volunteerService.getAllVolunteers();
        System.out.println("Returning " + volunteers.size() + " volunteers");
        return ResponseEntity.ok(volunteers);
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        System.out.println("✅ TEST ENDPOINT HIT");
        return ResponseEntity.ok("Backend is working! " + System.currentTimeMillis());
    }
    
    // DTO for login request
    public static class LoginRequest {                  
        private String email;
        private String password;
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}