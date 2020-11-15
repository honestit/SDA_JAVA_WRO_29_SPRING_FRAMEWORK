package pl.honestit.spring.kb.data.converters;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import pl.honestit.spring.kb.data.model.KnowledgeSource;
import pl.honestit.spring.kb.data.repository.SkillRepository;
import pl.honestit.spring.kb.dto.AddKnowledgeSourceDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@DisplayName("Specification for KnowledgeSourceConverter class")
class KnowledgeSourceConverterTest {

    KnowledgeSourceConverter converter;
    SkillRepository skillRepository;

    @BeforeEach
    void setUp() {
        skillRepository = Mockito.mock(SkillRepository.class);
        converter = new KnowledgeSourceConverter(skillRepository);
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

        @Test
        @DisplayName("- should fail when converted from null value")
        void test3() {
            data = null;

            Assertions.assertThrows(IllegalArgumentException.class, () -> converter.from(data), "Did not fail on null input");

            org.assertj.core.api.Assertions.assertThatThrownBy(() -> converter.from(data), "Did not fail on null input")
                    .hasMessageContaining("null")
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Disabled
        @Test
        @DisplayName("- should fail when converted from data with null values")
        void test4(AddKnowledgeSourceDTO data) {

        }

        @Test
        @DisplayName("- should get skills from database")
        void test5() {
            Mockito.when(skillRepository.findAllById(ArgumentMatchers.anyIterable())).thenReturn(List.of());

            converter.from(data);

            Mockito.verify(skillRepository, Mockito.times(1)).findAllById(ArgumentMatchers.anyIterable());
        }

        @Test
        @DisplayName("- should get skills from database using provided ids")
        void test6() {
            data.setConnectedSkillsIds(Set.of(1L,10L,100L));
            Mockito.when(skillRepository.findAllById(data.getConnectedSkillsIds())).thenReturn(List.of());

            converter.from(data);

            Mockito.verify(skillRepository, Mockito.times(1)).findAllById(data.getConnectedSkillsIds());
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