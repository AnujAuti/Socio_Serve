package com.example.atharv.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Add @JsonIgnore to prevent circular references
@OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
@JsonIgnore
private List<VolunteerRegistration> volunteerRegistrations = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "event_registered_volunteers", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "volunteer_email")
    private List<String> registeredVolunteers = new ArrayList<>();

    private String title;
    private String description;
    private String category;

    @JsonFormat(pattern = "yyyy-MM-dd")
@Column(name = "event_date")
private LocalDate eventDate;

@JsonFormat(pattern = "HH:mm")
@Column(name = "start_time")
private LocalTime startTime;

@JsonFormat(pattern = "HH:mm")
@Column(name = "end_time")
private LocalTime endTime;

    private String location;

    @Column(name = "max_volunteers")
    private int maxVolunteers;

    @Column(name = "current_volunteers")
    private int currentVolunteers;

    @Column(name = "organizer_id")
    private Long organizerId;

    @Column(name = "organizer_name")
    private String organizerName;


    private String status; // "UPCOMING", "ONGOING", "COMPLETED", "CANCELLED"

    // Transient field for frontend - no database column
    @Transient
    private boolean volunteerRegistered;

    // Constructors
    public Event() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getMaxVolunteers() { return maxVolunteers; }
    public void setMaxVolunteers(int maxVolunteers) { this.maxVolunteers = maxVolunteers; }

    public int getCurrentVolunteers() { return currentVolunteers; }
    public void setCurrentVolunteers(int currentVolunteers) { this.currentVolunteers = currentVolunteers; }

    public Long getOrganizerId() { return organizerId; }
    public void setOrganizerId(Long organizerId) { this.organizerId = organizerId; }

    public String getOrganizerName() { return organizerName; }
    public void setOrganizerName(String organizerName) { this.organizerName = organizerName; }

    public List<VolunteerRegistration> getVolunteerRegistrations() { return volunteerRegistrations; }
    public void setVolunteerRegistrations(List<VolunteerRegistration> volunteerRegistrations) { 
        this.volunteerRegistrations = volunteerRegistrations; 
    }

    public List<String> getRegisteredVolunteers() { return registeredVolunteers; }
    public void setRegisteredVolunteers(List<String> registeredVolunteers) {
        this.registeredVolunteers = registeredVolunteers;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Transient field getters and setters
    public boolean isVolunteerRegistered() { return volunteerRegistered; }
    public void setVolunteerRegistered(boolean volunteerRegistered) { this.volunteerRegistered = volunteerRegistered; }
}