package pl.honestit.spring.kb.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.honestit.spring.kb.data.model.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {
}
