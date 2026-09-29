package com.filmyjury.service;

import com.filmyjury.model.Judge;
import com.filmyjury.repository.JudgeRepository;
import com.filmyjury.repository.ScoreCardRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JudgeService {
    private final JudgeRepository repo;
    private final ScoreCardRepository scoreCardRepository;

    public JudgeService(JudgeRepository repo, ScoreCardRepository scoreCardRepository) {
        this.repo = repo; this.scoreCardRepository = scoreCardRepository;
    }

    public List<Judge> all() { return repo.findAll(); }
    public Judge get(Long id) { return repo.findById(id).orElseThrow(() -> new RuntimeException("Judge not found")); }
    public Judge save(Judge j) { return repo.save(j); }

    public Judge update(Long id, Judge d) {
        Judge j = get(id);
        j.setName(d.getName()); j.setEmail(d.getEmail()); j.setExpertise(d.getExpertise());
        return repo.save(j);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new RuntimeException("Judge not found");
        scoreCardRepository.findByJudgeId(id).forEach(scoreCardRepository::delete);
        repo.deleteById(id);
    }
}
