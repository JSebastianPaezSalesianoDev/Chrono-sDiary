package com.accesodatos.service.event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accesodatos.dto.eventdto.EventRequestDto;
import com.accesodatos.dto.eventdto.EventResponseDto;
import com.accesodatos.dto.eventdto.EventSimpleResponseDto;
import com.accesodatos.entity.EventEntity;
import com.accesodatos.entity.Invitation;
import com.accesodatos.entity.UserEntity;
import com.accesodatos.exception.ResourceNotFoundException;
import com.accesodatos.mapper.event.EventMapper;
import com.accesodatos.repository.event.EventRepository;
import com.accesodatos.repository.invitation.InvitationRepository;
import com.accesodatos.repository.security.UserRepository;
import com.accesodatos.service.email.EmailService;
@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventMapper eventMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    InvitationRepository invitationRepository;

    @Autowired 
    private EmailService emailService;
    @Override
    @Transactional
    public EventResponseDto createEvent(EventRequestDto eventRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            currentUsername = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else if (authentication != null && authentication.getPrincipal() instanceof String) {
            currentUsername = (String) authentication.getPrincipal();
        }

        if (currentUsername == null) {
            throw new RuntimeException("Usuario no autenticado. No se puede crear el evento.");
        }

        UserEntity creator = userRepository.findUserEntityByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario creador no encontrado en la base de datos."));

        EventEntity event = eventMapper.toEvent(eventRequestDto);

        event.setCreator(creator);
        creator.addEventCreated(event);

        List<String> invitedEmails = eventRequestDto.getInvitedUserEmails();
        if (invitedEmails != null && !invitedEmails.isEmpty()) {
            for (String email : invitedEmails) {
                if (email == null || email.trim().isEmpty()) {
                    System.out.println("No hay emails");
                    continue;
                }

                String trimmedEmail = email.trim();
                Optional<UserEntity> inviteeOptional = userRepository.findUserEntityByEmail(trimmedEmail);

                if (inviteeOptional.isPresent()) {
                    UserEntity invitee = inviteeOptional.get();

      
                    if (invitee.getId().equals(creator.getId())) {
                        System.out.println("INFO: El creador del evento (" + creator.getUsername() + ") intentó invitarse a sí mismo.");
                        continue;
                    }

                    Invitation invitation = new Invitation();
                    invitation.setStatus("PENDING");
                    invitation.setEvent(event); 
                    invitation.setInvitee(invitee);

                    event.addInvitation(invitation);
                    invitee.addInvitationReceived(invitation);

                    try {
                        String to = invitee.getEmail();
                        String subject = "Invitación a evento: " + event.getTitle();
             
                        String body = creator.getUsername() + " te ha invitado al evento \"" + event.getTitle() + "\".";

                         emailService.sendEmail(to, subject, body);
                         System.out.println("INFO: Email de invitación enviado a: " + trimmedEmail);
                     } catch (Exception e) {
                         System.err.println("ERROR: No se pudo enviar el email de invitación a " + trimmedEmail + ": " + e.getMessage());
                                         }
              


                } else {
                    System.out.println("Usuario con email '" + trimmedEmail + "' para invitación no encontrado");
                }
            }	
        }

        EventEntity savedEvent = eventRepository.save(event);

        return eventMapper.toEventResponse(savedEvent);
    }
	@Override
	public List<EventResponseDto> getAllSimpleEventsByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with id "+ userId +" not found")));

        List<EventEntity> createdEvents = new ArrayList<>(user.getEvents()); 

  
        List<EventEntity> acceptedInvitedEvents = user.getInvitationsReceived().stream()
                .filter(invitation -> "ACCEPTED".equalsIgnoreCase(invitation.getStatus()))
                .map(Invitation::getEvent) 
                .collect(Collectors.toList());

        
        Set<EventEntity> allAssociatedEventsSet = new HashSet<>(createdEvents);
        allAssociatedEventsSet.addAll(acceptedInvitedEvents);

        return allAssociatedEventsSet.stream()
                .map(eventMapper::toEventResponse) 
                .collect(Collectors.toList());
    }

	@Override
	public void deleteEvent(Long id) {
		EventEntity event = eventRepository.findById(id)
		.orElseThrow(() -> new ResourceNotFoundException(String.format("Event not found, id:", id)));
		
		eventRepository.delete(event);
	}

	@Override
	public EventResponseDto updateEvent(Long id, EventRequestDto eventRequestDto) {
		EventEntity event = eventRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(String.format("Event not found, id:", id)));
		event.setTitle(eventRequestDto.getTitle());
		event.setDescription(eventRequestDto.getDescription());
		event.setStartTime(eventRequestDto.getStartTime());
		event.setEndTime(eventRequestDto.getEndTime());
		event.setLocation(eventRequestDto.getLocation());
		
		EventEntity updatedEvent = eventRepository.save(event);
		
		return eventMapper.toEventResponse(updatedEvent);
	}
	@Override
    public EventSimpleResponseDto getEventById(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event with id: " + eventId + " not found"));
        return eventMapper.toEventSimpleResponse(event);
    }
}