package com.filmyjury.controller;

import com.filmyjury.model.Entry;
import com.filmyjury.service.EntryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/entries")
public class EntryController {
    private final EntryService service;
    public EntryController(EntryService service) { this.service = service; }

    @GetMapping public List<Entry> all() { return service.all(); }
    @GetMapping("/{id}") public Entry get(@PathVariable Long id) { return service.get(id); }
    @PostMapping public Entry create(@Valid @RequestBody Entry e) { return service.save(e); }
    @PutMapping("/{id}") public Entry update(@PathVariable Long id, @Valid @RequestBody Entry e) { return service.update(id,e); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}
