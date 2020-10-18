# A. Inversion of Control i Spring Core

Pierwsza sekcja wprowadza Cię do kluczowego wzorca architektonicznego dla zrozumienia działania dzisiejszych framwork'ów - wzorca Inversion Of Control (IoC). Wzorzec ten, wraz z rozwojem języka Java umożliwiającym jego efektywną implementację, zrewolucjonizował tworzenie zaawansowanych aplikacji.

Największym i najkosztowniejszym problemem związanym z każdą dużą aplikacją, a w szczególności z jej rozwojem, są zależności między występującymi w niej elementami. Czym zależności więcej i czym trwalsze tym możliwości ich modyfikacji, zamiany czy nawet usunięcia są mniejsze i kosztowniejsze.

Wzorzec IoC pozwala wprowadzić bardzo luźne zaleźności przenosząc rolę ich uzupełniania na kontener IoC. Jedną z najpopularniejszych i najefektywniejszych realizacji tego wzorca jest _Dependency Injection_. Spring Core (Spring Context) pełni rolę implementacji kontenera DI i choć istnieje wiele innych implementacji, to ta szczególnie spodobała się programistom.

Pierwsza sekcja zaprasza Cię do przygody ze wzorcem Inversion Of Control, z Dependency Injection oraz ze światem technologii Spring.

---

## Wprowadzenie

Technologia Spring składa się z wielu projektów. Jednym z najważniejszych jest Spring Framework stanowiący niejako trzon i punkt powiązania wszystkich pozostałych projektów. Kluczem do jego zrozumienia jest obsługa zależności, określanych też mianem wiązań.

Dostępny w ramach Spring Framework moduł Spring Core (Spring Context) stanowi tzw. kontener Dependency Injection. Kontenery tego rodzaju pozwalają nam tworzyć aplikacje oparte na wzorcu Inversion of Control i jego implementacji czyli Dependency Injection. W sposób deklaratywny określamy jakie obiekty są nam potrzebne do działania: na poziomie klas, metod czy pól. Za tworzenie obiektów i faktyczne wywoływanie naszego kodu odpowiada kontener DI.

W dalszej części tej sekcji nauczysz się:
- w jaki sposób skonfigurować projekt do pracy ze Spring Framework,
- jak wykorzystywać klasy typu `ApplicationContext` umożliwiające obsługę konfiguracji zaleźności,
- jak tworzyć i rejestrować komponenty,
- jak wiązać komponenty ze sobą, aby zależności pojawiały się w sposób dynamiczny

Zaczynajmy!

---

## A.1. Przygotowanie do pracy - konfiguracja projektu

Swoją przygodę ze Spring Framework zaczniesz od najprostszego, a więc konfiguracji projektu. Bazujemy tutaj na środowisku IntelliJ Ultimate, które posiada wbudowaną, bardzo efektywną, integrację z technologią Spring (w szczególności ze Spring Core i Spring MVC). Przyspiesza i usprawnia to pracę, o czym się już niedługo przekonasz.

---

### A.1.1. Utworzenie projektu

W pierwszym kroku utworzymy nowy projekt, w którym będziemy realizować zadania z tej sekcji.

Twoje kolejne zadania, to:
- zrobienie klona swojego forka,
- utworzenie nowego projektu typu maven-webapp w katalogu `projects` w głównym katalogu repozytorium,
- skonfigurowanie zaleźności.

**Zaczynamy!**

---

1. Jeżeli jeszcze nie wykonałeś `fork-a` tego repozytorium, to teraz jest na to dobry moment.
1. Sklonuj Twój `fork` na komputer w katalogu z repozytoriami. Wykorzystaj polecenie `git clone <adres_Twojego_forka>`. Bardzo ważne, abyś skonował/a swój `fork`, a nie repozytorium główne.

   > **Pamiętaj**, że nie musisz tworzyć nowego katalogu dla repozytorium. Polecenie `git clone` zrobi to za Ciebie.
   
1. Przejdź do utworzonego katalogu i wykonaj w nim polecenie `git status`, aby mieć pewność, że wszystko działa.
1. Wywołaj również polecenie `git remote -v`, aby sprawdzić adres zdalnego repozytorium - powinien to być adres Twojego `fork`-a.
1. W repozytorium znajduje się katalog `projects`. Jest to katalog, w którym będziesz tworzyć kolejne projekty. Nasz projekt stworzymy właśnie w tym katalogu.

---

**Mamy już poprawnie skonfigurowane repozytorium oraz przestrzeń, w której będą powstawały nasze projekt. Stwórzmy nowy projekt do poznawania Spring Core.**

1. W IntelliJ utwórz nowy projekt (**całkiem nowy, nie ze źródeł ani nie z systemu kontroli wersji**).
1. Wybierz typ projektu `Maven` dla języka Java
1. Określ:
   - `groupId` : `pl.honestit.spring`
   - `artifactId` : `spring-core`
