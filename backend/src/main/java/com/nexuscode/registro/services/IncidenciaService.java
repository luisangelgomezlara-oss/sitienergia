package com.nexuscode.registro.services;

import com.nexuscode.registro.entities.Incidencia;
import com.nexuscode.registro.dto.IncidenciaRequest;
import java.util.List;

public interface IncidenciaService {
    // Para el Cliente
    Incidencia registrarIncidencia(Long usuarioId, IncidenciaRequest request);
    List<Incidencia> obtenerPorUsuario(Long usuarioId);

    // Para el Admin
    List<Incidencia> obtenerTodas();
    Incidencia asignarTecnico(Long incidenciaId, Long tecnicoId);

    // Para el Técnico
    List<Incidencia> obtenerPorTecnico(Long tecnicoId);
    Incidencia actualizarEstado(Long incidenciaId, String nuevoEstado);
}