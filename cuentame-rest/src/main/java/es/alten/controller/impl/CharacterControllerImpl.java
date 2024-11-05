package es.alten.controller.impl;

import es.alten.bo.CharacterBO;
import es.alten.controller.CharacterController;
import es.alten.dto.CharacterDTO;
import es.alten.rest.impl.RestControllerImpl;
import es.alten.domain.Character;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/characters")
@Tag(name = "characters")
public class CharacterControllerImpl
    extends RestControllerImpl<Character, CharacterDTO, Long, CharacterBO>
    implements CharacterController {}
