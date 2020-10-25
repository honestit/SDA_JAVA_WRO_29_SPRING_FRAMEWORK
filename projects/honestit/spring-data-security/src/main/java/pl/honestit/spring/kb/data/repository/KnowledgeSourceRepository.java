package pl.honestit.spring.kb.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.honestit.spring.kb.data.model.KnowledgeSource;

public interface KnowledgeSourceRepository extends JpaRepository<KnowledgeSource, Long> {
}
