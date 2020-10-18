# C. Obsługa danych i bezpieczeństwa ze Spring Data i Spring Security

Rozpoczynasz przygodę z dwoma kolejnymi projektami ekosystemu Spring: Spring Data oraz Spring Security. Zdecydowana większość systemów w jakiś sposób wykorzystuje dane i prawie zawsze dane te są składowane poza aplikacją, w różnego rodzaju bazach danych. Projekt Spring Data umożliwi Ci wygodne i wydajne przetwarzanie danych zapisanych w bazie danych.

Drugim niezwykle istotnym elementem dzisiejszych aplikacji jest bezpieczeństwo. Z zasadą bezpieczeństwa spotkałeś/aś się przecież już na samym początku nauki języka Java: mowa tu o hermetyzacji. Wszystko w języku Java powinno być z założenia prywatne, a tylko uzasadnione wyjątki zasługują na upublicznienie. To samo podejście powinno być stosowane na wszystkich kolejnych etapach związanych z rozwojem aplikacji - z założenia, wszystko powinno być chronione, a tylko wyjątki upublicznione. Projekt Spring Security pozwala Ci w łatwy i szybki sposób wdrożyć bezpieczeństwo do Twojej aplikacji, zarówno na poziomie uwierzytelniania użytkowników jak i ich autoryzowania.

Celem sekcji jest wyposażenie Cię w zrozumienie oraz umiejętności przetwarzania zewnętrznych danych w Twojej aplikacji. Przetwarzania w sposób bezpieczny.

---

## Wprowadzenie

Z opisu sekcji dowiedziałeś/aś się dlaczego Spring Data i Spring Security są ważne. Teraz przyszedł czas, abyś dowiedział/a się jakie wyzwania stoją przed Tobą w tej sekcji.

Rozpoczniesz pracę z istniejącym projektem wykorzystującym Spring MVC, poprawnie skonfigurowanym i przygotowanym. Projekt reprezentuje prostą aplikację do zarządzania umiejętnościami. 

Umiejętności są zgrupowane w kategoriach, a użytkownik może je zdobywać dodając nowe źródła wiedzy do swojego portfolio. Źródła wiedzy to książki, artykuły, blogi, kursy wideo i inne grupy materiałów. Z każdym takim źródeł związany jest konkretny zestaw umiejętności. Użytkownik potwierdzając znajomość danego źródła nabywa umiejętności. Umiejętności mogą się kumulować, więc jeżeli użytkownik potwierdzi znajomość trzech źródeł związanych z językiem Java, to zyska tą umiejętność 3-krotnie.

Zbiór umiejętności i źródła wiedzy są zarządzane przez administratora, który może je dodawać i usuwać. Każde źródło przy dodawaniu ma określony zestaw umiejętności. Źródeł, które są potwierdzone przez użytkowników (choć jednego) nie można usuwać. Źródła nie można też edytować, więc jedynym sposobem na poprawe błędnie wprowadzonego źródła wiedzy jest jego usunięcie i dodanie na nowo. Stąd źródła wiedzy mają status mówiący o ich aktywności - źródło wiedzy jest dostępne dla użytkowników dopiero po aktywacji, a więc zmianie tego statusu.

Przedstawiony projekt na pewno wygląda dość atrakcyjnie. Ma niestety jedną podstawową wadę - nie do końca działa. Chociaż posiada warstwę widoku i kontrolera, a nawet warstwę serwisów, to brakuję w nim życia. Z wprowadzanymi danymi nic się nie dzieje, a tam gdzie ich oczekujemy nic się nie pojawia. Mało tego - nie jest poprawnie zabezpieczony, bo administratorem może poczuć się każdy użtkownik i bez problemu skorzystać z odpowiedniego panelu, aby dodać "_Przygody Kubusia Puchatka_" jako dobre źródło wiedzy na temat języka Java...

Teraz powinienneś/powinnaś doskonale rozumieć, jak ważne zadanie przed Tobą stawiam!

---

## C.1. Konfiguracja Spring Data

Ropoczynamy, nie inaczej, od konfiguracji Spring Data. Autor projektu nie znał tej technologii, więc zrobił tyle ile potrafił z wykorzystaniem Spring MVC, a resztę zostawił mądrzejszym od siebie. Tutaj właśnie odnajdujesz miejsce na swoje działania!

Kończąc tą część sekcji uzyskamy w pełni poprawnie skonfigurowany projekt:
- wykorzystujący Spring Data, 
- bazujący na istniejącej bazie danych,
- wypełnionej danymi testowymi,
- z poprawną konfiguracją kontekstu i źródeł danych,
- ze zmapowanym światem relacyjnym na świat obiektowy

**Do dzieła!**

---

### C.1.1. Utworzenie projektu (:watch: 15 minut)

**Twój Cel:** Utworzenie poprawnego projektu w IntelliJ, typu maven, na podstawie istniejących źródeł.

W katalogu `projects` w repozytorium znajduje się katalog `KnowledgeApp`. Jest to katalog projektu, który będziesz rozwijać. Jego konfiguracja jest niezależna od środowiska i składa się z katalogu `src` ze źródłami oraz pliku `pom.xml` z konfiguracją mavena.

Wykonaj kolejne kroki, aby ukończyć zadanie:
1. W IntelliJ otwórz katalog `KnowledgeApp` jako nowy projekt.   
1. Jeżeli w katalogu `src` katalogi `java` i `resources` nie zostały poprawnie oznaczone odpowiednio jako katalog plików źródłowych i katalog zasobów, to oznacz je w ten sposób.

   > Opcja `Mark Directory as` z menu kontekstowego
   
1. Sprawdź czy po utworzeniu projekt poprawnie się buduje (nie testuj czy się uruchamia).
1. Sprawdź konfigurację projektu (w menu `File -> Project Structure`):
   - czy dla projektu zostało poprawnie wybrane JDK (minimum Java 1.8),
   - czy istnieje pojedynczy moduł,
   - czy dla modułu zostało porawnie skonfigurowane JDK (minimum Java 1.8),
   - czy w projekcie zostały poprawnie skonfigurowane warstwy (Facets): Spring MVC, Web.
