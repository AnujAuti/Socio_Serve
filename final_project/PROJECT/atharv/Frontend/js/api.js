// API Base URL
const API_BASE = 'http://localhost:8092/api';

// Volunteer APIs
async function registerVolunteer(volunteerData) {
    console.log('Sending registration data:', volunteerData);
    
    try {
        const response = await fetch(`${API_BASE}/volunteers/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(volunteerData)
        });
        
        console.log('Response status:', response.status);
        
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || `HTTP error! status: ${response.status}`);
        }
        
        const result = await response.json();
        console.log('Registration successful:', result);
        return result;
        
    } catch (error) {
        console.error('Registration API error:', error);
        throw error;
    }
}

async function loginVolunteer(email, password) {
    console.log('Attempting login for:', email);
    
    try {
        const response = await fetch(`${API_BASE}/volunteers/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password })
        });
        
        console.log('Login response status:', response.status);
        
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || `HTTP error! status: ${response.status}`);
        }
        
        const result = await response.json();
        console.log('Login successful:', result);
        return result;
        
    } catch (error) {
        console.error('Login API error:', error);
        throw error;
    }
}

// Event APIs
async function getAllEvents() {
    try {
        const response = await fetch(`${API_BASE}/events`);
        if (!response.ok) throw new Error('Failed to fetch events');
        return await response.json();
    } catch (error) {
        console.error('Error fetching events:', error);
        return [];
    }
}

async function getUpcomingEvents() {
    try {
        const response = await fetch(`${API_BASE}/events/upcoming`);
        if (!response.ok) throw new Error('Failed to fetch upcoming events');
        return await response.json();
    } catch (error) {
        console.error('Error fetching upcoming events:', error);
        return [];
    }
}

// Registration APIs
async function registerForEvent(volunteerId, eventId) {
    try {
        const response = await fetch(`${API_BASE}/registrations/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ volunteerId, eventId })
        });
        
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || `HTTP error! status: ${response.status}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('Event registration error:', error);
        throw error;
    }
}

async function getVolunteerRegistrations(volunteerId) {
    try {
        const response = await fetch(`${API_BASE}/registrations/volunteer/${volunteerId}`);
        if (!response.ok) throw new Error('Failed to fetch registrations');
        return await response.json();
    } catch (error) {
        console.error('Error fetching registrations:', error);
        return [];
    }
}

async function cancelRegistration(registrationId) {
    try {
        const response = await fetch(`${API_BASE}/registrations/${registrationId}`, {
            method: 'DELETE'
        });
        return response.ok;
    } catch (error) {
        console.error('Error cancelling registration:', error);
        return false;
    }


}
// Organizer APIs
async function registerOrganizer(organizerData) {
    console.log('Sending organizer registration:', organizerData);
    
    try {
        const response = await fetch(`${API_BASE}/organizers/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(organizerData)
        });
        
        console.log('Organizer registration response status:', response.status);
        
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || `HTTP error! status: ${response.status}`);
        }
        
        const result = await response.json();
        console.log('Organizer registration successful:', result);
        return result;
        
    } catch (error) {
        console.error('Organizer registration API error:', error);
        throw error;
    }
}

async function loginOrganizer(email, password) {
    console.log('Attempting organizer login for:', email);
    
    try {
        const response = await fetch(`${API_BASE}/organizers/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password })
        });
        
        console.log('Organizer login response status:', response.status);
        
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || `HTTP error! status: ${response.status}`);
        }
        
        const result = await response.json();
        console.log('Organizer login successful:', result);
        return result;
        
    } catch (error) {
        console.error('Organizer login API error:', error);
        throw error;
    }
}

async function getOrganizerEvents(organizerId) {
    try {
        const response = await fetch(`${API_BASE}/events/organizer/${organizerId}`);
        if (!response.ok) throw new Error('Failed to fetch organizer events');
        return await response.json();
    } catch (error) {
        console.error('Error fetching organizer events:', error);
        return [];
    }
}

async function createEvent(eventData) {
    try {
        const response = await fetch(`${API_BASE}/events`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(eventData)
        });
        
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || `HTTP error! status: ${response.status}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('Event creation error:', error);
        throw error;
    }
}