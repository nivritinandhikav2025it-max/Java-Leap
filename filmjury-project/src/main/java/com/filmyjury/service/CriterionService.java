package com.filmyjury.service;

import com.filmyjury.model.Criterion;
import com.filmyjury.repository.CriterionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CriterionService {
    private final CriterionRepository repo;
    public CriterionService(CriterionRepository repo) { this.repo = repo; }
    public List<Criterion> all() { return repo.findAll(); }
    public Criterion get(Long id) { return repo.findById(id).orElseThrow(() -> new RuntimeException("Criterion not found")); }
    public Criterion save(Criterion c) { return repo.save(c); }
    public Criterion update(Long id, Criterion d) {
        Criterion c = get(id);
        c.setName(d.getName()); c.setMaxScore(d.getMaxScore()); c.setDescription(d.getDescription());
        return repo.save(c);
    }
    public void delete(Long id) { repo.deleteById(id); }
}
