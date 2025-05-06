package com.accesodatos.service;

import java.util.List;
import java.util.Optional;

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
import com.accesodatos.repository.EventRepository;
import com.accesodatos.repository.InvitationRepository;
import com.accesodatos.repository.security.UserRepository;

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
            throw new RuntimeException("Usuario no autenticado");
        }

        Optional<UserEntity> creatorOptional = userRepository.findUserEntityByUsername(currentUsername);
        if (!creatorOptional.isPresent()) {
            throw new RuntimeException("Usuario creador no encontrado en la base de datos");
        }
        UserEntity creator = creatorOptional.get();

        EventEntity event = eventMapper.toEvent(eventRequestDto);

        event.setCreator(creator);
        creator.addEventCreated(event);

        List<String> invitedEmails = eventRequestDto.getInvitedUserEmails();
        if (invitedEmails != null && !invitedEmails.isEmpty()) {
            for (String email : invitedEmails) {
                Optional<UserEntity> inviteeOptional = userRepository.findUserEntityByEmail(email);

                if (inviteeOptional.isPresent()) {
                    UserEntity invitee = inviteeOptional.get();

                    if (!invitee.getId().equals(creator.getId())) {
                        Invitation invitation = new Invitation();
                        invitation.setEvent(event);
                        invitation.setInvitee(invitee);
                        invitation.setStatus("PENDING");

                        event.addInvitation(invitation);
                        invitee.addInvitationReceived(invitation);
                    }
                } else {
                    System.out.println("WARN: Correo de invitado no encontrado: " + email);
                }
            }
        }

        EventEntity savedEvent = eventRepository.save(event);

        return eventMapper.toEventResponse(savedEvent);
    }

	@Override
	public List<EventSimpleResponseDto> getAllSimpleEventsByUserId(Long id) {
		
		UserEntity user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(String.format("User not found", id)));
		
		return user.getEvents().stream().map(eventMapper::toEventSimpleResponse).toList();
	}

	@Override
	public void deleteEvent(Long id) {
		EventEntity event = eventRepository.findById(id)
		.orElseThrow(() -> new ResourceNotFoundException(String.format("Event not found, id:", id)));
		
		eventRepository.delete(event);
	}
}