package com.accesodatos.controller.invitation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.accesodatos.dto.apiDto.ApiResponseDto;
import com.accesodatos.dto.invitation.InvitationRequestDto;
import com.accesodatos.dto.invitation.InvitationResponseDto;
import com.accesodatos.service.email.EmailServiceImpl;
import com.accesodatos.service.invitation.InvitationServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/invitation")
@CrossOrigin(origins = "http://localhost:5173/")
@Tag(name = "Invitation", description = "Controller for managing invitations")
public class InvitationController {

    @Autowired
    InvitationServiceImpl invitationService;
    @Autowired EmailServiceImpl emailService;
    
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new invitation", description = "Creates a new invitation for a user to an event")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Invitation created",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InvitationResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    public ResponseEntity<ApiResponseDto<InvitationResponseDto>> createInvitation(
            @Valid @RequestBody InvitationRequestDto dto) {
        InvitationResponseDto response = invitationService.createInvitation(dto);
        return new ResponseEntity<>(new ApiResponseDto<>("Invitation created", HttpStatus.CREATED.value(), response), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update an invitation", description = "Updates the status of an invitation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Invitation updated",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InvitationResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Invitation not found", content = @Content)
    })
    public ResponseEntity<ApiResponseDto<InvitationResponseDto>> updateInvitation(
            @Parameter(description = "ID of the invitation to update", required = true) @PathVariable Long id,
            @Valid @RequestBody InvitationRequestDto dto) {
        InvitationResponseDto response = invitationService.updateInvitation(id, dto);
        return new ResponseEntity<>(new ApiResponseDto<>("Invitation updated", HttpStatus.OK.value(), response), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an invitation", description = "Deletes an invitation by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Invitation deleted"),
        @ApiResponse(responseCode = "404", description = "Invitation not found", content = @Content)
    })
    public ResponseEntity<Void> deleteInvitation(
        @Parameter(description = "ID of the invitation to delete", required = true) @PathVariable Long id) {
        invitationService.deleteInvitation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get invitations by user ID", description = "Fetch all invitations received by a user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Invitations fetched",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InvitationResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<ApiResponseDto<List<InvitationResponseDto>>> getInvitationsByUserId(
        @Parameter(description = "User ID", required = true) @PathVariable Long userId) {
        List<InvitationResponseDto> invitations = invitationService.getAllInvitationsByUserId(userId);
        return new ResponseEntity<>(new ApiResponseDto<>("Invitations fetched", HttpStatus.OK.value(), invitations), HttpStatus.OK);
    }
    
    @PostMapping("/send")
    @Operation(summary = "Send a custom email", description = "Send a custom email (for testing)")
    public String sendEmail(
        @Parameter(description = "Recipient email", required = true) @RequestParam String to,
        @Parameter(description = "Email subject", required = true) @RequestParam String subject,
        @Parameter(description = "Email body", required = true) @RequestParam String body) {
        emailService.sendEmail(to, subject, body);
        return "Correo enviado exitosamente a " + to;
    }
    
    
    
}
