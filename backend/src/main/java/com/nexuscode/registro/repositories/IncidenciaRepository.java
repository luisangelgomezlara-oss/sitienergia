package com.nexuscode.registro.repositories;

import com.nexuscode.registro.entities.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {
    
    // Métodos personalizados que Spring JPA construye automáticamente:
    
    // Para que un Cliente vea sus propios reportes
    List<Incidencia> findByUsuarioId(Long usuarioId);
    
    // Para que un Técnico vea los trabajos que le asignó el Admin
    List<Incidencia> findByTecnicoAsignadoId(Long tecnicoId);
    
    // Para filtrar por estados
    List<Incidencia> findByEstado(String estado);
}