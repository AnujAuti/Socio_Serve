package com.example.atharv.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "story_likes")
public class StoryLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "story_id")
    private VolunteerStory story;
    
    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;
    
    private LocalDateTime likedAt;

    public StoryLike() {
        this.likedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }                    
    public VolunteerStory getStory() { return story; }
    public void setStory(VolunteerStory story) { this.story = story; }

    public Volunteer getVolunteer() { return volunteer; }
   public void setVolunteer(Volunteer volunteer) { this.volunteer = volunteer; }
    public LocalDateTime getLikedAt() { return likedAt; }
    public void setLikedAt(LocalDateTime likedAt) { this.likedAt = likedAt; }

}
