# B. Tworzenie aplikacji webowych ze Spring MVC

Każda technologia, aby była atrakcyjna, poza pięknymi ideami musi też udostępniać zestaw praktycznych narzędzi i technik umożliwiających sprawne tworzenie aplikacji. W przypadku aplikacji webowych takim narzędziemi i zbiorem technik jest Spring MVC. Obecny w technologii od samego początku był propozycją twórcy Spring podejście do obsługi warstwy webowej lżejsze niż servlety.

Spring MVC nie odrzuca technologii Servletów, ale na niej bazuje. Stąd tak ważna jest znajomość technologii Servletów i JSP przed rozpoczęciem pracy ze Spring MVC. Nie mniej, Spring MVC wnosi lżejsze podejście oparte na pojedynczym Servlecie, pełniącym rolę wzorca _Front Controller_ i delegującym obsługę żądań do lekkich kontrolerów.

Świat Spring MVC jest równie fascynujący jak świat Servletów, a wszystko, co wiesz o Servletach, zaprocentuje w pracy ze Spring MVC.

---

## Wprowadzenie

Rozpoczynasz pracę z drugą częścią repozytorium. Część ta dotyczy Spring MVC. Główny nacisk położymy na to, abyś nie tylko nauczył/a się wielu nowych _rzeczy_, ale również, abyś większość z nich zrozumiał/a.

W trakcie zadań:
- skonfigurujesz projekt pod Spring MVC i samego Spring MVC, z jego kluczowym elementem: `DispatcherServlet`,
- nauczysz się tworzyć kontrolery i obsługiwać ich rozbudowane mapowania,
- wymienisz swoje dotychczasowe doświadczenia z pracą z obiektem `HttpServletRequest` i `HttpServletResponse` na nowe, w których pracować będziesz z samymi parametrami, ścieżkami i widokami,
- zrealizujesz kolejne funkcjonalności zgodne ze wzorcem MVC, a także wykorzystujące inne wzorce projektowe typu _Entity Controller_, _Service Layer_ czy _Data Transfer Object_.

Czeka Cię dużo ciężkiej pracy :)

---

## B.1. Konfiguracja Spring MVC

Na początek potrzebujemy projektu z wymaganym zestawem zależności i bazowych konfiguracji. Potem przygotujemy konfiguracę samego Spring MVC, aby wpiąć go w cykl życia kontenera (Tomcata). Bez tego nasz Spring, choć fajny, to nie będzie uczestniczył w obsłudze żądań użytkownika i generowaniu dla niego odpowiedzi. Ostatni krok, który mamy do wykonania, to przetestowanie całej konfiguracji z wykorzystaniem prostego kontrolera typu `HelloWorld`.

---

### B.1.1. Przygotowanie projektu (:watch: 10 minut)

Zaczynamy od utworzenia projektu z poprawnym zestawem zależności i skonfigurowanym Tomcat'em.

Twoje kolejne zadania to:
- utworzenie w katalogu `projects` nowego projektu maven'owego o typie webapp i nazwie `SpringMVC`,
- uzupełnienie zależności do Spring Context, Spring MVC, Servlets w wersji 4 oraz JSTL,
- konfiguracja Tomcat'a i konfiguracja deploymentu naszej aplikacji,
- uruchomienie Tomcat'a i weryfikacja czy aplikacji deploy'uje się poprawnie.

**Zaczynamy!**

---

1. Stwórz nowy projekt w IntelliJ, jako projekt typu maven i o archetypie `org.apache.maven.archetypes:maven-archetype-webapp`.
1. Ustaw grupę projektu na `pl.honestit.spring` oraz artefakt na `spring-mvc`.
1. Nazwij projekt `SpringMVC` i stwórz go w ścieżce `projects/SpringMVC` (katalog `project` jest wewnątrz katalogu repozytorium).
1. Dostosuj plik `pom.xml`, aby:
   - pliki źródłowe miały kodowanie `UTF-8`,
   - wykorzystywana była wersja Java `1.8` lub wyższa (np. `1.11`).
1. Utwórz katalogi `java` oraz `resources` wewnątrz katalogu `main`. Pierwszy oznacz jako katalog z plikami źródłowymi, a drugi jako katalog z zasobami (opcje `Mark Directory as -> ...`).
   
**Podstawową konfigurację mamy za sobą. Teraz zależności.**

1. Uzupełnij zestaw zależności do Spring Context i Spring Web MVC w wersjach `5.0.7.RELEASE`.

   > Pamiętaj, że obie zależności należą do tej samej grupy i ich wersje MUSZĄ być spójne.
   
1. Uzupełnij zestaw zależności do Servletów i JSTL w wersjach odpowiednio `4.0.1` oraz `1.2`.
1. Przebuduj projekt, aby mieć pewność, że na tym etapie wszystko działa poprawnie.

**Przechodzimy do konfiguracji Tomcat'a i deploy'owania naszej aplikacji**

1. Utwórz konfigurację Tomcat'a (musi być wersja 9-ta, aby obsługiwał specyfikację Servletów w wersji 4-tej), zgodnie z poniższymi zasadami:
   - konfiguracja lokalna,
   - nazwa `Tomcat Local`,
   - obsługa HTTP na porcie `8080`.
