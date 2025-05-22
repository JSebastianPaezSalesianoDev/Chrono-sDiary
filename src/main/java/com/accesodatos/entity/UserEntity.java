package com.accesodatos.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"invitationsReceived", "roles", "events"})
@Entity
@Table(name = "users")
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String username;
	
	@Column(name = "email", unique = true, nullable = false, length = 100) 
	private String email; 
	
	@Column(nullable = false)
	private String password;
	
	@Column(name = "is_enabled")
	private boolean isEnabled;
	
	@Column(name = "account_no_expired")
	private boolean accountNoExpired;

	@Column(name = "account_no_locked")
	private boolean accountNoLocked;

	@Column(name = "credential_no_expired")
	private boolean credentialNoExpired;
	
	@OneToMany(mappedBy = "invitee", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	@Builder.Default
	private Set<Invitation> invitationsReceived = new HashSet<>(); 
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name ="user_id"),
									inverseJoinColumns = @JoinColumn(name = "role_id"))
	@Builder.Default
	private Set<Role> roles = new HashSet<>();
	
	@OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	@Builder.Default	
	private List<EventEntity> events = new ArrayList<>();
	
	public void addEventCreated(EventEntity event) {
		if (events == null) {
			events = new ArrayList<>();
		}
		events.add(event);
		event.setCreator(this); 
	}

	public void removeEventCreated(EventEntity event) {
		if (events != null) {
			events.remove(event);
		}
		event.setCreator(null);
	}

	public void addInvitationReceived(Invitation invitation) {
		if (invitationsReceived == null) {
			invitationsReceived = new HashSet<>();
		}
		invitationsReceived.add(invitation);
		invitation.setInvitee(this); 
	}

	public void removeInvitationReceived(Invitation invitation) {
		if (invitationsReceived != null) {
			invitationsReceived.remove(invitation);
		}
		invitation.setInvitee(null);
	}
	
}