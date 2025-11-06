package com.example.atharv.dto;

public class VolunteerRegistrationDTO {
    private String volunteerEmail;
    private Long eventId;

    // Constructors, getters, setters
    public VolunteerRegistrationDTO() {}

    public VolunteerRegistrationDTO(String volunteerEmail, Long eventId) {
        this.volunteerEmail = volunteerEmail;
        this.eventId = eventId;
    }

    public String getVolunteerEmail() { return volunteerEmail; }
    public void setVolunteerEmail(String volunteerEmail) { this.volunteerEmail = volunteerEmail; }
    
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
}
