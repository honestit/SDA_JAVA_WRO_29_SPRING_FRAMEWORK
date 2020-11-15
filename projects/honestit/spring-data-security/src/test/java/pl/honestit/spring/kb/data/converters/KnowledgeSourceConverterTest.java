package pl.honestit.spring.kb.data.converters;

import org.junit.jupiter.api.*;
import pl.honestit.spring.kb.dto.AddKnowledgeSourceDTO;

import java.util.Set;

@DisplayName("Specification for KnowledgeSourceConverter class")
class KnowledgeSourceConverterTest {

    KnowledgeSourceConverter converter;

    @BeforeEach
    void setUp() {
        converter = new KnowledgeSourceConverter();
    }

    @Nested
    @DisplayName("Specification for converting from add knowledge source dto")
    class FromAddKnowledgeSourceDTO {

        AddKnowledgeSourceDTO data;

        @BeforeEach
        void setUp() {
            data = AddKnowledgeSourceDTO.builder()
                    .name("Some source name")
                    .description("Some description")
                    .url("Some url")
                    .connectedSkillsIds(Set.of())
                    .build();
        }

        @Test
        @DisplayName("- should convert valid data without exception")
        void test1() {

            Assertions.assertDoesNotThrow(() -> converter.from(data));
        }

    }

}