1. Konfigurację maven'a pozostaw bez zmian.
1. Projekt powinien nazywać się `SpringCore` i zostać utworzony w katalogu `projects` w repozytorium.
1. Po utworzeniu projektu upewnij się, że masz włączoną opcję `Enable Auto-Import` w mavenie.
1. W katalogu `src/main` utwórz katalogi `java` i `resources` (jeżeli ich nie ma). Następnie pierwszy oznacz jako główny katalog z plikami źródłowymi (`Mark Directory as -> Sources Root`), a drugi jako główny katalog z zasobami (`Mark Directory as -> Resources Root`).
1. Zbuduj projekt, aby mieć pewność, że wszystko działa.

---

**Teraz pozostało dodać wymagane konfiguracje i zaleźności do pliku `pom.xml`.**

1. W pliku `pom.xml` upewnij się, że istnieje poprawna konfiguracja kodowania plików źródłowych oraz kompilacji plików źródłowych są ustawione minimum na `1.8`. Znajdziesz to, w sekcji `properties`, która powinna wyglądać tak:

   ```xml
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>
   ```
   
1. Do sekcji `properties` dodaj nową właściwość o nazwie `spring.framework.version` i wartości `5.1.3.RELEASE`. Będzie to właściwość, którą wykorzystamy, aby w jednym miejscu utrzymywać wersję wszystkich zależności do artefaktów ze Spring Framework.
1. Dodaj teraz zaleźność do Spring Context. Grupa to `org.springframework`, a artefakt to `spring-context`. Wersje ustaw z wykorzystaniem wcześniej stworzonej właściwości `spring.framework.version`.
1. Ponownie przebuduj projekt, aby sprawdzić czy wszystko poprawnie się uzupełniło.

**Gratulację! Projekt został przygotowany.**

---

### A.1.2. Pierwszy komponent

Masz już poprawnie skonfigurownay projekt. Pozostało teraz przetestować czy aby na pewno Spring Context w nim działa.

Twoje kolejne zadania to:
- utworzenie prostej klasy `HelloWorld`, która dostarczy pojedynczą metodę wyświetlającą napis `hello, wordl!`,
- utworzenie konfiguracji kontekstu Spring-a do zarejestrowania i pobrania komponentu `HelloWorld`,
- wykorzystanie bean'a `HelloWorld` i sprawdzenie czy działa.

**Zaczynamy!**

---

1. Utwórz klasę `HelloWorld` w pakiecie `pl.honesit.spring.core.warmup`.
1. Zaimplementuj w klasie metodę `public void sayHello()`, która wyświetli na konsoli napis `Hello, world!`.
1. W pakiecie `pl.honestit.spring.core.app` utwórz klasę `Application`, a w niej metodę `main` umożliwiającą uruchomienie klasy.
1. Stwórz nowy obiekt klasy `GenericApplicationContext`

   > Klasa `GenericApplicationContext` umożliwia bardzo wszechstronną rejestrację komponentów. My wykorzystamy ją aby zarejestrować komponent bezpośrednio z poziomu kodu
   
1. Skorzystaj z metody `registerBean` klasy `GenericApplicationContext`, aby zarejestrować nasz komponent. Nazwijmy go `helloBean` i oczywiście ma być klasy `HelloWorld`.
1. Skoro komponent został zarejestrowany, to można go już pobrać z kontekstu. Wykorzystaj do tego metodę `getBean`, która oczekuje nazwy bean'a oraz jego klasy.
1. Na koniec wywołaj na uzyskanym bean'ie klasy `HelloWorld` metodę `sayHello`, aby sprawdzić czy wszystko działa.
1. Uruchom klasę `Application` i sprawdź czy w konsoli pojawił się nasz komunikat powitalny.

**Udało Ci się wykorzystać Spring Context do obsługi pierwszego bean'a! Może nie wydawać Ci się to czymś szczególnie nadzwyczajnym, ale właśnie zacząłeś/aś używać Spring'a ;)**

[Rozwiązanie zadania](solutions/A.1.2.md)

---

### Gratuluję!

Pierwszą część masz za sobą: projekt został skonfigurowany i działa w nim poprawnie kontekst Spring'a. Śmiało możesz realizować dalsze zadania.


## A.2. Kontekst aplikacji i konfigurowanie komponentów

W tej części przejdziemy przez różne sposoby tworzenia kontekstów aplikacji w oparciu o konfigurację XML oraz automatyczne skanowanie komponentów bazujące na adnotacjach. Obie techniki pozwalają na wygodniejsze konfigurowanie kontekstu i zarządzanie komponentami niż ich ręczna rejestracja.

---

### A.2.1. Konfiguracja komponentów z wykorzystaniem plików XML

Pierwszym sposobem na konfigurowanie komponentów w Spring'u jest wykorzytanie pliku konfiguracyjnego w formacie XML. Plik ten musi być zgodny ze schematem [spring-beans](http://www.springframework.org/schema/beans/spring-beans.xsd).

Taką konfigurację możemy zaczytać w naszym kontekście bezpośrednio ze ścieżki klas (_classpath_) albo z systemu pliku (ścieżka względna lub absolutna). My wykorzystamy pierwszą opcję jako znacznie wygodniejszą.

