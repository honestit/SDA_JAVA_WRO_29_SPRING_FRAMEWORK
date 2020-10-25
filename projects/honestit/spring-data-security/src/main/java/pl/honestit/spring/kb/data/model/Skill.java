package pl.honestit.spring.kb.data.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity // Oznaczamy, że klasa jest encją
@Table(name = "skills") // Dostosowujemy nazwę tabeli dla naszej encji
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class Skill {

    @Id // Wskazuje pole klucza głównego
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Wskazujemy aby baza danych dobrała mechanizm generowania wartości klucza głównego
    private Long id;
    @Column(unique = true, nullable = false)
    // Określamy aby nazwa umiejętności była unikalna i wymagana
    private String name;
    @Column(nullable = false) // Określamy aby nazwa kategorii dla umiejętności była wymagana
    private String category;
}