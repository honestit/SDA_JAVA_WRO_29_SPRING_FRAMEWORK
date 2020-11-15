# D. Testowanie

Przygoda z Frameworkiem Spring zaprowadziła nas w świat aplikacji webowych, które w sprawny sposób obsługują komunikację w protokole HTTP. Aplikacji, które wykorzystują bazy danych znacznie ułatwiając ich obsługę, konfigurację i pisanie zapytań. Również Spring to świat bezpieczeństwa, która dostarcza nam Spring Security. Jednak wszystkie te dobrodziejstwa są niczym, jeżeli nasza aplikacja jest zwyczajnie ... **zawodna**. 

Dzisiaj dostęp do wiedzy jest bardzo łatwy stąd zdecydowanie nad wiedzę przekładana jest zdolność jej wykorzystania i stosowania w praktyce. W domenie programowania jednym z czynników, które rozróżniają osoby z wiedzą, a osoby z umiejętnościami jest zdolność do tworzenia kodu **niezawodnego**.

W kolejnej sekcji sięgamy po testy jednostkowe, aby poprawić jakość naszego kodu i uzyskać jego długotrwałą niezawodność.

---

## Wprowadzenie

W ramach czwartej sekcji (_sekcji D_) kontynuujemy pracę z projektem bazy wiedzy (z sekcji C).

Rozpoczniemy przygodę od przypomnienia sobie podstaw programowania. Rozszerzymy te podstawy również o bardziej zaawansowane techniki testowania związane z parametryzacją testów czy testowanie wyjątków. Napiszemy testy z wykorzystaniem biblioteki `JUnit5` i`AssertJ`. Ta pierwsza część zostanie zrealizowana w oparciu o klasy, które nie posiadają zależności.

No właśnie - dobre testy charakteryzuje określenie FIRST, a więc:
- **F**ast : testy mają być szybkie
- **I**ndependent : testy mają byż niezależne
- **R**epeatable : testy mają być powtarzalne
- **S**elf-checking : same testy wiedzą czy przeszły czy nie, bez pomocy człowieka
- **T**imely : aktualne

Dalszą część naszej pracy poświęcimy testom warstwy serwisowej, a ta warstwa już nie jest niezależna. Będziemy musieli skorzystać z bibliotek mokujących, a więc takich, które potrafią zastąpić rzeczywiste zależności. Wykorzystamy tutaj standard dla języka Java, a więc bibliotekę `Mockito`. Tą samą procedurę możemy powtórzyć dla warstwy prezentacyjnej/komunikacyjnej, a więc kontrolerów.

Do dzieła!

---

## D.1. Testy jednostkowe z JUnit i AssertJ

Sekcję rozpoczniemy od dodania niezbędnych zależności oraz utworzenia podstawowej klasy testowej i najprostszego testu. Dalej przechodzimy do testowania dwóch klas związanych z walidacją danych nowego źródła wiedzy i nowego użytkownika.

Kończąc tą część sekcji uzyskamy:
- zkonfigurowane zależności do testów,
- testy jednostkowe złożonego procesu walidacji,
- testy oparte na asercjach JUnit 5,
- testy oparte na asercjach AssertJ,
- schemat podejścia do testów jednostkowych.

**Do dzieła!**

---

### D.1.1. Konfiguracja zależności i pierwszy test

**Twój Cel:** Uzupełnienie zależności związanych z testami jednostkowymi.

Wykonaj kolejne kroki, aby ukończyć zadanie:

1. W pliku `pom.xml` dodaj następujące zależnośći:
   - `org.junit.jupiter:junit-jupiter:5.6.2`,
   - `org.assertj:assertj-core:3.16.1`
   - `org.mockito:mockito-core:3.3.3`
   - `org.mockito.mockito-junit-jupiter:3.3.3`
   
