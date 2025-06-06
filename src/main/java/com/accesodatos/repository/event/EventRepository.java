package com.accesodatos.repository.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accesodatos.entity.EventEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

}
