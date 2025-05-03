package com.accesodatos.dto.security;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventResponseDto {
	private Long id;
	private String title;
	private String Description;
	private Date startTime;
	private Date endTime;
	private String location;
	
}
