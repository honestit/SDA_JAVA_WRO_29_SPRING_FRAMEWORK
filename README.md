# SPRING_FRAMEWORK
Repozytorium z zadaniami dotyczącymi technologii Spring


### Nota prawna

Drogi użytkowniku. Ze względu na ochronę wartości intelektualnej zachęcam Cię, abyś
na wstępie zapoznał się z poniższą notą prawną dotyczącą repozytorium. Uzyskałeś dostęp do repozytorium
i tym samym poniższe zapisy w całości Cię dotyczą. Proszę Cię o współpracę w ochronie tej wartości i przestrzeganie
niżej opisanych zasad. Dziękuję.

>Repozytorium, jego treść i zawartość podlegają ochronie prawnej. Wykorzystywanie materiałów
zawartych w repozytorium może odbywać się tylko za zgodą autora.

>Repozytorium jest **prywatne**. Udostępniane jest wybranym osobom i tylko przez te osoby może być wykorzystywane. 
Dalsze rozpowszechnianie i/lub kopiowanie repozytorium bez zgody autora, w całości lub we fragmentach, jest zabronione.

>Autorem i właścicielem repozytorium jest Michał Kupisiński Honest IT Consulting. 

>Repozytorium oraz każda jego kopia (fork) muszą zachować powyższą notę prawną.


## Przygotowanie repozytorium