Twoje kolejne zadania to:
- utworzenie pliku `beans.xml`,
- rejestracja komponentu `HelloWorld` w pliku `beans.xml`,
- utworzenie kontekst typu `ClassPathXmlApplicationContext` w oparciu o plik konfiguracyjny,
- pobranie bean'a i sprawdzenie czy poprawnie działa.

**Zaczynamy!**

---

1. W katalogu `src/main/resources` utwórz nowy plik `beans.xml`. Plik powinien zawierać kod XML zgodny ze strukturą `spring-beans`. Głównym elementem (korzeniem) pliku powinien być zestaw tagów `<beans></beans>`.
1. Przy pomocy tagu `<bean>` zarejestruj nasz komponent klasy `HelloWorld`. Określ atrybuty `name` oraz `class`. Wartości powinny być analogiczne jak przy ręcznej rejestracji z zadania `A.1.2.`.

---

**Komponent został poprawnie zarejestrowany, teraz czas na kontekst.**

1. W klasie `Application` przenieść dotychczasowy kod metody `main` do metody `private static void genericWarmUp()`.

   > Chodzi o to, aby kod z poprzedniego zadania nie zginął, ale aby nie był już używany
   
1. W metodzie `main` utwórz nowy obiekt klasy `ClassPathXmlApplicationContext`. W parametrze konstruktora wskaż ścieżkę do pliku `beans.xml`. Oczywiście wspomniany obiekt zapisz do zmiennej.

---

**To wszystko co jest potrzebne do utworzenia kontekstu. Teraz będziemy mogli swobodnie rejestrować nowe komponenty w pliku `beans.xml`. Najpierw jednak sprawdźmy jeszcze czy wszystko działa.**

1. Z obiektu kontekstu pobierz bean'a komponentu `HelloWorld`. Jego nazwa musi być zgodna z wartością atrybutu `name` w konfiguracji.
1. Na pobranym bean'ie wywołaj metodę `sayHello`, aby upewnić się, że wszystko działa.

**Gratuację! Udało Ci się poprawnie przygotować kontekst oparty na konfiguracji XML.**


[Rozwiązanie zadania](solutions/A.2.1.md)

---

### A.2.2. Konfiguracja komponentów z wykorzystaniem adnotacji i automatycznego skanowania

Konfiguracje XML umożliwiają centralne zarządzanie, ale często są zbyt pracochłonne i rozbudowane. Alternatywnym sposobem jest konfiguracja w oparciu o adnotacje i automatyczne skanowanie komponentów. Ze względu na to, że programiści lubią adnotacje, to pewnie chętniej będziesz korzystał/a z tego mechanizmu ;)

> Należy w tym miejscu wnieść pewną uwagę. Jeżeli dobrze sobie przypomnisz, to już w technologii Servletów spotkałeś/aś się z tym, że konfiguracja XML pozwala na więcej niż konfiguracja poprzez adnotacje. Wynika to w głównej mierze z dwóch rzeczy: 

> - adnotacje mają swoje obostrzenia, co do typów parametrów,
> - konfiguracje poszczególnych klas są niezależne od innych. 
  
> Podobnie w przypadku konfiguracji Spring'a - wersja oparta na XML może więcej.
  

Twoje kolejne zadania to:
- utworzenie klasy konfiguracyjnej, która będzie odpowiadała za dostarczanie bean'a typu `HelloWorld`,
- utworzenie kontekstu opartego na adnotacjach,
- przetestowanie działania komponentu `HelloWorld`,
- _samorejestracja_ z wykorzystanie adnotacji `@Component` w klasie `HelloWorld`,
- poprawne włączenie opcji automatycznego skanowania w kontekście,
- porównanie pobieranych bean'ów różnymi metodami.

**Zaczynamy!**

---

1. W pakiecie `pl.honestit.spring.core.config` utwórz klasę `WarmUpConfiguration`.
1. Oznacz klasę adnotacją `@Configuration` oraz utwórz w niej pojedynczą metodę, która będzie zwracać obiekt klasy `HelloWorld`. Metoda musi być publiczna i mieć adnotację `@Bean`, niech się też nazywa `helloBean`.

---

**Konfiguracja za nami, teraz jej wykorzystanie.**

1. W klasie `Application` przenieś kod metody `main` do metody `private static void classPathWarmUp()`.

   > Podobnie jak wcześniej chodzi o to, aby dotychczasowy kod nie zginął, ale aby nie był już używany
   
1. W metodzie `main` utwórz nowy obiekt klasy `AnnotationConfigApplicationContext`, a w jej konstruktorze wskaż klasę konfiguracyjną `WarmUpConfiguration`.

---

**Czas na szybkie testy.**

1. W znany już Ci sposób pobierz z obiektu kontekstu nasz komponent klasy `HelloWorld`. Jakiej nazwy użyjesz?
1. Wywołaj metodę `sayHello` na pobranym beanie i uruchom aplikację, aby sprawdzić czy wszystko działa.

