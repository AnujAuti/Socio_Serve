package com.example.atharv.Controller;
import com.example.atharv.Entity.VolunteerStory;
import com.example.atharv.Service.VolunteerStoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
    
@RestController
@RequestMapping("/api/stories")
@CrossOrigin("*")
public class VolunteerStoryController {

    @Autowired
    private VolunteerStoryService storyService;

    // Get all published stories
    @GetMapping
    public ResponseEntity<List<VolunteerStory>> getAllStories() {
        try {
            List<VolunteerStory> stories = storyService.getPublishedStories();
            return ResponseEntity.ok(stories);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get stories by volunteer
    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<List<VolunteerStory>> getStoriesByVolunteer(@PathVariable Long volunteerId) {
        try {
            List<VolunteerStory> stories = storyService.getStoriesByVolunteer(volunteerId);
            return ResponseEntity.ok(stories);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Create new story
    @PostMapping
    public ResponseEntity<?> createStory(@RequestBody VolunteerStoryService.CreateStoryRequest request) {
        try {
            VolunteerStory story = storyService.createStory(request);
            return ResponseEntity.ok(story);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating story: " + e.getMessage());
        }
    }

    // Like/unlike a story
    @PostMapping("/{storyId}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long storyId, @RequestParam Long volunteerId) {
        try {
            boolean liked = storyService.toggleLike(storyId, volunteerId);
            Map<String, Object> response = new HashMap<>();
            response.put("liked", liked);
            response.put("message", liked ? "Story liked!" : "Story unliked!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error toggling like: " + e.getMessage());
        }
    }

    // Check if user liked a story
    @GetMapping("/{storyId}/has-liked")
    public ResponseEntity<Boolean> hasUserLiked(@PathVariable Long storyId, @RequestParam Long volunteerId) {
        try {
            boolean hasLiked = storyService.hasUserLikedStory(storyId, volunteerId);
            return ResponseEntity.ok(hasLiked);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    // Search stories
    @GetMapping("/search")
    public ResponseEntity<List<VolunteerStory>> searchStories(@RequestParam String q) {
        try {
            List<VolunteerStory> stories = storyService.searchStories(q);
            return ResponseEntity.ok(stories);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get popular stories
    @GetMapping("/popular")
    public ResponseEntity<List<VolunteerStory>> getPopularStories(@RequestParam(defaultValue = "5") int limit) {
        try {
            List<VolunteerStory> stories = storyService.getPopularStories(limit);
            return ResponseEntity.ok(stories);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
