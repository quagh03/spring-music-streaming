package org.example.musicstream.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.example.musicstream.dtos.requests.user.UserCreationRequest;
import org.example.musicstream.dtos.response.api.ApiBaseResponse;
import org.example.musicstream.dtos.response.api.ApiSuccessResponse;
import org.example.musicstream.services.user.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "User API")
@RestController
@RequestMapping("${api.base-url}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @Operation(summary = "Get all users with pagination and sorting")
    @GetMapping()
    // http://localhost:8080/api/v1/users?pageNumber=0&pageSize=10&sortBy=dateOfBirth:desc
    public ResponseEntity<ApiBaseResponse> getAllUsers(
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(required = false) String sortBy

    ){
        return ResponseEntity.ok().body(
                new ApiSuccessResponse<>(HttpStatus.OK.value(), "Users retrieved successfully", userServiceImpl.getAllUsersWithSortBy(pageNumber, pageSize, sortBy)));
    }

    @Operation(summary = "Get user by id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiBaseResponse> getUserById(@PathVariable Long id){
        return ResponseEntity.ok().body(
                new ApiSuccessResponse<>(HttpStatus.OK.value(), "User retrieved successfully", userServiceImpl.getUserById(id)));
    }

    @Operation(summary = "Create a new user")
    @PostMapping()
    public ResponseEntity<ApiBaseResponse> createUser(@Valid @RequestBody UserCreationRequest userCreationRequest){
        return ResponseEntity.ok().body(
                new ApiSuccessResponse<>(HttpStatus.OK.value(), "User created successfully", userServiceImpl.createUser(userCreationRequest)));
    }

    @Operation(summary = "Change status of user", description = "This endpoint is used to change the status of a user.")
    @PatchMapping("/{id}/update-status")
    public ResponseEntity<ApiBaseResponse> changeUserStatus(@PathVariable Long id){
        boolean status = userServiceImpl.changeUserStatus(id);

        return ResponseEntity.ok().body(
                new ApiSuccessResponse<>(HttpStatus.ACCEPTED.value(), status ? "User has been activated!" : "User has ben deactivated!", status));
    }
}
