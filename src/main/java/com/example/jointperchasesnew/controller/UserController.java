    package com.example.jointperchasesnew.controller;

    import com.example.jointperchasesnew.dto.UserRegistrationDto;
    import com.example.jointperchasesnew.dto.UserReplenishmentDto;
    import com.example.jointperchasesnew.representation.UserRepresentation;
    import com.example.jointperchasesnew.service.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.hateoas.CollectionModel;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.net.URI;

    @RestController
    @RequestMapping("/")
    public class UserController {

        private UserService userService;

        @Value("${app.url.base}")
        private String urlBase;

        @GetMapping("/{username}")
        public ResponseEntity<UserRepresentation> getUser(@PathVariable String username) {
            UserRepresentation representation = userService.getUser(username);
            return ResponseEntity.ok(representation);
        }

        @GetMapping("/users")
        public ResponseEntity<CollectionModel<UserRepresentation>> getAllUsers() {
            CollectionModel<UserRepresentation> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        }

        @PostMapping("/register")
        public ResponseEntity<UserRepresentation> register(@RequestBody UserRegistrationDto userRegDto) {
            UserRepresentation representation = userService.register(userRegDto);
            return ResponseEntity.created(URI.create(urlBase + representation.getUsername())).body(representation);
        }

        @PatchMapping("/user/deposit")
        public ResponseEntity<UserRepresentation> deposit(@RequestBody UserReplenishmentDto userDepositDto) {
            UserRepresentation representation = userService.deposit(userDepositDto);
            return ResponseEntity.ok(representation);
        }



        @Autowired
        public void setUserService(UserService userService) {
            this.userService = userService;
        }
    }