1. Dodaj artefakt naszej aplikacji (w wersji rozpakowanej - `exploded`) do deploy'mentu Tomcata. Ustaw ścieżkę kontekstu (`application path`) artefaktu na ścieżkę główną, czyli `/`.
1. Uruchom teraz Tomcat'a aby sprawdzić czy nasza aplikacja jest poprawnie na nim deployowana. Powinna otworzyć się strona `index.jsp` z katalogu `webapp`.
1. Na koniec usuń stronę `index.jsp` (była nam potrzebna tylko do potwierdzenia poprawnej konfiguracji i możliwości uruchomienia tomcat'a, teraz potrzebna już nie jest).

**Gratulację! Projekt został przygotowany**.

---

### B.1.2. Przygotowanie Spring MVC (:watch: 20 minut)

Mając projekt aplikacji webowej i poprawnie skonfigurowanego Tomcat'a są podstawy aby myśleć o wykorzystaniu Spring MVC. Czas zamienić myśli w czyny!

Twoje kolejne zadania to:
- Utworzenie głownej klasy konfiguracyjnej odpowiedzialnej za skanowanie komponentów globalnych,
- Utworzenie klasy konfiguracyjnej dla Spring MVC,
- Rejestracja kontekstów oraz Servletu `DispatcherServlet` w kontenerze.

**Zaczynamy!**

1. W pakiecie `pl.honestit.spring.mvc.config` utwórz klasę `RootConfig`.
1. Oznacz klasę adnotacją `@Configuration`, aby mogła dostarczać konfiguracji beanów oraz włącz na niej automatyczne skanowanie komponentów dla pakietów `pl.honestit.spring.mvc.core` - adnotacja `@ComponentScan`. Pamiętaj, aby wykluczyć ze skanowania adnotację `@EnableWebMvc`. Dzięki temu nasza konfiguracja główna nie będzie powielać logiki adnotacji `@EnableWebMvc` na konfiguracji Spring MVC.

   > Wykluczenie obsługi adnotacji `@EnableWebMvc` nie jest niezbędne gdy skanowane pakiety przez `RootConfig` nie zawierają konfiguracji Spring MVC, ale lepiej dodać tą opcję, gdyby schemat Twoich pakietów później się zmienił.
   
1. Utwórz pakiet `pl.honestit.spring.mvc.core`, aby wykluczyć ewentualne problemy z nieistniejącym pakietem.

Nasza klasa na razie nie udostępnia żadnych komponentów i pewnie przez jakiś czas jeszcze nie będzie tego robić, ale to się zmieni. 

**Stwórzmy teraz konfigurację dla Spring MVC.**

1. W pakiecie `pl.honestit.spring.mvc.config` utwórz klasę `WebConfig`, która zaimplementuje interfejs `WebMvcConfigurer`.
1. Oznacz klasę adnotacją `@Configuration`, aby stała się konfiguracją oraz włącz na niej automatyczne skanowanie dla pakietu `pl.honestit.spring.mvc.web`. Utwórz też ten pakiet, aby wykluczyć problemy z nieistniejącym pakietem.
1. Włącz Spring MVC adnotacją `@EnableWebMvc`.

**Konfiguracja Spring MVC również za Tobą. Przyszedł czas na dogadanie się z kontenerem Servletów.**

1. W pakiecie `pl.honestit.spring.mvc.config` stwórz klasę `ApplicationInitializer`, która będzie implementowała interfejs `WebApplicationInitializer`. Dostarcz pustą implementację dla wymaganej metody `onStartup` i zrealizuj kolejne punkty, aby uzyskać implementację końcową.
1. W metodzie `onStartup` stwórz główny kontekst aplikacji - klasa `AnnotationConfigWebApplicationContext`. Kontekst ten ma obsługiwać klasę `RootConfig`. Pamiętaj również o rejestracji listenera `ContextLoaderListener`.
1. Zaimplementuj dalej w metodzie konfigurację kolejnego kontekstu, ale tym razem dla konfiguracji `WebConfig`.
1. Ostatnim krokiem jest zarejestrowanie w obiekcie klasy `ServletContext` servletu `DispatcherServlet`. Pamiętaj, aby w konstruktorze klasy `DispatcherServlet` przekazać kontekst obsługujący konfigurację `WebConfig`.
1. Po zarejestrowaniu servletu `DispatcherServlet` skonfiguruj jego mapowanie na ścieżkę `/` oraz ustaw, aby ładował się przy starcie kontenera.
1. Uruchom teraz ponownie aplikację - w logach podczas deploy'owania powinny pojawić się informację dotyczące ładowania kontekstów, a sam deploy'ment powinien zakończyć się bez błędów (sukcesem).

**Kolejne gratulację - jesteś blisko działającego Spring MVC!**

[Rozwiązanie zadania](solutions/B.1.2.md)

---

### B.1.3. Pierwszy kontroler (:watch: 30 minut)

Nasz projekt wygląda na gotowy do przyjmowania żądań użytkownika i kierowania ich do Spring MVC. Brakuje tylko kogoś kto te żądania mógłby obsłużyć.

Twoje kolejne zadania to:
- utworzenie prostego kontrolera `HelloController` obsługującego ścieżkę `/hello`,
- dodanie obsługi metody `GET`, w ramach której zrealizujesz jeden z dwóch wariantów:
  - zwrócisz tekst `Hello, world`,
  - przekażesz obsługę do strony `hello.jsp` w ścieżce `/WEB-INF/views/hello.jsp`,
- przetestujesz kontroler czy prawidłowo obsługuje żądanie.

**Zaczynamy!**

---

1. W pakiecie `pl.honestit.spring.mvc.web.controllers` utwórz klasę `HomeController`.
1. Oznacz klasę `HomeController` odpowiednimi adnotacjami, aby klasa ta stała się komponentem o roli kontrolera MVC.
1. W klasie zaimplementuj metodę `public String sayHello()`. Metoda powinna służyć do obsług żądań typu `GET`, na ścieżce `/hello` - zadecyduj czy skorzystasz z globalnego mapowania kontrolera czy mapowania samej metody.

**Pierwszy wariant - tutaj**

1. Dodaj do metody `sayHello` adnotację `@ResponseBody`. Adnotacja ta mówi, że wynik działania metody powinien być uwzględniony w odpowiedzi HTTP. Gdy tej adnotacji nie ma, to wynik działania metody będzie interpretowany jako identyfikator (ścieżka) widoku do dalszej obsługi żądania (w końcu to MVC).
1. W metodzie zwróć klasyczne powitanie :)
1. Uruchom aplikację i przejdź na adres `localhost:8080/hello`, aby sprawdzić czy Twoje powitanie pojawiło się.

**Drugi wariant - tutaj**

1. W katalogu `WEB-INF` stwórz katalog `views`, a w nim plik `hello.jsp`.
1. W pliku `hello.jsp` wyświetl klasyczne powitanie dla użytkownika.
1. W metodzie `sayHello` musimy zwrócić wartość będąca poprawnym identyfikatorem widoku, który docelowo wygeneruje treść odpowiedzi. Nasz plik ma się znaleźć w ścieżce projektu `src/main/webapp/WEB-INF/views/hello.jsp`. Na podstawie tej informacji zwróć w metodzie `sayHello` poprawną ścieżkę do pliku `hello.jsp`. 

   > Przypomnij sobie tylko w którym miejscu zaczynają się ścieżki wewnątrz aplikacji Servletowej.
   
