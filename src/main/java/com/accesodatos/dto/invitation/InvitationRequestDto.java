package com.accesodatos.dto.invitation;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvitationRequestDto {
    private Long eventId;
    private Long userId;
    private String status;
}
