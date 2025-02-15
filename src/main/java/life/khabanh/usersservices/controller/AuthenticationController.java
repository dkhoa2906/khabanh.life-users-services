package life.khabanh.usersservices.controller;

import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import life.khabanh.usersservices.dto.request.*;
import life.khabanh.usersservices.dto.response.*;
import life.khabanh.usersservices.service.AuthenticationService;
import life.khabanh.usersservices.wrapper.AuthenticationResponseWrapper;
import life.khabanh.usersservices.wrapper.IntrospectResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication Controller", description = "Handles user authentication, token management, and session control")
public class AuthenticationController {

    AuthenticationService authenticationService;



    @Operation(summary = "Authenticate user", description = "Validates user credentials and returns an access token and a refresh token.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Authentication successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponseWrapper.class))),
            @ApiResponse(
                    responseCode = "401", description = "Invalid credentials",
                    content = @Content(
                            schema = @Schema(example = "{ \"code\": 4102, \"message\": \"Incorrect password\" }"))),
            @ApiResponse(
                    responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"code\": 4101, \"message\": \"User does not exist\" }"))),
            @ApiResponse(
                    responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"code\": 5001, \"message\": \"Missing required field(s)\" }")))
    })
    @PostMapping("/token")
    public ApiFormResponse<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(request);
        return ApiFormResponse.<AuthenticationResponse>builder().result(response).build();
    }



    @Operation(summary = "Introspect Token", description = "Checks the validity of an access token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token is valid",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IntrospectResponseWrapper.class))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"code\": 4103, \"message\": \"Unauthenticated\" }"))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"code\": 5001, \"message\": \"Missing request body\" }")))
    })
    @PostMapping("/introspect")
    public ApiFormResponse<IntrospectResponse> introspect(
            @Valid @RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        IntrospectResponse response = authenticationService.introspect(request);
        return ApiFormResponse.<IntrospectResponse>builder().result(response).build();
    }



    @Operation(
            summary = "Refresh Token",
            description = "Generates a new access token using a refresh token. Old access token will become invalid after this.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token successfully refreshed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponseWrapper.class))),
            @ApiResponse(responseCode = "401", description = "Invalid/Expired refresh token or old access token found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"code\": 4103, \"message\": \"Unauthenticated\" }"))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"code\": 5001, \"message\": \"Missing required field(s)\" }")))
    })
    @PostMapping("/refresh")
    public ApiFormResponse<AuthenticationResponse> refresh(
            @Valid @RequestBody TokenRefreshRequest request) throws ParseException, JOSEException {
        AuthenticationResponse response = authenticationService.refreshToken(request);
        return ApiFormResponse.<AuthenticationResponse>builder().result(response).build();
    }

    @Operation(summary = "Log Out", description = "Invalidates the provided access token and refreshToken then logs the user out.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation =ApiFormResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"code\": 4103, \"message\": \"Unauthenticated\" }")))
    })
    @PostMapping("/logout")
    public ApiFormResponse<Void> logout(@Valid @RequestBody LogOutRequest request)
            throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiFormResponse.<Void>builder().build();
    }
}