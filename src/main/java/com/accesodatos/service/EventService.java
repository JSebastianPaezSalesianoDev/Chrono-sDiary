package com.accesodatos.service;

import java.util.List;

import com.accesodatos.dto.eventdto.EventRequestDto;
import com.accesodatos.dto.eventdto.EventResponseDto;
import com.accesodatos.dto.eventdto.EventSimpleResponseDto;

public interface EventService {
	EventResponseDto createEvent(EventRequestDto eventRequestDto);
	List<EventSimpleResponseDto> getAllSimpleEventsByUserId(Long id);
	void deleteEvent(Long id);
	EventResponseDto updateEvent(Long id, EventRequestDto eventRequestDto);
}
