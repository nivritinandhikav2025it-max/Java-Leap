package com.filmyjury.repository;

import com.filmyjury.model.ScoreCard;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScoreCardRepository extends JpaRepository<ScoreCard, Long> {
    boolean existsByJudgeIdAndEntryId(Long judgeId, Long entryId);
    List<ScoreCard> findByEntryId(Long entryId);
    List<ScoreCard> findByJudgeId(Long judgeId);
}
