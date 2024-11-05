package es.alten.controller.impl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import es.alten.bo.UserBO;
import es.alten.controller.UserController;
import es.alten.domain.User;
import es.alten.dto.UserDTO;
import es.alten.rest.impl.RestControllerImpl;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/users")
@Tag(name = "users")
public class UserControllerImpl extends RestControllerImpl<User, UserDTO, Long, UserBO>
    implements UserController {
}