1. Przygotuj konfigurację uruchomieniową w oparciu o Tomcat'a.
1. Uruchom projekt i sprawdź czy pojawi się strona główna.

**Gratulację!** Udało Ci się poprawnie zaimportować projekt i go uruchomić. Możemy teraz przystąpić do analizy tego, co w nim zastałeś/aś.

---

### C.1.2. Analiza projektu (:watch: 30 minut)

**Twój Cel:** Zrozumienie działania projektu oraz elementów każdej z warstw aplikacji

Aplikacja już działa, więc przyszła pora na testy. Zacznij od tego aby ją po prostu poklikać. Pamiętaj tylko, aby zalogować się zanim przejdziesz na którąkolwiek ze stron poza stroną główną. 

> W logowaniu przechodzą każde dane :)

W dalszej kolejności zapoznaj się z opisem warstw/pakietów poniżej oraz klasami, które się w nich znajdują.

`pl.honestit.spring.kb.config` - Pakiet zawierający klasy konfiguracyjne do obsługi głównych komponentów oraz Spring MVC
`pl.honestit.spring.kb.core.service` - Pakiet zawierający klasy reprezentujące komponenty warstwy usługowej (Service Layer)
`pl.honestit.spring.kb.data.model` - Pakiet zawierający klasy będące encjami (ale jeszcze nie działające jako encje)
`pl.honestit.spring.kb.data.repositories` - Pakiet z repozytoriami (ale jeszcze pusty)
`pl.honestit.spring.kb.dto` - Pakiet z klasami implementującymi wzorzec projektowy Data Transfer Object
`pl.honestit.spring.kb.mvc.controllers` - Pakiet z kontrolerami do obsługi widoku
`pl.honestit.spring.kb.util` - Pakiet z klasami pomocniczymi

Prześledź teraz kolejne strony:
- w jaki sposób dane dla widoku są przygotowywane przez kontroler,
- w jaki sposób obsługiwane są linki i formularze na stronach.

Zobacz, że na widokach i w kontrolerach wykorzystujemy tylko klasy z pakietu `pl.honestit.spring.kb.dto`. Nigdzie nie pojawiają się klasy z pakietu `pl.honestit.spring.kb.data.model`. Jest to związane z rozdzieleniem warstw w architekturze tej aplikacji. Encje dzięki temu pozostają w swoim środowisku i nie powodują problemów na poziomie widoków i kontrolerów.

Przyjrzyj się teraz uważnie dostępnym serwisom `UserService`, `SkillService` oraz `KnowledgeSourceService` w pakiecie `pl.honestit.spring.kb.core.services`. Wszystkie posiadają metody wymagane przez kontrolery przy czym każda z metod ma pustą implementację lub implementację wykorzystującą dane testowe. Od tych właśnie klas rozpoczyna się Twoja rola: będziesz uzupełniał/a kod serwisów, dostarczając jednocześnie wymagane mechanizmy na poziomie repozytoriów (których jeszcze nie ma).

> Uwaga: Aplikacja jest w dość uproszczonej formie, chociażby ze względu na to, że do operacji aktywacji źródła wiedzy czy jego potwierdzenia podchodzimy hurra optymistycznie - żadnej walidacji, obsługi błędów. Tak to zostawimy, aby skupić się głównie na Spring Data (w dalszej kolejności na Spring Security).

**Gratulację!** Jeżeli udało Ci się prześledzić działanie całego projektu i przeanalizować jego kod, to możesz zaczynać przygodę ze Spring Data.

---

### C.1.3. Uzupełnienie zależności (:watch: 15 minut)

**Twój cel**: Uzupełnienie wymaganych zależności dla Spring Data JPA, Hibernate i MySQL

Z projektu Spring Data będziemy wykorzystywać moduł Spring Data JPA, a więc rozwiązanie dedykowane dla specyfikacji Java Persistence API. Bazujemy w treści zadań na bazie danych MySQL w wersji 8., ale możesz wykorzystać dowolną inną bazę danych. Pamiętaj tylko, że wtedy musisz dostosować zależności oraz konfigurację jednostki trwałej (kolejny punkt) pod Twoją bazę danych.

Wykonaj kolejne kroki, aby ukończyć zadanie:
1. Uzupełnij zaleźność do Spring Data JPA, wybierz wersję `2.0.8.RELEASE` lub wyższą.
1. Uzupełnij zależność do Hibernate Core, wybierz wersję `5.3.3.Final` lub wyższą.
1. Uzupełnij zalezność do MySQL Java Connector, wybierz wersję `8.0.11` lub wyższą.
1. Przebuduj projekt i wykonaj reimport maven'a, aby upewnić się, że wszystkie zależności poprawnie się pobrały
1. Uruchom projekt, aby upewnić się, że wszystko działa.

**Gratulację!** Wszystko wygląda na to, że zależności zostały poprawnie uzupełnione.

[Rozwiązanie zadania](solutions/C.1.3.md)

---

### C.1.4. Jednostka trwała i klasa konfiguracyjna (:watch: 25 minut)

**Twój Cel:** Dostarczenie klasy konfiguracyjnej dla elementów Spring Data JPA oraz konfiguracji jednostki trwałej w postaci pliku `persistence.xml`.

Wykorzystanie Spring Data JPA w naszym projekcie wymaga dostarczenia nowej klasy z adnotacją `@Configuration`. Powinna ona aktywować repozytoria JPA oraz zarządzanie transakcjami, a także dostarczać wymagane bean'y. Na koniec klasę konfiguracyjną należy zarejestrować w konfiguracji `DispatcherConfiguration` jako kolejną główna klasę konfiguracyjną.

