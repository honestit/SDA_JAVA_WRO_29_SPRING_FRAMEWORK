Zadanie wykonanymy w trzech krokach. W pierwszej kolejności zróbmy rzecz najłatwiejszą, a więc obsługę listy źródeł wiedzy znanych użytkownikowi. Następnie obsługę źródeł wiedzy nieznanych użytkownikowi (i to jest najtrudniejsze). Na smak zaś będzie samo zatwierdzenie znajomości źródła wiedzy - równie bardzo prosta operacja.

---

**Pobranie źródeł wiedzy znanych użytkownikowi** wymaga od nas tylko i wyłącznie pobrania encji użytkownika, który nas interesuje, i skonwertowania zbioru znanych przez niego encji `KnowledgeSource` na listę obiektów `KnowledgeSourceDTO`. Nie musimy w tym celu dopisywać żadnej nowej metody do repozytorium `UserRepository` ani `KnowledgeSourceRepository`.

Kod metody `List<KnowledgeSourceDTO> getSourcesKnownByUser(LoggedUserDTO user)` w głownej mierze polega na konwersji encji `Skill` do obiektu `SkillDTO` oraz encji `KnowledgeSource` do encji `KnowledgeSourceDTO`. Taki kod będzie się powtarzał, więc wartościowe byłoby wprowadzenie metod statycznych, które taką konwersje realizują (w przyszłości).

```java
    public List<KnowledgeSourceDTO> getSourcesKnownByUser(LoggedUserDTO user) {
        User userEntity = userRepository.findById(user.getId()).orElseThrow(IllegalArgumentException::new);
        return userEntity.getKnownSources().stream().map(sourceEntity -> {
            KnowledgeSourceDTO sourceDTO = new KnowledgeSourceDTO();
            sourceDTO.setId(sourceEntity.getId());
            sourceDTO.setName(sourceEntity.getName());
            sourceDTO.setDescription(sourceEntity.getDescription());
            sourceDTO.setUrl(sourceEntity.getUrl());
            sourceDTO.setActive(sourceEntity.getActive());
            sourceDTO.setConnectedSkills(sourceEntity.getConnectedSkills().stream().map(skillEntity -> {
                SkillDTO skillDTO = new SkillDTO();
                skillDTO.setId(skillEntity.getId());
                skillDTO.setName(skillEntity.getName());
                skillDTO.setCategory(skillEntity.getName());
                return skillDTO;
            }).collect(Collectors.toSet()));
            return sourceDTO;
        }).collect(Collectors.toList());
    }
```

---

**Pobranie źródeł wiedzy nieznanych użytkownikowi** jest już trudniejsze i możemy je zrealizować w wariancie opartym na połączeniu przetwarzania w Javie i na poziomie bazy danych oraz w wariancie wykorzystującym tylko bazę danych.

Pierwsza opcja polegałaby na pobraniu listy wszystkich źródeł wiedzy (metoda `findAll` w `KnowledgeSourceRepository`) oraz odrzuceniu z tej listy źródeł nieaktywnyh i źródeł, które już zna użytkownik. Tak długo jak nieaktywnych źródeł jest mało albo użytkownik nie zna 1/3 wszystkich źródeł, to to rozwiązanie pozostaje efektywne i akceptowalne.

Druga opcja, którą wybieramy, zakłada pojedyncze zapytanie, które już zwróci właściwą listę źródeł wiedzy nieznanych użytkownikowi i aktywnych. Takie zapytanie moglibyśmy napisać natywnie lub spróbować ułożyć jego wersję JPQL. Posłużymy się jednak zapytaniem utworzonym na podstawie nazwy metody. Aby było to możliwe potrzebujemy przejścia ze źródła wiedzy do zbioru powiązanych z nim użytkowników. Obecnie mamy tylko mapowanie od encji `User` do encji `KnowledgeSource`.

Poniżej kod klasy `KnowledgeSource` z mapowaniem w drugą stronę:

```java
package pl.hit.spring.data.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "knowledge_sources")
public class KnowledgeSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String url;
    private Boolean active;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "knowledge_sources_skills",
        joinColumns = @JoinColumn(name = "source_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> connectedSkills = new HashSet<>();

    // Mapowanie w drugą stronę, opartę na mapowaniu z encji `User`
    @ManyToMany(mappedBy = "knownSources")
    private Set<User> knowingUsers = new HashSet<>();

    // Gettery, settery, metody equals, hashCode i toString
}
```

Teraz nic prostszego jak zapisać właściwe zapytanie w `KnowledgeSourceRepository`:

