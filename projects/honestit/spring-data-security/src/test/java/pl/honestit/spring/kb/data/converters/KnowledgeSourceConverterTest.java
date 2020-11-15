package pl.honestit.spring.kb.data.converters;

import org.junit.jupiter.api.*;
import pl.honestit.spring.kb.data.model.KnowledgeSource;
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

        @Test
        @DisplayName("- should return converted object with same values")
        void test2() {

            KnowledgeSource converted = converter.from(data);

            assertThatShouldBePropertyConverted(converted);

            org.assertj.core.api.Assertions.assertThat(converted)
                    .isNotNull()
                    .hasAllNullFieldsOrPropertiesExcept("name", "description", "url", "knowingUsers", "connectedSkills")
                    .hasFieldOrPropertyWithValue("name", converted.getName())
                    .hasFieldOrPropertyWithValue("description", converted.getDescription())
                    .hasFieldOrPropertyWithValue("url", converted.getUrl());

            org.assertj.core.api.Assertions.assertThat(converted.getKnowingUsers())
                    .isNotNull().isEmpty();

        }

        private void assertThatShouldBePropertyConverted(KnowledgeSource converted) {
            Assertions.assertNotNull(converted);
            Assertions.assertNull(converted.getId());
            Assertions.assertNull(converted.getActive());
            Assertions.assertEquals(data.getName(), converted.getName());
            Assertions.assertEquals(data.getDescription(), converted.getDescription());
            Assertions.assertEquals(data.getUrl(), converted.getUrl());
        }

    }

}