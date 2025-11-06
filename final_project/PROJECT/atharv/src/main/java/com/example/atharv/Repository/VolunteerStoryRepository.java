package com.example.atharv.Repository;
import com.example.atharv.Entity.VolunteerStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VolunteerStoryRepository extends JpaRepository<VolunteerStory, Long> {
    
    List<VolunteerStory> findByPublishedTrueOrderByCreatedAtDesc();
    
    List<VolunteerStory> findByVolunteerIdAndPublishedTrueOrderByCreatedAtDesc(Long volunteerId);
    
    List<VolunteerStory> findByEventIdAndPublishedTrueOrderByCreatedAtDesc(Long eventId);
    
    List<VolunteerStory> findByCategoryAndPublishedTrueOrderByCreatedAtDesc(String category);
    
    @Query("SELECT s FROM VolunteerStory s WHERE s.published = true AND " +
           "(LOWER(s.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.content) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "ORDER BY s.createdAt DESC")
    List<VolunteerStory> searchStories(@Param("query") String query);
    
    @Query("SELECT COUNT(s) FROM VolunteerStory s WHERE s.volunteer.id = :volunteerId")
    int countByVolunteerId(@Param("volunteerId") Long volunteerId);
}