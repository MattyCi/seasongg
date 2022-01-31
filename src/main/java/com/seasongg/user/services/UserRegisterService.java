package com.seasongg.user.services;

import com.seasongg.common.SggService;
import com.seasongg.user.models.Reguser;
import com.seasongg.user.models.RegistrationRequest;
import com.seasongg.user.models.RegistrationResponse;
import com.seasongg.user.models.UserBuilder;
import com.seasongg.user.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegisterService extends SggService {

    @Autowired
    private ReguserRepository reguserRepository;

    @Autowired
    private ObjectProvider<UserBuilder> userBuilders;

    @Autowired
    private UserUtils userUtils;

    private final Logger LOG = LoggerFactory.getLogger(UserRegisterService.class);

    private static final String ERROR_ALREADY_AUTHENTICATED = "Please log out before trying to register a new account.";

    @Transactional(rollbackFor = Exception.class)
    public RegistrationResponse registerUser(RegistrationRequest registrationRequest) throws RegistrationException {

        if (userUtils.isUserAuthenticated()) {
            throw new RegistrationException(ERROR_ALREADY_AUTHENTICATED);
        }

		Reguser reguser;

        try {

			reguser = userBuilders.getObject(
				registrationRequest.getUsername(),
				registrationRequest.getPassword(),
				registrationRequest.getPasswordVerify()
			).build();

        } catch (BeanCreationException e) {

			if (e.getCause() instanceof IllegalArgumentException) {
				LOG.info("Error when user tried creating account with username: {}. Error is: {}",
						registrationRequest.getUsername(), e.getMessage());
				throw new RegistrationException(e.getCause().getMessage());
			}
			throw e;

        }

        reguserRepository.save(reguser);

        return new RegistrationResponse(reguser.getUsername());

    }

    public static class RegistrationException extends SggServiceException {
        public RegistrationException(String message) {
            super(message);
        }
    }

}
