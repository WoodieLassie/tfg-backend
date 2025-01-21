package es.judith.bo.impl;

import es.judith.dao.CharacterRepository;
import es.judith.domain.Character;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
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
  @Test
  void saveTest() {
    Character mockCharacter = new Character();
    mockCharacter.setId(1L);
    given(repository.save(mockCharacter)).willReturn(mockCharacter);
    Character dbCharacter = characterBO.save(mockCharacter);

    verify(repository, times(1)).save(mockCharacter);

    Assertions.assertNotNull(dbCharacter);
    Assertions.assertEquals(mockCharacter, dbCharacter);
  }
  @Test
  void deleteTest() {
    Character mockCharacter = new Character();
    mockCharacter.setId(1L);
    willDoNothing().given(repository).deleteById(mockCharacter.getId());
    willDoNothing().given(repository).deleteFromRelatedTable(mockCharacter.getId());
    characterBO.delete(mockCharacter.getId());
    verify(repository, times(1)).deleteById(mockCharacter.getId());
    verify(repository, times(1)).deleteFromRelatedTable(mockCharacter.getId());
  }
}
