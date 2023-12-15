package com.app.benevole.controller;

import com.app.benevole.helper.ApiError;
import com.app.benevole.helper.ApiFormat;
import com.app.benevole.jwt.JwtUtils;
import com.app.benevole.model.User;
import com.app.benevole.request.UserLogin;
import com.app.benevole.request.UserRequest;
import com.app.benevole.response.LoginResponse;
import com.app.benevole.response.UserResponse;
import com.app.benevole.security.CustomUserDetails;
import com.app.benevole.service.RoleService;
import com.app.benevole.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    @Operation(summary = "Register new user in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New User have been created successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "4**", description = "An error occurred during data validation.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    }
            )
    })
    public ResponseEntity<?> userSignup(@Valid @RequestBody UserRequest request) {
        User registered = userService.create(request);
        return ResponseEntity.ok(
                new ApiFormat(
                        new UserResponse(registered)
                )
        );
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate an user in system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "4**", description = "An error occurred while login processed.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    }
            )
    })
    public ResponseEntity<?> userLogin(@Valid @RequestBody UserLogin request) {
        String email = request.getEmail(); // get user email
        String password = request.getPassword(); // get user pass
        // instantiate authentication holder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        // user authentication class for authenticate current user
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // set the authenticate user ins the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // get the current auth user from the security context
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        // here, we can implement eh logic to check
        // if this account is actually use by another user

        // create user accessToken
        String accessToken = jwtUtils.generateUserAccessToken(email);
        // get rules and permissions for auth user
        List<String> authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // create user auth object
        LoginResponse authUser = LoginResponse.builder()
                .id(userDetails.getId()).email(userDetails.getEmail())
                .username(userDetails.getUsername()).token(accessToken)
                .rulesAndPermissions(authorities)
                .build();

        // return authentication object
        return ResponseEntity.ok(authUser);
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged out successfully")
    })
    public ResponseEntity<?> userLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // get the auth user from security context
        // auth.getPrincipal() and delete all existing token from DB

        // logout the user
        new SecurityContextLogoutHandler().logout(request, response, auth);
        return ResponseEntity.noContent().build();
    }
}
