package com.example.atharv.Controller;




import com.example.atharv.Entity.Organizer;
import com.example.atharv.Repository.OrganizerRepository;
import com.example.atharv.Service.OrganizerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/organizers")
@CrossOrigin("*")
public class OrganizerController {
    
    @Autowired
    private OrganizerRepository organizerRepository;
    private OrganizerService organizerService;


    
    // Test endpoint
    @GetMapping("/test")
    public String test() {
        return "✅ Organizer API is working!";
    }
    
    // Register organizer
    @PostMapping("/register")
    public ResponseEntity<?> registerOrganizer(@RequestBody Organizer organizer) {
        try {
            // Save organizer
            Organizer savedOrganizer = organizerRepository.save(organizer);

            // Debug log
        System.out.println("Registered organizer: " + savedOrganizer.getContactPerson() + " from " + savedOrganizer.getOrganizationName());
            
            // Return response with organization details
            Map<String, Object> response = new HashMap<>();
            response.put("id", savedOrganizer.getId());
            response.put("organizationName", savedOrganizer.getOrganizationName());
            response.put("contactPerson", savedOrganizer.getContactPerson());
            response.put("email", savedOrganizer.getEmail());
            response.put("phone", savedOrganizer.getPhone());
            response.put("message", "Organization registered successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }
    
    // Get organizer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Organizer> getOrganizerById(@PathVariable Long id) {
        Optional<Organizer> organizer = organizerRepository.findById(id);
        return organizer.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }


    // Organizer login
@PostMapping("/login")
public ResponseEntity<?> loginOrganizer(@RequestBody Map<String, String> loginRequest) {
    try {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        
        Optional<Organizer> organizer = organizerService.loginOrganizer(email, password);
        
        if (organizer.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", organizer.get().getId());
            response.put("organizationName", organizer.get().getOrganizationName());
            response.put("contactPerson", organizer.get().getContactPerson());
            response.put("email", organizer.get().getEmail());
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid email or password"));
        }
    } catch (Exception e) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", "Login failed: " + e.getMessage()));
    }
}
}