package com.example.jointperchasesnew.controller;

import com.example.jointperchasesnew.representation.BaseRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/")
public class CommonController {

    @GetMapping("/")
    public ResponseEntity<BaseRepresentation> getOptions() {
        BaseRepresentation representation = new BaseRepresentation();
        representation.add(linkTo(methodOn(UserController.class).register(null))
                .withRel("register")
                .withType("POST")
                .withName("User Register"));
        return ResponseEntity.ok(representation);
    }
}