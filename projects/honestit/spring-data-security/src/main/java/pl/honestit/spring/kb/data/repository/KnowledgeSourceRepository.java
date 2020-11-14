package pl.honestit.spring.kb.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.honestit.spring.kb.data.model.KnowledgeSource;
import pl.honestit.spring.kb.data.model.User;

import java.util.Arrays;
import java.util.Set;

public interface KnowledgeSourceRepository extends JpaRepository<KnowledgeSource, Long> {

    Set<KnowledgeSource> findDistinctSourcesByActiveIsTrueAndKnowingUsersNotContains(User user);
}