1. Uruchom aplikaję i przejdź na adres `localhost:8080/hello`, aby sprawdzić czy Twoje strona została poprawnie wyświetlona.
1. Spróbuj teraz jeszcze wejść bezpośrednio na stronę z powitaniem `hello.jsp` - czyli nie przez ścieżkę kontrolera, a przez ścieżkę do pliku `.jsp`. Czy udało się to zrobić?

   > Przypomnij sobie rolę/znaczenie katalogu `WEB-INF` i zasady jego obsługi przez kontener webowy.

**Jeżeli zrealizowałeś/aś jeden z dwóch wariantów, to teraz pora na ten, którego nie wybrałeś/aś ;)**

1. Zapoznaj się teraz z drugim wariantem, czyli tym, którego nie wybrałeś/aś.
1. Oba warianty zakładają obsługę na ścieżce `/hello` i metodzie `GET`, co nie jest możliwe. Stąd ten nie wybrany wcześniej wariant musisz zrealizować na nowej ścieżce, np. `/hello2`, ale oczywiście w tym samym kontrolerze.
1. Obsłuż drugi wariant.

**Pełne gratulacje! Nasz pierwszy kontroler działa! Nasza pierwsza strona JSP w Spring MVC działa!**

[Rozwiązanie zadania](solutions/B.1.3.md)

---

### Gratuluję!

Udało Ci się przejść przez całą fazę rozruchową! Posiadasz skonfigurowany projekt gotowy do niepohamowanego wzrostu. Posiadasz konfigurację Spring MVC, która ten wzrost będzie wspierała! Posiadasz kontroler typu `HelloWorld`, który jest pierwszym etapem tego jak projekt wzrasta!

---

## B.2. Tworzenie i mapowanie kontrolerów

Jednym z problemów technologii Servletów jest ich toporne mapowanie żądań. Ogranicza się ona tylko do ścieżek zasobów (`request URI`). Każda bardziej prezycyzyjna konfiguracja, która byłaby nam potrzebna, musi być ręcznie obsłużona. Jest to nadwyraz częsta sytuacja. Spójrz chociaż na nasz prosty `HomeController` z zadania B.1.3. - wyobrażasz sobie, że dla każdego z wariantów trzeba by tworzyć osobny servlet?

Spring MVC wprowadza dużo bardziej prezycyzjne możliwości mapowania żądań. Ze wszystkimi możliwościami będziesz się stopniowo zapoznawać w miarę nauki tej technologii. Natomiast z tymi najbardziej kluczowymi czas zapoznać się już teraz.

---

### B.2.1. Mapowanie względne i bezwzględne (:watch: 40 minut)

Najprostszymi mapowaniami są mapowania bezwzględne, w których ścieżka wynika wprost z adnotacji kontrolera lub adnotacji jego metody. Często wygodniejszymi są jednak mapowania względne, w których ścieżka wynika z połączenia adnotacji kontrolera i adnotacji metody.

> W trakcie realizacji zadania w każdej z metod kontrolerów (albo na całym kontrolerze) skorzystaj z adnotacji `@ResponseBody`, aby wynik metody był bezpośrednio wyświetlany jako odpowiedź. Zwracany tekst powinien w jakiś sposób nawiązywać do nazwy/roli metody, aby było wiadomo, która metoda została wywołana.

Przykładowa implementacja kontrolera, który obsłuży ścieżkę `/example` na metodzie `GET` może wyglądać jak niżej:

```java
@Controller
@ResponseBody
public class ExampleController {
    
    @GetMapping("/example")
    public String get() {
        return "ExampleController.get";
    }
}
```

ale może też wyglądać tak:

```java
@Controller
@ResponseBody
@RequestMapping("/example")
public class ExampleController {

    @GetMapping
    public String get() {
        return "ExampleController.get";
    }
}
```

> Wszystkie klasy kontrolerów twórz w pakiecie `pl.honestit.spring.mvc.web.controllers`.

Twoje kolejne zadania to:

1. Dostarczenie kontrolera `IntroController`, który obsłuży ścieżkę `/intro` na metodzie `GET`. Przetestuj działanie kontrolera.
1. Dostarczenie kontrolera `FillFormController`, który obsłuży ścieżkę `/fill-form` na metodzie `POST`. Przetestuj działanie kontrolera.
1. Dostarczenie kontrolera `TestRegisterController`, który obsłuży ścieżkę `/test-register` na metodzie `GET` i `POST` (osobnymi metodami kontrolera). Przetestuj działanie kontrolera.
1. Dostarczenie kontrolera `TestBookController`, który obsłuży ścieżki:
   - `/test-book` na metodzie `GET`,
   - `/test-book/add` na metodzie `POST`,
   - `/test-book/edit` na metodzie `POST`,
   - `/test-book/delete` na metodzie `POST`.
   
   Przetestuj działanie kontrolera.
   
1. Dostarczenie kontrolera `TestUserController`, który obsłuży ścieżki:
   - `/test-user` na metodzie `GET`,
   - `/test-user` na metodzie `POST`,
   - `/test-user` na metodzie `PUT`,
   - `/test-user` na metodzie `DELETE`.
   
   Przetestuj działanie kontrolera.
  

[Rozwiązanie zadania](solutions/B.2.1.md)

---

### B.2.2. Obsługa parametrów żądania (:watch: 40 minut)

Poprzednie zadanie pokazało jedno z udogodnień Spring MVC, które chcielibyśmy mieć na poziomie obsługi Servletu: łatwe delegowanie obsługi ścieżek do konkretnych metod, a w szczególności ścieżek względnych (podrzędnych).

Kolejnym udogodnieniem jest możliwość wyeliminowania potrzeby pracy z obiektami `HttpServletRequest` i `HttpServletResponse`. Obiekty te Spring MVC traktuje jako obiekty bardzo niskopoziomowe reprezentujące całe żądanie i całą odpowiedź. Spring MVC proponuje tutaj lżejsze podejście, polegające na dużej automatyzacji ze strony Spring'a. Najpierw jednak zobaczymy, że wciąż możliwe jest wykorzystanie tych obiektów w metodach kontrolera. Dopiero potem poznamy jak to robi Spring MVC.

W ramach tego zadania będziesz pracować z surowym żądaniem - w stylu Servletów - oraz w stylu Spring MVC

Twoje kolejne zadania to:

- Utworzenie kontrolera `ParametersController` w pakiecie `pl.honestit.spring.mvc.web.controllers`, który:
  - dla żądania `GET /parameters/raw` wyświetli parametry `first` i `second` wykorzystując obiekt `HttpServletRequest`,
  - dla żądania `GET /parameters/spring` wyświetli parametry `first` i `second` wykorzystując adnotację
  `@RequestParam`
