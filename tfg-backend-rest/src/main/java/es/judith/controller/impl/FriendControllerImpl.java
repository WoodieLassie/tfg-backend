package es.judith.controller.impl;

import es.judith.bo.AuthBO;
import es.judith.bo.FriendBO;
import es.judith.bo.UserBO;
import es.judith.controller.FriendController;
import es.judith.domain.Friend;
import es.judith.domain.User;
import es.judith.dto.FriendDTO;
import es.judith.dto.FriendInputDTO;
import es.judith.dto.UserDTO;
import es.judith.exceptions.AlreadyExistsException;
import es.judith.exceptions.BadInputException;
import es.judith.exceptions.NotExistingIdException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/friends")
@Tag(name = "friends")
//Necesitará utilizar AuthController para el usuario actual
public class FriendControllerImpl implements FriendController {

    private final FriendBO friendBO;
    private final AuthBO authBO;
    private static final Logger LOG = LoggerFactory.getLogger(FriendControllerImpl.class);
    private final UserBO userBO;

    public FriendControllerImpl(FriendBO friendBO, AuthBO authBO, UserBO userBO) {
        this.friendBO = friendBO;
        this.authBO = authBO;
        this.userBO = userBO;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<UserDTO>> findAllFriends(Long userId) {
        Long currentUserId = authBO.getCurrentUser().getId();
        List<UserDTO> currentUserFriends = friendBO.getAllFriends(currentUserId);
        return ResponseEntity.status(HttpStatus.OK).body(currentUserFriends);
    }

    @Override
    @GetMapping("/requests")
    public ResponseEntity<List<UserDTO>> findAllSentRequests(Long userId) {
        Long currentUserId = authBO.getCurrentUser().getId();
        List<UserDTO> currentSentRequests = friendBO.getAllSentRequests(currentUserId);
        return ResponseEntity.status(HttpStatus.OK).body(currentSentRequests);
    }

    @Override
    @GetMapping("/requested")
    public ResponseEntity<List<UserDTO>> findAllReceivedRequests(Long userId) {
        Long currentUserId = authBO.getCurrentUser().getId();
        List<UserDTO> currentReceivedRequests = friendBO.getAllReceivedRequests(currentUserId);
        return ResponseEntity.status(HttpStatus.OK).body(currentReceivedRequests);
    }

    //El usuario loggeado siempre será el sender
    //POST
    @Override
    @PostMapping
    public ResponseEntity<Friend> sendRequest(@RequestBody FriendInputDTO friendDTO) {
        User currentUser = authBO.getCurrentUser();
        if (Objects.equals(currentUser.getId(), friendDTO.getUserReceiverId())) {
            throw new BadInputException(
                    "You cannot be your own friend, sorry :("
            );
        }
        User requestedUser = userBO.findOne(friendDTO.getUserReceiverId());
        if (requestedUser == null) {
            throw new NotExistingIdException(
                    "User with id " + friendDTO.getUserReceiverId() + "does not exists"
            );
        }
        if (friendBO.checkIfRelated(currentUser.getId(), friendDTO.getUserReceiverId())) {
            throw new AlreadyExistsException(
                    "An active request or a friendship with this user already exists"
            );
        }
        Friend friend = friendDTO.obtainDomainObject();
        if (friend == null) {
            throw new NotExistingIdException(
                    "User with id " + friendDTO.getUserReceiverId() + " does not exist");
        }
        friend.setUserReceiver(requestedUser);
        friend.setUserSender(currentUser);
        friendBO.save(friend);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    //Solo posible si el usuario loggeado es el receiver. Si no lo es, error 401
    //PATCH. Solo cambia el requestStatus
    @Override
    @PatchMapping("/requested/{requestId}")
    public ResponseEntity<FriendDTO> acceptRequest(@PathVariable Long requestId) {
        return null;
    }

    //Servirá tanto para requests por parte del sender y el receiver, como para amistades ya aceptadas
    @Override
    @DeleteMapping("/{requestId}")
    public ResponseEntity<FriendDTO> delete(@PathVariable Long requestId) {
        return null;
    }
}
