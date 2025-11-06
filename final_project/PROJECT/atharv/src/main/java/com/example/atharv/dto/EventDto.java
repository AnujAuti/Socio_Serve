package com.example.atharv.dto;



import java.time.LocalDate;
import java.time.LocalTime;

public class EventDto {
    private Long id;
    private String title;
    private String description;
    private String category;
    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private int maxVolunteers;
    private int currentVolunteers;
    private Long organizerId;
    private String organizerName;
    private String status;
    
    // Constructors, getters, and setters
    public EventDto() {}
    
    // Add all getters and setters
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
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}       