1. W projekcie utwórz pakiet `validation` wewnątrz pakietu `data` (docelowa struktura: `pl.honestit.spring.kb.data.validation`. Następnie w nim utwórz jeszcze pakiet `validators`.
1. W pakiecie `validators` utwórz klasę `KnowledgeSourceValidator` i umieść w niej poniższy kod (uzupełnij brakujące importy).

   ```java
    @Component @Getter @Setter
    public class KnowledgeSourceValidator {

        private Set<String> badWords = Set.of("dupa", "kupa", "cholera");
        private int minDescriptionLength = 20;
        private int maxDescriptionLength = 160;
        private int minNameLength = 6;
        private int maxNameLength = 120;

        public boolean isValidForSave(KnowledgeSource data) {
            if (data == null) return false;

            if (data.getName() == null || data.getName().isBlank()) return false;
            if (!isNameQualitative(data.getName())) return false;

            if (data.getDescription() == null || data.getDescription().isBlank()) return false;
            if (!isDescriptionQualitative(data.getDescription())) return false;

            if (data.getUrl() == null || data.getUrl().isBlank()) return false;
            if (!isUrlQualitative(data.getUrl())) return false;

            if (data.getActive() == null || data.getActive()) return false;

            if (data.getConnectedSkills() == null || data.getConnectedSkills().isEmpty()) return false;
            return true;
        }

        private boolean isDescriptionQualitative(String description) {
            String[] words = description.split("\\s+");
            if (words.length < 5) return false;

            if (Stream.of(words).anyMatch(badWords::contains)) return false;

            int wordsLength = Stream.of(words).mapToInt(String::length).sum();
            if (wordsLength < minDescriptionLength) return false;
            if (wordsLength > maxDescriptionLength) return false;

            return true;
        }

        private boolean isNameQualitative(String name) {
            String[] words = name.split("\\s+");
            if (words.length < 1) return false;

            if (Stream.of(words).anyMatch(badWords::contains)) return false;

            int wordsLength = Stream.of(words).mapToInt(String::length).sum();
            if (wordsLength < minNameLength) return false;
            if (wordsLength > maxNameLength) return false;

            return true;
        }

        private boolean isUrlQualitative(String url) {
            return url.startsWith("http://") || url.startsWith("https://");
        }
    }
   ```

1. Utwórz klasę testową `KnowledgeSourceValidatorTest` dla naszego walidatora. Klasa powinna powstać w analogicznym pakiecie w strukturze katalogów testowych (katalog `test` zamiast `main`).

   > Tip: możesz skorzystać ze skrótu `ctrl + shift + t` i utworzyć nową klasę testową. IntelliJ zrobi resztę :)
   
1. W klasie testowej dopisz pojedynczą metodę:

   ```java
   @Test
   void check() {
   
   }
   ```
   
   > Uważaj tylko na import. Adnotacja `@Test` powinna pochodzić z pakietu `org.junit.jupiter.api`, a nie z pakietu `org.junit`. To bardzo ważne!
   
1. Uruchom test, aby potwierdzić, że wszystko działa.

**Gratulację!** Udało Ci się poprawnie uzupełnić niezbędne zależności dla testów jednostkowych

---

### D.1.2 Testy jednostkowe dla klasy walidatora

**Twój Cel:** Napisanie testów jednostkowych z wykorzystaniem JUnit5

Konfiguracja naszych zależności oraz pierwsza klasa testowa jest gotowa, więc nic nie stoi na przeszkodzie, aby zacząć testować. Tą pierwszą klasę przetestujemy razem, aby zbudować schemat, z którego możesz korzystać przy kolejnych klasach.

**Gratulację!** Udało Ci się dostarczyć testy dla naszego walidatora. To wielki krok na drodze ku niezawodności!

---

### D.1.3. Testowanie walidacji użytkownika z TDD

**Twój cel**: Ćwiczenia z testów jednostkowych z wykorzystaniem JUnit5, w podejściu TDD

W ramach tego zadania utworzysz klasę walidatora do walidacji zapisu nowego użytkownika (tej funkcjonalności jeszcze nie ma w projekcie, ale ją dodamy). Testy wykonasz w oparciu o schemat z klasy `KnowledgeSourceValidatorTest`.

