package com.nexuscode.registro.category;

import jakarta.persistence.*;

@Entity
@Table(name = "categorias_incidencia")
public class IncidentCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String nombre;
    @Column(nullable = false) private String area;
    @Column(nullable = false) private boolean activa = true;

    protected IncidentCategory() {}
    public IncidentCategory(String nombre, String area) { this.nombre = nombre; this.area = area; }
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}