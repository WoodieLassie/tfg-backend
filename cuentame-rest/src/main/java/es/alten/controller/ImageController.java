package es.alten.controller;

import es.alten.web.BaseController;
import org.springframework.http.ResponseEntity;

public interface ImageController extends BaseController {
    ResponseEntity<byte[]> findByName(String name);
}
