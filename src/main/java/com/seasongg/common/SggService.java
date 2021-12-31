package com.seasongg.common;

import com.seasongg.user.models.Reguser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SggService {

    protected static final int SUCCESS = 0;
    public static final int APPLICATION_ERROR = 1;
    public static final int UNKNOWN_ERROR = 7;

    public static final String UNKNOWN_ERROR_TEXT = "Sorry, something went wrong on our end. Please try again.";
    private static final String NOT_AUTHENTICATED_ERROR = "Sorry, you must be logged in to perform this action";

    public String getUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public Reguser getExecutingUser() throws SggServiceException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return (Reguser) principal;
        } else {
            throw new SggServiceException(NOT_AUTHENTICATED_ERROR);
        }
    }

    public static class SggServiceException extends Exception {
        public SggServiceException(String message) {
            super(message);
        }
    }

}
