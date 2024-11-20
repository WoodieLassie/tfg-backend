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
class CharacterBOImplTest {
  private static final List<Long> CHARACTER_IDS_TEST = new ArrayList<>(List.of(1L, 6L, 5L));

  @InjectMocks CharacterBOImpl characterBO;

  @Mock CharacterRepository repository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.characterBO = new CharacterBOImpl(repository);
  }

  @Test
  void findAllById() {
    List<Character> mockCharacters = new ArrayList<>();
    Character mockCharacter1 = new Character();
    Character mockCharacter2 = new Character();
    Character mockCharacter3 = new Character();
    mockCharacter1.setId(1L);
    mockCharacter2.setId(6L);
    mockCharacter3.setId(5L);
    mockCharacters.add(mockCharacter1);
    mockCharacters.add(mockCharacter2);
    mockCharacters.add(mockCharacter3);
    when(repository.findAllById(CHARACTER_IDS_TEST)).thenReturn(mockCharacters);
    List<Character> dbCharacters = characterBO.findAllById(CHARACTER_IDS_TEST);

    verify(repository, times(1)).findAllById(CHARACTER_IDS_TEST);

    Assertions.assertNotNull(dbCharacters);
    Assertions.assertEquals(3, dbCharacters.size());
  }
}
