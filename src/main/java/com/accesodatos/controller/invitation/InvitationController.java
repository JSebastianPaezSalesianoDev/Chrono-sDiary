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
import com.accesodatos.service.invitation.InvitationServiceImpl;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/invitation")
@CrossOrigin(origins = "http://localhost:5173/")
public class InvitationController {

    @Autowired
    InvitationServiceImpl invitationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<InvitationResponseDto>> createInvitation(
            @Valid @RequestBody InvitationRequestDto dto) {
        InvitationResponseDto response = invitationService.createInvitation(dto);
        return new ResponseEntity<>(new ApiResponseDto<>("Invitation created", HttpStatus.CREATED.value(), response), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<InvitationResponseDto>> updateInvitation(
            @PathVariable Long id, @Valid @RequestBody InvitationRequestDto dto) {
        InvitationResponseDto response = invitationService.updateInvitation(id, dto);
        return new ResponseEntity<>(new ApiResponseDto<>("Invitation updated", HttpStatus.OK.value(), response), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvitation(@PathVariable Long id) {
        invitationService.deleteInvitation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDto<List<InvitationResponseDto>>> getInvitationsByUserId(@PathVariable Long userId) {
        List<InvitationResponseDto> invitations = invitationService.getAllInvitationsByUserId(userId);
        return new ResponseEntity<>(new ApiResponseDto<>("Invitations fetched", HttpStatus.OK.value(), invitations), HttpStatus.OK);
    }
    
    
    
    
    
    
}
