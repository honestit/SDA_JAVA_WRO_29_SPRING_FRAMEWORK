package pl.honestit.spring.kb.data.validation.validators;

import org.junit.jupiter.api.*;
import pl.honestit.spring.kb.data.model.KnowledgeSource;
import pl.honestit.spring.kb.data.model.Skill;

import java.util.Set;

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

        @Test
        @DisplayName("- should pass for valid knowledge source")
        void test1() {
            KnowledgeSource source = new KnowledgeSource();
            source.setName("Alibaba i 7 rozbójników");
            source.setDescription("Ciekawa książka o Alibabie, który ma 7 rozbójników i jest niczym Senior w gronie Juniorów :D");
            source.setUrl("http://alibaba.com");
            source.setActive(false);
            Skill someSkill = new Skill();
            someSkill.setId(1L);
            source.setConnectedSkills(Set.of(someSkill));

            boolean result = validator.isValidForSave(source);

            Assertions.assertTrue(result, "Valid knowledge source did not pass");
        }

    }

}