Wykonaj kolejne kroki, aby ukończyć zadanie:
1. Utwórz klasę `JpaConfig` w pakiecie `pl.honestit.spring.kb.config`. Oznacz klasę adnotacjami:
   - `@Configuration`,
   - `@ComponentScan` (wskaz odpowiedni pakiet reprezentujący warstwę danych),
   - `@EnableTransactionManagement`,
   - `@EnableJpaRepositories` (wskaż odpowiedni pakiet z repozytoriami do skanowania)
1. W klasie `JpaConfig` zdefiniuj metodę udostępniającą bean'a typu `LocalEntityManagerFactoryBean`. Pamiętaj, aby metoda nazywała się `entityManagerFactory` (przypomnij sobie dlaczego to takie ważne). Wymagane jest tylko, aby w obiekcie typu `LocalEntityManagerFactoryBean` ustawić poprawną nazwę jednostki trwałej (`PersisteneUnit`). Sama nazwa nie ma teraz znaczenia - dobierz dowolną sensowną.
1. W klasie `JpaConfig` zdefiniuj kolejną metodę udostępniającą bean'a typu `JpaTransactionManager` i przyjmującą jako parametr obiekt klasy `EntityManagerFactory`.
1. Gotową klasę `JpaConfig` dodaj do zestawu głównych klas konfiguracyjnych w `DispatcherConfiguration`
1. Uruchom projekt i sprawdź czy działa

   > Ufff... ufff... Ufff... chwila oddechu :) ... ale nie będzie działać :(
   
1. W katalogu `src/main/resources/META-INF` utwórz plik `persistence.xml`. Uzupełnij w nim poprawną konfigurację jednostki trwałej:
   1. Wykorzystaj nazwę jednostki użytą w punkcie 2.,
   1. Skonfiguruj ścieżkę do bazy danych, użytkownika oraz hasło,
   1. Skonfiguruj ustawienia sterownika oraz dialektu zgodnie z Twoją bazą danych,
   1. Jeżeli chcesz możesz również ustawić parametry związane z:
      - kodowaniem znaków (`useUnicode`, `characterEncoding`, `CharSet`),
      - formatowaniem i wyświetlaniem zapytań SQL (`show_sql`, `format_sql`),
      - automatycznym tworzeniem bazy danych (`hbm2ddl.auto`).
1. Ponownie uruchom projekt. Tym razem aplikacja powinna wystartować bez błędów.

Pełna konfiguracja może wyglądać tak:

```xml
<persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence
http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd"
             version="2.1">

    <persistence-unit name="knowledgePU">
        <properties>
            <property name="javax.persistence.jdbc.url"
                      value="jdbc:mysql://localhost:3306/knowledge_db?createDatabaseIfNotExist=true&amp;serverTimezone=UTC"/>
            <!--Zmienić nazwę użytkownika na swojego-->
            <property name="javax.persistence.jdbc.user" value="root"/>
            <!--Zmienić hasło użytkownika na swoje-->
            <property name="javax.persistence.jdbc.password" value="pass"/>
            <property name="javax.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.dialect" value="org.hibernate.dialect.MySQL8Dialect"/>
            <property name="hibernate.connection.useUnicode" value="true"/>
            <property name="hibernate.connection.characterEncoding" value="utf8"/>
            <property name="hibernate.connection.CharSet" value="utf8"/>
        </properties>
    </persistence-unit>

</persistence>
```

**Gratulacje!** Dostarczyłeś/aś poprawną konfigurację Spring Data JPA i konfigurację jednostki trwałej. Zostało teraz sprawienie aby nasze encje zaczęły działać jak encje!

[Rozwiązanie zadania](solutions/C.1.4.md)

---

### C.1.5. Uzupełnienie encji (:watch: 45 minut)

**Twój Cel:** Konfiguracja adnotacjami klas z pakietu `pl.honestit.spring.kb.data.model`, aby odzwierciedlały prawidłową strukturę bazy danych i możliwe było zasilenie bazy danymi testowymi.

W katalogu `src/main/resources/META-INF/sql` znajdziesz pliki `createStructures.sql` oraz `initData.sql`.  Pierwszy plik zawiera zapytania tworzące tabele i pokazujące właściwą strukturę bazy danych. Drugi plik to dane testowe. W Tobie tylko znany sposób wykorzystaj adnotację z Java Persistence API, aby encje `User`, `Skill` oraz `KnowledgeSource` odpowiadały strukturą w bazie danych. Najpierw zadbaj o poprawną obsługę pól mapowanych na kolumny we wszystkich encjach, a potem pól mapujących relacje. Pracuj pokolei na każdej encji.

Wykonaj kolejne kroki, aby ukończyć zadanie:
1. Sprawdź czy w pliku `persistence.xml` wykorzystujesz właściwość:

   `<property name="hibernate.hbm2ddl.auto" value="validate"/>`
   
   Dzięki niej przy każdym uruchomieniu aplikacji silnik Hibernate'a będzie weryfikował czy encje, które posiadasz w aplikacji odpowiadają w 100% schematowi bazy danych.
1. Jeżeli wcześniej nie poświęciłeś/aś wystarczającej uwagi klasom z pakietu `pl.honestit.spring.kb.data.model`, to teraz to zrób.
1. Zapoznaj się ze strukturą tabel w pliku `createStructures.sql` - to jest cel, który chcesz uzyskać
1. Wykorzystaj adnotacje z listy poniższej (lub dowolne inne), aby uzyskać właściwy efekt:
   - `@Entity` (oznacza klasę jako encję),
   - `@Table` (pozwala na wskazanie między innymi wprost nazwy tabeli dla encji),
   - `@Id` (oznacza pole jako klucz główny),
   - `@GeneratedValue` (wskazuje, że dane pole ma być generowane w sposób automatyczny),
   - `@Column` (pozwala na ustawienie szczegółowych informacji o polu),
   - `@OneToMany`, `@OneToOne`, `@ManyToOne`, `@ManyToMany` (pozwalaja na określenie relacji na polach o typie innych encji).
   
   > Skorzystaj z wcześniejszej podpowiedzi, aby najpierw obsłużyć kolumny płaskie dla każdej encji, a potem zająć się relacjami. Najłatwiej będzie Ci zacząć od klasy `Skill`, bo nie ma zależności do innych encji. Potem `KnowledgeSource`, a na końcu `User`.
   
1. Uruchamiaj i modyfikuj tak długo, aż uzyskasz właściwą strukturę na bazie danych i Hibernate poprawnie zwaliduje model bez błędów. Pamiętaj tylko, że musisz rozwiązywać błędy walidacji jeden po drugim oraz, że szczegółowe informacje o tych błędach będą dostępne w zakładce z logami serwera (Tomcat Local), a nie w zakładce z konsolą serwera (Tomcat Server).
1. Na koniec upewnij się, że projekt się uruchamia i wszystkie strony działają bez zmian.
1. Uruchom skrypt z danymi testowymi `testData.sql` (w katalogu `src/resources/META-INF`), aby poprawnie załadowały się do projektu. 

**Gratulacje!** Skoro wkońcu zakończyłeś/aś pętlę powyżej, to znaczy, że udało Ci się poprawnie skonfigurować encje. To tylko 3 stwory, ale nawet z taką małą grupką może być nie mało zamieszania! Najważniejsze, że mamy dane testowe i możemy rozpocząć ożywianie naszej aplikacji.

[Rozwiązanie zadania](solutions/C.1.5.md)

---

### Gratulacje!

Przyszedł czas na te duże Gratulacje, bo właśnie udało Ci się ukończyć pierwszą część tej sekcji!

---


## C.2. Implementacja serwisów i elementów warstwy dostępu do danych

Pierwszą część mamy za soba, więc teraz nic Cię nie powstrzyma przed ożywieniem naszej aplikacji i utrwaleniem efektów, które w niej mają miejsce (takie jak potwierdzanie źródła danych). 

Kończąc tą część sekcji uzyskamy:
- pełne implementacje repozytoriów użytkowników, umiejętności i źródeł wiedzy,
- serwisy działające z wykorzystaniem repozytoriów,
- dużo powtarzającego się i męczącego kodu konwertowania encji na obiekty DTO i w drugą stronę,
- piękną i prostą aplikację w całej swojej okazałości,
- kompletnie niezabezpieczoną :)

