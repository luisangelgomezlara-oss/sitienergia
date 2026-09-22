package com.nexuscode.registro.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/categorias")
public class IncidentCategoryController {
    private final IncidentCategoryRepository categories;
    public IncidentCategoryController(IncidentCategoryRepository categories) { this.categories = categories; }
    @GetMapping public List<IncidentCategory> all() { return categories.findAll(); }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") @ResponseStatus(HttpStatus.CREATED)
    public IncidentCategory create(@Valid @RequestBody CategoryRequest request) { return categories.save(new IncidentCategory(request.nombre(), request.area())); }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')")
    public IncidentCategory update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        IncidentCategory category = categories.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria no encontrada"));
        category.setNombre(request.nombre()); category.setArea(request.area()); return categories.save(category);
    }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) { IncidentCategory category = categories.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)); category.setActiva(false); categories.save(category); }
    public record CategoryRequest(@NotBlank String nombre, @NotBlank String area) {}
}