Wykonaj kolejne kroki:
1. W pakiecie `pl.honestit.spring.kb.data.validation` utwórz klasę `UserValidator`. Klasa ma być poprawnym komponentem Springa.
1. W klasie `UserValidator` napisz metodę `public boolean isValidForSave(User data)`. Pozostaw jej implementację pustą (domyślną, niech zwraca wartość `false`).
1. Utwórz klasę testową `UserValidatorTest` dla klasy `UserValidator`.
1. Bazując na TDD dostarczaj kolejne testy jednostkowe. Po napisaniu testu (który nie przechodzi) uzupełnij implementację metody `isValidFor` tak, aby przeszła test, ale nie pisz więcej niż to, czego test wymaga.

   > Tip: Zacznij od testu, który sprawdzi _happy path_, a więc ścieżkę, w której zakładamy, że wartości wejściowe są poprawne i metoda robi, to co ma zrobić. Następnie możesz dostarczać testy negatywne, czyli takie, które testują przypadki błędnych danych. Tutaj wybierz jedną z dwóch dróg: albo dopisuj testy pod kątem kolejnych pól obiektu `User` albo dopisuj testy dla grup warunków (np. pól, które nie mogą być nullami).

   > Pamiętaj: piszesz tylko jeden test, upewniasz się, że implementacja go nie przechodzi, a następnie uzupełniasz implementację, aby test przeszła. **Nie pisz więcej niż jeden test na raz i nie uzupełniaj implementacji ponad to czego dotyczy test**.

**Gratulację!** Udało Ci się dostarczyć testy dla kolejnego walidatora. W drodze ku niezawodości jesteś coraz dalej :)

---

### D.1.4. Wykorzystanie dodatkowych asercji

**Twój Cel:** Wykorzystanie bardziej rozbudowanych asercji do testowania wyników metod i wyjątków

Klasy walidatorów ograniczały się do zwracania prostej informacji typu `true`/`false` mówiącej o poprawnym wyniku walidacji bądź nie. Nie ma w nich miejsca na bardziej rozbudowane asercje. Teraz sięgniemy po klasy konwerterów, które takie możliwości już nam stworzą.

Testy dla tej klasy napiszemy wspólnie, a następnie ponownie przećwiczysz je na dodatkowej metodzie konwertującej w następnym zadaniu. Jednak przed testami wykonaj kolejne kroki:
1. W projekcie utwórz pakiet `converters` wewnątrz pakietu `data` (pełna ścieżka `pl.honestit.spring.kb.data.converters`).
1. W pakiecie `converters` utwórz klasę `KnowledgeSourceConverter`. Uzupełnij jej implementacje zgodnie z poniższym fragmentem. Uzupełnij brakujące zależności.

   ```java
    @Component
    public class KnowledgeSourceConverter {

        public KnowledgeSource from(AddKnowledgeSourceDTO data) {
            if (data == null) throw new IllegalArgumentException("Nie można konwertować z null");

            KnowledgeSource knowledgeSource = new KnowledgeSource();
            if (data.getName() == null) throw new IllegalArgumentException("Źródło wiedzy musi mieć nazwę");
            knowledgeSource.setName(data.getName());

            if (data.getDescription() == null) throw new IllegalArgumentException("Źródło wiedzy musi mieć opis");
            knowledgeSource.setDescription(data.getDescription());

            if (data.getUrl() == null) throw new IllegalArgumentException("Źródło wiedzy musi mieć url");
            knowledgeSource.setUrl(data.getUrl());

            if (data.getConnectedSkillsIds() == null) throw new IllegalArgumentException("Źródło wiedzy musi mieć umiejętności");
            // TODO Przepisanie umiejętności zrealizujemy w dalszej części

            return knowledgeSource;
        }
    }
   ```
   
Teraz wspólnie przejdziemy do bardziej zaawansowanych przypadków testowych.


**Gratulacje!** Udało Ci się wykorzystać asercje z biblioteki AssertJ do bardziej złożonych przypadków oraz testowania wyjątków.

---

### D.1.5. Testowanie konwersji w źródła wiedzy w TDD

**Twój Cel:** Ćwiczenia z testów jednostkowych z wykorzystaniem bardziej złożonych asercji.

W poprzedniem zadaniu poznałeś/aś wykorzystanie bardziej złożonych asercji z biblioteki `AssertJ` oraz sposoby testowania wyjątków. W tym zadaniu rozwiniesz nasz konwerter o konwersję z obiektu encji na obiekt DTO oraz dostarczysz testy jednostkowe dla tego procesu.

