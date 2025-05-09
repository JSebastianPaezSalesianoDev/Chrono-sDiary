package com.accesodatos.entity;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id") 
@ToString(exclude = {"invitations", "creator"})
@Entity
@Table(name = "Events")
public class EventEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "event_id")
	private Long id;
	
	@Column(name = "title", length = 50)
	private String title;
	
	@Column(name = "description", length = 300)
	private String description;
	
	@CreationTimestamp
	@Column(name = "start_time")
	private Date startTime;
	
	@CreationTimestamp	
	@Column(name = "end_time")
	private Date endTime;
	
	@Column(length = 100)
	private String location;
	
	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Invitation> invitations = new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonBackReference
	private UserEntity creator;
	
	public void addInvitation(Invitation invitation) {
		if (invitations == null) {
			invitations = new ArrayList<>();
		}
		invitations.add(invitation);
		invitation.setEvent(this); 
	}

	public void removeInvitation(Invitation invitation) {
		if (invitations != null) {
			invitations.remove(invitation);
		}
		invitation.setEvent(null); 
	}
	
}