1. Upewnij się, że został Ci nadany dostęp do repozytorium i masz prawo utworzenia `fork` i wykonania `pull-request`
1. Utwórz `fork` repezytorium na swoim koncie github ([*fork*](https://guides.github.com/activities/forking/))
1. Sklonuj swój `fork` na dysk. Najlepiej w jakimś folderze zbiorczym, np. `repositories` albo `workspace`
   - Wykorzystaj do tego polecenie `git clone`
   - Upewnij się, że repozytorium zostało poprawnie utworzone przy pomocy `git status`
   
   > **Pamiętaj, aby utworzyć klon Twojego `fork-a`, a nie repozytorium głównego**
   
1. Pamiętaj aby po każdym rozwiązanym zadaniu dodać je (`git add`) i zakomitować (`git commit`)
1. Na koniec pamiętaj aby wypchnąć wszystkie zmiany do swojego `fork-a` (polecenie: `git push`) oraz utworzyć `pull-request` ([*pull request*](https://help.github.com/articles/creating-a-pull-request)) do repozytorium głównego. Musisz to zrobić na koniec zajęć z danego modułu.

   > Możesz potem dalej realizować zadania, commitować je oraz wypychać. Nowe zadania i zmiany w istniejących zostaną automatycznie uwzględnione we wcześniejszym `pull-request`.
1. Baw się dobrze przy rozwiązywaniu zadań ;)

## Wprowadzenie

Repozytorium podzielone jest na kolejne sekcje, które wprowadzą Cię w świat Spring'a.
Każda z sekcji adresuje najważniejsze elementy procesu poznawczego, a więc:
* przedstawi Ci nowe zagadnienia,
* umożliwi ich poznanie,
* dostarczy narzędzi do zrozumienia oraz analizy,
* pozwoli zacząć praktycznie stosować.

Rozpoczniesz od nauki podstaw Spring'a, a więc pracy z projektem Spring Framework. W głównej mierze czas ten poświęcić na zrozumienie wzorca Inversion of Control (IoC) i jego realizacji w module Spring Context. Dalej zdobędziesz praktyczne umiejętności tworzenia aplikacji webowych z wykorzystaniem Spring MVC. Kolejna część poświęcona będzie dwóm bardziej zaawansowanym projektom, a więc Spring Security oraz Spring Data. Praca z tymi technologiami pozwoli Ci tworzyć bezpieczne aplikacje i sprawnie obsługiwać warstwę danych.

Zapraszam Cię do rozpoczęcia pracy z repozytorium.


## A. Inversion of Control i Spring Core

Pierwsza sekcja wprowadza Cię do kluczowego wzorca architektonicznego dla zrozumienia działania dzisiejszych framwork'ów - wzorca Inversion Of Control (IoC). Wzorzec ten, wraz z rozwojem języka Java umożliwiającym jego efektywną implementację, zrewolucjonizował tworzenie zaawansowanych aplikacji.

Największym i najkosztowniejszym problemem związanym z każdą dużą aplikacją, a w szczególności z jej rozwojem, są zależności między występującymi w niej elementami. Czym zależności więcej i czym trwalsze tym możliwości ich modyfikacji, zamiany czy nawet usunięcia są mniejsze i kosztowniejsze.

Wzorzec IoC pozwala wprowadzić bardzo luźne zaleźności przenosząc rolę ich uzupełniania na kontener IoC. Jedną z najpopularniejszych i najefektywniejszych realizacji tego wzorca jest _Dependency Injection_. Spring Core (Spring Context) pełni rolę implementacji kontenera DI i choć istnieje wiele innych implementacji, to ta szczególnie spodobała się programistom.

Pierwsza sekcja zaprasza Cię do przygody ze wzorcem Inversion Of Control, z Dependency Injection oraz ze światem technologii Spring.

[Rozpocznij pracę z tą sekcją](sections/section_a/README.md)

---

## B. Tworzenie aplikacji webowych ze Spring MVC

Każda technologia, aby była atrakcyjna, poza pięknymi ideami musi też udostępniać zestaw praktycznych narzędzi i technik umożliwiających sprawne tworzenie aplikacji. W przypadku aplikacji webowych takim narzędziemi i zbiorem technik jest Spring MVC. Obecny w technologii od samego początku był propozycją twórcy Spring podejście do obsługi warstwy webowej lżejsze niż servlety.

Spring MVC nie odrzuca technologii Servletów, ale na niej bazuje. Stąd tak ważna jest znajomość technologii Servletów i JSP przed rozpoczęciem pracy ze Spring MVC. Nie mniej, Spring MVC wnosi lżejsze podejście oparte na pojedynczym Servlecie, pełniącym rolę wzorca _Front Controller_ i delegującym obsługę żądań do lekkich kontrolerów.

Świat Spring MVC jest równie fascynujący jak świat Servletów, a wszystko, co wiesz o Servletach, zaprocentuje w pracy ze Spring MVC.


[Rozpocznij pracę z tą sekcją](sections/section_b/README.md)

---

## C. Obsługa danych i bezpieczeństwa ze Spring Data i Spring Security

Rozpoczynasz przygodę z dwoma kolejnymi projektami ekosystemu Spring: Spring Data oraz Spring Security. Zdecydowana większość systemów w jakiś sposób wykorzystuje dane i prawie zawsze dane te są składowane poza aplikacją, w różnego rodzaju bazach danych. Projekt Spring Data umożliwi Ci wygodne i wydajne przetwarzanie danych zapisanych w bazie danych.

Drugim niezwykle istotnym elementem dzisiejszych aplikacji jest bezpieczeństwo. Z zasadą bezpieczeństwa spotkałeś/aś się przecież już na samym początku nauki języka Java: mowa tu o hermetyzacji. Wszystko w języku Java powinno być z założenia prywatne, a tylko uzasadnione wyjątki zasługują na upublicznienie. To samo podejście powinno być stosowane na wszystkich kolejnych etapach związanych z rozwojem aplikacji - z założenia, wszystko powinno być chronione, a tylko wyjątki upublicznione. Projekt Spring Security pozwala Ci w łatwy i szybki sposób wdrożyć bezpieczeństwo do Twojej aplikacji, zarówno na poziomie uwierzytelniania użytkowników jak i ich autoryzowania.

Celem sekcji jest wyposażenie Cię w zrozumienie oraz umiejętności przetwarzania zewnętrznych danych w Twojej aplikacji. Przetwarzania w sposób bezpieczny.

[Rozpocznij pracę z tą sekcją](sections/section_c/README.md)

---