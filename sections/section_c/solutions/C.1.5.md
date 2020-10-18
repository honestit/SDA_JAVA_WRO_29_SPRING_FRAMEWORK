Najlepiej jest zacząć od klasy `Skill` ponieważ nie posiada ona żadnych zależności, więc będzie najłatwiej ją skonfigurować. Sama encja również jest najprostsza.

Konfiguracja adnotacjami klasy `Skill` może wyglądać jak niżej:

```java
package pl.honestit.spring.kb.data.model;

import javax.persistence.*;
import java.util.Objects;

@Entity // Oznaczamy, że klasa jest encją
@Table(name = "skills") // Dostosowujemy nazwę tabeli dla naszej encji
public class Skill {

    @Id // Wskazuje pole klucza głównego
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Wskazujemy aby baza danych dobrała mechanizm generowania wartości klucza głównego
    private Long id;
    @Column(unique = true, nullable = false) // Określamy aby nazwa umiejętności była unikalna i wymagana
    private String name;
    @Column(nullable = false) // Określamy aby nazwa kategorii dla umiejętności była wymagana
    private String category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return Objects.equals(id, skill.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
```

Następnie przechodzimy do klasy `KnowledgeSource`, która powiązana jest z klasą `Skill`. Mając tą drugą, nie będziemy mieli błędów w klasie `KnowledgeSource`.

Konfiguracja adnotacjami klasy `KnowledgeSource` może wyglądać jak niżej:

```java
package pl.honestit.spring.kb.data.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity // Oznaczamy, że klasa jest encją
@Table(name = "knowledge_sources") // Dodajemy informację o nazwie tabeli dla encji
public class KnowledgeSource {

    @Id // Wskazujemy pole klucza głównego
    @GeneratedValue(strategy = GenerationType.IDENTITY) // oraz strategię generowania wartości oparta na bazie danych
    private Long id;
    @Column(unique = true, nullable = false) // Określamy nazwę źródła jako unikalną i wymaganą
    private String name;
    @Column(columnDefinition = "TEXT") // Definiujemy docelowy typ kolumny jako TEXT zamiast domyślnego varchar(255)
    private String description;
    private String url;
    private Boolean active;

    @ManyToMany // Dostarczamy mapowanie wiele do wielu między źródłami wiedzy a umiejętnościami
    @JoinTable(name = "knowledge_sources_skills", // Wskazujemy wprost nazwę tabeli mapująej
        joinColumns = @JoinColumn(name = "source_id"), // nazwę pola referencji do encji KnowledgeSource
        inverseJoinColumns = @JoinColumn(name = "skill_id") // oraz nazwę pola referencji do encji Skill
    )
    private Set<Skill> connectedSkills = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Skill> getConnectedSkills() {
        return connectedSkills;
    }

    public void setConnectedSkills(Set<Skill> connectedSkills) {
        this.connectedSkills = connectedSkills;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnowledgeSource that = (KnowledgeSource) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "KnowledgeSource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", active=" + active +
                '}';
    }
}
```

Ostatnia klasa `User` również jest teraz możliwa do zmapowania, bo mamy zakończone mapowanie powiązanej z nią encji `KnowledgeSource`.

Konfiguracja adnotacjami klasy `User` może wyglądać jak niżej:

```java
package pl.honestit.spring.kb.data.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity // Wskazanie klasy jako encji
@Table(name = "users") // Określenie nazwy tabeli
public class User {

    @Id // Wskazanie pola klucza głównego
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Wskazanie sposobu generowania automatycznej wartości klucza głównego w oparciu o bazę danych
    private Long id;
    @Column(unique = true, nullable = false) // Określenie kolumny login jako unikalnej i wymaganej
    private String login;
    @Column(nullable = false) // Określenie kolumny password jako wymaganej
    private String password;
    @Column(name = "first_name") // Określenie nazwy kolumny dla pola firstName (zgodnej z konwencją SQL)
    private String firstName;
    @Column(name = "last_name") // Określenie nazwy kolumny dla pola lastName (zgodnej z konwencją SQL)
    private String lastName;

    @ManyToMany // Wskazanie relacji wiele do wielu między użytkownikami, a źródłami wiedzy
    @JoinTable(name = "users_known_sources", // Wskazanie wprost nazwy tabeli mapującej
        joinColumns = @JoinColumn(name = "user_id"), // nazwy kolumny referencji do encji User
        inverseJoinColumns = @JoinColumn(name = "source_id") // oraz nazwy kolumny referencji do encji KnowledgeSource
    )
    private Set<KnowledgeSource> knownSources = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<KnowledgeSource> getKnownSources() {
        return knownSources;
    }

    public void setKnownSources(Set<KnowledgeSource> knownSources) {
        this.knownSources = knownSources;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
```

Zwróć jeszcze uwage, że w skryptach tworzących tabele mamy również tabele `users_roles`, która przechowuje informacje o rolach przypisanych do użytkownika. Dla tej tabeli nie będziemy tworzyć encji - posłuży ona nam do konfiguracji Spring Security w dalszej części repozytorium.
