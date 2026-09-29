package com.filmyjury.controller;

import com.filmyjury.model.Criterion;
import com.filmyjury.service.CriterionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/criteria")
public class CriterionController {
    private final CriterionService service;
    public CriterionController(CriterionService service) { this.service = service; }

    @GetMapping public List<Criterion> all() { return service.all(); }
    @GetMapping("/{id}") public Criterion get(@PathVariable Long id) { return service.get(id); }
    @PostMapping public Criterion create(@Valid @RequestBody Criterion c) { return service.save(c); }
    @PutMapping("/{id}") public Criterion update(@PathVariable Long id, @Valid @RequestBody Criterion c) { return service.update(id,c); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}