**Do dzieła!**

---

### C.2.1. Podstawowe repozytoria (:watch: 15 minut)

**Twój Cel:** Utwórzenie interfejsów repozytoriów dla klas `User`, `Skill` oraz `KnowledgeSource` dziedziczących po `JpaRepository` i będących poprawnymi komponentami.

Wykonaj kolejne kroki, aby ukończyć zadanie:

1. Utwórz interfejsy `UserRepository`, `SkillRepository` oraz `KnowledgeSourceRepository` w pakiecie `pl.honestit.spring.kb.data.repositories`.
1. Każdy z interfejsów powinien rozszerzać `JpaRepository` z odpowiednio dobranym typem encji oraz typem klucza głównego.
1. Wstrzyknij poprzez konstruktor `UserRepository` do klasy `UserService`.
1. Wstrzyknij poprzez konstruktor `SkillRepository` do klasy `SkillService`.
1. Wstrzyknij poprzez konstruktor `KnowledgeSourceRepository` do klasy `KnowledgeSourceService`.
1. Uruchom projekt i upewnij się, że funkcjonalności nie przestały działać.

**Gratulace!** Zadanie zakończone! Repozytoria zostały utworzone i pomimo, że z perspektywy samych interfejsów `XXXRepository` wygląda jakby nic w nich nie było, to dostępne są już wszystkie metody na ścieżce dziedziczenia od `JpaRepository` w górę.


> 30 sekund przerwy na: Uffff... uffff.... ufffff głęboki oddech!

[Rozwiązanie zadania](solutions/C.2.1.md)

---

### C.2.2. Implementacja procesu logowania (:watch: 15 minut)

**Twój Cel:** Dostarczenie implementacji procesu logowania, który będzie wykorzystywał repozytoria i encję `User` do weryfikacji poprawności danych logowania i zwrócenia właściwego użytkownika.

Wykonaj kolejne kroki, aby ukończyć zadanie:
1. Dostarcz implementację metody `checkCredentials` w `UserSerice`:
   - uzupełnij klasę `UserRepository` o metodę, która dostarczy informacji czy istnieje użytkownik dla podanego loginu i hasła.
   - wykorzystaj powyższą metode w serwisie.   
1. Dostarcz implementację metody `getUser` w `UserService`:
   - uzupełnij klasę `UserRepository` o metodę, która zwróci użytkownika na podstawie loginu i hasła,
   - wykorzystaj powyższą metodę w serwisie,
   - skonwertuj encje na obiekt dto.
1. Uruchom projekt i sprawdź czy proces logowania pozwala Ci wykorzystać faktyczne dane i odrzuca próby niepoprawnego zalogowania.

**Gratulacje!** Implementacja pierwszego procesu na pewno poszła lekko i przyjemnie. Jeszcze kilka ich zostało, ale w każdym kolejnym będzie chodzić o zgrubsza te same czynności.

[Rozwiązanie zadania](solutions/C.2.2.md)

---

### C.2.3. Implementacja pobierania umiejętności użytkownika (:watch: 15 minut)

**Twój Cel:** Dostarczenie implementacji pobierania zestawu umiejętności konkretnego użytkownika, aby móc je poprawnie wyświetlić na stronie "Twoje umiejętności".

Wykonaj kolejne kroki, aby ukończyć zadanie:

1. Dostarcz implementację metody `getSkillsForUser` w `UserService`.

   > Użytkownik pozwiązany jest tylko ze źródłami wiedzy, a więc aby uzyskać listę jego umiejętności musisz najpierw pobrać jego źródła wiedzy, a potem dla nich odpowiednie umiejętności. Pamiętaj też, że ta metoda powinna zwracać listę wszystkich umiejętności (z powtórzeniami).
   
