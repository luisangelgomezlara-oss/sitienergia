package com.nexuscode.registro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Usamos "record" de Java que es perfecto y ligero para los DTOs
public record IncidenciaRequest(
    @NotNull(message = "La categoría es obligatoria") 
    Long categoriaId,
    
    @NotBlank(message = "La descripción no puede estar vacía") 
    String descripcion,
    
    @NotBlank(message = "La ubicación es obligatoria") 
    String ubicacion
) {}