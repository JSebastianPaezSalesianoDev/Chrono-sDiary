package com.accesodatos.dto.invitation;

import java.sql.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor; 

@Data
@NoArgsConstructor
@AllArgsConstructor 
public class InvitationResponseDto {
    private Long id;
    private Long eventId;
    private String eventTitle; 
    private Long invitingUserId; 
    private String invitingUserName;
    private Long userId; 
    private String status;
    private Date creationDate;
}