---

**Kolejny krok, to sprawienie, aby komponenty mogły same siebie rejestrować.**

1. W klasie `HelloWorld` umieść adnotację `@Component`.
1. W metodzie `main` klasy `Application` wywołaj teraz na obiekcie kontekstu (`AnnotationConfigApplicationContext`) metodę `scan`. Musisz w niej wskazać nazwę pakietu, który ma być przeskanowany w celu odnalezienia komponentów. Możesz podać jeden lub wiele pakietów.

   > Warto wiedzieć, że skanowanie w poszukiwaniu komponentów odbywa się rekursywnie, a więc skanowane są również wszystkie podpakiety wskazanego pakietu. Rodzi to pokusę, aby zawsze wskazywać bardzo ogólny pakiet (najwyższego poziomu), ale odradzam Ci takiej praktyki. Po pierwsze Twoja    aplikacja nie powinna robić rzeczy niepotrzebnych, a po drugie może to być przyczyną błędów konfiguracyjnych.
   
1. Konfiguracja kontekstu poza konstruktorem wymaga również jego odświeżenia, do czego służy metoda `refresh`.

   > Tutaj uwaga: w naszej wersji nie używamy metody `refresh`. Używa jej się tylko raz dla kontekstu, a w sytuacji, gdy kontekst został stworzony inaczej niż konstruktorem bezargumentowym, to `refresh` zostało już wywołane.
   
---

**Przyszedł czas na docelowe testy tego jakie komponenty dostajesz w jednym i drugim przypadku.**

1. Pobierz teraz komponent klasy `HelloWorld`, ale tak, abyś otrzymał/a bean'a zarejestrowanego adnotacją `@Component`, a nie z klasy `WarmUpConfiguration`. Jakiej nazwy bean'a użyjesz?
1. Wywołaj na pobranym obiekcie metodę `sayHello` i sprawdź czy działa.
1. Wyświetl teraz na konsoli porównanie obu uzyskanych obiektów operatorem `==`. Jaki wynik się pojawia?
1. Pobierz ponownie oba bean'y, ale zapisz je do nowych zmiennych. Porównaj bean'y w parach (dwie zmienne zawierające bean udostępniany przez konfigurację i dwie zmienne zawierające bean udostępniany przez adnotację `@Component`). Jakie wartości otrzymujesz?
1. Spróbuj teraz pobrać bean'a typu `HelloWorld` ale o nazwie `"buzzBuzz"`. Czy to się udało? Jeżeli nie, to w klasie `WarmUpConfiguration` dopisz metodę zwracającą właśnie takiego bean'a.
1. Spróbuj teraz pobrać bean'a typu `HelloWorld`, ale nie podając nazwy (wykorzystaj odpowiednią metodę `getBean`). Czy to się udało?
1. Zapisz w komentarzu swoje obserwacje dotyczące pobierania beanów

**Mam nadzieję, że poprawnie udało Ci się zaobserwować niuanse nazywania i pobierania bean'ów. Wobec tego jest czego Ci gratulować oraz możesz czuć się przygotowany/a, do trzeciej części tej sekcji.**

[Rozwiązanie zadania](solutions/A.2.2.md)

---

### A.2.3. Nowe komponenty w projekcie

Potrafisz już stworzyć kontekst aplikacji oraz rejestrować i wykorzystywać komponenty. Nic nie stoi na przeszkodzie, aby w Twoim projekcie pojawiło się więcej komponenentów niż nasz pojedynczy `HelloWorld`.

Twoje kolejne zadania to:
- utworzenie interfejsu `Printer` z pojedynczą metodą `print`,
- utworzenie trzech implementacji tego interfejsu, z których:
  - pierwsza będzie wyświetlać dane na konsoli,
  - druga dopisywać do pliku w katalogu domowym,
  - trzecia wyświetlać w graficznym okienku
- zarejestrowanie klas jako komponenty przy pomocy adnotacji
- pobranie wszystkich komponentów i przetestowanie ich działania.

**Zaczynamy!**

---

1. Utwórz interfejs `Printer` w pakiecie `pl.honestit.spring.core.components.printers`. Interfejs powinien posiadać pojedynczą metodę `void print(String message)`.
1. Utwórz w pakiecie `pl.hit.spring.core.components.printers` trzy implementacje tego interfejsu:
   - `ConsolePrinter`, która przekazany tekst wyświetli na konsoli,
   - `FilePrinter`, która przekazany tekst dopisze do pliku `out.log` w katalogu domowym użytkownika,
   - `DialogPrinter`, która wyświetli okienko z przekazanym tekstem.
1. Zarejestruj komponenty jako bean'y wybierając poniższe sposoby:
   - rejestracja w klasie konfiguracyjnej `WarmUpConfiguration`,
   - rejestracja adnotacją `@Component`.
   
   > Pamiętaj, że w zależności od wyboru potrzebujesz odpowiedniego kontekstu.
   
