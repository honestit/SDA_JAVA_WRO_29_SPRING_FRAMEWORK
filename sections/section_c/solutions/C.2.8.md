Ostatnia z brakujących operacji, to dodawanie nowego źródła wiedzy. W implementacji tej operacji musimy zadbać o to, aby:
- nazwa źródła wiedzy była unikalna,
- źródło wiedzy posiadało przynajmniej jedną umiejętność, która istnieje w bazie.

Oczywiście nowe źródło wiedzy domyślnie dodajemy jako nieaktywne, aby administrator miał szanse usunięcia tego źródła w przypadku popełnienia błędu. 

Potrzebujemy najpierw jednego prostego fragmentu kodu czyli implementacji metody `List<SkillDTO> getAllSkills()` z klasy `SkillService`, która zwróci listę dostępnych umiejętności dla formularza dodawania nowego źródła wiedzy:

```java
    public List<SkillDTO> getAllSkills() {
        return skillRepository.findAll().stream().map(skill -> {
            SkillDTO skillDTO = new SkillDTO();
            skillDTO.setId(skill.getId());
            skillDTO.setName(skill.getName());
            skillDTO.setCategory(skill.getCategory());
            return skillDTO;
        }).collect(Collectors.toList());
    }
```

Do reppozytorium źródeł wiedzy dodamy metodę, która pozwoli nam szybko sprawdzić czy źródło o danej nazwie już istnieje:

```java
    Optional<KnowledgeSource> findByName(String sourceName);
```

Teraz uzupełniamy implementację metody `boolean addNewSource(KnowledgeSourceDTO newKnowledgeSource)` w klasie `KnowledgeSourceService`:

```java
    public boolean addNewSource(AddKnowledgeSourceDTO newKnowledgeSource) {
        Objects.requireNonNull(newKnowledgeSource);
        Objects.requireNonNull(newKnowledgeSource.getName());
        Objects.requireNonNull(newKnowledgeSource.getConnectedSkillsIds());

        // Sprawdzamy czy źródło o takiej nazwie przypadkiem już nie istnieje
        String sourceName = newKnowledgeSource.getName();
        KnowledgeSource knowledgeSourceByName = knowledgeSourceRepository.findByName(sourceName).orElse(null);
        if (knowledgeSourceByName != null) {
            return false;
        }

        // Tworzymy podstawowy obiekt encji i uzupełniamy proste wartości
        KnowledgeSource entity = new KnowledgeSource();
        entity.setName(sourceName);
        entity.setDescription(newKnowledgeSource.getDescription());
        entity.setUrl(newKnowledgeSource.getUrl());

        // Pobieramy teraz listę umiejętności na podstawie listy identyfikatorów i ją ustawiamy gdy nie
        // jest pusta
        List<Skill> skills = skillRepository.findAllById(newKnowledgeSource.getConnectedSkillsIds());
        // Upewniamy się, że obie listy są sobie równe co do rozmiaru
        if (skills.size() != newKnowledgeSource.getConnectedSkillsIds().size()) {
            throw new IllegalStateException("Wskazana lista umiejętności i uzyskana lista umiejętności są różnej długości!");
        }
        // a jeżeli są puste, to kończymy działanie metody i nie zapisujemy encji
        if (skills.isEmpty()) {
            return false;
        }
        entity.getConnectedSkills().addAll(skills);
        
        // Na koniec zapisujemy encję w bazie danych
        knowledgeSourceRepository.save(entity);
        return true;
    }
```

Dodawanie nowych źródeł wiedzy gotowe!