1. Uruchom projekt i sprawdź czy na stronie umiejętności użytkownika poprawnie pojawiają się jego _skille_.

**Gratulacje.** Kolejne zadanie wykonane!

[Rozwiązanie zadania](solutions/C.2.3.md)

---

### C.2.4. Implementacja potwierdzania źródła wiedzy (:watch: 25 minut)

**Twój Cel:** Umożliwienie użytkownikowi nabywania nowych umiejętności poprzez potwierdzanie nieznanych mu jeszcze źródeł wiedzy.

Wykonaj kolejne kroki, aby ukończyć zadanie:
1. Dostarcz implementację metody `getSourcesKnownByUser` w `KnowledgeSourceService`, która zwróci listę obiektów `KnowledgeSourceDTO` reprezentujących znane użytkownikowi źródła wiedzy.
1. Dostarcz implementację metody `getSourcesUnknownByUser` w `KnowledgeSourceService`, która zwróci listę obiektów `KnowledgeSourceDTO` reprezentujących NIEznane użytkownikowi źródła wiedzy.
1. Dostarcz implementację metody `addNewSource` w `UserService`:
   - wstrzyknij do `UserService` repozytorium `KnowledgeSourceRepository` (poprzez konstruktor),
   - pobierz wskazane źródło oraz użytkownika,
   - upewnij sie, że użytkownik nie ma już tego źródła,
   - dodaj źródło do użytkownika i zapisz użytkownika.
1. Uruchom projekt i sprawdź czy dodawanie źródła działa poprawnie. Upewnij się, że po dodaniu źródła do użytkownika pojawia się ono na liście znanych źródeł i znika z listy nieznanych oraz, że zmienia się statystyka umiejętności użytkownika.

**Gratuluję!** Kolejna ważna funkcjonalność zakończona. Od strona użytkownika wszystko wygląda już naprawdę pięknie. Możesz już wykorzystać projekt do gromadzenia swoich umiejętności ;)

[Rozwiązanie zadania](solutions/C.2.4.md)

---

### C.2.5. Implementacja pobierania danych na stronę główną (:watch: 40 minut)

**Twój Cel:** Ożywienie strony głównej, aby dane na stronie odpowiadały rzeczywistej liście najbardziej umiejętnych użytkowników oraz najpopularniejszych umiejętności.

> To zadanie może być trudniejsze, więc jeżeli w którymś momencie zakopiesz się i przykryjesz gruzem SQL'a, to przejdź śmiało do kolejnych zadań albo zastosuj [Hammock Technique](https://data-sorcery.org/2010/12/29/hammock-driven-dev/)

Wykonaj kolejne kroki, aby ukończyć zadanie:

1. Dostarcz implementację metody `getTopUsers` w `UserService`.
   - uzyskaj w _jakiś sposób_ listę użytkowników (albo ich identyfikatorów) posortowanych po ilości posiadanych umiejętności (z powtórzeniami),
   - pobierz niezbędne dane, aby móc przygotować listę 10 obiektów `TopUserDTO`,
   - pamiętaj też o sortowaniu, aby na liście 1. użytkownik był naprawdę najlepszy.
   
   > Unikaj jak ognia próby zrealizowania tego zadania w Java'ie pobierając listę wszystkich użytkowników i przetwarzając tą listę w pamięci (w kodzie). Za wszelką cenę zmuś bazę danych do pomocy. Ale koniec końców lepiej mieć funkcjonalność niż jej nie mieć...
   
1. Dostarcz implementację metody `getTopSkills` w `SkillService`.
   - podobnie jak w poprzednim punkcie również teraz w _jakiś sposób_ pobierz listę umiejętności najczęściej występujących wśród użytkowników czyli wśród posiadanych źródeł wiedzy (nie wśród wszystkich),
   - dla każdej umiejętności znajdź użytkownika, który posiada ją najwięcej razy,
   - pamiętaj też o sortowaniu, aby na liście 1. umiejętność była naprawdę najbardziej popularna.
   
   > Podobnie tutaj nie próbuj bazować na liście wszystkich użytkowników, ale maksymalnie wykorzystaj bazę danych do pomocy. I ponownie, lepiej jednak mieć funkcjonalność niż jej nie mieć...
   
1. Uruchom projekt i sprawdź czy strona główna zaczęła prezentować rzeczywiste dane. Możesz zalogować się na kilku testowych użytkowników i sprawdzić czy uda Ci się ich wypromować na szczyt listy razem z ich ulubionymi umiejętnościami.

**Gratuluję!** Najfajniejsza strona w całej aplikacji dzięki Tobie właśnie ożyła! Gdyby tylko poprzedni developer w tym projekcie miał takie umiejętności, to nie musiałbym tego wszystkiego pisać...

[Rozwiązanie zadania](solutions/C.2.5.md)

---

### C.2.6. Implementacja usuwania źródła wiedzy w panelu administracyjnym (:watch: 15 minut)

**Twój Cel:** Umożliwienie usunięcia źródła wiedzy z poziomu panelu administracyjnego, gdy administrator pomylił się przy jego dodawaniu, a źródło wciaż jest nieaktywne.

Wykonaj kolejne kroki, aby ukończyć zadanie:

1. Dostarcz implementację metody `deleteSource` w `KnowledgeSourceService`.

   > Aby źródło mogło być usunięte musi być nieaktywne i nie potwierdzone przez żadnego użytkownika. Samą aktywność uzyskasz z obiektu `KnowledgeSource` ale brak potwierdzeń warto sprawdzić dodatkowym zapytaniem.
   
1. Uruchom projekt i spróbuj usunąć kilka nieaktywnych źródeł wiedzy. Postaraj się nie usuwać wszystkich, bo nieakwytne źródła wiedzy przydadzą Ci się do kolejnego zadania. Upewnij się, że po usunięciu źródła zniknęły z listy w panelu administracyjnym.

