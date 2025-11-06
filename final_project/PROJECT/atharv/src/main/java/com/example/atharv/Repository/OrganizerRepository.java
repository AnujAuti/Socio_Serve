package com.example.atharv.Repository;



import com.example.atharv.Entity.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrganizerRepository extends JpaRepository<Organizer, Long> {
    Optional<Organizer> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Organizer> findByOrganizationName(String organizationName);
}
