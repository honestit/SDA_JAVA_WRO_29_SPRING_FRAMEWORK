package pl.honestit.spring.kb.data.validation.validators;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pl.honestit.spring.kb.data.model.KnowledgeSource;

import java.util.Set;
import java.util.stream.Stream;

@Component
@Getter
@Setter
public class KnowledgeSourceValidator {

    private Set<String> badWords = Set.of("dupa", "kupa", "cholera");
    private int minDescriptionLength = 20;
    private int maxDescriptionLength = 160;
    private int minNameLength = 6;
    private int maxNameLength = 120;

    public boolean isValidForSave(KnowledgeSource data) {
        if (data == null) return false;

        if (data.getDescription() == null || data.getDescription().isBlank()) return false;
        if (!isDescriptionQualitative(data.getDescription())) return false;

        if (data.getName() == null || data.getName().isBlank()) return false;
        if (!isNameQualitative(data.getName())) return false;

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
        System.out.println(wordsLength);
        if (wordsLength < minDescriptionLength) return false;
        if (wordsLength > maxDescriptionLength) return false;

        return true;
    }

    private boolean isNameQualitative(String name) {
        String[] words = name.split("\\s+");
        if (words.length < 1) return false;

        if (Stream.of(words).anyMatch(badWords::contains)) return false;

        if (Stream.of(words).allMatch(word -> word.matches("\\d+"))) return false;

        int wordsLength = Stream.of(words).mapToInt(String::length).sum();
        if (wordsLength < minNameLength) return false;
        if (wordsLength > maxNameLength) return false;

        return true;
    }

    private boolean isUrlQualitative(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }
}
