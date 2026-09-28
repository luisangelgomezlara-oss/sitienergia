package com.nexuscode.registro.services;

import com.nexuscode.registro.dto.IncidenciaRequest;
import com.nexuscode.registro.entities.Incidencia;
import com.nexuscode.registro.entities.IncidentCategory;
import com.nexuscode.registro.entities.User;
import com.nexuscode.registro.repositories.IncidenciaRepository;
import com.nexuscode.registro.repositories.IncidentCategoryRepository;
import com.nexuscode.registro.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class IncidenciaServiceImpl implements IncidenciaService {

    private final IncidenciaRepository incidenciaRepository;
    private final UserRepository userRepository;
    private final IncidentCategoryRepository categoryRepository;

    // Inyección de dependencias (SRP: El servicio une los repositorios)
    public IncidenciaServiceImpl(IncidenciaRepository incidenciaRepository, 
                                 UserRepository userRepository, 
                                 IncidentCategoryRepository categoryRepository) {
        this.incidenciaRepository = incidenciaRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Incidencia registrarIncidencia(Long usuarioId, IncidenciaRequest request) {
        User cliente = userRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        
        IncidentCategory categoria = categoryRepository.findById(request.categoriaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));

        Incidencia nuevaIncidencia = new Incidencia();
        nuevaIncidencia.setUsuario(cliente);
        nuevaIncidencia.setCategoria(categoria);
        nuevaIncidencia.setDescripcion(request.descripcion());
        nuevaIncidencia.setUbicacion(request.ubicacion());
        
        return incidenciaRepository.save(nuevaIncidencia);
    }

    @Override
    public List<Incidencia> obtenerPorUsuario(Long usuarioId) {
        return incidenciaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Incidencia> obtenerTodas() {
        return incidenciaRepository.findAll();
    }

    @Override
    public Incidencia asignarTecnico(Long incidenciaId, Long tecnicoId) {
        Incidencia incidencia = incidenciaRepository.findById(incidenciaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Incidencia no encontrada"));
        
        User tecnico = userRepository.findById(tecnicoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Técnico no encontrado"));

        incidencia.setTecnicoAsignado(tecnico);
        incidencia.setEstado("ASIGNADA"); // Cambia el estado automáticamente
        return incidenciaRepository.save(incidencia);
    }

    @Override
    public List<Incidencia> obtenerPorTecnico(Long tecnicoId) {
        return incidenciaRepository.findByTecnicoAsignadoId(tecnicoId);
    }

    @Override
    public Incidencia actualizarEstado(Long incidenciaId, String nuevoEstado) {
        Incidencia incidencia = incidenciaRepository.findById(incidenciaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Incidencia no encontrada"));
        
        incidencia.setEstado(nuevoEstado);
        return incidenciaRepository.save(incidencia);
    }
}