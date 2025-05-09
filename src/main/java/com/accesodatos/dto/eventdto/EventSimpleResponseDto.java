package com.accesodatos.dto.eventdto;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor	
public class EventSimpleResponseDto {
	private Long id;
	private String title;
	private Date startTime;
	private Date endTime;
}