1. W pakiecie `pl.honestit.spring.core.app` utwórz klasę `PrinterApplication` z metodą `main` i stwórz w niej kontekst oparty na adnotacjach i odpowiednio go skonfiguruj.
1. Pobierz każdy komponent po jego nazwie i sprawdź czy prawidłowo działa.
1. Pobierz teraz każdy komponent po jego typie i sprawdź czy prawidłowo działa.
1. Spróbuj teraz pobrać komponenty po nazwie, ale typie `Printer` oraz po samym typie `Printer`, bez nazwy. Jakie są tego efekty?

**Gratuluję! Udało Ci się stworzyć, zarejestrować i poprawnie wykorzystać cały zestaw komponentów.**

[Rozwiązanie zadania](solutions/A.2.3.md)

---

### Gratuluję!

Masz za sobą pracę z kontekstem i rejestracją komponentów. Udało Ci się również utworzyć nowy zestaw komponentów. Jesteś w pełni gotowy/a do pracy z zależnościami.


## A.3. Obsługa zależności

Trzeci kluczowy element, który udostępnia Spring Framework, to automatyczne wiązania, a więc uzupełnianie zaleźności w sposób scenatralizowany, zgodny ze wzorcem Inversion of Control. 

Do tej pory udało nam się skonfigurować kontekst Spring'a, zarejestrować na różne sposoby komponenty i zmusić Spring'a, aby to on dostarczał nam bean'y - czyli instancje naszych klas komponentów zarządzane przez mechanizmy Spring'a.

W ramach kolejnych zadań sprawimy, aby nasze komponenty posiadały zaleźności i aby były one automatycznie uzupełniane przez kontener _Dependency Injection_ zaszyty w Spring Framework.

> **UWAGA** Zadania A.3.1 - A.3.3 nie są związane ze Springiem i są osobną częścią, która pokazuje samodzielne wprowadzanie luźnych zależności i wzorca Inversion of Control. **Zacznij od zadania A.3.4**

---

### A.3.1. Wprowadzenie luźnych zaleźności

W poprzedniej części zakończyliśmy pracę wprowadzając dodatkowe komponenty: `ConsolePrinter`, `FilePrinter` oraz `DialogPrinter`. Wszystkie te komponenty implementowały pojedynczy interfejs `Printer`. Zadbamy teraz, aby komponent `HelloWorld` korzystał z mechanizmu `Printer`'a.

Twoje kolejne zadania to:
- wprowadzenie do klasy `HelloWorld` luźnych zaleźności, które pozwolą konfigurować działanie metody `sayHello` różnymi wersjami implementacji interfejsu `Printer`,
- przetestowanie klasy `HelloWorld` z ręcznym uzupełnieniem zależnośći.

**Zaczynamy!**

---

1. W klasie `HelloWorld` dodaj pole prywatne `printer`. Dobierz typ tego pola tak, aby zachować luźne powiązania.
1. Do klasy dodaj konstruktor przyjmujący parametr. Parametr ten powinien ustawiać pole `printer`.
1. Do klasy dodaj również metodę `public void setPrinter`, która przyjmie taki sam parametr jak konstruktor i ustawi wartość pola `printer`.
1. Zmodyfikuj metodę `sayHello`, aby wypisanie tekstu `"Hello, world!"` metoda zlecała obiektowi `printer`.

---

**Mam nadzieję, że z sukcesem udało Ci się wprowadzić luźne zaleźności. Przyszedł teraz czas na testy.**

1. W klasie `HelloWorld` utwórz metodę `main`, która pozwoli nam uruchomić tą klasę i przeprowadzić proste testy.
1. Stwórz pierwszy obiekt klasy `HelloWorld` i skonfiguruj go tak, aby wyświetlał powitanie na konsoli.
1. Stwórz kolejne dwa obiekty, z których jeden będzie wyświetlał powitanie w pliku a drugi w oknie dialogowym.
1. Uruchom testy klasy `HelloWorld` i sprawdź czy wszystkie informacje prawidłowo się wyświetliły.
1. Pomyśl teraz o kilku zmianach i ich konsekwencjach:
   - co by było gdybyś w klasie `HelloWorld` na sztywno tworzył/a obiekt np. `ConsolePrinter`? 
   - co by się musiało stać, gdybyś chciał/a do swojej aplikacji dorzucić `FilePrinter` czy `DialogPrinter`?
   - co by było gdyby w klasie `HelloWorld` pojawiło się jeszcze 10 rożnych zaleźności?
   
**Stworzyliśmy luźne zaleźności, ale nie pozbyliśmy się wcale tworzenia obiektów klas implementujących `Printer`. Nie mniej, należą Ci się jak najbardziej gratulacje, a o rozwiązaniu pozostałego problemu pomyślimy za chwilę.**

[Rozwiązanie zadania](solutions/A.3.1.md)

---

### A.3.2. Eliminacja zależności z wykorzystaniem Factory Method

