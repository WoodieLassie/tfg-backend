package es.judith.controller.impl;

import es.judith.bo.AuthBO;
import es.judith.bo.FriendBO;
import es.judith.bo.UserBO;
import es.judith.controller.FriendController;
import es.judith.domain.Friend;
import es.judith.domain.user.User;
import es.judith.dto.friend.FriendDTO;
import es.judith.dto.friend.FriendInputDTO;
import es.judith.dto.swagger.FriendSwaggerDTO;
import es.judith.dto.swagger.RecommendationSwaggerDTO;
import es.judith.dto.user.UserProfileDTO;
import es.judith.exceptions.AlreadyExistsException;
import es.judith.exceptions.BadInputException;
import es.judith.exceptions.NotExistingIdException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.Serial;
import java.net.URI;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/friends")
@Tag(name = "friends")
public class FriendControllerImpl implements FriendController {

    @Serial
    private static final long serialVersionUID = 4464637391250250507L;
    private final transient FriendBO friendBO;
    private final transient AuthBO authBO;
    private static final Logger LOG = LoggerFactory.getLogger(FriendControllerImpl.class);
    private final transient UserBO userBO;

    public FriendControllerImpl(FriendBO friendBO, AuthBO authBO, UserBO userBO) {
        this.friendBO = friendBO;
        this.authBO = authBO;
        this.userBO = userBO;
    }

    @Override
    @Operation(
            method = "GET",
            summary = "Get all friendships")
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FriendSwaggerDTO.class))
            })
    @ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = @Content(schema = @Schema(hidden = true)))
    @GetMapping
    public ResponseEntity<Map<Long, UserProfileDTO>> findAllFriends() {
        LOG.debug("FriendControllerImpl: Finding all friendships");
        Long currentUserId = authBO.getCurrentUser().getId();
        Map<Long, UserProfileDTO> currentUserFriends = friendBO.getAllFriends(currentUserId);
        return ResponseEntity.status(HttpStatus.OK).body(currentUserFriends);
    }

    @Override
    @Operation(
            method = "GET",
            summary = "Get all sent unaccepted friend requests")
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FriendSwaggerDTO.class))
            })
    @ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = @Content(schema = @Schema(hidden = true)))
    @GetMapping("/requests")
    public ResponseEntity<Map<Long, UserProfileDTO>> findAllSentRequests() {
        LOG.debug("FriendControllerImpl: Finding all sent requests");
        Long currentUserId = authBO.getCurrentUser().getId();
        Map<Long, UserProfileDTO> currentSentRequests = friendBO.getAllSentRequests(currentUserId);
        return ResponseEntity.status(HttpStatus.OK).body(currentSentRequests);
    }

    @Override
    @Operation(
            method = "GET",
            summary = "Get all unaccepted received requests")
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FriendSwaggerDTO.class))
            })
    @ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = @Content(schema = @Schema(hidden = true)))
    @GetMapping("/requested")
    public ResponseEntity<Map<Long, UserProfileDTO>> findAllReceivedRequests() {
        LOG.debug("FriendControllerImpl: Finding all received requests");
        Long currentUserId = authBO.getCurrentUser().getId();
        Map<Long, UserProfileDTO> currentReceivedRequests = friendBO.getAllReceivedRequests(currentUserId);
        return ResponseEntity.status(HttpStatus.OK).body(currentReceivedRequests);
    }

    @Override
    @Operation(method = "POST", summary = "Send a friend request")
    @ApiResponse(
            responseCode = "204",
            description = "No Content",
            content = {@Content(schema = @Schema(implementation = Friend.class))})
    @ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = @Content(schema = @Schema(hidden = true)))
    @ApiResponse(
            responseCode = "409",
            description = "Conflict",
            content = {@Content(schema = @Schema(hidden = true))})
    @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = {@Content(schema = @Schema(hidden = true))})
    @SecurityRequirement(name = "Authorization")
    @PostMapping
    public ResponseEntity<Friend> sendRequest(@RequestBody FriendInputDTO friendInputDTO) {
        User currentUser = authBO.getCurrentUser();
        if (Objects.equals(currentUser.getId(), friendInputDTO.getUserReceiverId())) {
            throw new BadInputException(
                    "You cannot be your own friend, sorry :("
            );
        }
        User requestedUser = userBO.findOne(friendInputDTO.getUserReceiverId());
        if (requestedUser == null) {
            throw new NotExistingIdException(
                    "User with id " + friendInputDTO.getUserReceiverId() + " does not exist"
            );
        }
        if (friendBO.checkIfRelated(currentUser.getId(), friendInputDTO.getUserReceiverId())) {
            throw new AlreadyExistsException(
                    "An active request or a friendship with this user already exists"
            );
        }
        Friend friend = friendInputDTO.obtainDomainObject();
        friend.setUserReceiver(requestedUser);
        friend.setUserSender(currentUser);
        LOG.debug("FriendControllerImpl: Sending friend request");
        friendBO.save(friend);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Override
    @Operation(method = "PATCH", summary = "Accept a friend request")
    @ApiResponse(
            responseCode = "204",
            description = "No Content",
            content = {@Content(schema = @Schema(hidden = true))})
    @ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = @Content(schema = @Schema(hidden = true)))
    @ApiResponse(
            responseCode = "409",
            description = "Conflict",
            content = {@Content(schema = @Schema(hidden = true))})
    @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = {@Content(schema = @Schema(hidden = true))})
    @SecurityRequirement(name = "Authorization")
    @PatchMapping("/requested/{requestId}")
    public ResponseEntity<FriendDTO> acceptRequest(@PathVariable Long requestId) {
        User currentUser = authBO.getCurrentUser();
        Friend friendRequest = friendBO.findOne(requestId);
        if (friendRequest == null) {
            throw new NotExistingIdException(
                    "Friend request with id " + requestId + " doest no exist"
            );
        }
        if (!friendBO.checkIfRequestReceiver(currentUser.getId(), friendRequest.getUserSender().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        if (friendRequest.isRequestStatus()) {
            throw new AlreadyExistsException(
                    "Friend request has already been accepted"
            );
        }
        friendRequest.setRequestStatus(true);
        friendRequest.setId(requestId);
        LOG.debug("FriendControllerImpl: Accepting request with id {}", requestId);
        friendBO.save(friendRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Override
    @Operation(
            method = "DELETE",
            summary = "Delete a friend or an unaccepted friend request")
    @ApiResponse(
            responseCode = "204",
            description = "No content",
            content = {@Content(schema = @Schema(hidden = true))})
    @ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = @Content(schema = @Schema(hidden = true)))
    @ApiResponse(
            responseCode = "404",
            description = "Not Found",
            content = @Content(schema = @Schema(hidden = true)))
    @SecurityRequirement(name = "Authorization")
    @DeleteMapping("/{requestId}")
    public ResponseEntity<FriendDTO> delete(@PathVariable Long requestId) {
        User currentUser = authBO.getCurrentUser();
        Friend friendRequest = friendBO.findOne(requestId);
        if (friendRequest == null) {
            throw new NotExistingIdException(
                    "Friend request with id " + requestId + " doest no exist"
            );
        }
        if (Objects.equals(currentUser.getId(), friendRequest.getUserSender().getId())) {
            if (!friendBO.checkIfRelated(currentUser.getId(), friendRequest.getUserReceiver().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        }
        else if (!friendBO.checkIfRelated(currentUser.getId(), friendRequest.getUserSender().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        LOG.debug("FriendControllerImpl: Deleting friendship with id {}", requestId);
        friendBO.delete(requestId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