- przetestowanie działania obu metod kontrolera.

**Zaczynamy!**

---

> Jeżeli nie potrzebujesz pomocy, to przeskocz opis niżej i zapoznaj się tylko z częścią
dotyczącą testowania.

1. Utwórz klasę `ParametersController` w pakiecie `pl.honestit.spring.mvc.web.controllers`. Oznacz ją wymaganymi adnotacjami aby była kontrolerem oraz obsługiwała ścieżkę `/parameters`.
1. Utwórz metodę `public String serveRawParameters`, która będzie przyjmować obiekt klasy `HttpServletRequest`. Metodę oznacz adnotacjami `@GetMapping("/raw")` oraz `@ResponseBody`.
1. W metodzie `serveRawParameters` pobierz z obiektu `HttpServletRequest` parametry o nazwie `first` i `second` oraz wyświetl je w odpowiedzi. Uważaj tylko na potencjalny `NullPointerException` gdyby się okazało, że któregoś z parametrów nie ma.
1. Utwórz teraz drugą metodę `public String serveSpringParameters`, która będzie przyjmować parametry
`String first` oraz `String second`. Metodę oznacz adnotacjami `@GetMapping("/spring")` oraz `@ResponseBody`.
1. Oznacz parametry metody `serveSpringParameters` odpowiednio adnotacjami `@RequestParam("first")` oraz `@RequestParam`. W drugim przypadku zwróć uwage, że ma nie być nazwy parametru wewnątrz adnotacji.
1. Zaimplementuj teraz treśc metody, aby poprawnie wyświetlała wartości parametrów. Uważaj tylko na potencjalny `NullPointerException`.

#### Implementacje są gotowe, więc przyszedł czas na ich testy, TESTY, TeStY, tEsTy, TESTY!!!

1. Wywołaj żądanie [localhost:8080/parameters/raw?first=a&second=b](localhost:8080/parameters/raw?first=a&second=b). Jaki efekt?
1. Wywołaj żądanie [localhost:8080/parameters/raw?first=a](localhost:8080/parameters/raw?first=a). Jaki efekt?
1. Wywołaj żądanie [localhost:8080/parameters/raw?second=b](localhost:8080/parameters/raw?second=b). Jaki efekt?
1. Wywołaj żądanie [localhost:8080/parameters/raw](localhost:8080/parameters/raw). Jaki efekt?

1. Wywołaj żądanie [localhost:8080/parameters/spring?first=a&second=b](localhost:8080/parameters/spring?first=a&second=b). Jaki efekt?
1. Wywołaj żądanie [localhost:8080/parameters/spring?first=a](localhost:8080/parameters/spring?first=a). Jaki efekt?
1. Wywołaj żądanie [localhost:8080/parameters/spring?second=b](localhost:8080/parameters/spring?second=b). Jaki efekt?
1. Wywołaj żądanie [localhost:8080/parameters/spring](localhost:8080/parameters/spring). Jaki efekt?

   > Jeżeli nie stała się rzecz niezwykła, to trzy ostatnie żądanie nie zakończyły się poprawnym wyświetleniem parametrów. Mało tego, Twój skwapliwie napisany kod, przygotowany na pracę z `NullPointerException`, nawet nie miał okazji zadziałać. Wykonaj kolejne kroki, aby Spring MVC dał Ci możliwość wykorzystania tego kodu.
   
1. W metodzie `serveSpringParameters` dodaj do adnotacji `@RequestParam` nad parametrem `first` wartość `required = false`.
1. W metodzie `serveSpringParameters` dodaj do adnotacji `@RequestParam` nad parametrem `second` wartość `defaultValue = "Abrakadabra"`.
1. Wywołaj ponownie żądania do ścieżki `/parameters/spring`. Jaki teraz jest efekt?
1. Zastanów się nad domyślnym sposobem obsługi żądania przez Spring MVC - tutaj w kontekście parametrów. Porównaj takie podejście, do obsługi parametrów w Servletach.
1. Na koniec usuń z metody obsługującej ścieżkę `/parameters/spring` adnotacje `@RequestParam`. Przetestuj czy metoda działa poprawnie i jaki sposób zachowania przyjmuje Spring MVC gdy w ogóle nie ma adnotacji `@RequestParam`?

**Gratuluję ukończenia ważnego zadania!**

[Rozwiązanie zadania](solutions/B.2.2.md)

---

### B.2.3. Obsługa parametrów ścieżek (:watch: 40 minut)

Spring MVC nie tylko pozwala obsługiwać w dużo łatwiejszy (oraz inny) sposób parametry żądania. Umożliwia również wygodną pracę z tzw. parametrami ścieżki, a więc segmentami adresu żądania, które mają dla naszego kontrolera jakieś szczególne znaczenie. Możliwość ta dotyczy w głównej mierze aplikacji w stylu REST, gdzie w ścieżkach zaszywa się identyfikatory konkretnych obiektów. Korzysta się jednak z niej także w aplikacjach generujących odpowiedź HTML.

Twoje kolejne zadania to:

- utworzenie kontrolera `PathsParamsController` w pakiecie `pl.honestit.spring.mvc.web.controllers`, który będzie symulował obsługę użytkownika na różnych ścieżkach, ale zaczynających się od `/paths/user`,
- utworzenie metody obsługującej `GET /paths/user/raw/30/delete`, która z wykorzystaniem obiektu `HttpServletRequest` wyświetli identyfikator użytkownika będący przedostatnim fragmentem ścieżki oraz informacje o operacji będącą ostatnim fragmentem ścieżki

  > Wartość identyfikatora i metody mogą się zmieniać. Podana ścieżka jest tylko przykładowa.
  
- utworzenie metody obsługującej `GET /paths/user/spring/30/delete`, która z wykorzystaniem adnotacji `@PathVariable` zmapuje fragmenty ścieżki do odpowiednich parametrów,
- przetestowanie działania obu mechanizmów.

> Format ścieżki z zadania można przedstawić symbolicznie tak: `/paths/user/raw/{zmienny identyfikator użytkownika}/{zmienne działanie do wykonania}`; lub tak: `/paths/user/spring/{zmienny identyfikator użytkownika}/{zmienne działanie do wykonania}`.

**Zaczynamy!**

---

> Jeżeli chcesz zrobić zadanie samodzielnie na podstawie ogólnych opisów, to przejdź tylko do części dotyczącej testów, a opisy szczegółowe pomiń.

