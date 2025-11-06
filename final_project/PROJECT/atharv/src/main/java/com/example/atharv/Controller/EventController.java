package com.example.atharv.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.atharv.Entity.Event;
import com.example.atharv.Service.EventService;
import com.example.atharv.dto.EventDto;

@RestController
@RequestMapping("/api/events")
@CrossOrigin("*")
public class EventController {
    
    @Autowired
    private EventService eventService;

    // Get all events
   

    // Get event by ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        System.out.println("Getting event by ID: " + id);
        Optional<Event> event = eventService.getEventById(id);
        return event.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    // Get events by organizer ID
    @GetMapping("/organizer/{organizerId}")
    public ResponseEntity<List<Event>> getEventsByOrganizer(@PathVariable Long organizerId) {
        try {
            System.out.println("Fetching events for organizer ID: " + organizerId);
            List<Event> events = eventService.getEventsByOrganizerId(organizerId);
            System.out.println("Found " + events.size() + " events for organizer " + organizerId);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            System.out.println("Error fetching organizer events: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Create new event
    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        System.out.println("Creating new event: " + event.getTitle());
        return eventService.createEvent(event);
    }

    // Update event
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        try {
            System.out.println("Updating event ID: " + id);
            Event updatedEvent = eventService.updateEvent(id, eventDetails);
            return ResponseEntity.ok(updatedEvent);
        } catch (RuntimeException e) {
            System.out.println("Event not found for update: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    // Delete event
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        try {
            System.out.println("Deleting event ID: " + id);
            eventService.deleteEvent(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            System.out.println("Event not found for deletion: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    // Get events for volunteer dashboard
    @GetMapping("/volunteer")
    public List<Event> getEventsForVolunteers() {
        System.out.println("Getting events for volunteer dashboard");
        return eventService.getAllEvents();
    }

    @GetMapping
public ResponseEntity<List<EventDto>> getAllEvents() {
    try {
        List<Event> events = eventService.getAllEvents();
        List<EventDto> eventDTOs = events.stream().map(event -> {
            EventDto dto = new EventDto();
            dto.setId(event.getId());
            dto.setTitle(event.getTitle());
            dto.setDescription(event.getDescription());
            dto.setCategory(event.getCategory());
            dto.setEventDate(event.getEventDate());
            dto.setStartTime(event.getStartTime());
            dto.setEndTime(event.getEndTime());
            dto.setLocation(event.getLocation());
            dto.setMaxVolunteers(event.getMaxVolunteers());
            dto.setCurrentVolunteers(event.getCurrentVolunteers());
            dto.setOrganizerId(event.getOrganizerId());
            dto.setOrganizerName(event.getOrganizerName());
            dto.setStatus(event.getStatus());
            return dto;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(eventDTOs);
    } catch (Exception e) {
        return ResponseEntity.badRequest().build();
    }
}
}