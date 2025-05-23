package com.accesodatos.service.invitation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accesodatos.dto.invitation.InvitationRequestDto;
import com.accesodatos.dto.invitation.InvitationResponseDto;
import com.accesodatos.entity.EventEntity;
import com.accesodatos.entity.Invitation;
import com.accesodatos.entity.UserEntity;
import com.accesodatos.exception.ResourceNotFoundException;
import com.accesodatos.mapper.invitation.InvitationMapper;
import com.accesodatos.repository.event.EventRepository;
import com.accesodatos.repository.invitation.InvitationRepository;
import com.accesodatos.repository.security.UserRepository;
import com.accesodatos.service.email.EmailService;

@Service
public class InvitationServiceImpl implements InvitationService {

    @Autowired
    InvitationRepository invitationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    InvitationMapper mapper;
    
    @Autowired
    private EmailService emailService;


    /**
     * Crea una nueva invitación y envía un correo al usuario invitado.
     */
    @Override
    public InvitationResponseDto createInvitation(InvitationRequestDto dto) {
        Invitation invitation = mapper.toInvitation(dto);

        EventEntity event = eventRepository.findById(dto.getEventId())
            .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        UserEntity user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserEntity creator = event.getCreator(); 

        invitation.setEvent(event);
        invitation.setInvitee(user);
        invitation.setStatus(dto.getStatus());

        Invitation saved = invitationRepository.save(invitation);

        // Enviar email de invitación
        String to = user.getEmail(); 
        String subject = "Invitación a evento: " + event.getTitle();
        String body = creator.getUsername() + " te ha invitado al evento \"" + event.getTitle() + "\"";

        emailService.sendEmail(to, subject, body);

        return mapper.toResponseDto(saved);
    }


    /**
     * Actualiza el estado de una invitación existente.
     */
    @Override
    public InvitationResponseDto updateInvitation(Long id, InvitationRequestDto dto) {
        Invitation invitation = invitationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));

        invitation.setStatus(dto.getStatus());
        return mapper.toResponseDto(invitationRepository.save(invitation));
    }

    /**
     * Elimina una invitación por su ID.
     */
    @Override
    public void deleteInvitation(Long id) {
        Invitation invitation = invitationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));
        invitationRepository.delete(invitation);
    }

    /**
     * Obtiene todas las invitaciones recibidas por un usuario.
     */
    @Override
    public List<InvitationResponseDto> getAllInvitationsByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getInvitationsReceived()
            .stream()
            .map(invitation -> {
                EventEntity event = invitation.getEvent(); 
                UserEntity invitingUser = event.getCreator(); 

                return new InvitationResponseDto(
                    invitation.getId(),
                    event.getId(),
                    event.getTitle(),
                    invitingUser.getId(),
                    invitingUser.getUsername(),
                    user.getId(), 
                    invitation.getStatus(),
                    invitation.getCreationDate()
                );
            
            })
            .collect(Collectors.toList());
    }
}