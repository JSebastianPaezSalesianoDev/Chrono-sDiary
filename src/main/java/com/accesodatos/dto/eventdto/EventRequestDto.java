package com.accesodatos.dto.eventdto;

import java.sql.Date;
import java.util.List;


import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class EventRequestDto {
	private String title;
	private Date startTime;
	private Date endTime;
	private List<String> invitedUserEmails;
	private String location;
	private String description;
}