**Gratuluję!** Zadanie ukończone, więc teraz będzie można dodawać "_Kubusia Puchatka_" jako dobre źródło Javy, a potem bezpiecznie je usunąć przed aktywacją i udostępnieniem użytkownikom.

[Rozwiązanie zadania](solutions/C.2.6.md)

---

### C.2.7. Implementacja aktywowania źródła wiedzy w panelu administracyjnym (:watch: 10 minut)

**Twój Cel:** Włączenie funkcji aktywowania źródeł wiedzy, aby stały się dostępne dla użytkowników do potwierdzenia.

Wykonaj kolejne kroki, aby ukończyć zadanie:

1. Dostarcz implementację metody `activateSource` w `KnowledgeSourceService`.
1. Uruchom projekt i aktywuj kilka nieaktywnych źródeł. Upewnij się, że po aktywowaniu źródła pojawiły się do potwierdzenia na liście źródeł z widoku użytkownika.

**Gratuluję!** Teraz te cenne źródła mogą być wykorzystywane przez użytkowników. Pozostało już tylko umożliwienie dodawania nowych, wartościowych źródeł!

[Rozwiązanie zadania](solutions/C.2.7.md)

---

### C.2.8. Implementacja dodawania źródła wiedzy w panelu administracyjnym (:watch: 20 minut)

**Twój Cel:** Zapewnienie pełnej funkcjonalności aplikacji dzięki umożliwieniu trwałego zapisywania nowych źródeł danych.

Wykonaj kolejne kroki, aby ukończyć zadanie:
1. Dostarcz implementację metody `addNewSource` w `KnowledgeSourceService`:
   - w pierwszej kolejności upewnij się, że obiekt `KnowledgeSourceDTO` posiada wszystkie niezbędne dane,
   - zweryfikuj metodą z repozytorium `KnowledgeSourceRepository` czy nazwa źródła nie jest już używana,
   - utwórz nowe źródło na podstawie obiektu DTO (pamiętaj, aby na start było nieaktywne).
1. Uruchom projekt i dodaj kilka źródeł danych. Część z nich usuń, a część aktywuj. Upewnij się, że poprawnie pojawiają się w różnych procesach obsługiwanych przez aplikację oraz że te aktywowane można potwierdzić z poziomu użytkownika.

**Gratuluję!** Ukończyłeś/aś ważne zadanie, bo to jedyna funkcjonalność, która pozwala na wprowadzanie nowych danych do aplikacji z poziomu samej aplikacji.

[Rozwiązanie zadania](solutions/C.2.8.md)

---

### Gratulacje!

Kolejną część tej sekcji możesz uznać za zrealizowaną! Kawał dobrej roboty i implementacji. Wierzę, że Spring Data stał się właśnie Twoim dobrym przyjacielem i wykorzystanie go w omawianym zakresie nie będzie stanowiło dla Ciebie problemu.

---

## C.3. Konfiguracja Spring Security

Nasza aplikacja posiada bardzo prostą warstwę bezpieczeństwa, która opiera się na dostępności atrybutu sesji o nazwie `user` i typie `LoggedUserDTO` we wszystkich metodach kontrolera dotyczących funkcjonalności związanych z użytkownikiem. Jeżeli wcześniej użytkownik nie przeszedł przez kontroler autentykacji, a więc nie zalogował się, to takiego atrybutu w sesji nie będzie i Spring MVC automatycznie obsłuży to błędem `400 Bad Request`.

W kolejnej części postaramy się naszą aplikację zabezpieczyć bardziej profesjonalnie. Wyjaśnimy sobie też czym ta poprawność jest. Teraz skupimy się na samym wdrożeniu Spring Security do projektu, co oznacza:
- uzupełnienie wymaganych zależności,
- aktywowanie Spring Security i jego konfigurację w klasie konfiguracyjnej,
- proste testy logowania.

**Do dzieła!**

---

### C.3.1. Uzupełnienie zależności (:watch: 5 minut)

**Twój Cel:** Uzupełnienie zależności do Spring Security Config oraz Spring Security Web, aby móc wykorzystać te biblioteki w projekcie.

Wykonaj kolejne kroki, aby ukończyć zadanie:
1. Uzupełnij zależność do Spring Security Config (wersja `5.0.6.RELEASE` lub wyższa).
1. Uzupełnij zależność do Spring Security Web (wersja `5.0.6.RELEASE` lub wyższa).
1. Przebuduj projekt i upewnij się, że masz możliwość zaimportowania w dowolnej klasie poniższych typów:
   - `org.springframework.security.config.annotation.web.configuration.EnableWebSecurity`
   - `org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter`
   
**Gratuluję!** Może na tym etapie dodawanie kolejnych zaleźności, to dla Ciebie już nic wielkiego, ale robota wykonana = należą się gratulacje!

[Rozwiązanie zadania](solutions/C.3.1.md)

---

### C.3.2. Klasa konfiguracyjna (:watch: 15 minut)

**Twój Cel:** Przygotowanie klasy konfiguracyjnej Spring'a dla elementów związanych ze Spring Security.

Klasa konfiguracyjna powinna:
- posiadać adnotację `@Configuration`, 
- aktywować Spring Security poprzez adnotację `@EnableWebSecurity`, 
- rozszerzać klasę `WebSecurityConfigurerAdapter`, 
- dostarczać puste implementacje metod `configure(AuthenticationManagerBuilder)` oraz `configure(HttpSecurity)`,
- zostać dorzucona do głównych klas konfiguracyjnych w `DispatcherConfiguration`. 

Nie zapomnijmy również o listenerze (_patrz materiały do zajęć_), który zepnie Spring Security z kontenerem.

