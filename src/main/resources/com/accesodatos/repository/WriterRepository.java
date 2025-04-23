package com.accesodatos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accesodatos.entity.Writer;

@Repository
public interface WriterRepository extends JpaRepository<Writer, Long> {

}