1. Utwórz kontroler `PathParamsController` w pakiecie `pl.honestit.spring.mvc.web.controllers`. Oznacz kontroler wymaganymi adnotacjami, aby obsługiwał ścieżkę `/paths/user`. 
1. Utwórz metodę `public String raw(HttpServletRequest request`) i oznacz ją adnotacjami, aby obsługiwała żądania typu `GET` na ścieżce względnej `/raw/**` i samodzielnie generowała odpowiedź (bez przekazania do widoku).
1. Zaimplementuj metodę `raw`, aby z wykorzystaniem parametru `HttpServletRequest` odczytywała identyfikator użytkownika będący przed ostatnim fragmentem ścieżki żądania oraz operacje jaką należy wykonać będącą ostatnim fragmentem ścieżki żądania. Pamiętaj, że jedno i drugie może się nie pojawić - przygotuj się na taką sytuację w kodzie metody. Metoda ma wyświetlić (zwrócić) informacje o wartościach parametrów.
1. Utwórz teraz metodę `public String spring(String id, String operation)` i oznacz ją adnotacjami, aby obsługiwała żądania typu `GET` na ścieżce względnej `/raw/{id}/{operation}` i samodzielnie generowała odpowiedź (bez przekazania do widoku). Użycie par `{` i `}` w mapowaniu określa parametry ścieżki.
1. Oznacz parametry metody odpowiednio adnotacją `@PathVariable("id")` oraz `@PathVariable` (tutaj bez nazwy parametru ścieżki wewnątrz adnotacji).
1. Zaimplementuj metodę `spring`, aby poprawnie zwróciła informację o parametrach ścieżki. Pamiętaj, że mogą się nie pojawić, więc przygotuj swoją implementację na taki wariant.

#### Implementacje są gotowe więc przyszedł czas na ich testy, TESTY, TeStY, tEsTy, TESTY!!!

1. Wywołaj żądanie [localhost:8080/paths/user/raw/30/delete](localhost:8080/paths/user/raw/30/delete). Jaki efekt?
1. Wywołaj żądanie [localhost:8080/paths/user/raw/30](localhost:8080/paths/user/raw/30). Jaki efekt?
1. Wywołaj żądanie [localhost:8080/paths/user/raw/80/delete](localhost:8080/paths/user/raw/80/delete). Jaki efekt?
1. Wywołaj żądanie [localhost:8080/paths/user/raw/80/edit](localhost:8080/paths/user/raw/80/edit). Jaki efekt?

1. Wywołaj żądanie [localhost:8080/paths/user/spring/30/delete](localhost:8080/paths/user/spring/30/delete). Jaki efekt?
1. Wywołaj żądanie [localhost:8080/paths/user/spring/70/add](localhost:8080/paths/user/spring/70/add). Jaki efekt?
1. Wywołaj żądanie [localhost:8080/paths/user/spring/100](localhost:8080/paths/user/spring/100). Jaki efekt?
1. Wywołaj żądanie [localhost:8080/paths/user/spring/edit](localhost:8080/paths/user/spring/edit). Jaki efekt?
1. Wywołaj żądanie [localhost:8080/paths/user/spring/delete/30](localhost:8080/paths/user/spring/delete/30). Jaki efekt?

**Mam nadzieję, że wyniki Twoich testów są dla Ciebie całkowicie zrozumiałe. Zrealizuj teraz jeszcze te kilka dodatkowych punktów.**

1. Nasz fragment ścieżki mówiącej o identyfikatorze jest liczbą, więc spróbujmy oczekiwać parametru już
w formacie, który byłby dla nas wygodniejszy. Zmień w tym celu typ parametru `id` w metodzie `spring` z klasy `String` na klasę `Long`. Wykonaj następujące żądania testowe:
   - [localhost:8080/paths/user/spring/120/remove](localhost:8080/paths/user/spring/120/remove) - jaki efekt?
   - [localhost:8080/paths/user/spring/buba/delete](localhost:8080/paths/user/spring/buba/delete) - jaki efekt?
1. Zmień definicję ścieżki w mapowaniu tak, aby korzystała z wyrażenia regularnego dla fragmentu `id`. Wyrażenie powinno dopuszczać pojawienie się w tym miejscu tylko liczb. Ponownie wykonaj dwa testowe żądania z poprzedniego punktu. Jaki teraz jest efekt?
1. Zmień definicję ścieżki w mapowania tak, aby korzystała z wyrażenia regularnego również dla fragmentu `operation` i dopuszczała tylko wartości: `add`, `edit`, `get` i `delete`. Ponownie wykonaj testowe żądania z poprzedniegi punktu. Jaki teraz jest efekt?

**Kolejny duży krok za Tobą. Moje gratulację!**

[Rozwiązanie zadania](solutions/B.2.3.md)

---

### Gratuluję!

Wykonałeś/aś solidną robotę z mapowaniem kontrolerów i obsługą parametrów żądań. To bardzo ważne, abyś z tymi podstawowymi mechanizmami radził/a sobie bez żadnych problemów. W tym obszarze nie możesz sobie pozwolić na błądzenie. Sposób działania mapowania, obsługa parametrów, ich wymagalność i zachowanie Spring MVC ma być dla Ciebie jasne i oczywiste.

---

## B.3. Współpraca widoku i kontrolera

Zebrałeś/aś już podstawowe i bardzo ważne umiejętności do praktycznego wykorzystania Spring MVC w celu przyjmowania żądań HTTP i ich wstępnej obsługi (na poziomie parametrów). Przejdziemy teraz do tworzenia odpowiedzi bazującej na widokach zamiast surowym tekście. Wykorzystamy technologię JSP jako tą najbardziej Ci znaną. Zakładam, że wiesz jak działa technologia JSP, jak korzysta się z podstawowych tagów biblioteki JSTL, a w szczególności jak działa język wyrażeń `Expression Language (EL)`. Także zasięgi atrybutów i towarzyszące im mapy strony, żądania, sesji czy aplikacji nie powinny być Ci obce. Jeżeli jest inaczej, to masz teraz 5 minut na uzupełnienie tych informacji i do dzieła ;)

---

### B.3.1. Konfiguracja widoków (:watch: 15 minut)

W zadaniu B.1.3. miałeś/aś już okazję przekazywać obsługe widoku z kontrolera do strony JSP. Jest to standardowa zagrywka, której oczekuje od nas wzorzec MVC, a dla Spring MVC jest to zachowanie domyślne. Dostajemy je z pudełkiem Spring MVC, a gdy chcemy inaczej, to musismy to wprost wskazywać (np. adnotacją `@ResponseBody`). Aby wygodniej pracowało się z widokami JSP wykonamy najpierw prostą, dodatkową konfigurację (dla rozgrzewki).

