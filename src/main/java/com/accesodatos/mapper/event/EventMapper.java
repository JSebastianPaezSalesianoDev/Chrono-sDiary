package com.accesodatos.mapper.event;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.accesodatos.dto.eventdto.EventRequestDto;
import com.accesodatos.dto.security.EventResponseDto;
import com.accesodatos.entity.EventEntity;

@Mapper(componentModel = "spring")
public interface EventMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "invitations", ignore = true)
	@Mapping(target = "creator", ignore = true)
	EventEntity toEvent(EventRequestDto eventRequestDto);
	
	EventResponseDto toEventResponse(EventEntity eventEntity);
}
