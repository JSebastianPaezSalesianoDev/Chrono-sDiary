package com.accesodatos.service;

import com.accesodatos.dto.eventdto.EventRequestDto;
import com.accesodatos.dto.security.EventResponseDto;

public interface EventService {
	EventResponseDto createEvent(EventRequestDto eventRequestDto);
}
