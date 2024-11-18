package es.alten.bo.impl;

import es.alten.dao.CharacterRepository;
import es.alten.domain.Character;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CharacterBOImplTest {
  private static final List<Long> CHARACTER_IDS_TEST = new ArrayList<>(List.of(1L, 6L, 5L));

  @InjectMocks CharacterBOImpl characterBO;

  @Mock CharacterRepository repository;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    this.characterBO = new CharacterBOImpl(repository);
  }

  @Test
  void findAllById() {
    List<Character> mock_characters = new ArrayList<>();
    Character mock_character1 = new Character();
    Character mock_character2 = new Character();
    Character mock_character3 = new Character();
    mock_character1.setId(1L);
    mock_character2.setId(6L);
    mock_character3.setId(5L);
    mock_characters.add(mock_character1);
    mock_characters.add(mock_character2);
    mock_characters.add(mock_character3);
    when(repository.findAllById(CHARACTER_IDS_TEST)).thenReturn(mock_characters);
    List<Character> dbCharacters = characterBO.findAllById(CHARACTER_IDS_TEST);

    verify(repository, times(1)).findAllById(CHARACTER_IDS_TEST);

    Assertions.assertNotNull(dbCharacters);
    Assertions.assertEquals(dbCharacters.size(), 3);
  }
}
