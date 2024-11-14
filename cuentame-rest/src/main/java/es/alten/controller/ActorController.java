package es.alten.controller;

import es.alten.rest.BaseController;
import org.springframework.http.ResponseEntity;

public interface ActorController extends BaseController {
    ResponseEntity<byte[]> findImageById(Long id);
}