Twoje kolejne zadania to:
- skonfigurowanie bean'a typu `ViewResolver`, aby umożliwiał wygodniejsze przekierowania do widoków,

**Zaczynamy!**

1. W klasie `WebConfig` dodaj nową metodę o sygnaturze `public ViewResolver viewResolver()`. Postaraj
się użyć dokładnie takiej nazwy metody, bo metoda ta będzie generować beana, a na pewno pamiętasz jak ważne są nazwy metod przy generowaniu beanów, prawda?
1. Metodę oznacz adnotacją `@Bean`, a w implementacji utwórz nowy obiekt klasy `InternalResourceViewResolver`. Skonfiguruj mu wartość prefiks na `/WEB-INF/views/` oraz sufiks na `.jsp`. Na koniec zwróć ten obiekt w metodzie.

   > Ustawienie prefiksa i sufiksa sprawia, że nasze widoki teraz muszą zwracać tylko brakujący fragment ścieżki pliku `jsp` pomiędzy `/WEB-INF/views/` a `.jsp`, np. `hello`. Jeżeli będzie dla Ciebie mylący brak `.jsp` na końcu ścieżek zwracanych z kontrolerów, to możesz nie ustawiać sufiksu.
   
1. Zmodyfikuj teraz kontroller `HelloController`, aby zwracany z niej identyfikator widoku był tylko fragmentem ścieżki pliku `hello.jsp`.
1. Przetestuj aplikację i sprawdź czy kontroler dalej działa jak trzeba.

**Poszło gładko, prawda? To dobrze - moje gratulacje!**

[Rozwiązanie zadania](solutions/B.3.1.md)

---

### B.3.2. Wyświetlanie danych z wykorzystaniem modelu (:watch: 45 minut)

Na pewno dobrze pamiętasz, że strony JSP udostępniają dedykowany zasięg i mapę atrybutów dla konkretnej strony. Związane to było z obiektem `pageContext` i `pageScope`. W Spring MVC funkcjonuje podobny mechanizm, ale dużo bardziej uniwersalny, bo w niczym nie ograniczony do stron `JSP`. Mechanizm ten określamy modelem i reprezentuje go klasa `Model`.

Jak wiesz z informacji o wzorcu MVC, to rolą kontrolera jest przetworzenie danych z widoku (od użytkownika) oraz przygotowanie danych do wyświetlenia w widoku. Te dane do wyświetlenia w Spring MVC podawane są na tacy :) a tą tacą jest wspomniany obiekt `Model`.

> Spring MVC umożliwia wykorzystanie w naszych metodach kontrolera wielu różnych parametrów. Nie musimy się zastanawiać skąd one się biorą (chociaż kierunek -> `DispatcherServlet` pewnie jest dobrym tropem). W końcu Spring MVC, to element Spring Framework, a Spring Framework to Dependency Injection, a Dependency Injection to Inversion of Control, a Inversion of Control oznacza, że to nie nasz problem :) Darowanemu koniowi w zęby się nie zagląda, więc bierzmy, skoro dają, i korzystajmy! Jest to podejście deklaratywne, a więc oczekujemy parametru konkretnego typu w metodzie kontrolera i ten parametr dostajemy (oczywiście nie bez ograniczeń).

Twoje kolejne zadania to:

- zapoznanie się z klasą użytkownika ([tutaj](sources/User.java)), którą wykorzystasz w swoim projekcie,
- utworzenie kontrolera `UserController`, który zgodnie ze wzorcem MVC przygotuje
dane użytkownika do wyświetlenia w widoku,
- w kontrolerze utworzysz metodę dla `GET /users/{id:[0-9]+}`, oczekującą parametru typu `Model`, a w samej metodzie stworzysz testowego użytkownika (jego `id` powinno mieć wartość zgodną z parametrem ścieżki) i wstawisz go do modelu jako atrybut.
- utworzenia strony JSP `webapp/WEB-INF/views/user.jsp`, która wyświetli dane użytkownika korzystając z modelu,
- utworzenie metody kontrolera, która dla mapowania `GET /users/add` przekaże obsługę do widoku z formularzem (szukaj w dalszych punktach),
- utworzenia metody kontrolera, która dla mapowania `POST /users/add` pobierze parametry żądania (uzupełnione w formularzu) i utworzy nowego użytkownika (powinien dostać **kolejne** `id` zaczynając od `1`), a następnie przekaże jego wyświetlenie do widoku `webapp/WEB-INF/views/user.jsp` - tego, który utworzyłeś/aś kilka punktów wcześniej,
- utworzenie strony JSP `webapp/WEB-INF/views/add-user.jsp` z formularzem dodawania użytkownika, gdzie formularz powinien kierować na adres `/users/add` i korzystać z metody `POST`,
- przeprowadzenie testów wyświetlania i tworzenia użytkowników.

**Zaczynamy!**

---

1. Zapoznaj się z klasą `User` dostępną [tutaj](sources/User.java). Klasa reprezentuje prostego użytkownika posiadającego identyfikator, imię, nazwisko, wiek oraz płeć.
1. Przenieś kod klasy `User` do swojego projektu, z zachowaniem pakietów.

#### Zajmiemy się utworzeniem kontrolera udostępniającego użytkownika dla widoku i samym widokiem.

1. W pakiecie `pl.honestit.spring.mvc.web.controllers` utwórz klasę `UserController`. Klasa powinna pełnić rolę kontrolera oraz obsługiwać ścieżkę główną `/users`, więc ustaw odpowiednie adnotacje.
1. Stwórz metodę `public String showUser`. Metoda ma przyjmować obiekt klasy `Model` oraz parametr ścieżki o nazwie `id` i typie `Long`.
1. Dodaj mapowanie ścieżki do metody aby obsługiwała żądania sparametryzowane `GET /users/{id:[0-9]+}`.
1. W metodzie `showUser` utwórz nowy obiekt użytkownika i dodaj go jako atrybut modelu pod kluczem `user`. Użytkownik ma mieć wartość pola `id` zgodną z parametrem metody/ścieżki `id`.
1. Na koniec zwróć z metody identyfikator widoku `/WEB-INF/views/user.jsp`. Pamiętaj tylko, że w poprzednim zadaniu skonfigurowałeś/aś klasę `ViewResolver`, więc zachowaj z tej ścieżki tylko to, co trzeba.

