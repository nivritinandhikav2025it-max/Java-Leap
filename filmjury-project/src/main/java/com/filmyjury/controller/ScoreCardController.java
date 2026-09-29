package com.filmyjury.controller;

import com.filmyjury.model.ScoreCard;
import com.filmyjury.service.ScoreCardService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scorecards")
public class ScoreCardController {
    private final ScoreCardService service;
    public ScoreCardController(ScoreCardService service) { this.service = service; }

    @GetMapping public List<ScoreCard> all() { return service.all(); }
    @GetMapping("/{id}") public ScoreCard get(@PathVariable Long id) { return service.get(id); }

    @PostMapping
    public ScoreCard create(@RequestBody ScoreCardRequest request) {
        return service.create(request.entryId(), request.judgeId(), request.scoreCard());
    }

    @PutMapping("/{id}")
    public ScoreCard update(@PathVariable Long id, @RequestBody ScoreCardRequest request) {
        return service.update(id, request.entryId(), request.judgeId(), request.scoreCard());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }

    public record ScoreCardRequest(Long entryId, Long judgeId, ScoreCard scoreCard) {}
}
