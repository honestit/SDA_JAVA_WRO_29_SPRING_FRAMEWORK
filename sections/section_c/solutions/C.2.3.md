W tym zadaniu nie musimy w żaden sposób rozszerzać inferfejsu repozytorium użytkownika. Proces tu obsługiwany jest dostępny tylko dla zalogowanego użytkownika, a zalogowany użytkownik został zapisany po zalogowaniu w sesji jako obiekt klasy `LoggedUserDTO`. W obiekcie tym mamy zapisany login użytkownika oraz jego identyfikator. Dzięki temu możemy w serwisie pobrać użytkownika po identyfikatorze (kluczu głównym), a metoda tego typu dostępna jest we wszystkich repozytoriach dziedziczących po `JpaRepository`: `findById`

Dalsza implementacja serwisu to pobranie dla użytkownika wszystkich jego źródeł wiedzy, a dla nich wszystkich powiązanych zbiorów umiejętnośći. Ostateczna lista umiejętności ma być listą, a więc zawierać powtórzenia. Dochodzi również konwersja między encjami `Skill` i obiektami `SkillDTO`, które chcemy otrzymać na liście i udostępnić w wyniku do warstwy kontrolerów.

Implementacja metody `List<SkillDTO> getSkillsForUser(LoggedUserDTO user)`:

```java
    public List<SkillDTO> getSkillsForUser(LoggedUserDTO user) {
        User userEntity = userRepository.findById(user.getId()).orElse(null);
        if (userEntity == null) {
            return Collections.emptyList();
        }

        List<SkillDTO> skills = new LinkedList<>();
        for (KnowledgeSource source : userEntity.getKnownSources()) {
            for (Skill skillEntity : source.getConnectedSkills()) {
                SkillDTO skillDTO = new SkillDTO();
                skillDTO.setId(skillEntity.getId());
                skillDTO.setName(skillEntity.getName());
                skillDTO.setCategory(skillEntity.getCategory());
                skills.add(skillDTO);
            }
        }
        return skills;
    }
```

Przy implementacji tej metody warto prześledzić jej działanie, a w szczególności zapytania SQL, które generuje pod spodem Hibernate. Zobaczymy, że mają tutaj miejsce 3 grupy zapytań:
- pojedyncze zapytanie do pobrania użytkownika po kluczu głównym,
- pojedyncze zapytanie do pobrania zbioru źródeł wiedzy powiązanych z użytkownikiem,
- wiele zapytań, po jednym dla każdego źródła wiedzy, które zwracają zbiór umiejętności z tym źródłem wiedzy powiązanych.

Przez trzecią grupę, jest to bardzo niewydajne rozwiązanie już przy pobraniu pojedynczego użytkownika, który posiadają wiele (setki, tysiące) zbiorów danych. Gdyby natomiast to zapytanie uruchomić w pętli dla grupy użytkowników albo wszystkich użytkowników w aplikacji - byłoby to katastrofalne działanie pod względem wydajności. Już na poziomie języka Java mamy tutaj złożoność rzędu N*M (w uproszczeniu N^2). Wprowadzając pętlę po użytkownikach uzyskujemy rozwiązanie o złożonośći N*M*K (w uproszczeniu N^3), a więc nieakceptowalne i przy tym ma tu miejsce N^2 apytań. Dla 100 użytkowników, z których każdy ma 50 znanych źródeł mamy 5000 zapytań do bazy danych!!!

Warto poszukać rozwiązania bardziej wydajnego, które udostępni nam od razu listę encji `Skill` dla zadanego użytkownika, bez pośredniego pobierania źródeł wiedzy.

Poniżej zaprezentowano takie zapytanie, które można wykorzystać w metodzie serwisu dla poprawy wydajności. Kod powinien znaleźć się w repozytorium użytkownika, a samo zapytanie jest oparte o składnię JPQL:

```java

    @Query(value = "SELECT s FROM User u JOIN u.knownSources uks JOIN uks.connectedSkills s WHERE u.id = ?1")
    List<Skill> findAllNonDistinctObtainedSkillsForUser(Long id);
```

Dla naszych danych testowych pierwotna implementacja metody `getSkillsForUser` w `UserService` jest wystarczająca, ale jeżeli chcesz to skorzystaj z poniższej wersji tej metody. Wykorzystujemy w niej dostarczone zapytanie, a samo przetwarzanie bazuje na strumieniach:

```java
    public List<SkillDTO> getSkillsForUserOpt(LoggedUserDTO user) {
        return userRepository.findAllNonDistinctObtainedSkillsForUser(user.getId())
        .stream()
        .filter(skill -> skill != null)
        .map(skill -> {
            SkillDTO skillDTO = new SkillDTO();
            skillDTO.setId(skill.getId());
            skillDTO.setName(skill.getName());
            skillDTO.setCategory(skill.getCategory());
            return skillDTO;
        })
        .collect(Collectors.toList());
    }
```