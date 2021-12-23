package com.seasongg.users.services;

import com.seasongg.users.Reguser;
import com.seasongg.users.ReguserRepository;
import com.seasongg.users.models.RegistrationRequest;
import com.seasongg.users.models.RegistrationResponse;
import com.seasongg.users.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class UserRegisterService {

    @Autowired
    private ReguserRepository reguserRepository;

    @Autowired
    private ObjectProvider<UserBuilder> userBuilders;

    @Autowired
    private UserUtils userUtils;

    private final Logger LOG = LoggerFactory.getLogger(UserRegisterService.class);

    public static final String ERROR_ALREADY_AUTHENTICATED = "Please log out before trying to register a new account.";
    public static final String UNKNOWN_REGISTRATION_ERROR = "Sorry, something went wrong on our end. Please try again.";

    public RegistrationResponse registerUser(RegistrationRequest registrationRequest) {

        if (userUtils.isUserAuthenticated()) {
            return new RegistrationResponse(1, ERROR_ALREADY_AUTHENTICATED);
        }

        Reguser regUser = new Reguser();

        try {

            UserBuilder userBuilder = userBuilders.getObject(regUser);

            userBuilder.buildUsername(registrationRequest.getUsername());

            userBuilder.buildPassword(registrationRequest.getPassword(), registrationRequest.getPasswordVerify());

        } catch (UserBuilder.UserBuilderException e) {

            LOG.info("Error when user tried creating account with username: {}. Error is: {}",
                    registrationRequest.getUsername(), e.getMessage());

            return new RegistrationResponse(1, e.getMessage());

        } catch (Exception e) {

            LOG.error("Unexpected error when user tried creating account with username: {}.",
                    registrationRequest.getUsername(), e);

            return new RegistrationResponse(7, UNKNOWN_REGISTRATION_ERROR);

        }

        Timestamp registrationTime = new Timestamp(System.currentTimeMillis());

        regUser.setRegistrationTime(registrationTime);

        reguserRepository.save(regUser);

        return new RegistrationResponse(regUser.getUsername());

    }

}
