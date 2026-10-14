package es.judith.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.judith.bo.ActorBO;
import es.judith.bo.CharacterBO;
import es.judith.domain.Actor;
import es.judith.domain.Character;
import es.judith.dto.actor.ActorInputDTO;
import es.judith.dto.actor.ActorNoCharacterDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = ActorControllerImpl.class)
@WebMvcTest(ActorControllerImpl.class)
class ActorControllerImplTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ActorBO actorBO;
    @MockitoBean
    private CharacterBO characterBO;
    @MockitoBean
    private ActorInputDTO actorInputDTOMock;

    @Test
    void findAllTestOk() throws Exception {
      mockMvc.perform(get("/api/actors"))
              .andExpect(status().isOk());
    }
    @Test
    void findByIdOk() throws Exception {
        ActorNoCharacterDTO actorDTO = new ActorNoCharacterDTO();
        actorDTO.setId(1L);
        Actor actor = actorDTO.obtainDomainObject();
        given(actorBO.findOne(actor.getId())).willReturn(actor);
        mockMvc.perform(get("/api/actors/{id}", actorDTO.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(actorDTO)));
    }
    @Test
    void findByIdNotFound() throws Exception {
        ActorNoCharacterDTO actorDTO = new ActorNoCharacterDTO();
        actorDTO.setId(2L);
        given(actorBO.findOne(1L)).willReturn(null);
        mockMvc.perform(get("/api/actors/{id}", actorDTO.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void findImageByIdOk() throws Exception {
        String imageData = "askdjfkfdsjl";
        byte[] image = imageData.getBytes();
        Actor actor = new Actor();
        actor.setId(1L);
        actor.setImageData(image);
        given(actorBO.findImageById(actor.getId())).willReturn(image);
        mockMvc.perform(get("/api/actors/image/{id}", actor.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().bytes(image));
    }

    @Test
    void findImageByIdNotFound() throws Exception {
        Actor actor = new Actor();
        actor.setId(1L);
        given(actorBO.findImageById(actor.getId())).willReturn(null);
        mockMvc.perform(get("/api/actors/image/{id}", actor.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void addOk() throws Exception {
        Character character = new Character();
        character.setId(1L);
        List<Long> list = List.of(character.getId());
        List<Character> listCharacters = List.of(character);
        ActorInputDTO actorInputDTO = new ActorInputDTO();
        actorInputDTO.setName("name");
        actorInputDTO.setBirthDate(new Date(1, 1, 1970));
        actorInputDTO.setGender("el genero es un constructo social que debe ser abolido");
        actorInputDTO.setBirthLocation("el planeta tierra");
        actorInputDTO.setNationality("la humanidad");
        actorInputDTO.setCharacterIds(list);
        Actor actor = actorInputDTO.obtainDomainObject();
        actor.setCharacters(listCharacters);
        given(actorBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(characterBO.findAllById(list)).willReturn(listCharacters);
        given(actorBO.save(actor)).willReturn(actor);
        mockMvc.perform(post("/api/actors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actorInputDTO)))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}