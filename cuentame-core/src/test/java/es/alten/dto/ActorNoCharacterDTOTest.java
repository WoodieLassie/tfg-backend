package es.alten.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActorNoCharacterDTOTest {
    ActorNoCharacterDTO actorNoCharacterDTO = new ActorNoCharacterDTO();

    @BeforeEach
    void setUp() {
        actorNoCharacterDTO = new ActorNoCharacterDTO();
    }

    @Test
    void testEquals() {
        actorNoCharacterDTO.equals(null);
        Assertions.assertNotEquals(null, actorNoCharacterDTO);
    }

    @Test
    void testHashCode() {
        ActorNoCharacterDTO x = new ActorNoCharacterDTO();
        x.setName("actor");
        ActorNoCharacterDTO y = new ActorNoCharacterDTO();
        y.setName("actor");
        Assertions.assertTrue(x.equals(y) && y.equals(x));
        Assertions.assertEquals(x.hashCode(), y.hashCode());
    }
}
