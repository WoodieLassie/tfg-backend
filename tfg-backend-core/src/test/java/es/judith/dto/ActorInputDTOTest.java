package es.judith.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Calendar;

class ActorInputDTOTest {
  ActorInputDTO actorInputDTO = new ActorInputDTO();

  @BeforeEach
  void setUp() {
    actorInputDTO = new ActorInputDTO();
  }

  @Test
  void testEquals() {
    actorInputDTO.equals(null);
    Assertions.assertNotEquals(null, actorInputDTO);
  }

  @Test
  void testHashCode() {
    ActorInputDTO x = new ActorInputDTO();
    x.setName("actor");
    ActorInputDTO y = new ActorInputDTO();
    y.setName("actor");
    Assertions.assertTrue(x.equals(y) && y.equals(x));
    Assertions.assertEquals(x.hashCode(), y.hashCode());
  }

  @Test
  void testAllFieldsArePresent() {
    ActorInputDTO x = new ActorInputDTO();
    ActorInputDTO y = new ActorInputDTO();
    y.setId(1L);
    y.setName("actor");
    y.setGender("gender");
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 1);
    calendar.set(Calendar.MONTH, Calendar.JANUARY);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    y.setBirthDate(new Date(calendar.getTimeInMillis()));
    y.setNationality("nationality");
    y.setBirthLocation("location");
    y.setCharacterId(1L);
    Assertions.assertFalse(x.allFieldsArePresent());
    Assertions.assertTrue(y.allFieldsArePresent());
  }
}
