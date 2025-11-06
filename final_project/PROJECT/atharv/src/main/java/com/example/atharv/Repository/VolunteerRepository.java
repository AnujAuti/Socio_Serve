package com.example.atharv.Repository;



import com.example.atharv.Entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    Optional<Volunteer> findByEmail(String email);
    boolean existsByEmail(String email);
}
