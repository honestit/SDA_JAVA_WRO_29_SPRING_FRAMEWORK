Tym razem mamy do dostarczenia implementacje dwóch metod, która zbierają dość dużo informacji i udostępniają je w postaci dedykowanych obiektów transferowych `TopSkillDTO` oraz `TopUserDTO`. Zrealizujemy te implementacje w dwóch wariantach:
- pierwszym, zakładającym przetwarzanie po stronie Javy,
- drugim, zakładającym większość przetwarzania po stronie bazy danych.

#### Wariant 1 - Java

Na początek musimy dobrze zrozumieć, co oznacza najlepszy użytkownik. Najlepszy użytkownik to taki, który ma najwięcej nieunikalnych umiejętności. Nie ma znaczenia czy posiada 100 razy tą samą umiejętność, czy 100 unikalnych umiejętności po 1 raz. Istotna jest tutaj suma, która w obu przypadkach daje 100. Możemy to też tak wyjaśnić, że wartość użytkownika na liście top 10 wynika z sumy wszystkich jego poziomów umiejętności na stronie `Twoje Umiejętności`.

Zbudujemy listę 10 obiektów klasy `TopUserDTO`, która będzie reprezentowała 10 użytkowników posiadających największą ilość umiejętności. Interesować nas też będzie ilość unikalnych umiejętności oraz ilość znanych źródeł danych każdego z tych użytkowników.

Całość kodu znajduje się w metodzie `List<TopUserDTO> getTopUsers(int topUsersCount)` klasy `UserService:

```java
    public List<TopUserDTO> getTopUsers(int topUsersCount) {
        // Pobieramy wszystkich użytkowników
        List<User> allUsers = userRepository.findAll();

        // Budujemy mapę - każdemu użytkownikowi przypisując listę jego nieunikalnych umiejętności
        Map<User, List<Skill>> userSkills = new HashMap<>();
        allUsers.forEach(user -> userSkills.put(user, userRepository.findAllNonDistinctObtainedSkillsForUser(user.getId())));

        // Tworzymy listę najlepszych użytkowników jako obiektów TopUserDTO
        List<TopUserDTO> topUsers = new ArrayList<>();
        userSkills.forEach((user, skills) -> {
            TopUserDTO topUserDTO = new TopUserDTO();
            topUserDTO.setLogin(user.getLogin());
            topUserDTO.setAllSkillsCount(skills.size());
            // Listę unikalnych umiejętności wrzucamy do zbioru, aby uzyskać tylko unikalne (bazuje na implementacji metody equals i hashCode w klasie Skill)
            topUserDTO.setUniqueSkillsCount(new HashSet<>(skills).size());
            topUserDTO.setKnowledgeSourceCount(user.getKnownSources().size());
            topUsers.add(topUserDTO);
        });

        // Sortujemy listę w odwrotnej kolejności, a więc najpierw użytkownicy mający najwięcej nieunikalnych umiejętności
        topUsers.sort(Comparator.comparingInt(TopUserDTO::getAllSkillsCount).reversed());

        // Pobieramy maksymalnie topUsersCount, ale nie więcej niż liczba wszystkich użytkowników
        List<TopUserDTO> expectedTopUsersList = topUsers.subList(0, topUsersCount < topUsers.size() ? topUsersCount : topUsers.size());
        return expectedTopUsersList;
    }
```

> Zwróc uwagę, że ta implementacja pobiera całą naszą bazę danych, bo wszystkich użytkowników, dla nich wszystkie nieunikalne ich umiejętności, a potem jeszcze wszystkie źródła wiedzy każdego użytkownika. To co pozostanie nietknięte to tylko nieaktwyne i nieznane nikomu źródła wiedzy.

--- 

Przejdźmy teraz do implementacji drugiej części zadania, a więc pobierania listy najlepszych umiejętności. Tutaj również bazować będziemy na przetwarzaniu z poziomu języka Java. Poniżej znajduje się kod metody `List<TopSkillDTO> getTopSkills()` z klasy `SkillService`. Kolejne kroki zostały opisane komentarzami bezpośrednio w kodzie.

```java
    public List<TopSkillDTO> getTopSkills(int topSkillsCount) {
        // Pobieramy wszystkich użytkowników, aby uzyskać ich umiejętności.
        // W końcu interesują nas tylko umiejętności posiadane przez użytkowników
        List<User> allUsers = userRepository.findAll();

        // Tworzymy mapę użytkowników i ich umiejętności
        Map<User, List<Skill>> usersSkills = new HashMap<>();
        allUsers.forEach(user -> usersSkills.put(user,
                userRepository.findAllNonDistinctObtainedSkillsForUser(user.getId())));

        // Teraz musimy stworzyć mapę obiektów TopSkillDTO, dla której wartościami będzie lista
        // użytkowników którzy tą umiejętność posiadają. Później rozmiar tej listy posłuży nam
        // do wyboru najbardziej popularnych umiejętności, a ilość wystąpień konkretnego użytkownika
        // na tej liście do określenia kto jest TOP w tej umiejętności.
        Map<TopSkillDTO, List<User>> topSkillsToUsers = new HashMap<>();
        usersSkills.forEach((user, skills) -> {
            skills.forEach(skill -> {
                TopSkillDTO topSkillDTO = new TopSkillDTO();
                topSkillDTO.setName(skill.getName());
                topSkillDTO.setCategory(skill.getCategory());
                topSkillsToUsers.merge(topSkillDTO, new ArrayList<User>(Arrays.asList(user)),(oldUsers, newUsers) -> {
                    oldUsers.addAll(newUsers);
                    return oldUsers;
                });
            });
        });

        // Teraz zapiszemy wpisy w mapie w postaci uporządkowanej listy i pobierzemy z niej sublistę o oczekiwanej ilości elementów
        List<Map.Entry<TopSkillDTO, List<User>>> topSkillsEntries = topSkillsToUsers
                .entrySet()
                .stream()
                .sorted((o1, o2) -> - (o1.getValue().size() - o2.getValue().size()))
                .collect(Collectors.toList());
        List<Map.Entry<TopSkillDTO, List<User>>> subList = topSkillsEntries
                .subList(0, topSkillsCount < topSkillsEntries.size() ? topSkillsCount : topSkillsEntries.size());

        // Pozostał teraz dla każdego obiektu TopSkillDTO znaleźć użytkownika, który występuje na liście najwięcej razy.
        List<TopSkillDTO> topSkillsList = new ArrayList<>();
        subList.forEach(topSkillDTOListEntry -> {
            TopSkillDTO topSkill = topSkillDTOListEntry.getKey();
            Map<User, Integer> countingMap = new HashMap<>();
            topSkillDTOListEntry.getValue().forEach(user -> {
                countingMap.merge(user, 1, (oldCount, newCount) -> oldCount + newCount);
            });
            Set<Map.Entry<User, Integer>> countingMapEntries = countingMap.entrySet();
            int topCount = 0;
            User topUser = null;
            for (Map.Entry<User, Integer> entry : countingMapEntries) {
                if (topUser == null || topCount < entry.getValue()) {
                    topUser = entry.getKey();
                    topCount = entry.getValue();
                }
            }
            topSkill.setBestUser(topUser.getLogin());
            topSkillsList.add(topSkill);
        });

        return topSkillsList;
    }
