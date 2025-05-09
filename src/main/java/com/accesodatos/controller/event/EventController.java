package com.accesodatos.controller.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import com.accesodatos.dto.apiDto.ApiResponseDto;
import com.accesodatos.dto.eventdto.EventRequestDto;
import com.accesodatos.dto.eventdto.EventResponseDto;
import com.accesodatos.dto.eventdto.EventSimpleResponseDto;
import com.accesodatos.service.event.EventServiceImpl;

import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173/")
public class EventController {	
	
	private static final String  EVENT_RESOURCE = "/event";
	
	private static final String EVENT_USER = EVENT_RESOURCE + "/{id}";
	

	
	@Autowired
	EventServiceImpl eventServiceImpl;
	
	@GetMapping(EVENT_RESOURCE + "/ping")
	public ResponseEntity<String> pong() {
		return ResponseEntity.ok("pong event...");
	}
	
	@GetMapping(value = EVENT_USER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<EventResponseDto>> getEventById(@PathVariable Long id){
		EventResponseDto event = eventServiceImpl.getEventById(id);
		ApiResponseDto<EventResponseDto> response = new ApiResponseDto<>("Event fetched successfully", 
											HttpStatus.OK.value(), event);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping(value = EVENT_RESOURCE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<EventResponseDto>> createEvent(
			@Valid @RequestBody EventRequestDto eventRequestDto) {
		EventResponseDto createEvent = eventServiceImpl.createEvent(eventRequestDto);
		ApiResponseDto<EventResponseDto> response = new ApiResponseDto<>("Event created successfully",
				HttpStatus.CREATED.value(), createEvent);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@GetMapping(value = EVENT_USER + "/event", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<List<EventSimpleResponseDto>>> getEventByUserId(@PathVariable Long id){
		List<EventSimpleResponseDto> events = eventServiceImpl.getAllSimpleEventsByUserId(id);
		ApiResponseDto<List<EventSimpleResponseDto>> response = new ApiResponseDto<>("events fetched successfully", 
											HttpStatus.OK.value(), events);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping(value = EVENT_USER)
	public ResponseEntity<Void> deleteEvent(@PathVariable Long id){
		eventServiceImpl.deleteEvent(id);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PutMapping(value = EVENT_USER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<EventResponseDto>> updateEvent(@PathVariable Long id,
			@Valid @RequestBody EventRequestDto eventRequestDto) {
		EventResponseDto eventUpdated  = eventServiceImpl.updateEvent(id, eventRequestDto);
		ApiResponseDto<EventResponseDto> response = new ApiResponseDto<>("Event updated successfully",
				HttpStatus.OK.value(), eventUpdated);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
