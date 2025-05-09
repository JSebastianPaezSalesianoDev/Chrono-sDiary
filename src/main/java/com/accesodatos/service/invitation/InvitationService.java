package com.accesodatos.service.invitation;

import java.util.List;

import com.accesodatos.dto.invitation.InvitationRequestDto;
import com.accesodatos.dto.invitation.InvitationResponseDto;

public interface InvitationService {
    InvitationResponseDto createInvitation(InvitationRequestDto dto);
    InvitationResponseDto updateInvitation(Long id, InvitationRequestDto dto);
    void deleteInvitation(Long id);
    List<InvitationResponseDto> getAllInvitationsByUserId(Long userId);
}