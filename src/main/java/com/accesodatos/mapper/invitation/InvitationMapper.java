package com.accesodatos.mapper.invitation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.accesodatos.dto.invitation.InvitationRequestDto;
import com.accesodatos.dto.invitation.InvitationResponseDto;
import com.accesodatos.entity.Invitation;

@Mapper(componentModel = "spring")
public interface InvitationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "invitee", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    Invitation toInvitation(InvitationRequestDto dto);

    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "invitee.id", target = "userId")
    InvitationResponseDto toResponseDto(Invitation entity);
}
