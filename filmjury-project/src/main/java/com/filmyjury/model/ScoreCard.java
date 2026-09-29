package com.filmyjury.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "score_cards",
       uniqueConstraints = @UniqueConstraint(name = "uk_judge_entry", columnNames = {"judge_id", "entry_id"}))
public class ScoreCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "entry_id")
    @NotNull
    private Entry entry;

    @ManyToOne(optional = false)
    @JoinColumn(name = "judge_id")
    @NotNull
    private Judge judge;

    @Min(0) @Max(10)
    private Double creativity = 0.0;

    @Min(0) @Max(10)
    private Double originality = 0.0;

    @Min(0) @Max(10)
    private Double technical = 0.0;

    @Min(0) @Max(10)
    private Double presentation = 0.0;

    private Double total = 0.0;

    public ScoreCard() {}

    public void calculateTotal() {
        this.total = (creativity + originality + technical + presentation) / 4.0;
    }

    @PrePersist
    @PreUpdate
    public void beforeSave() { calculateTotal(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Entry getEntry() { return entry; }
    public void setEntry(Entry entry) { this.entry = entry; }
    public Judge getJudge() { return judge; }
    public void setJudge(Judge judge) { this.judge = judge; }
    public Double getCreativity() { return creativity; }
    public void setCreativity(Double creativity) { this.creativity = creativity; }
    public Double getOriginality() { return originality; }
    public void setOriginality(Double originality) { this.originality = originality; }
    public Double getTechnical() { return technical; }
    public void setTechnical(Double technical) { this.technical = technical; }
    public Double getPresentation() { return presentation; }
    public void setPresentation(Double presentation) { this.presentation = presentation; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
}
