package com.accesodatos.dto.security;



import java.util.List;

import com.accesodatos.dto.eventdto.EventSimpleResponseDto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor	
public class UserEventsResponse {
	private Long id;
	private String username;
	private List<EventSimpleResponseDto> events;
}
