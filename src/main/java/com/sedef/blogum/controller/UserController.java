package com.sedef.blogum.controller;
import com.sedef.blogum.businnes.concretes.UserService;
import com.sedef.blogum.model.User;
import com.sedef.blogum.request.ChangePasswordRequest;
import com.sedef.blogum.request.RegisterRequest;
import com.sedef.blogum.request.ResetPasswordRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody  RegisterRequest request) {
       User userSaved =  userService.addUser(request);
      return ResponseEntity.ok(userSaved);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable int id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        return ResponseEntity.ok(userList);
    }

    @GetMapping(path = "confirm")
    public ResponseEntity<String> userActivationLink(@RequestParam("token") String token) {
        String message =  userService.userActivationLink(token);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        String message = userService.changePassword(request);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/forget-password")
    public ResponseEntity<String> forgetPasswordSend(@RequestBody String email) {
        String message = userService.forgetPasswordSend(email);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token, @RequestBody ResetPasswordRequest request) {
        String message = userService.resetPassword(token, request);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/us")
    public String getUserString() {
        return "This is USER!";
    }
}