Wykonaj kolejne kroki, aby ukończyć zadanie:
1. Utwórz klasę `SecurityConfig` w pakiecie `pl.honestit.spring.kb.config`.
1. Oznacz ją adnotacją `@Configuration`, aby mogła pełnić rolę konfiguracji i dostarczać bean'y.
1. Oznacz ją adnotacja `@EnableWebSecurity`, aby aktywować integrację Spring Security ze Spring MVC.
1. Niech klasa `SecurityConfig` rozszerza klasę `WebSecurityConfigurerAdapter`, aby mogła korzystać z wielu domyślnych implementacji podstawowych mechanizmów.
1. Nadpisz metodę `protected void configure(AuthenticationManagerBuilder)` i pozostaw pustą implementację.
1. Nadpisz metodę `protected void configure(HttpSecurity)` i pozostaw pustą implementację.
1. Dodaj klasę `SecurityInitializer` w pakiecie `pl.honestit.spring.kb.config`. Wystarczy, że ta klasa będzie rozszerzała `AbstractSecurityWebApplicationInitializer`. Nic więcej nie musi robić.
1. Uruchom projekt i sprawdź czy dotychczasowe funkcjonalności działają bez zarzutu. Na tym etapie Spring Security nie powinno przynosić jeszcze żadnego efektu, ale też nic nie powinno popsuć.

**Gratuluję!** Spring Security znalazło się w naszym projekcie, a to droga do wielkiej, bezpiecznej i świetnej aplikacji!

[Rozwiązanie zadania](solutions/C.3.2.md)

---

### Gratulacje!

Właśnie udało Ci się wdrożyć do aplikacji w sposób poprawny Spring Security i zintegrować je ze Spring MVC i kontenerem. Dobra robota!

---

## C.4. Zabezpieczenie aplikacji

Posiadamy w naszej aplikacji już działające Spring Security, więc pozostaje skorzystać z jego możliwości i zabezpieczyń aplikację.

Będziemy dążyć do tego, aby:
- strona główna pozostawała dostępna dla wszystkich,
- operacja logowania była dostępna tylko dla niezalogowanych użytkowników,
- operacja wylogowania była dostępna tylko dla zalogowanych użytkoników,
- wszystkie strony były dostępne dla użytkowników posiadających rolę _USER_,
- strony panelu administracyjnego były dostępne dla użytkowników posiadających role _ADMIN_.

**Do dzieła!**

---

### C.4.1. Użytkownicy testowi (:watch: 15 minut)

**Twój Cel:** Wykorzystanie użytkowników zapisanych w pamięci, do przetestowania warstwy bezpieczeństwa. Potrzebni Ci będą dwaj użytkownicy: jeden z rolą użytkownika i drugi z rolą administratora.

Wykonaj kolejne kroki, aby ukończyć zadanie:
1. W metodzie `configure(AuthenticationManagerBuilder)` klasy `SecurityConfig` uzupełnij konfigurację użytkowników zapisanych w pamięci.
1. Skonfiguruj użytkownika o loginie `user`, haśle `{noop}user` oraz roli `USER`.
1. Skonfiguruj użytkownika o loginie `admin`, haśle `{noop}admin` oraz rolach `USER` i `ADMIN`.

   > Faktyczne hasła użytkowników to odpowiednio `user` i `admin`, więc czym jest `{noop}`? Spring Security od wersji 5 przyjął mechanizm, który dynamicznie dobiera algorytm szyfrowania dla hasła na podstawie tak zapisanego identyfikatora szyfrowania. Pozwala to posiadać w systemie hasła zaszyfrowane rozmaitymi algorytmami. Identyfikator `{noop}` określa, że hasło ma być traktowane jako czyste hasło tekstowe - plain text.
   
1. Uruchom projekt, aby mieć pewność, że wszystko działa (kompiluje się). Na tym etapie nie mamy specjalnie co testować, bo nasza konfiguracja nie wymaga żadnego bezpieczeństwa.

**Gratuluję!** Są użytkownicy testowi, więc można zabezpieczyć aplikację.

[Rozwiązanie zadania](solutions/C.4.1.md)

---

### C.4.2. Autoryzacja zasobów z formularzem logowania (:watch: 20 minut)

**Twój Cel:** Masz za zadanie skonfigurowanie warstwy bezpieczeństwa zgodnie z ogólnymi wytycznymi. Pamiętaj w trakcie realizacji zadania, że konfigurację powinniśmy dostarczać od najbardziej konkretnych ścieżek do najmniej. Ustawisz również standardowy formularz logowania i mechanizm wylogowywania, który będzie przenosił na stronę główną.

Wykonaj kolejne kroki, aby ukończyć zadanie:
1. Zacznij od wyłączenia klasy `AuthenticationController`. Możesz ją usunąć lub usunąć z niej wszystkie adnotacje Spring'a, aby nie była dłużej uwzględniana w aplikacji.
1. W metodzie `configure(HttpSecurity)` w klasie `SecurityConfig` uzupełnij konfigurację bezpieczeństwa ścieżek:

   1. Dla ścieżki `/home` zezwól na dostęp wszystkich.
   
      > Dostep wszystkim może być zagwarantowany metodą `permitAll`.
      
   1. Dla ścieżki `/login` zezwól na dostęp tylko nieuwierzytelnionym użytkownikom.
   
      > Użytkowników niezautentykowanych reprezentuje metoda `anonymous`.
      
   1. Dla ścieżki `/logout` zezwól na dostęp tylko użytkownikom zautentykowanym.
   
      > Użytkowników zautentykowanych reprezentuje metod `authenticated`.
      
   1. Dla ścieżki `/user` i `/user/**` zezwól na dostęp użytkownikom posiadającym rolę `USER`.
   
      > Konkretnej roli będzie wymagała metoda `hasRole`.
      
   1. Dla ścieżki `/admin` i `/admin/**` zezwól na dostęp użytkownikom posiadającym rolę `ADMIN`.
   
   1. Wszystkie pozostałe ścieżki udostępnij tylko uwierzytelnionym użytkownikom.
   
      > Wszystkie ścieżki określa metoda `anyRequest`.
      
