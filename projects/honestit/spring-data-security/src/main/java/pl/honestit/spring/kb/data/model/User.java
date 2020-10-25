package pl.honestit.spring.kb.data.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity // Wskazanie klasy jako encji
@Table(name = "users") // Określenie nazwy tabeli
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class User {

    @Id // Wskazanie pola klucza głównego
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Wskazanie sposobu generowania automatycznej wartości klucza głównego w oparciu o bazę danych
    private Long id;
    @Column(unique = true, nullable = false) // Określenie kolumny login jako unikalnej i wymaganej
    private String login;
    @Column(nullable = false) // Określenie kolumny password jako wymaganej
    private String password;
    @Column(name = "first_name")
    // Określenie nazwy kolumny dla pola firstName (zgodnej z konwencją SQL)
    private String firstName;
    @Column(name = "last_name")
    // Określenie nazwy kolumny dla pola lastName (zgodnej z konwencją SQL)
    private String lastName;

    @ManyToMany // Wskazanie relacji wiele do wielu między użytkownikami, a źródłami wiedzy
    @JoinTable(name = "users_known_sources", // Wskazanie wprost nazwy tabeli mapującej
            joinColumns = @JoinColumn(name = "user_id"), // nazwy kolumny referencji do encji User
            inverseJoinColumns = @JoinColumn(name = "source_id") // oraz nazwy kolumny referencji do encji KnowledgeSource
    )
    private Set<KnowledgeSource> knownSources = new HashSet<>();
}