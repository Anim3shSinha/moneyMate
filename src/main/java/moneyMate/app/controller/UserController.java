package moneyMate.app.controller;

import lombok.RequiredArgsConstructor;
import moneyMate.app.dto.UserDto;
import moneyMate.app.entity.User;
import moneyMate.app.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.registerUser(userDto));
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authenticateUser(@RequestBody UserDto userDto) {
        var authObject = userService.authenticateUser(userDto);
        var token = (String) authObject.get("token");
        System.out.println("Jwt token: " + token);
        return ResponseEntity.ok()
                .header("Authorization", token)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                .body(authObject.get("user"));
    }
}