package com.accesodatos.dto.invitation;

import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvitationResponseDto {
    private Long id;
    private Long eventId;
    private Long userId;
    private String status;
    private Date creationDate;
}
