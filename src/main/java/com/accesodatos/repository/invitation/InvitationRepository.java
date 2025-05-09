package com.accesodatos.repository.invitation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.accesodatos.entity.Invitation;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
}
