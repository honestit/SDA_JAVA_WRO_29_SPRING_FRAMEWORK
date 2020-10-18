W ramach tego zadania spotykasz się z klasycznym podziałem ról między kontrolerem, serwisem i repozytorium. Kontroler odpowiada za pobranie danych z widoku (tutaj z żądania HTTP) oraz ich wstępne przetworzenie (konwersja i walidacja). Część z tych zadań wykonuje za kontroler sam Spring, bo jeżeli w żądaniu nie pojawią się oba parametry `login` i `password`, to użytkownik po drugiej stronie otrzyma błąd `400 Bad Request`, a metoda kontrolera nie będzie w ogóle wywołana.

Kontroler przekazuje właściwe wykonanie logiki biznesowej do serwisu. Każda metoda serwisu odpowiada za jakiś fragment procesu. Przy logowaniu mamy dwa łączne lub rozłączne fragmenty procesu dostarczenia użytkownika o podanych danych autentykacji. My wykorzystujemy podejście rozłączne gdzie jeden krok, to sprawdzenie poprawności samych danych, a drugi krok to pobranie użytkownika na podstawie tych danych.

Metody serwisu w dużej mierze będą wywoływać bardziej szczegółowe i jednostkowe metody repozytoriów, przetwarzać uzyskane z nich dane i dokonywać konwersji między światem encji i światem obiektów transferowych (DTO). To czy z serwisów będziemy wysyłać na zewnatrz obiekty DTO konkretnych klas, czy treść w formacie JSON albo XML, czy wreszcie surowe dane tekstowe - to nie ma znaczenia. Znaczenie ma tutaj to, aby encje nie opuszczały warstwy serwisów i warstwy transakcyjnej. Zasada jest taka, że jeżeli chcemy na grupie encji przeprowadzić zestaw operacji, to robimy to w transakcji, a transakcja rozpoczyna się w metodzie serwisu.

---

W pierwszym kroku implementujemy metodę `checkCredentials`, która ma za zadanie dostarczyć informację typu `true/false`, czy przekazane dane logowania poprawnie reprezentują istniejącego użytkownika. Implementacja jest rozbita na klasę `UserRepository`, w której znajduje się natywne zapytanie - tak najłatwiej uzyskać tą odpowiedź. Rolą serwisu jest tutaj pośredniczenie, a więc wywołanie metody repozytorium i przekazanie jej wyniku do kontrolera.
 
> Pamiętaj, że nie dostarczamy implementacji metody repozytorium - na tym polega cała moc projektu Spring Data. Wystarczy tylko metoda o poprawnej sygnaturze. Jej implementacja będzie dostarczona w oparciu o samą sygnaturę lub sygnaturę i zawartość adnotacji `@Query`.

Odpowiednia metoda w `UserRepository`:

```java
    @Query(nativeQuery = true,
        value = "SELECT CASE WHEN count(*) >= 1 THEN 'true' ELSE 'false' END " +
                "FROM users WHERE login = ?1 AND password = ?2")
    Boolean checkIfUserExists(String login, String password);
```

Kod metody `boolean checkCredentials(String login, String password)` w `UserService`:

```java
    public boolean checkCredentials(String login, String password) {
        boolean existsUser = userRepository.checkIfUserExists(login, password);
        return existsUser;
    }
```

---

Drugi krok procesu to pobranie już faktycznej encji po wartościach pól `login` oraz `password`. Tym razem w `UserRepository` konstruujemy zapytanie opartę o nazwę metody, bo jest to najławiejsza opcja. W serwisie nie ograniczymy się natomiast do zwykłego przekazania wyniku. Powinniśmy upewnić się, że użytkownika znaleźliśmy, a następnie skonwertować go do obiektu `LoggedUserDTO`.

Metoda w `UserRepository`:

```java
    User getUserByLoginAndPassword(String login, String password);
```

Kod metody `LoggedUserDTO getUser(String login, String password)` w `UserService`:

```java
    public LoggedUserDTO getUser(String login, String password) {
        LoggedUserDTO loggedUserDTO = null;
        User user = userRepository.getUserByLoginAndPassword(login, password);

        if (user != null) {
            loggedUserDTO = new LoggedUserDTO();
            loggedUserDTO.setId(user.getId());
            loggedUserDTO.setLogin(user.getLogin());
            loggedUserDTO.setFirstName(user.getFirstName());
            loggedUserDTO.setLastName(user.getLastName());
        }
        return loggedUserDTO;
    }
```    