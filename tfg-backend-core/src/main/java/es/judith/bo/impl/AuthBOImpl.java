package es.judith.bo.impl;

import es.judith.bo.AuthBO;
import es.judith.domain.User;
import es.judith.domain.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.io.Serializable;

@Service
public class AuthBOImpl implements AuthBO, Serializable {
    @Serial
    private static final long serialVersionUID = 8489438578421447527L;
    private final transient BCryptPasswordEncoder passwordEncoder;
    {
        new BCryptPasswordEncoder(10);
    }
    private final transient AuthenticationManager authenticationManager;
    private static final Logger LOG = LoggerFactory.getLogger(AuthBOImpl.class);

    public AuthBOImpl(BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public User getCurrentUser() {
        LOG.debug("AuthBOImpl: getCurrentUser");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getUser();
    }
    @Override
    public boolean verifyCredentials(String email, String password) {
        LOG.debug("AuthBOImpl: login");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        return authentication.isAuthenticated();
    }
    @Override
    public String encryptPassword(String password) {
        LOG.debug("AuthBOImpl: encryptPassword");
        return passwordEncoder.encode(password);
    }
}
