package pl.honestit.spring.kb.data.validation.validators;

import org.assertj.core.api.AssertFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import pl.honestit.spring.kb.data.model.KnowledgeSource;
import pl.honestit.spring.kb.data.model.Skill;

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

    }

    static List<String> lowQualityNames() {
        KnowledgeSourceValidator defaultValidator = new KnowledgeSourceValidator();
        List<String> names = new ArrayList<>();
        names.add("   ");
        names.add("123 123");
        names.add(IntStream.rangeClosed(0, defaultValidator.getMinNameLength() - 1).mapToObj(i -> "a").collect(Collectors.joining()));
        names.add(IntStream.rangeClosed(0, defaultValidator.getMaxNameLength() + 1).mapToObj(i -> "a").collect(Collectors.joining()));
        names.addAll(defaultValidator.getBadWords());
        return names;
    }

}