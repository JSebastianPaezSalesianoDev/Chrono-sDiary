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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173/")
@Tag(name = "Event", description = "Controller for managing events")
public class EventController {	
	
	private static final String  EVENT_RESOURCE = "/event";
	
	private static final String EVENT_USER = EVENT_RESOURCE + "/{id}";
	

	
	@Autowired
	EventServiceImpl eventServiceImpl;
	
	@GetMapping(EVENT_RESOURCE + "/ping")
	@Operation(summary = "Ping endpoint for Event", description = "Returns pong event...")
	public ResponseEntity<String> pong() {
		return ResponseEntity.ok("pong event...");
	}
	
	@GetMapping(value = EVENT_USER, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get event by ID", description = "Fetch a single event by its ID")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Event fetched successfully",
			content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = EventSimpleResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
	})
	public ResponseEntity<ApiResponseDto<EventSimpleResponseDto>> getEventById(
		@Parameter(description = "ID of the event to fetch", required = true) @PathVariable Long id){
		EventSimpleResponseDto event = eventServiceImpl.getEventById(id);
		ApiResponseDto<EventSimpleResponseDto> response = new ApiResponseDto<>("Event fetched successfully", 
											HttpStatus.OK.value(), event);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping(value = EVENT_RESOURCE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Create a new event", description = "Creates a new event and sends invitations if provided")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Event created successfully",
			content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = EventResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
	})
	public ResponseEntity<ApiResponseDto<EventResponseDto>> createEvent(
			@Valid @RequestBody EventRequestDto eventRequestDto) {
		EventResponseDto createEvent = eventServiceImpl.createEvent(eventRequestDto);
		ApiResponseDto<EventResponseDto> response = new ApiResponseDto<>("Event created successfully",
				HttpStatus.CREATED.value(), createEvent);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@GetMapping(value = EVENT_USER + "/event", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get all events by user ID", description = "Fetch all events created or accepted by a user")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Events fetched successfully",
			content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = EventResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "User not found", content = @Content)
	})
	public ResponseEntity<ApiResponseDto<List<EventResponseDto>>> getEventByUserId(
		@Parameter(description = "User ID", required = true) @PathVariable Long id){
		List<EventResponseDto> events = eventServiceImpl.getAllSimpleEventsByUserId(id);
		ApiResponseDto<List<EventResponseDto>> response = new ApiResponseDto<>("events fetched successfully", 
											HttpStatus.OK.value(), events);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping(value = EVENT_USER)
	@Operation(summary = "Delete an event", description = "Deletes an event by its ID")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Event deleted successfully"),
		@ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
	})
	public ResponseEntity<Void> deleteEvent(
		@Parameter(description = "ID of the event to delete", required = true) @PathVariable Long id){
		eventServiceImpl.deleteEvent(id);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PutMapping(value = EVENT_USER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Update an event", description = "Updates an existing event by its ID")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Event updated successfully",
			content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = EventResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
	})
	public ResponseEntity<ApiResponseDto<EventResponseDto>> updateEvent(
		@Parameter(description = "ID of the event to update", required = true) @PathVariable Long id,
		@Valid @RequestBody EventRequestDto eventRequestDto) {
		EventResponseDto eventUpdated  = eventServiceImpl.updateEvent(id, eventRequestDto);
		ApiResponseDto<EventResponseDto> response = new ApiResponseDto<>("Event updated successfully",
				HttpStatus.OK.value(), eventUpdated);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
