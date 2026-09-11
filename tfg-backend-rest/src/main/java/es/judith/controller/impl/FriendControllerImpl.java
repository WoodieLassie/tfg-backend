package es.judith.controller.impl;

import es.judith.controller.FriendController;
import es.judith.domain.Friend;
import es.judith.dto.FriendDTO;
import es.judith.dto.FriendInputDTO;
import es.judith.dto.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

//Necesitará utilizar AuthController para el usuario actual
public class FriendControllerImpl implements FriendController {
    @Override
    public ResponseEntity<List<UserDTO>> findAllFriends(Long userId) {
        return null;
    }

    @Override
    public ResponseEntity<List<UserDTO>> findAllSentRequests(Long userId) {
        return null;
    }

    @Override
    public ResponseEntity<List<UserDTO>> findAllReceivedRequests(Long userId) {
        return null;
    }

    //El usuario loggeado siempre será el sender
    @Override
    public ResponseEntity<Friend> sendRequest(FriendInputDTO friendInputDTO) {
        return null;
    }

    //Solo posible si el usuario loggeado es el receiver. Si no lo es, error 401
    @Override
    public ResponseEntity<FriendDTO> acceptRequest(Long requestId) {
        return null;
    }

    //Servirá tanto para requests por parte del sender y el receiver, como para amistades ya aceptadas
    @Override
    public ResponseEntity<FriendDTO> delete(Long requestId) {
        return null;
    }
}
