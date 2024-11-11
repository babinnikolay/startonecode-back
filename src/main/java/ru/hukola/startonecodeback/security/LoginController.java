package ru.hukola.startonecodeback.security;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hukola.startonecodeback.user.User;
import ru.hukola.startonecodeback.user.UserService;

@RestController
@AllArgsConstructor
public class LoginController {

    private final UserService userService;

    @RequestMapping("/login")
    public ResponseEntity<User> getUserDetails(Authentication authentication) {
        User user = userService.findUserByName(authentication.getName());
        return ResponseEntity.ok(user);
    }
}

