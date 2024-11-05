package es.alten.controller.impl;

import es.alten.bo.ActorBO;
import es.alten.controller.ActorController;
import es.alten.domain.Actor;
import es.alten.dto.ActorDTO;
import es.alten.rest.impl.RestControllerImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/actors")
@Tag(name = "actors")
public class ActorControllerImpl extends RestControllerImpl<Actor, ActorDTO, Long, ActorBO>
    implements ActorController {}
