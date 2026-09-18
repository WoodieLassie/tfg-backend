package es.judith.bo;

import es.judith.domain.User;

public interface AuthBO {
    User getCurrentUser();
    boolean verifyCredentials(String email, String password);
    String encryptPassword(String password);
}