```java
    Set<KnowledgeSource> findDistinctSourcesByActiveIsTrueAndKnowingUsersNotContains(User user);
```

i je wykorzystać w metodzie `List<KnowledgeSourceDTO> getSourcesUnknownByUser(LoggedUserDTO user)` klasy `KnowledgeSourceRepository`:

```java
    public List<KnowledgeSourceDTO> getSourcesUnknownByUser(LoggedUserDTO user) {
        User userEntity = userRepository.findById(user.getId()).orElseThrow(IllegalArgumentException::new);
        return knowledgeSourceRepository.findDistinctSourcesByActiveIsTrueAndKnowingUsersNotContains(userEntity)
                .stream().map(sourceEntity -> {
                    KnowledgeSourceDTO sourceDTO = new KnowledgeSourceDTO();
                    sourceDTO.setId(sourceEntity.getId());
                    sourceDTO.setName(sourceEntity.getName());
                    sourceDTO.setDescription(sourceEntity.getDescription());
                    sourceDTO.setUrl(sourceEntity.getUrl());
                    sourceDTO.setActive(sourceEntity.getActive());
                    sourceDTO.setConnectedSkills(sourceEntity.getConnectedSkills().stream().map(skillEntity -> {
                        SkillDTO skillDTO = new SkillDTO();
                        skillDTO.setId(skillEntity.getId());
                        skillDTO.setName(skillEntity.getName());
                        skillDTO.setCategory(skillEntity.getName());
                        return skillDTO;
                    }).collect(Collectors.toSet()));
                    return sourceDTO;
                }).collect(Collectors.toList());
    }
```

---

**Zatwierdzenie znajomości źródła wiedzy przez użytkownika** to już tylko odnalezienie użytkownika, odnalezienie źródła wiedzy i dodanie go do źródeł wiedzy znanych użytkownikowi. Wykonujemy tą operację warunkowo jeżeli użytkownik takiego źródła jeszcze nie posiada. Wcześniej jednak konieczne jest jeszcze uzupełnienie metody pobierającej źródło wiedzy po identyfikatorze. Zwróć uwagę, że większość kodu tej metody to konwersja z encji na DTO.

Kod metody `KnowledgeSourceDTO getKnowledgeSource(Long sourceId)` w klasie `KnowledgeSourceService`:

```java
    public KnowledgeSourceDTO getSource(Long sourceId) {
        KnowledgeSource knowledgeSource = knowledgeSourceRepository.findById(sourceId).orElseThrow(IllegalArgumentException::new);
        KnowledgeSourceDTO knowledgeSourceDTO = new KnowledgeSourceDTO();
        knowledgeSourceDTO.setId(knowledgeSource.getId());
        knowledgeSourceDTO.setName(knowledgeSource.getName());
        knowledgeSourceDTO.setDescription(knowledgeSource.getDescription());
        knowledgeSourceDTO.setActive(knowledgeSource.getActive());
        knowledgeSourceDTO.setUrl(knowledgeSource.getUrl());
        knowledgeSourceDTO.setConnectedSkills(knowledgeSource.getConnectedSkills().stream().map(skillEntity -> {
            SkillDTO skillDTO = new SkillDTO();
            skillDTO.setId(skillEntity.getId());
            skillDTO.setName(skillEntity.getName());
            skillDTO.setCategory(skillEntity.getName());
            return skillDTO;
        }).collect(Collectors.toSet()));
        return knowledgeSourceDTO;
    }
```

Kod metody `void addNewSource(LoggedUserDTO user, KnowledgeSourceDTO source)` w klasie `UserService`:

```java
    public void addNewSource(LoggedUserDTO user, KnowledgeSourceDTO source) {
        User userEntity = userRepository.findById(user.getId()).orElseThrow(IllegalArgumentException::new);
        KnowledgeSource knowledgeSource = knowledgeSourceRepository.findById(source.getId()).orElseThrow(IllegalArgumentException::new);
        // Sprawdzamy czy źródło jest nieaktywne, bo jeżeli tak to nie można go potwierdzić
        if (!knowledgeSource.getActive()) {
            throw new IllegalArgumentException("Nie można potwierdzić nieaktywnego źródła wiedzy");
        }
        // Dodajemy źródłowie wiedzy użytkownikowi tylko wtedy gdy go jeszcze nie ma
        if (!userEntity.getKnownSources().contains(knowledgeSource)) {
            userEntity.getKnownSources().add(knowledgeSource);
        }
    }
```