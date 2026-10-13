package es.judith.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.judith.bo.ActorBO;
import es.judith.bo.CharacterBO;
import es.judith.domain.Actor;
import es.judith.dto.actor.ActorDTO;
import es.judith.dto.actor.ActorNoCharacterDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(ActorControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
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
}