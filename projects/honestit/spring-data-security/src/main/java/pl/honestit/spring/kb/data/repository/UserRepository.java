package pl.honestit.spring.kb.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.honestit.spring.kb.data.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
