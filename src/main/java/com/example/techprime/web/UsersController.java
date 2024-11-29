package com.example.techprime.web;

import com.example.techprime.domain.entities.User;
import com.example.techprime.domain.service.UsersService;
import com.example.techprime.dto.AuthDto;
import com.example.techprime.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.HEAD, RequestMethod.TRACE})
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UsersController {

    private static final Logger log = LoggerFactory.getLogger(UsersController.class);
    private final UsersService usersService;

    @GetMapping("/user-info/{id}")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable int id) {
        Optional<User> userById = usersService.findUserById(id);

        if (userById.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var userInfo = UserDto.builder()
                .id(userById.get().getId())
                .name(userById.get().getName())
                .email(userById.get().getEmail())
                .role(userById.get().getRole())
                .credit(userById.get().getCredit())
                .status(userById.get().getDeleted())
                .build();

        return ResponseEntity.ok().body(userInfo);
    }

    @PostMapping("/create")
    public ResponseEntity<User> saveUser(@RequestBody UserDto user) {
        return ResponseEntity.ok(usersService.create(user));
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> listUsers() {
        List<User> users = usersService.listUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody UserDto user) {
        return ResponseEntity.ok().body(usersService.update(user));
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthDto> authUser(@RequestBody AuthDto authDto) {
        return ResponseEntity.ok().body(usersService.auth(authDto));
    }

    @PatchMapping("/users/{id}/{status}")
    public ResponseEntity<?> updateUserStatus(@PathVariable Integer id, @PathVariable String status) {
        Optional<User> user = usersService.findUserById(id);

        if (user.isPresent()) {
            User existingUser = user.get();

            if ("activated".equalsIgnoreCase(status)) {
                existingUser.setDeleted(false);
            } else if ("deactivated".equalsIgnoreCase(status)) {
                existingUser.setDeleted(true);
            } else {
                return ResponseEntity.badRequest().body("Status inválido");
            }

            usersService.updateUser(existingUser);
            return ResponseEntity.ok().body("Status do usuário atualizado com sucesso.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
    }
}