#### Tworzymy stronę, na której będą prezentowane dane użytkwonika

1. Utwórz teraz stronę JSP, która będzie prezentowała użytkownika. Strona powinna być w katalogu `WEB/INF/views` i nazywać się `user.jsp`.
1. Na stronie wykorzystaj język wyrażeń EL do wyświetlenia informacji o użytkowniku. Do wstawionego użytkownika możesz się odwołać w ten sposób: `${user}`, a do jego pola, np. `firstName` w ten sposób: `${user.firstName}` albo `${user.getFirstName()}`. Pamiętaj tylko aby wyświetlić wszystkie dane użytkownika.
1. Przy wyświetlaniu wieku wyświetl dodatkową informację warunkową czy użytkownik jest pełnoletni czy nie.
1. Przetestuj działanie kontrolera i strony czy wszystko wyświetla się jak trzeba.

#### Świetnie, wszystko działa! Czas na obsługę tworzenia użytkownika.

1. Uzupełnij klasę kontrolera o metodę `public String prepareUserCreation`. Metoda ma obsługiwać ścieżkę `GET /users/add` oraz przekazywać wygenerowanie widoku do strony `/WEB-INF/views/add-user.jsp` (oczywiście ze zwróceniem tylko niezbędnego fragmentu ścieżki do pliku JSP).
1. Uzupełnij klasę kontrolera o metodę `public String createUser(...)`. Metoda powinna przyjąć parametry pozwalające utworzyć użytkownika, a więc zgodne z jego polami (poza polem `id`) oraz dodatkowy parametr klasy `Model`.
1. Na podstawie parametrów utwórz nowego użytkownika i nadaj mu kolejny identyfikator... skąd go wziąć? Np. z sesji. Pobierz z obiektu `HttpSession` atrybut `nextUserId`. Jeżeli tam jest to wykorzystaj jego wartość przy tworzeniu użytkownika, potem ją zwiększ o 1 i wstaw z powrotem do sesji (jako ten sam atrybut). Jeżeli go tam nie ma, to przyjmij dla niego początkowa wartość równą `1`, użyj jej do utworzenia użytkownika, a wartość `2` wstaw do sesji pod kluczem `nextUserId`.

   > Dla ułatwienia możesz też stworzyć pole w klasie `UserController` i użyć go w roli licznika
   
1. Na koniec umieść utworzonego użytkownika w mapie atrybutów modelu po kluczem `user` i przekaż obsługę widoku ponownie do strony `user.jsp`.

#### Ostatnim elementem jest sam formularz, więc czym prędzej się za niego zabierajmy.

1. Utwórz stronę `add-user.jsp` w katalogu `/WEB-INF/views`
1. Na stronie umieść formularz, który umożliwi dodanie nowego użytkownika. Kilka uwag:
   - dla pola `id` klasy `User` nie powinno być pola formularza,
   - dla pola `age` klasy `User` pole formularza powinno być typu liczbowego,
   - dla pola `gender` klasy `User` pole formularza powinno być listą pojedynczego wyboru z opcjami `Male` i `Female`.
   - formularz powinien wysyłać żądanie typu `POST` na ścieżkę `users/add`.
1. Przetestuj działanie całego kontrolera. Wysłanie poprawnie wypełnionego formularza powinno wygenerować widok użytkownika, który właśnie został dodany. Kolejno tworzeni użytkownicy powinni mieć następne wartości `id` oraz pola zgodne z danymi wprowadzonymi w formularzu.

#### Implementacja jest gotowa.Przyszedł czas na kilka testów.

1. Usuń z formularza pole dla `age` albo zmień wartość jego atrybutu `name` np. na `someNastyAge`. Wypełnij formularz i prześlij do kontrolera. Jak zachowuje się teraz kontroler? Czy w ogóle się w jakiś sposób zachowuje?
1. Dodaj do formularza nowe pole `country` będące listą wyboru z opcjami `Poland`, `Germany`, `USA` i atrybutem `name` o wartości `country`. Wypełnij formularz i prześlij do kontrolera. Jaki efekt spowodowało dodanie pola do formularza?
1. Przejdź do metody `createUser` i zmień nazwę atrybutu pod którym dodajesz użytkownika do mapy modelu. Wstaw na przykład go pod kluczem `newUser`. Uruchom aplikację, wypełnij formularz i prześlij do kontrolera. Co się stało i czemu się stało?

**Uffff! Jeżeli to wszystko jest już za Tobą, nic Cię nie zaskoczyło (a jeśli, to przynajmniej wiesz już co i dlaczego), to wykonałeś/aś kawał, nie... KAAAWAAAAŁ!! dobrej roboty. Zwróć uwagę jak wiele jest MVC, w Twoim kodzie i jak bardzo jest ... szlachetny!**

[Rozwiązanie zadania](solutions/B.3.2.md)

---

### B.3.3. Alternatywna konfiguracja Spring MVC (:watch: 15 minut)

Dostarczyliśmy konfigurację Spring MVC bazującą na implementacji klasy `WebApplicationIntilializer`. Istnieje również alternatywna konfiguracja bazująca na rozszerzeniu klasy `AbstractAnnotationConfigDispatcherServletInitializer`. Przekonasz się za chwilę, że przyniesie ona dużo lepsze efekty :)

Twoje kolejne zadania to:
- utworzenie klasy `DispatcherConfiguration`, która będzie rozszerzać `AbstractAnnotationConfigDispatcherServletInitializer`,
- zaimplementowanie wymaganej metody `Class<?>[] getRootConfigClasses()` - musisz w niej zwrócić tablicę główny klas konfiguracyjnych (u nas jest to klasa `RootConfig`),
- zaimplementowanie wymaganej metody `Class<?>[] getServletConfigClasses()` - musisz w niej zwrócić tablicę klas konfiguracyjnych servletów (czyli związanych ze Spring MVC, u nas jest to klasa `WebConfig`),
- zaimplementowanie wymaganej metody `String[] getServletMappings()` - musisz w niej zwrócić tablicę wartości typu `String` reprezentujących mapowanie _Dispatchera_ (wystarczy `"/"`),
- na koniec należy wyłączyć naszą wcześniejszą klasę `ApplicationInitializer` (choćby usuwając z niej informacje, że implementuje `WebApplicationInitializer`) i cieszyć się szeroką integracją Spring MVC z IntelliJ.

