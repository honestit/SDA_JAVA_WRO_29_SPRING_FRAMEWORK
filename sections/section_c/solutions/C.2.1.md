Wszystkie trzy komponenty repozytoriów będą interfejsami rozszerzającymi `JpaRepository` i sparametryzowanymi klasą odpowiedniej encji oraz klasą klucza głównego tej encji. Dzidziczenie po interfejsie `JpaRepository` daje w naszych repozytoriach automatycznie możliwość wykorzystania metod związanych z tworzeniem, modyfikowaniem, usuwaniem i odczytywaniem pojedynczej encji, pobieraniem grupy encji czy wykorzytaniem paginacji i sortowania wyników (tego obszaru nie omawiamy).

Poniżej znajduje się kod deklaracji wszystkich trzech repozytoriów:

1. Repozytorium `UserRepository`:

   ```java
    package pl.honestit.spring.kb.data.repositories;
    
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.stereotype.Repository;
    import pl.honestit.spring.kb.data.model.User;
    
    public interface UserRepository extends JpaRepository<User, Long> {
    }
   ```
   
1. Repozytorium `KnowledgeSourceRepository`:

   ```java
    package pl.honestit.spring.kb.data.repositories;
    
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;
    import pl.honestit.spring.kb.data.model.KnowledgeSource;
    
    public interface KnowledgeSourceRepository extends JpaRepository<KnowledgeSource, Long> {
     
    }
   ```
   
1. Repozytorium `SkillRepository`:

   ```java
    package pl.honestit.spring.kb.data.repositories;
    
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;
    import pl.honestit.spring.kb.data.model.Skill;
    
    public interface SkillRepository extends JpaRepository<Skill, Long> {
     
    }
   ```

Mając tak zdefiniowane repozytoria, każde możemy wstrzyknąć do odpowiednich serwisów. Ze względu na nasz podział aplikacji w ten sposób, że istnieje encja, repozytorium oraz serwis grupujący usługi związane z daną encją, to połączenie repozytorium użytkownika z serwisem obsługi użytkowników wydaję się być intuicyjne. Później może okazać się, że w klasie `UserService` potrzebujemy poza `UserRepository` również `SkillRepository` czy `KnowledgeRepository`. Zawsze możemy dorzucić kolejne zależności do naszych klas.

Wszystkie repozytoria wstrzykujemy z wykorzystaniem konstruktora.

1. Wykorzystanie `UserRepository` w `UserService`:

```java
package pl.honestit.spring.kb.core.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.honestit.spring.kb.data.model.KnowledgeSource;
import pl.honestit.spring.kb.data.model.Skill;
import pl.honestit.spring.kb.data.model.User;
import pl.honestit.spring.kb.data.repositories.UserRepository;
import pl.honestit.spring.kb.dto.KnowledgeSourceDTO;
import pl.honestit.spring.kb.dto.LoggedUserDTO;
import pl.honestit.spring.kb.dto.SkillDTO;
import pl.honestit.spring.kb.dto.TopUserDTO;
import pl.honestit.spring.kb.utils.TestDataGenerator;

import java.util.*;

@Service
@Transactional // Wykorzystujemy adnotację aby wszystkie metody wywoływane były w transakcjach
public class UserService {

    private final UserRepository userRepository;

    // Nie musimy oznaczać konstruktora adnotacją @Autowired, bo jest to jedyny konstruktor
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    
    // ... Pozostała część kodu
}
```

1. Wykorzystanie `KnowledgeSourceRepository` w `KnowledgeSourceService`:

```java
package pl.honestit.spring.kb.core.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.honestit.spring.kb.data.repositories.KnowledgeSourceRepository;
import pl.honestit.spring.kb.dto.AddKnowledgeSourceDTO;
import pl.honestit.spring.kb.dto.KnowledgeSourceDTO;
import pl.honestit.spring.kb.dto.LoggedUserDTO;
import pl.honestit.spring.kb.utils.TestDataGenerator;

import java.util.List;

@Service
@Transactional
public class KnowledgeSourceService {

    private final KnowledgeSourceRepository knowledgeSourceRepository;

    public KnowledgeSourceService(KnowledgeSourceRepository knowledgeSourceRepository) {
        this.knowledgeSourceRepository = knowledgeSourceRepository;
    }
    
    // ... Pozostała część kodu serwisu
}

```

1. Wykorzystanie `SkillRepository` w `SkillService`:

```java
package pl.honestit.spring.kb.core.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.honestit.spring.kb.data.repositories.SkillRepository;
import pl.honestit.spring.kb.dto.SkillDTO;
import pl.honestit.spring.kb.dto.TopSkillDTO;
import pl.honestit.spring.kb.utils.TestDataGenerator;

import java.util.List;

@Service
@Transactional
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }
    
    // ... Pozostała część kodu serwisu
}
```