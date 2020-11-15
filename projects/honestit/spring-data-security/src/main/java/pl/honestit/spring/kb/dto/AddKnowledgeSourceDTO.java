package pl.honestit.spring.kb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AddKnowledgeSourceDTO {

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank @URL
    private String url;
    @NotNull @NotEmpty
    private Set<Long> connectedSkillsIds;
}
