package com.nexuscode.registro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexuscode.registro.entities.IncidentCategory;

public interface IncidentCategoryRepository extends JpaRepository<IncidentCategory, Long> {}