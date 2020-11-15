package pl.honestit.spring.kb.data.converters;

import org.springframework.stereotype.Component;
import pl.honestit.spring.kb.data.model.KnowledgeSource;
import pl.honestit.spring.kb.dto.AddKnowledgeSourceDTO;

@Component
public class KnowledgeSourceConverter {

    public KnowledgeSource from(AddKnowledgeSourceDTO data) {
        if (data == null) throw new IllegalArgumentException("Nie można konwertować z null");

        KnowledgeSource knowledgeSource = new KnowledgeSource();
        if (data.getName() == null) throw new IllegalArgumentException("Źródło wiedzy musi mieć nazwę");
        knowledgeSource.setName(data.getName());

        if (data.getDescription() == null) throw new IllegalArgumentException("Źródło wiedzy musi mieć opis");
        knowledgeSource.setDescription(data.getDescription());

        if (data.getUrl() == null) throw new IllegalArgumentException("Źródło wiedzy musi mieć url");
        knowledgeSource.setUrl(data.getUrl());

        if (data.getConnectedSkillsIds() == null) throw new IllegalArgumentException("Źródło wiedzy musi mieć umiejętności");
        // TODO Przepisanie umiejętności zrealizujemy w dalszej części

        return knowledgeSource;
    }
}