1. Skonfiguruj dalej logowanie w oparciu o formularz logowania. Dla tej opcji nie musisz dostarczać żadnej dodatkowej parametryzacji.
1. Skonfiguruj wylogowywanie na ścieżce `/logout` oraz przekierowanie na ścieżkę strony głównej (`/home`).
1. Nasza aplikacja do wylogowywania używa linku, a więc metody `GET`, aby było to możliwe w Spring Security musisz równiez wyłączyć mechanizm CSRF wywołując na obiekcie `HttpSecurity` metodę `csrf()` i potem `disable()`.
1. Uruchom aplikację i sprawdź czy zabezpiczenia zostały wdrożone. Na stronę główną powinieneś móc się dostać bez żadnego logowania, ale pozostałe strony powinny być już zabezpieczone. Przetestuj działanie linku do wylogowania.

   > Możliwe, że już sam zauważyłeś/aś ale nasi użytkownicy w Spring Security nijak mają się do użytkowników w bazie danych. W efekcie, choć aplikacja stała się bezpieczna, to przestała działać. Naprawimy to w kolejnym zadaniu.

**Gratuluję!** Udało Ci się wdrożyć wszystkie mechanizmy bezpieczeństwa. Wierzę, że nie było to w cale trudne a efekt, który uzyskałeś/aś jest niesamowity! Pozostało teraz szybko udać się do kolejnego punktu, aby naprawić działanie aplikacji.

[Rozwiązanie zadania](solutions/C.4.2.md)

---

### C.4.3. Użytkownicy z bazy danych

**Twój Cel:** Wdrożenie autentykacji opartej na użytkownikach z bazy danych oraz integracja Spring Security z klasą `LoggerUserDTO`.

Wykonaj kolejne kroki, aby ukończyć zadanie:
1. W pierwszej kolejności dostarcz w klasie `SecurityConfig` poprawną konfigurację źródła danych do połączenia z bazą danych. Możesz wykorzystać poniższy kod i dostosować ścieżkę do bazy, użytkownika i hasło do Twojej instancji MySQL lub innej bazy danych:

   ```java
   @Bean
   private DataSource dataSource() {
       DriverManagerDataSource dm = new DriverManagerDataSource();
       dm.setDriverClassName("com.mysql.cj.jdbc.Driver");
       dm.setUrl("jdbc:mysql://localhost:3306/knowledge_db");
       dm.setUsername("root");
       return dm;
   }
   ```
1. W metodzie `configure(AuthenticationBuilderManager)` usuń konfigurację opartą na użytkownikach testowych (_in memory_) i wprowadź konfigurację opartą na bazie danych.
1. Zdefiniuj zapytanie pobierające login i hasło użytkownika na podstawie tabeli, która reprezentuje użytkowników. W naszej aplikacji użytkownicy nie mają pola mówiącego o ich aktywności, więc zamiast wartości takiego pola (kolumny) zwracaj zawsze `true`.
1. Zdefiniuj zapytanie pobierające login i rolę użytkowników na podstawie tabeli, która reprezentuje role użytkowników. Nie mamy odpowiadającej encji, ale zwróć uwagę, że w skryptach z danymi jest odpowiednia tabela i zapytania ją wypełniające.
1. Zmodyfikuj skrypty danych, aby hasła posiadały przedrostek `{noop}` i ponownie wypełnij bazę danymi.
1. Uruchom teraz aplikację i sprawdź czy jest możliwe zalogowanie się na któregoś z użytkowników z bazy danych.

   > Przerwa na uffff... uffff.... ufffff... głęboki oddech
   
1. Przeszliśmy na użytkowników z bazy danych, ale nasza aplikacja wciąż nie działa. Rozwiążemy ten problem implementując obiekt interceptora żądań, który na podstawie zalogowanego użytkownika w Spring Security dostarczy obiekt `LoggedUserDTO` do sesji.

   > Interceptory w Spring MVC działają wobec kontrolerów jak filtry wobec Servletów. Warto o tym pamiętać.
   
1. Utwórz klasę `SessionInterceptor` w pakiecie `pl.honestit.spring.kb.mvc.interceptors`. Niech ta klasa rozszerza klasę `HandlerInterceptorAdapter` i nadpisuję metodę `preHandle`.
1. Wstrzyknij do klasy poprzez setter serwis `UserService`.
1. W nadpisanej metodzie `preHandle` pobierz z obiektu żądania (`HttpServletRequest`) obiekt `Principal`, a z niego nazwę zalogowanego użytkownika (metoda `getName`). W ten sposób uzyskałeś/aś login użytkownika, który się faktycznie zalogował.
1. W dalszej części metody, na podstawie nazwy użytkownika, pobierz obiekt `LoggedUserDTO` z serwisu `UserService` (dopisz w tym serwisie odpowiednią metodę i jej implementację) oraz ustaw pobranego użytkownika w sesji pod atrybutem `"user"`.

   > Obiekt sesji możesz pobrać z obiektu `HttpServletRequest`.
   
1. Interceptor jest gotowy, więc pozostało tylko go zarejestrować w klasie `WebConfig`:
   - dostarcz metodę zwracającą obiekt `SessionInterceptor` jako bean'a,
   - nadpisz metodę `addInterceptors(InterceptorRegistry)` i dodaj do obiektu rejestru nasz interceptor pobrany z metody, którą przed chwilą utworzyłeś/aś.
1. Wszystko powinno być teraz gotowe do uruchomienia, więc nie czekając dłużej uruchom projekt i sprawdź czy zarówno elementy związane z bezpieczeństwem jak i z funkcjonalnościami aplikacji działają prawidłowo.

**Gratuluję!** Udało Ci się! Warstwa bezpieczeństwa działa poprawnie.

[Rozwiązanie zadania](solutions/C.4.3.md)

---

#### Gratulacje!

Wykonałeś/aś kawał dobrej i nie łatwej roboty. Powinna rozpierać Cię duma, bo udało Ci się nasz malutki projekt doprowadzić do wielkiego, rozwojowego dzieła.