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

    @Override
    public InvitationResponseDto createInvitation(InvitationRequestDto dto) {
        Invitation invitation = mapper.toInvitation(dto);

        EventEntity event = eventRepository.findById(dto.getEventId())
            .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        UserEntity user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        invitation.setEvent(event);
        invitation.setInvitee(user);
        invitation.setStatus(dto.getStatus());

        Invitation saved = invitationRepository.save(invitation);
        return mapper.toResponseDto(saved);
    }

    @Override
    public InvitationResponseDto updateInvitation(Long id, InvitationRequestDto dto) {
        Invitation invitation = invitationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));

        invitation.setStatus(dto.getStatus());
        return mapper.toResponseDto(invitationRepository.save(invitation));
    }

    @Override
    public void deleteInvitation(Long id) {
        Invitation invitation = invitationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));
        invitationRepository.delete(invitation);
    }

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