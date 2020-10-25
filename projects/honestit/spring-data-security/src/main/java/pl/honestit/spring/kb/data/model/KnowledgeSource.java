package pl.honestit.spring.kb.data.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity // Oznaczamy, że klasa jest encją
@Table(name = "knowledge_sources") // Dodajemy informację o nazwie tabeli dla encji
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class KnowledgeSource {

    @Id // Wskazujemy pole klucza głównego
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // oraz strategię generowania wartości oparta na bazie danych
    private Long id;
    @Column(unique = true, nullable = false) // Określamy nazwę źródła jako unikalną i wymaganą
    private String name;
    @Column(columnDefinition = "TEXT")
    // Definiujemy docelowy typ kolumny jako TEXT zamiast domyślnego varchar(255)
    private String description;
    private String url;
    private Boolean active;

    @ManyToMany // Dostarczamy mapowanie wiele do wielu między źródłami wiedzy a umiejętnościami
    @JoinTable(name = "knowledge_sources_skills", // Wskazujemy wprost nazwę tabeli mapująej
            joinColumns = @JoinColumn(name = "source_id"), // nazwę pola referencji do encji KnowledgeSource
            inverseJoinColumns = @JoinColumn(name = "skill_id") // oraz nazwę pola referencji do encji Skill
    )
    private Set<Skill> connectedSkills = new HashSet<>();

    @ManyToMany(mappedBy = "knownSources")
    private Set<User> knowingUsers = new HashSet<>();
}