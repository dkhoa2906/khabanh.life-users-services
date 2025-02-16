package life.khabanh.usersservices.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import life.khabanh.usersservices.dto.request.UserCreationRequest;
import life.khabanh.usersservices.dto.request.UserUpdateRequest;
import life.khabanh.usersservices.dto.response.ApiFormResponse;
import life.khabanh.usersservices.dto.response.UserResponse;
import life.khabanh.usersservices.service.UserService;
import life.khabanh.usersservices.wrapper.UserResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User Controller", description = "Handles user management operations such as creation, retrieval, update, and deletion.")
public class UserController {
    UserService userService;



    @Operation(summary = "Create a new user", description = "Registers a new user in the system.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "User created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(
                    responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"code\": 5001, \"message\": \"Missing required field(s)\" }"))),
            @ApiResponse(
                    responseCode = "409", description = "User already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"code\": 4104, \"message\": \"User already exists\" }")))
    })
    @PostMapping("/new")
    public ApiFormResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiFormResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }



    @Operation(summary = "Get current user details", description = "Retrieves the authenticated user's information by access token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseWrapper.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"code\": 4101, \"message\": \"User does not exist\" }"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/me")
    public ApiFormResponse<UserResponse> getCurrentUser() {
        return ApiFormResponse.<UserResponse>builder()
                .result(userService.getCurrentUser())
                .build();
    }


    @Operation(summary = "Get user details", description = "Retrieves user information based on the user ID. The getter's access token must match the target user.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(
                    responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"code\": 4101, \"message\": \"User does not exist\" }"))),
            @ApiResponse(
                    responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{userId}")
    public ApiFormResponse<UserResponse> getUser(@PathVariable("userId") String id) {
        return ApiFormResponse.<UserResponse>builder()
                .result(userService.getUser(id))
                .build();
    }

    @Operation(summary = "Update user details", description = "Updates user information for the given user ID.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(
                    responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"code\": 4101, \"message\": \"User does not exist\" }"))),
            @ApiResponse(
                    responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{userId}")
    public ApiFormResponse<UserResponse> updateUser(@PathVariable("userId") String id, @RequestBody @Valid UserUpdateRequest request) {
        return ApiFormResponse.<UserResponse>builder()
                .result(userService.updateUser(id, request))
                .build();
    }

    @Operation(summary = "Delete a user", description = "Removes a user from the system based on the user ID.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "User deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(
                    responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"code\": 4101, \"message\": \"User does not exist\" }"))),
            @ApiResponse(
                    responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{userId}")
    public ApiFormResponse<UserResponse> deleteUser(@PathVariable("userId") String id) {
        return ApiFormResponse.<UserResponse>builder()
                .result(userService.deleteUser(id))
                .build();
    }

    @Operation(summary = "Get all users", description = "Retrieves all users. Only accessible by admins.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "List of users retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseWrapper.class))),
            @ApiResponse(
                    responseCode = "403", description = "Forbidden - Admin access required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"code\": 403, \"message\": \"Access denied\" }"))),
            @ApiResponse(
                    responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/all")
    public ApiFormResponse<List<UserResponse>> getAllUsers() {
        return ApiFormResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }
}