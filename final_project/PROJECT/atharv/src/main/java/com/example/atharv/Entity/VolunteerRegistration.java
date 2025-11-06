package com.example.atharv.Entity;





import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "volunteer_registrations")
public class VolunteerRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    private String status; // "REGISTERED", "CANCELLED"

    // Constructors
    public VolunteerRegistration() {}

    public VolunteerRegistration(Volunteer volunteer, Event event) {
        this.volunteer = volunteer;
        this.event = event;
        this.registrationDate = LocalDateTime.now();
        this.status = "REGISTERED";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Volunteer getVolunteer() { return volunteer; }
    public void setVolunteer(Volunteer volunteer) { this.volunteer = volunteer; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}