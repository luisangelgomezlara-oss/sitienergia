package com.nexuscode.registro.controllers;

import com.nexuscode.registro.dto.IncidenciaRequest;
import com.nexuscode.registro.entities.Incidencia;
import com.nexuscode.registro.services.IncidenciaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidencias")
public class IncidenciaController {

    // Principio SOLID (DIP): Dependemos de la interfaz, no de la implementación
    private final IncidenciaService incidenciaService;

    public IncidenciaController(IncidenciaService incidenciaService) {
        this.incidenciaService = incidenciaService;
    }

    // --------------------------------------------------------
    // ENDPOINTS PARA EL CLIENTE
    // --------------------------------------------------------
    
    @PostMapping("/cliente/{usuarioId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')") // Solo clientes (o el admin) pueden reportar
    public Incidencia registrar(@PathVariable Long usuarioId, @Valid @RequestBody IncidenciaRequest request) {
        return incidenciaService.registrarIncidencia(usuarioId, request);
    }

    @GetMapping("/cliente/{usuarioId}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public List<Incidencia> obtenerPorCliente(@PathVariable Long usuarioId) {
        return incidenciaService.obtenerPorUsuario(usuarioId);
    }

    // --------------------------------------------------------
    // ENDPOINTS PARA EL ADMINISTRADOR
    // --------------------------------------------------------

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Seguridad estricta: Solo Admin ve todo
    public List<Incidencia> obtenerTodas() {
        return incidenciaService.obtenerTodas();
    }

    @PutMapping("/{id}/asignar/{tecnicoId}")
    @PreAuthorize("hasRole('ADMIN')") // Solo Admin puede asignar trabajos
    public Incidencia asignarTecnico(@PathVariable Long id, @PathVariable Long tecnicoId) {
        return incidenciaService.asignarTecnico(id, tecnicoId);
    }

    // --------------------------------------------------------
    // ENDPOINTS PARA EL TÉCNICO
    // --------------------------------------------------------

    @GetMapping("/tecnico/{tecnicoId}")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')") // Técnico ve su propia lista
    public List<Incidencia> obtenerPorTecnico(@PathVariable Long tecnicoId) {
        return incidenciaService.obtenerPorTecnico(tecnicoId);
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')") // Técnico puede actualizar a "RESUELTA"
    public Incidencia actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return incidenciaService.actualizarEstado(id, estado);
    }
}