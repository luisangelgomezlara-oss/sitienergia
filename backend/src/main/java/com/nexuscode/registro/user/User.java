package com.nexuscode.registro.user;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String nombre;
    @Column(nullable = false, unique = true) private String email;
    @Column(nullable = false) private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Role rol;
    private String telefono;
    @Column(nullable = false) private boolean activo = true;

    protected User() {}
    public User(String nombre, String email, String passwordHash, Role rol, String telefono) {
        this.nombre = nombre; this.email = email; this.passwordHash = passwordHash; this.rol = rol; this.telefono = telefono;
    }
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public Role getRol() { return rol; }
    public void setRol(Role rol) { this.rol = rol; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}