package com.accesodatos.dto.security;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsersEventsResponse {
	private List<UserEventsResponse> userEvents;
}