Zanim zaczniemy korzystać z Dependency Injection, jako naszego Złotego Grala luźnych zależności, wprowadzimy prostszy mechanizm - wzorzec Factory Method. Mam nadzieję, że ten wzorzec nie raz już stosowałeś/aś i będzie to dla Ciebie błahostką.

Twoje kolejne zadania to:
- wprowadzenie klasy implementującej wzorzec Factory Method i umożliwiającej wygodne tworzenie obiektów różnych wersji interfejsu `Printer`,
- zamiana ręcznego tworzenia obiektów klasy `ConsolePrinter`, `FilePrinter` i `DialogPrinter` na wykorzystanie wzorca Factory Method.

**Zaczynamy!**

---

1. Stwórz klasę `Printers` w pakiecie `pl.hit.spring.core.components.printers`.
1. W klasie `Printers` utwórz trzy publiczne metody statyczne. Dla każdej z tych metod typem zwracanym ma być `Printer`. Metody posłużą do tworzenia obiektów odpowiednich klas implementujących ten interfejs. Dobierz zatem dla nich, krótkie, jednoznaczne nazwy.
1. Zmodyfikuj teraz kod metody `main` w klasie `HelloWorld` w taki sposób, abyś nie musiał/a tworzyć ręcznie obiektów, a zamiast tego wywoływać odpowiednie metody klasy `Printers`.
1. Przetestuj działanie metody `main`.

---

**To nie koniec :) Wprowadzimy drobne zmiany, które pokażą Ci, jak bardzo _luźne zaleźności_ są nam potrzebne**

1. Wprowadź modyfikację do klasy `ConsolePrinter`. Niech klasa teraz posiada pole typu `PrintStream`. Pole to będzie reprezentować faktyczny strumień, do którego zapisywana jest wiadomość. Nazwij dowolnie pole i pozwól je ustawiać w konstruktorze klasy `ConsolePrinter`. W klasie powinien być dostępny tylko ten konstruktor (nie twórz konstruktora bezargumentowego).

    W których miejscach projektu kod przestał się kompilować? Czy klasa `HelloWorld` _ucierpiała_ na tej zmianie?
    
1. Popraw kod w klasie `Printers`. Niech dotychczasowa metoda udostępniająca obiekt klasy `ConsolePrinter` tworzy obiekt ustawiając mu strumień wyjściowy `System.out`.
1. Dopisz również metodę, która zwraca obiekt klasy `ConsolePrinter`, ale utworzony ze strumieniem wyjściowym `System.err`.
1. Uruchom testy w metodzie `main` klasy `HelloWorld` i sprawdź czy wszystko działa.

**Wielkie gratulacje! Nie tylko udało Ci się wprowadzić luźne wiązania na poziomie zaleźności i tworzenia obiektów, ale dodatkowo zmusiłeś/aś je do działania**.

[Rozwiazanie zadania](solutions/A.3.2.md)

---

#### A.3.3. Rozszerzone możliwości z Inversion of Control

Skoro mamy wdrożoną ideę Inversion of Control, na razie w postaci wzorca Factory Method, to warto skorzystać z naszego centralnego zarządzania. Skorzystamy z dwóch dodatkowych wzorców oraz wprowadzimy globalne modyfikacje do naszych obiektów typu `Printer`.

Twoje kolejne zadania to:
- wprowadzenie wzorca Singleton, tak aby w klasie `Printers` obiekty tworzone były tylko raz,
- wprowadzenie wzorca Decorator, tak, aby móc audytować wszystkie komunikaty wyświetlane z wykorzystaniem obiektów typu `Printer`.

**Zaczynamy!**

---

W pierwszej kolejności zauważ, że nasze klasy implementujące interfejs `Printer` nie określają żadnego stanu dla swoich obiektów (czyli pól). Jedynym wyjątkiem jest tutaj klasa `ConsolePrinter`, ale również jej stan jest dość niezmienny po utworzeniu obiektu.

W klasie `Printers` metody fabrykujące tworzą za każdym razem nowe obiekty, a skoro nasze obiekty nie posiadają stanu, to nie ma potrzeby aby powstawały od nowa. Wystarczy nam po jednej sztuce.

1. Wprowadź wzorzec Singleton (w dowolny znany Ci sposób), aby klasa `Printers` udostępniała pojedynczy obiekt każdej z klas za każdym razem gdy zostanie wywołana odpowiednia metoda fabrykująca. Pamiętaj tylko, że dla klasy `ConsolePrinter` mamy dwa obiekty o różnych stanach (argumentach konstruktora).
1. Przetestuj ponownie działanie klasy `Printers` uruchamiając metodę `main` z klasy `HelloWorld`.

---

**Wprowadziłeś/aś wzorzec Singleton. Teraz czas na Decorator.**