```

> Również w tej implementacji jesteśmy zmuszeni do przejścia po liście wszystkich użytkowników i wykonaniu algorytmu o bardzo dużej złożoności obliczeniowej. Mamy podwójną motywację, aby w większym stopniu zaangażować bazę danych i silnik Hibernate'a niż _natywny_ kod Javy.

---

#### Wariant 2 - baza danych

W wariancie drugim zakładamy wykorzystanie bazy danych i odpowiedniego zapytania do znalezienia tylko tych użytkowników, którzy mają najwięcej powiązanych umiejętności. Nasz kod różnić się będzie tylko jedną linijka, w której zamiast metody `findAll` z `UserRepository` wykorzystamy poniższą metodę:

```java
    @Query("SELECT u FROM User u LEFT JOIN u.knownSources uks LEFT JOIN uks.connectedSkills s GROUP BY u ORDER BY COUNT(s) DESC")
    List<User> findTopUsers(Pageable pageable);
```

Metoda ta wykorzystuje pojedyncze zapytanie, które zwraca użytkowników powiązanych z ich umiejętnościami, grupuje ich oraz sortuje na podstawie listy umiejętności. Dodatkowo metoda przyjmuje jako parametr obiekt klasy `Pageable`, który pozwala na pobieranie częściowych wyników. Wszystko powinno wyjaśnić wykorzystanie tej metody w klasie `UserService`:

```java
    public List<TopUserDTO> getTopUsers(int topUsersCount) {
        // Pobieramy użytkowników według zapytania i tylko 0 stronę (pierwszą) oraz maksymalnie do wskazanej ilości użytkowników
        List<User> allUsers = userRepository.findTopUsers(PageRequest.of(0, topUsersCount));

        // Budujemy mapę - każdemu użytkownikowi przypisując listę jego nieunikalnych umiejętności
        Map<User, List<Skill>> userSkills = new HashMap<>();
        allUsers.forEach(user -> userSkills.put(user, userRepository.findAllNonDistinctObtainedSkillsForUser(user.getId())));

        userSkills.forEach((user, skills) -> System.out.println("user: " + user + ", has skills: " + skills));

        // Tworzymy listę najlepszych użytkowników jako obiektów TopUserDTO
        List<TopUserDTO> topUsers = new ArrayList<>();
        userSkills.forEach((user, skills) -> {
            TopUserDTO topUserDTO = new TopUserDTO();
            topUserDTO.setLogin(user.getLogin());
            topUserDTO.setAllSkillsCount(skills.size());
            // Listę unikalnych umiejętności wrzucamy do zbioru, aby uzyskać tylko unikalne (bazuje na implementacji metody equals i hashCode w klasie Skill)
            topUserDTO.setUniqueSkillsCount(new HashSet<>(skills).size());
            topUserDTO.setKnowledgeSourceCount(user.getKnownSources().size());
            topUsers.add(topUserDTO);
        });

        // Sortujemy listę w odwrotnej kolejności, a więc najpierw użytkownicy mający najwięcej nieunikalnych umiejętności
        topUsers.sort(Comparator.comparingInt(TopUserDTO::getAllSkillsCount).reversed());

        // Pobieramy maksymalnie topUsersCount, ale nie więcej niż liczba wszystkich użytkowników
        List<TopUserDTO> expectedTopUsersList = topUsers.subList(0, topUsersCount < topUsers.size() ? topUsersCount : topUsers.size());
        return expectedTopUsersList;
    }
```