package com.example.atharv.Service;
import com.example.atharv.Entity.Event;
import com.example.atharv.Entity.StoryLike;
import com.example.atharv.Entity.Volunteer;

import com.example.atharv.Entity.VolunteerStory;
import com.example.atharv.Repository.EventRepository;
import com.example.atharv.Repository.StoryLikeRepository;
import com.example.atharv.Repository.VolunteerRepository;
import com.example.atharv.Repository.VolunteerStoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VolunteerStoryService 
{
    
    @Autowired
    private VolunteerStoryRepository storyRepository;
    
    @Autowired
    private StoryLikeRepository likeRepository;
    
    @Autowired
    private VolunteerRepository volunteerRepository;
    
    @Autowired
    private EventRepository eventRepository;

    // Create a new story
    public VolunteerStory createStory(CreateStoryRequest request) {
        Volunteer volunteer = volunteerRepository.findById(request.getVolunteerId())
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));
        
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        VolunteerStory story = new VolunteerStory();
        story.setTitle(request.getTitle());
        story.setContent(request.getContent());
        story.setImageUrl(request.getImageUrl());
        story.setVolunteer(volunteer);
        story.setEvent(event);
        story.setCategory(request.getCategory());
        story.setPublished(request.isPublished());

        return storyRepository.save(story);
    }

    // Get all published stories
    public List<VolunteerStory> getPublishedStories() {
        return storyRepository.findByPublishedTrueOrderByCreatedAtDesc();
    }

    // Get stories by volunteer
    public List<VolunteerStory> getStoriesByVolunteer(Long volunteerId) {
        return storyRepository.findByVolunteerIdAndPublishedTrueOrderByCreatedAtDesc(volunteerId);
    }

    // Like/unlike a story
    public boolean toggleLike(Long storyId, Long volunteerId) {
        VolunteerStory story = storyRepository.findById(storyId)
                .orElseThrow(() -> new RuntimeException("Story not found"));
        
        Volunteer volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));

        Optional<StoryLike> existingLike = likeRepository.findByStoryAndVolunteer(story, volunteer);

        if (existingLike.isPresent()) {
            // Unlike
            likeRepository.delete(existingLike.get());
            story.setLikes(story.getLikes() - 1);
            storyRepository.save(story);
            return false;
        } else {
            // Like
            StoryLike like = new StoryLike();
            like.setStory(story);
            like.setVolunteer(volunteer);
            likeRepository.save(like);
            story.setLikes(story.getLikes() + 1);
            storyRepository.save(story);
            return true;
        }
    }

    // Check if user liked a story
    public boolean hasUserLikedStory(Long storyId, Long volunteerId) {
        VolunteerStory story = storyRepository.findById(storyId)
                .orElseThrow(() -> new RuntimeException("Story not found"));
        
        Volunteer volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));

        return likeRepository.existsByStoryAndVolunteer(story, volunteer);
    }

    // Search stories
    public List<VolunteerStory> searchStories(String query) {
        return storyRepository.searchStories(query);
    }

    // Get popular stories (most liked)
    public List<VolunteerStory> getPopularStories(int limit) {
        return storyRepository.findByPublishedTrueOrderByCreatedAtDesc().stream()
                .sorted((s1, s2) -> Integer.compare(s2.getLikes(), s1.getLikes()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static class CreateStoryRequest {
        private String title;
        private String content;
        private String imageUrl;
        private Long volunteerId;
        private Long eventId;
        private String category;
        private boolean published = true;

        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public Long getVolunteerId() { return volunteerId; }
        public void setVolunteerId(Long volunteerId) { this.volunteerId = volunteerId;
            
        
    }

        public Long getEventId() { return eventId; }
        public void setEventId(Long eventId) { this.eventId = eventId; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public boolean isPublished() { return published; }
        public void setPublished(boolean published) { this.published = published; }
}


public boolean isVolunteerRegisteredForEvent(Long volunteerId, Long eventId) {
    // This should check if the volunteer is registered for the event
    // You'll need to implement this based on your registration system
    try {
        // Example implementation - adjust based on your registration entity
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        
        // Check if volunteer is in the registered volunteers list
        // This depends on how you track registrations
        return event.getRegisteredVolunteers() != null && 
               event.getRegisteredVolunteers().stream()
                    .anyMatch(v -> v.equals(volunteerId));
    } catch (Exception e) {
        return false;
    }
}
}
