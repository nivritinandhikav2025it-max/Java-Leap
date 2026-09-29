package com.filmyjury.service;

import com.filmyjury.model.Entry;
import com.filmyjury.model.Judge;
import com.filmyjury.model.ScoreCard;
import com.filmyjury.repository.EntryRepository;
import com.filmyjury.repository.JudgeRepository;
import com.filmyjury.repository.ScoreCardRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ScoreCardService {
    private final ScoreCardRepository repo;
    private final EntryRepository entryRepo;
    private final JudgeRepository judgeRepo;
    private final EntryService entryService;

    public ScoreCardService(ScoreCardRepository repo, EntryRepository entryRepo,
                            JudgeRepository judgeRepo, EntryService entryService) {
        this.repo = repo; this.entryRepo = entryRepo; this.judgeRepo = judgeRepo; this.entryService = entryService;
    }

    public List<ScoreCard> all() { return repo.findAll(); }
    public ScoreCard get(Long id) { return repo.findById(id).orElseThrow(() -> new RuntimeException("Score card not found")); }

    public ScoreCard create(Long entryId, Long judgeId, ScoreCard data) {
        if (repo.existsByJudgeIdAndEntryId(judgeId, entryId)) {
            throw new IllegalArgumentException("This judge has already submitted a scorecard for this entry.");
        }
        Entry entry = entryRepo.findById(entryId).orElseThrow(() -> new RuntimeException("Entry not found"));
        Judge judge = judgeRepo.findById(judgeId).orElseThrow(() -> new RuntimeException("Judge not found"));
        data.setEntry(entry); data.setJudge(judge); data.calculateTotal();
        ScoreCard saved = repo.save(data);
        entryService.recalculateFinalScore(entryId);
        return saved;
    }

    public ScoreCard update(Long id, Long entryId, Long judgeId, ScoreCard data) {
        ScoreCard s = get(id);
        if (!s.getEntry().getId().equals(entryId) || !s.getJudge().getId().equals(judgeId)) {
            if (repo.existsByJudgeIdAndEntryId(judgeId, entryId)) {
                throw new IllegalArgumentException("This judge already has another scorecard for this entry.");
            }
            Entry e = entryRepo.findById(entryId).orElseThrow(() -> new RuntimeException("Entry not found"));
            Judge j = judgeRepo.findById(judgeId).orElseThrow(() -> new RuntimeException("Judge not found"));
            s.setEntry(e); s.setJudge(j);
        }
        s.setCreativity(data.getCreativity()); s.setOriginality(data.getOriginality());
        s.setTechnical(data.getTechnical()); s.setPresentation(data.getPresentation());
        s.calculateTotal();
        ScoreCard saved = repo.save(s);
        entryService.recalculateFinalScore(saved.getEntry().getId());
        return saved;
    }

    public void delete(Long id) {
        ScoreCard s = get(id);
        Long entryId = s.getEntry().getId();
        repo.deleteById(id);
        entryService.recalculateFinalScore(entryId);
    }
}
