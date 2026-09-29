package com.filmyjury.service;

import com.filmyjury.model.Entry;
import com.filmyjury.repository.EntryRepository;
import com.filmyjury.repository.ScoreCardRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EntryService {
    private final EntryRepository entryRepository;
    private final ScoreCardRepository scoreCardRepository;

    public EntryService(EntryRepository entryRepository, ScoreCardRepository scoreCardRepository) {
        this.entryRepository = entryRepository;
        this.scoreCardRepository = scoreCardRepository;
    }

    public List<Entry> all() { return entryRepository.findAll(); }
    public Entry get(Long id) { return entryRepository.findById(id).orElseThrow(() -> new RuntimeException("Entry not found")); }
    public Entry save(Entry e) { return entryRepository.save(e); }

    public Entry update(Long id, Entry data) {
        Entry e = get(id);
        e.setTitle(data.getTitle()); e.setGenre(data.getGenre()); e.setTeamName(data.getTeamName());
        e.setVideoLink(data.getVideoLink()); e.setDescription(data.getDescription());
        return entryRepository.save(e);
    }

    public void delete(Long id) {
        if (!entryRepository.existsById(id)) throw new RuntimeException("Entry not found");
        scoreCardRepository.findByEntryId(id).forEach(scoreCardRepository::delete);
        entryRepository.deleteById(id);
    }

    public void recalculateFinalScore(Long entryId) {
        List<com.filmyjury.model.ScoreCard> cards = scoreCardRepository.findByEntryId(entryId);
        Entry e = get(entryId);
        double avg = cards.stream().mapToDouble(c -> c.getTotal() == null ? 0 : c.getTotal()).average().orElse(0);
        e.setFinalScore(Math.round(avg * 100.0) / 100.0);
        entryRepository.save(e);
    }
}