> Warto, abyś wcześniej zapoznał/a się z klasą `AbstractAnnotationConfigDispatcherServlet`. Sama jej nazwa powinna sugerować na jakim typie kontekstu bazuje. Wejdź w kod tej klasy i spróbuj przejść po kolejnych klasach nadrzędnych. Do jakiego interfejsu w końcu dotrzesz?

**Teraz to przede wszystkim sam/a możesz sobie pogratulować, bo wsparcie, które otrzymasz np. na stronach JSP, jest warte grzechu!**

---

### B.3.4. Słowniki danych w modelu (:watch: 30 minut)

Wprowadzimy do użytkownika pole `country` albo `nationality` (Twój wybór) reprezentujące jego narodowość/obywatelstwo/pochodzenia. Dopuszczalne wartości tego pola mogą się zmieniać, bo przecież nie koniecznie na początku nasza aplikacja będzie obsługiwać wszystkie kraje. Mie ma sensu, działajac w obrębie Europy, kazać użytkownikom wybierać państwo z tak egzotycznych możliwości jak Stany Zjednoczone czy San Escobar. Nie chcemy mieć na sztywno zapisanych możliwości wybóru narodowości bezpośrednio w widoku. Chcemy móc bardziej programistycznie nimi zarządzać. Potencjalnie nawet pobierać z bazy danych! Plan minimum to skonfigurować słowniki w kodzie kontrolera, a nie w kodzie strony jsp.

Twoje kolejne zadania to:

- rozszerzenie klasy `User` o pole reprezentujące narodowość - pole powinno być pojedynczą wartością typu `String`,
- udostępnienie na poziomie kontrolera `UserController` dla widoku listy dostępnych państw (przeczytaj małą podpowiedź jak to zrobić pod zadaniami),
- wykorzystanie biblioteki JSTL do automatycznego wygenerowania elementu `select` na formularzu dla listy państw,
- zmodyfikowanie strony wyświetlającej informacje o użytkowniku, aby pojawiła się informacja o jego narodowości,
- przetestowanie działania aplikacji

> Listę państw możesz tworzyć jako zmienną lokalną w metodzie przygotowującej formularz (`GET /users/add`) i umieszczać ją w modelu np. pod kluczem `countries`. Możesz również dostarczyć dedykowaną metodę, która zwraca listę państw (o sygnaturze `public List<String> countries`). Gdy oznaczysz taką metodę adnotacją `@ModelAttribute("countries")`, to uzyskasz globalny efekt ustawienia atrybutu `countries` we wszystkich widokach związanych z tym kontrolerem. Tak, jakby każda metoda dodawała osobno tą listę państw do modelu.

**Zadanie ukończone, a Ty poznałeś/aś kolejny użyteczny mechanim. I to w tak prosty sposób.**

---

### B.3.5. Service Layer i Data Transfer Object (:watch: 60 minut)

Nasza klasa użytkownika (`User`) aspiruje do tego, aby stać się kiedyś poważną klasą. Poważne klasy po pierwsze przechowuje się minimum w pamięci, a po drugie dają też pewne poważne dane od siebie.

W tym zadaniu rozszerzysz klasę `User` o dodatkowe pola, ale takie, których nie chcemy przekazywać do widoku, ani otrzymywać z formularza, nawet przez przypadek. Dostarczysz równiez klasę serwisu, której zadaniem będzie zapisywanie i pobieranie użytkowników (zamiast przechowywania w sesji jeżeli zrealizowałeś/aś wcześniejsze zadanie dodatkowe). To tak w skrócie, teraz w większych szczegółach.

Twoje kolejne zadania to:
- rozszerzenie klasy `User` o pola:
  - reprezentujące datę utworzenia użytkownika, typu `LocalDateTime`,
  - reprezentujące datę ostatniej modyfikacji użytkownika, typu `LocalDateTime`,
  - reprezentujące aktualną wersję obiektu użytkownika, typu `Long`,
- utworzenie klasy `UserService`, której rolą będzie przechowywanie użytkowników i ich udostepnianie, zatem powinna w niej być dostępna mapa użytkowników (z kluczami typu `Long` reprezentującymi `id` użytkownika, i z wartościami typu `User`),
- wskazanie klasy `UserService` jako komponent typu `@Service`,
- stworzenie klasy `UserDTO`, która będzie kopią klasy `User` ale bez pól reprezentujących datę utworzenia i datę modyfikacji (pola z wersją powinno zostać),
- zamienienie wykorzystania w kontrolerze klasy `User` na klasę `UserDTO`,
- utworzenie w klasie `UserService` metod:
  - do zapisu użytkownika: `public Long saveUser(UserDTO)` - metoda powinna na podstawie obiektu `UserDTO` utworzyć  obiekt klasy `User` i ustawić _ukryte_ pola, a docelowy obiekt zapisać w swojej _pamięci_ (mapie), na koniec metoda ma zwrócić identyfikator (`id`), pod którym nowy użytkownik został zapisany,
  - do pobrania użytkownika: `public UserDTO getUser(Long id)` - metoda powinna na podstawie parametru `id` odszukać użytkownika w swojej pamięci i zwrócić go w postaci obiektu klasy `UserDTO`, a więc bez naszych wrażliwych pól.
- wstrzyknij komponent `UserService` do kontrolera `UserController` i wykorzystaj go w:
  - metodzie, która obsługuje ścieżkę `GET /users/{id:[0-9]+}` do pobrania użytkownika z serwisu,
  - metodzie, która obsługuje ściezkę `POST /users/add` do zapisu użytkownika w serwisie.
  
> Zadanie wymaga całkiem sporo pracy i połączenia ze sobą wielu elementów, ale właśnie tak powinno to wyglądać.

[Rozwiązanie zadania](solutions/B.3.5.md)

**Gratuluję realizacji!**

---

### Gratuluję!

Ukończyłeś/aś ostatnią sekcję tej części repozytorium - niezwykle ważną. Nauczyłeś/aś się nie tylko konfigurowania projektu wykorzystującego Spring MVC, mapowania kontrolerów, pobierania parametrów żądania czy parametrów ścieżki, mapowania widoków czy wykorzystywania modelu do współpracy kontrolera z widokiem. Te rzeczy są ważne i praktyczne, ale Ty zrozumiałeś/aś jak to działa, a nie co z tym zrobić. Stanowi to podstawową różnicę między możliwością bycia programistą a chęcią bycia programistą :)