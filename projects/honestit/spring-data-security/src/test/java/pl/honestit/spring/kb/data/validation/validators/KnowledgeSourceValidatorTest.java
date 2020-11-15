package pl.honestit.spring.kb.data.validation.validators;

import org.assertj.core.api.AssertFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import pl.honestit.spring.kb.data.model.KnowledgeSource;
import pl.honestit.spring.kb.data.model.Skill;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Specification for KnowledgeSourceValidator class")
class KnowledgeSourceValidatorTest {

    KnowledgeSourceValidator validator;

    @BeforeEach
    void setUp() {
        validator = new KnowledgeSourceValidator();
    }

    @Nested
    @DisplayName("Specification for validating knowledge source for save")
    class IsValidForSave {

        KnowledgeSource source;

        @BeforeEach
        void setUp() {
            source = new KnowledgeSource();
            source.setName("Alibaba i 7 rozbójników");
            source.setDescription("Ciekawa książka o Alibabie, który ma 7 rozbójników i jest niczym Senior w gronie Juniorów :D");
            source.setUrl("http://alibaba.com");
            source.setActive(false);
            Skill someSkill = new Skill();
            someSkill.setId(1L);
            source.setConnectedSkills(Set.of(someSkill));
        }

        @Test
        @DisplayName("- should pass for valid knowledge source")
        void test1() {

            boolean result = validator.isValidForSave(source);

            Assertions.assertTrue(result, "Valid knowledge source did not pass");
            org.assertj.core.api.Assertions.assertThat(result)
                    .describedAs("Valid knowledge source did not pass")
                    .isTrue();
        }

        @ParameterizedTest
        @NullSource
        @EmptySource
        @ValueSource(strings = {" ", "\t", "\n", "     "})
        @DisplayName("- should not pass for invalid knowledge source name")
        void test2(String name) {
            source.setName(name);

            boolean result = validator.isValidForSave(source);

            Assertions.assertFalse(result, "Pass for invalid knowledge source name");
        }

        @ParameterizedTest
        @MethodSource("pl.honestit.spring.kb.data.validation.validators.KnowledgeSourceValidatorTest#lowQualityNames")
        @DisplayName("- should not pass for low quality knowledge source name")
        void test3(String name) {
            source.setName(name);

            boolean result = validator.isValidForSave(source);

            Assertions.assertFalse(result, "Pass for low quality knowledge source name");
        }

        @ParameterizedTest
        @NullSource
        @EmptySource
        @ValueSource(strings = {" ", "\t", "\n", "     "})
        @DisplayName("- should not pass for invalid knowledge source description")
        void test4(String description) {
            source.setDescription(description);

            boolean result = validator.isValidForSave(source);

            Assertions.assertFalse(result, "Pass for invalid knowledge source name");
        }

        @ParameterizedTest
        @MethodSource("pl.honestit.spring.kb.data.validation.validators.KnowledgeSourceValidatorTest#lowQualityDescriptions")
        @DisplayName("- should not pass for low quality knowledge source description")
        void test5(String description) {
            source.setName(description);

            boolean result = validator.isValidForSave(source);

            Assertions.assertFalse(result, "Pass for low quality knowledge source name");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("- should not pass for knowledge source without skills")
        void test5(Set<Skill> skills) {
            source.setConnectedSkills(skills);

            boolean result = validator.isValidForSave(source);

            Assertions.assertFalse(result, "Passed for knowledge source without skills");
        }

    }

    static List<String> lowQualityNames() {
        KnowledgeSourceValidator defaultValidator = new KnowledgeSourceValidator();
        List<String> names = new ArrayList<>();
        names.add("   ");
        names.add("123 123");
        names.add(IntStream.generate(() -> 1).limit(defaultValidator.getMinNameLength() - 1).mapToObj(i -> "a").collect(Collectors.joining()));
        names.add(IntStream.generate(() -> 1).limit(defaultValidator.getMaxNameLength() + 1).mapToObj(i -> "a").collect(Collectors.joining()));
        names.addAll(defaultValidator.getBadWords());
        return names;
    }

    static List<String> lowQualityDescriptions() {
        KnowledgeSourceValidator defaultValidator = new KnowledgeSourceValidator();
        List<String> names = new ArrayList<>();
        names.add("   ");
        names.add("123 123");
        names.add("some word less five");
        String text = IntStream.generate(() -> 1).limit(defaultValidator.getMinDescriptionLength() - 1).mapToObj(i -> "a").collect(Collectors.joining());
        System.out.println(text.length());
        names.add(IntStream.generate(() -> 1).limit(defaultValidator.getMinDescriptionLength() - 1).mapToObj(i -> "a").collect(Collectors.joining()));
        names.add(IntStream.generate(() -> 1).limit(defaultValidator.getMaxDescriptionLength() + 1).mapToObj(i -> "a").collect(Collectors.joining()));
        names.addAll(defaultValidator.getBadWords());
        return names;
    }

}