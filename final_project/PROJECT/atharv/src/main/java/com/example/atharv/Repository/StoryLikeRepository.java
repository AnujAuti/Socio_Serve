package com.example.atharv.Repository;
import com.example.atharv.Entity.StoryLike;
import com.example.atharv.Entity.Volunteer;

import com.example.atharv.Entity.VolunteerStory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryLikeRepository extends JpaRepository<StoryLike, Long> {
    

    Optional<StoryLike> findByStoryAndVolunteer(VolunteerStory story, Volunteer volunteer);
    boolean existsByStoryAndVolunteer(VolunteerStory story, Volunteer volunteer);
    
    void deleteByStoryAndVolunteer(VolunteerStory story, Volunteer volunteer);
    
    int countByStory(VolunteerStory story);
}