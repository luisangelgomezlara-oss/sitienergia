package com.nexuscode.registro.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidencias")
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación: Una incidencia pertenece a un usuario (Cliente que la reporta)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    // Relación: Una incidencia pertenece a una categoría
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private IncidentCategory categoria;

    // Relación: Un técnico asignado (Puede ser null al principio)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_asignado_id")
    private User tecnicoAsignado;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String ubicacion;

    @Column(nullable = false)
    private String estado; // Ejemplo: "PENDIENTE", "EN_PROCESO", "RESUELTA"

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = "PENDIENTE"; // Estado por defecto al crear
        }
    }

    // Getters y Setters (Puedes generarlos automáticamente con tu IDE o usar Lombok si lo tienen configurado)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUsuario() { return usuario; }
    public void setUsuario(User usuario) { this.usuario = usuario; }
    public IncidentCategory getCategoria() { return categoria; }
    public void setCategoria(IncidentCategory categoria) { this.categoria = categoria; }
    public User getTecnicoAsignado() { return tecnicoAsignado; }
    public void setTecnicoAsignado(User tecnicoAsignado) { this.tecnicoAsignado = tecnicoAsignado; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}