1. Utwórz klasę `LoggablePrinter`, która będzie implementowała interfejs `Printer`. Zadaniem klasy będzie logowanie do pliku `audit.log` wszystkich wiadomości zapisywanych do dowolnego obiektu implementującego interfejs `Printer`. Plik ma się znajdować w katalogu domowym.
1. Klasa powinna implementować wzorzec Decorator i dekorować działanie innych implementacji interfejsu `Printer`, więc spraw aby to robiła. Tutaj możesz zobaczyć jak taka przykładowa implementacja wygląda: [Decorator Pattern](https://github.com/iluwatar/java-design-patterns/tree/master/decorator).
1. W klasie `Printers` wykyrzystaj swojego Decorator'a, aby zwracane obiekty klas `ConsolePrinter`, `FilePrinter` oraz `DialogPrinter` logowały swoje wiadomości do pliku audotowego.
1. Przeprowadź testy i sprawdź czy w pliku `audit.log` pojawiają się wiadomości ze wszystkich obiektów.

---

**Mam nadzieję, że już dostrzegasz moc Inversion of Control.**

Wybraź sobie, że w Twojej aplikacji mogą pojawić się dziesiątki, setki a może nawet tysiące punktów, w których korzystasz z różnych obiektów typu `Printer`. W ten prosty sposób zoptymalizowałeś/aś zużycie pamięci oraz dodałeś/aś audytowanie tego co jest wyświetlane we wszystkich źródłach. Nie wprowadzając najmniejszej zmiany w kodzie korzystającym z klasy `Printers`. 

**VIVAT IoC! VIVAT!**

[Rozwiązanie zadania](solutions/A.3.3.md)

---

### A.3.4. Konfiguracja zaleźności w Spring Framework

Wiesz już, że luźne zaleźności to ważna sprawa, a wprowadzenie mechanizmów Inversion of Control, to czysty zysk dla Ciebie samego/samej. Udało się podjąć istotną, samodzielną próbę takiego wdrożenia, ale nie mniej - daleko naszemu prostemu mechanizmowi do tego, co dostajemy od Spring'a.

Przejdziemy zatem od IoC opartego na wzorcu Factory Method do IoC opartego na wzorcu Depndency Injection. W zadaniu wykorzystamy konfiguracje zależności opartą na adnotacjach oraz konfigurację opartą na pliku XML.

Twoje kolejne zadania to:
- uzupełnienie zaleźności do wybranej klasy implementującej interfejs `Printer`,
- konfiguracja wstrzyknięcia zależności przez konstruktor,
- konfiguracja wstrzyknięcia zależności przez metode typu set,
- testy działania klasy `HelloWorld` z automatycznie dowiązaną zależnością,
- próba wykorzystania _niezaspokojonych_ zaleźności i analiza efektów.

**Zaczynamy!**

---

1. W ramach tego zadania potrzebujemy wykorzystać kod `HelloWorld`, z którym skończyliśmy część A.2., bez zmian wniesionych w zadaniach A.3.1-A.3.3. Jeżeli nie wiesz jak się do tej wersji dogrzebać, to tutaj jest gotowiec: [HelloWorld](source/HelloWorld.java).
1. W klasie `HelloWorld` wprowadź pole reprezentujące obiekt dowolnej klasy typu `Printer`. Ważne, aby pole tym razem nie było typu `Printer`, a typu konkretnej klasy implementującej ten interfejs.
1. Dostarcz konstruktor, który przyjmie odpowiedni parametr i na jego podstawie skonfiguruje stworzone przed chwilą pole.
1. Zmodyfikuj również kod metody `sayHello`, aby przekazywała wyświetlenie tekstu `"Hello, world!"`, do metody `print` w polu typu `Printer`.
1. Skonfiguruj automatyczne uzupełnienie zależności na poziomie konstruktora korzystając z adnotacji `@Autowired`.
1. Uruchom aplikację (klasę `PrinterApplication`) i sprawdź czy uzyskany bean typu `HelloWorld` prawidłowo wykorzystuje wybraną przez Ciebie klasę _Printera_.
1. W ramach doświadczenia usuń adnotację `@Autowired` i sprawdź czy bean `HelloWorld` nadal działa. Jaka jest Twoja obserwacja?

---

**Brawo! Potrafisz wstrzykiwać zależności na poziomie konstruktora. Teraz czas na metody.**

1. W klasie `HelloWorld` dodaj kolejne pole reprezentujące jakiś obiekt wybranej klasy implementującej interfejs `Printer`, ale innej niż w pierwszym polu.
1. Dostarcz metodę typu `set` dla tego pola oraz zmodyfikuj kod metody `sayHello`, aby losowo wybierała raz jednego _printera_, a innym razem drugiego.
1. Skonfiguruj automatyczne wiązanie na poziomie metody `set` korzystając z adnotacji `@Autowired`.
1. Przeprowadź teraz testy w klasie `PrinterApplication`. Sprawdź czy nasz bean typu `HelloWorld` prawidłowo używa obu printerów.
1. W ramach doświadczenia usuń adnotację `@Autowired` i sprawdź czy bean `HelloWorld` nadal działa? Jaka jest Twoja obserwacja?

---

**Udało się wykorzystać również konfiguracje zależności na poziomie metod. Istnieje też konfiguracja na poziomie pól, ale z tego nie chcesz korzystać. Wykonamy jeszcze krótkie doświadzenie, które pokieruję Cię do kolejnego zadania.**

1. W klasie `HelloWorld` dodaj teraz pole `somePrinter` o typie `Printer`. 

   > Zauważ, że taka definicja pola jest zgodna z ideą luźnych powiązań, bo nie zamykamy się na konkretną implementację. Nie chcemy przecież, aby korzystając z tego Świętego Grala, jakim jest Dependency Injection i Spring Framework, zniknęło nam z oczu to, o co tu naprawdę chodzi.
1. Dla wprowadzonego pola utwórz metodę typu `set` oraz oznacz ją adnotacją `@Autowired`.
1. Uruchom teraz aplikację `PrinterApplication`. Czy nasz bean działa prawidłowo? Czy udało się go stworzyć?

---

**Spotkało Cię to samo nieszczęście, co w zadaniu A.2.3., a więc Spring nie potrafi zaspokoić naszej zależności, bo jest niejednoznaczna.** 

Również nie potrafiłby zaspokoić zależności, gdyby nie było żadnego kandydata. Pozostaje tak to nam zostawić, ale w kolejnym zadaniu rozwiążemy ten problem. Tymczasem... gratuluję wprowadzenia Dependency Injection opartego na Spring Framework!

[Rozwiązanie zadania](solutions/A.3.4.md)

---

### A.3.5. Zaleźności kwalifikowane

Już dwa razy spotkałeś/aś się z problemem niejednoznacznych zaleźności, które zablokowały nam ideę luźnych powiązań. Przyszedł czas na rozwiązanie tego problemu.

Twoje kolejne zadania to:
- wprowadzenie kwantyfikatoru `@Primary`, który precyzuje wiązanie,
- wprowadzenie kwantyfikatoru `@Qualifier`, bazującego na nazwanym beanie,
- wprowadzenie własnego kwantyfikatory precyzującego `@Qualifier`.

**Zaczynamy!**

1. Dodaj do klasy `ConsolePrinter` adnotację `@Primary` pochodzącą ze Spring Framework.
1. Dodaj adnotację `@Primary` również na metodzie `set` ustawiającej nasze luźne pole typu `Printer` w klasie `HelloWorld`.
1. Uruchom klasę `PrinterApplication`. Czy automatyczne wiązanie zaleźności powiodło się?

   > Adnotacja `@Primary` rozwiązuje problem naszej niejednoznaczności, ale należy używać jej ostrożnie i monitorować ile razy się pojawia. Jak łatwo się domyśleć, nie powinna więcej niż raz w danej grupie zależnych klas.

---

**Teraz czas na bardziej jednoznaczne dopasowanie**.

1. Zmień typ pola drugiego _printera_ również na interfejs `Printer` oraz odpowiadającą mu metodę `set`.
1. Dodaj nad metodą adnotację `@Qualifier`, a wniej wprowadź wartość tekstową odpowiadającą nazwie klasy konkretnej implementacji interfejsu `Printer`, której użyłeś (ta przed zmianą typu pola i settera). Pamiętaj tylko, aby pierwszą literę nazwy klasy zamienić na mała.
1. Uruchom klasę `PrinterApplication`. Czy automatyczne wiązanie zależności powiodło się?

   > Adnotacja `@Qualifier` pozwala na bardzo precyzyjny wybór komponentu podając wprost nazwę bean'a.
   
---
   
**Przyszedł czas na ostatni element, a więc naszą własną adnotację kwalifikującą.**

1. Utwórz adnotację `@Console` w pakiecie `pl.honestit.spring.core.qualifiers`. Pamiętaj aby oznaczyć swoją adnotację adnotacjami  konfiguracyjnymi `@Retention` oraz `@Target`. Nasza adnotacja powinna być dostępna w czasie wykonania oraz umożliwiać wykorzystanie na poziomie: konstruktora, metody, typu oraz pola.
1. Oznacz adnotację `@Console` również adnotacją `@Qualifier`.
1. Skoro adnotacja jest gotowa, to wprowadź ją w klasie `ConsolePrinter` (oznacz nią klasę)
1. W klasie `HelloWorld` zmień typ pierwszego pola _printera_ na `Printer` oraz dostosuj parametr konstruktora. Nad konstruktorem umieść również adnotacje `@Autowired` oraz Twoją `@Console`.
1. Uruchom klasę `PrinterApplication`. Czy automatyczne wiązanie zależności powiodło się?

**Gratulację! Potrafisz już programować zgodnie z zasadą luźnych powiązań i precyzować swoje powiązania tak, aby mechanizm Dependency Injection w Spring Framework nadal świetnie sobie radził i robił za Ciebie robotę!**

---

### Gratuluję!

Zrealizowałeś/aś ostatnie zadanie ostatniej części tej sekcji repozytorium. To na pewno był dla Ciebie cenny zbiór nowych doświadczeń oraz umiejętności. Idź z nimi dalej w świat i czyń sobie Spring'a poddanym!

