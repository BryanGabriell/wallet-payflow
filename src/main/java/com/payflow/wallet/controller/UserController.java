package com.payflow.wallet.controller;


import com.payflow.wallet.dto.user.UserRequest;
import com.payflow.wallet.dto.user.UserResponse;
import com.payflow.wallet.entity.User;
import com.payflow.wallet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest userRequest){
        UserResponse userResponse = userService.registerUser(userRequest);
        return ResponseEntity.status(200).body(userResponse);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserRequest userRequest, @PathVariable Long userId){
        UserResponse userResponse = userService.updateUser(userId,userRequest);
        return ResponseEntity.status(200).body(userResponse);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.status(204).build();
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody @Valid UserRequest request) {
        userService.updatePassword(id, request.password());

        return ResponseEntity.status(204).build();
    }
}
