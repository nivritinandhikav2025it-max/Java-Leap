package com.filmyjury.config;

import com.filmyjury.model.Criterion;
import com.filmyjury.repository.CriterionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seedCriteria(CriterionRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(make("Creativity", "Originality and creative approach"));
                repo.save(make("Originality", "Freshness and uniqueness of the concept"));
                repo.save(make("Technical", "Camera, editing, sound and technical execution"));
                repo.save(make("Presentation", "Storytelling, acting and overall presentation"));
            }
        };
    }

    private Criterion make(String name, String description) {
        Criterion c = new Criterion();
        c.setName(name); c.setMaxScore(10); c.setDescription(description);
        return c;
    }
}
