package es.judith.bo.impl;

import es.judith.bo.AuthBO;
import es.judith.domain.User;
import es.judith.domain.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.io.Serializable;

@Service
public class AuthBOImpl implements AuthBO, Serializable {
    @Serial
    private static final long serialVersionUID = 8489438578421447527L;
    private static final Logger LOG = LoggerFactory.getLogger(AuthBOImpl.class);

    @Override
    public User getCurrentUser() {
        LOG.debug("AuthBOImpl: getCurrentUser");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getUser();
    }
}
