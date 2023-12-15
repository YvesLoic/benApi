package com.app.benevole.security;

import com.app.benevole.model.User;
import com.app.benevole.service.UserService;
import com.app.benevole.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                        String.format("User with email: << %s >> not found!", email)));
        return new CustomUserDetails(user);
    }

    /**
     * User Details not used yet
     * @param userId
     * @return the authenticated user
     */
    public UserDetails loadUserByUserId(String userId) {
        User user = userService.findById(UUID.fromString(userId));
        return new CustomUserDetails(user);
    }
}