Wykonaj kolejne kroki:
1. Do klasy `KnowledgeSourceConverter` dopisz metodę: `public KnowledgeSourceDTO to(KnowledgeSource data)`. Celem metody będzie utworzenie obiektu `KnowledgeSourceDTO` na podstawie encji `KnowledgeSource`.
1. W oparciu o metodologię TDD uzupełnij klasę testową `KnowledgeSourceConverterTest` o niezbędne testy.

   > Pamiętaj: testy piszesz w metodologii TDD, a więc zaczynasz od testu, a potem implementacja itd.
   

**Gratulacje!** Udało Ci się samodzielnie napisać testy jednostkowe, które dostarczają bardziej złożone asercje.

---

### D.1.6. Wprowadzenie do mockowania zależności

**Twój Cel:** W ramach zadania wprowadzimy podstawy mockowania zależności, które są nam niezbędne, aby dokończyć konwersję

W klasie `KnowledgeSourceConverter` pominęliśmy etap konwertowania identyfikatorów umiejętności z klasy `AddKnowledgeSourceDTO`, dlaczego? Konwersja ta wymagała by od nas odwołania się do bazy danych, aby na podstawie identyfikatora uzyskać obiekt encji `Skill`. To z kolej możemy zrealizować przy pomocy `SkillRepository`. Jednak aby w klasie `KnowledgeSourceConverter` skorzystać ze `SkillRepository`, to musimy wprowadzić zależności.

Zadanie to wykonamy wspólnie, ale możesz też spróbować je zrealizować samodzielnie wykonując następujące kroki:
1. Dodaj do klasy `KnowledgeSourceConverter` pole typu `SkillRepository`. Zadbaj o poprawne wstrzyknięcie tego pola.
1. Uzupełnij implementację metody `from(AddKnowledgeSourceDTO)`, aby konwertowała listę identyfikatorów umiejętności z obiektu `AddKnowledgeSourceDTO` do listy encji `Skill`.

   > W wyniku powyższych dwóch zmian nasze testy jednostkowe mogą przestać się kompilować, bo w niepoprawny sposób tworzymy w nich obiekt klasy `KnowledgeSourceConverter`. Należy to poprawić, ale ... jak dostarczyć implementację interfejsu `SkillRepository`?
   
1. Wykorzystaj bibliotekę `Mockito` i metodę `mock` do utworzenia nowej instancji dla interfejsu `SkillRepository`.
1. Uzupełnij test _happy path_, aby uwzględniał również konwersję listy identyfikatorów umiejętności na listę encji umiejętności.

   > Uwaga: w tym teście zadbaj również o to, aby zweryfikować czy do konwersji została użyta odpowiednia metoda mocka.
   
1. Dopisz test negatywny gdy któryś z identyfikatorów nie został odnaleziony w bazie.
   
**Gratulacje!** Rozpocząłeś/ęłaś mockowanie zależności, to bardzo dobre! W kolejnej części rozwiniemy tą umiejętność :)

---

### Gratulacje!

Przyszedł czas na te duże Gratulacje, bo właśnie udało Ci się ukończyć pierwszą część tej sekcji!

---


## D.2. Mockowanie zależności

Pierwszą część mamy za soba i jednocześnie postawiliśmy pierwsze kroki w mockowaniu zależności. Dzięki temu w drugiej sekcji powinno być łatwiej, bo skupimy się w niej na dalszym pisaniu testów, w których trzeba będzie dużo mockować!

Na początek będziemy pracować wspólnie na serwisie odpowiedzialnym za obsługę źródeł wiedzy, a dokładniej za tworzenie nowego źródła wiedzy. Potem skupisz się na pracy samodzielnej, w której dostarczysz nowy serwis do zapisu użytkownika: nasze preludium do funkcjonalności rejestracji.

**Do dzieła!**

---

### D.2.1. Testowanie usługi zapisu nowego źródła wiedzy

**Twój Cel:** W zadaniu całościowo przetestujemy zapis nowego źródła wiedzy z wykorzystaniem dostarczonych w poprzedniej sekcji walidatora i konwertera.

