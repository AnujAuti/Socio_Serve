package com.example.atharv.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


// Story sharing feature
@Entity
@Table(name = "volunteer_stories")
public class VolunteerStory {
   

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private String imageUrl;
    
    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;
    
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    
    private LocalDateTime createdAt;
    
    private int likes = 0;
    
    private boolean published = false;
    
    private String category; // "INSPIRATION", "EXPERIENCE", "IMPACT"
    
    // Constructors
    public VolunteerStory() {
        this.createdAt = LocalDateTime.now();
    }
    
    public VolunteerStory(String title, String content, Volunteer volunteer, Event event) {
        this();
        this.title = title;
        this.content = content;
        this.volunteer = volunteer;
        this.event = event;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public Volunteer getVolunteer() { return volunteer; }
    public void setVolunteer(Volunteer volunteer) { this.volunteer = volunteer; }
    
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }
    
    public boolean isPublished() { return published; }
    public void setPublished(boolean published) { this.published = published; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}

