package com.accesodatos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accesodatos.dto.eventdto.EventRequestDto;
import com.accesodatos.dto.security.EventResponseDto;
import com.accesodatos.entity.EventEntity;
import com.accesodatos.mapper.event.EventMapper;
import com.accesodatos.repository.EventRepository;

@Service
public class EventServiceImpl implements EventService{
	
	@Autowired
	EventRepository eventRepository;
	@Autowired
	EventMapper eventMapper;
	@Override
	public EventResponseDto createEvent(EventRequestDto eventRequestDto) {
		EventEntity event = eventMapper.toEvent(eventRequestDto);
		EventEntity savedEvent = eventRepository.save(event);
		return eventMapper.toEventResponse(savedEvent);
	}

}