Zadanie będziemy realizować wspólnie, ale możesz spróbować również samodzielnie - wykonaj wtedy poniższe kroki:
1. Do klasy `KnowledgeSourceService` dodaj zależności do komponentów `KnowledgeSourceValidator` oraz `KnowledgeSourceConverter`.
1. Uzupełni kod metody `addNewSource(AddKnowledgeSourceDTO)` o wykorzystanie walidatora i konwertera.
1. Utwórz klasę testową `KnowledgeSourceServiceTest` i przygotuj w niej strukturę do testów metody `addNewSource(AddKnowledgeSourceDTO)`.
1. Dostarcz zestaw testów, które przetestują _happy path_ oraz przypadki negatywne dla tej funkcjonalności.

   > Zależności `KnowledgeSourceValidator` oraz `KnowledgeSourceConverter` będzie trzeba zamokować.

**Gratulace!** Zadanie zakończone! Udało się przeprowadzić dość rozbudowane testy, które wymagały zamokowania wielu zależności. Dobra robota!

---

### C.2.2. Testowanie usługi zapisu nowego użytkownika.

**Twój Cel:** Na podstawie poprzedniego zadania dostarczysz nowy serwis odpowiedzialny za obsługę użytkowników, a w nim metodę służącą do utworzenia nowego użytkownika w systemie.

To zadanie może być dość trudne, ale mam nadzieję, że sobie poradzisz :)

Wykonaj kolejne kroki:
1. W pakiecie `dto` utwórz nową klasę `AddUserDTO`, która będzie zawierała pola niezbędne do utworzenia (rejestracji) nowego użytkownika.
1. W klasie `UserService` utwórz nową metodę `public void createUser(AddUserDTO addUser)`. Metoda będzie miała za zadanie zapisanie użytkownika (encji `User`) w bazie.
1. Klasę `UserService` uzupełnij o zależności do komponentów walidacji `UserValidator`.
1. Utwórz w pakiecie `converters` klasę `UserConverter`. W klasie tej dostarcz metodę `public User from(AddUserDTO data)` z domyślną implementacją (niech zwraca `null` i niczego więcej nie robi). Uzupełnij w klasie `UserService` zależność do tego konwertera.

   > Zwróć uwagę, że z perspektywy klasy `UserService` nie ma znaczenia jak działa metoda `from` w klasie `UserConverter` - liczy się tylko to, aby ta metoda była. Również w testach jednostkowych klasę `UserConverter` będziemy mockować, więc ta implementacja nie ma znaczenia.
   
1. Utwórz klasę testową `UserServiceTest` oraz przygotuj schemat pod testy metody `createUser`.
1. W klasie testowej dostarcz mocki wymaganych zależności.

   > Teraz będzie pod górkę, ale trzymam za Ciebie kciuki ;)
   
1. Korzystając z TDD rozwijaj klasę testową `UserServiceTest` dopisując kolejne przypadki testowe dla metody `createUser`. Razem z testami uzupełniaj zaleźności.

   > Tip: Pamiętasz, że w projekcie wykorzystujemy warstwę bezpieczeństwa, a hasła nowo tworzonych użytkowników powinny być szyfrowane? Pomyśl czy w Twoim serwisie nie brakuje jeszcze jakiejść zależności i zadbaj, aby Twoje testy nie pominęły tego przypadku!


**Gratulacje!** Zadanie zakończone! Udało Ci się dopisać nową, ważną funkcjonalność i to korzystając z metodologii TDD. Good job!

---

### C.2.3. Nie poprzestajemy w wysiłkach :)

**Twój Cel:** Skoro tak dobrze idzie Ci testowanie kodu, to nie powstrzymuj się... testuj dalej ;)

Wybierz dowolną z usług, która jest realizowana przez serwisy i dostarcz dla niej testy!

> Tip: Proponuję Ci pominąć usługi związane ze zwracaniem listy top użytkowników i top umiejętności... Oczywiście wybierz co chcesz, ale sugerowałbym usługi związane np. z potwierdzaniem źródła wiedzy przez użytkownika, aktywowaniem źródła wiedzy albo usuwaniem źródła wiedzy.

**Gratulacje!** Obojętnie co wybrałeś/aś na pewno są podstawy do gratulacji!

--- 

#### Gratulacje!

Prace nad tym modułem właśnie zostały przez Ciebie zakończone! Świetna robota i jednocześnie początek przygody z zaawansowanym testowaniem!