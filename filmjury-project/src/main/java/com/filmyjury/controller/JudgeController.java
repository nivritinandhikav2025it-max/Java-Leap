package com.filmyjury.controller;

import com.filmyjury.model.Judge;
import com.filmyjury.service.JudgeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/judges")
public class JudgeController {
    private final JudgeService service;
    public JudgeController(JudgeService service) { this.service = service; }

    @GetMapping public List<Judge> all() { return service.all(); }
    @GetMapping("/{id}") public Judge get(@PathVariable Long id) { return service.get(id); }
    @PostMapping public Judge create(@Valid @RequestBody Judge j) { return service.save(j); }
    @PutMapping("/{id}") public Judge update(@PathVariable Long id, @Valid @RequestBody Judge j) { return service.update(id,j); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}
