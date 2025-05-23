package com.accesodatos.repository.invitation;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accesodatos.entity.EventEntity;
import com.accesodatos.entity.Invitation;
import com.accesodatos.entity.UserEntity;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

	    Optional<Invitation> findByEventAndInvitee(EventEntity event, UserEntity invitee);
	
}
