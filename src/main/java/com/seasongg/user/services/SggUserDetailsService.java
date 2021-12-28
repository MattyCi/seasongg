package com.seasongg.user.services;

import com.seasongg.user.models.Reguser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SggUserDetailsService implements UserDetailsService {

    @Autowired
    private ReguserRepository reguserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Reguser reguser = reguserRepository.findByUsernameIgnoreCase(username);

        if (reguser == null) {
            throw new UsernameNotFoundException(username);
        }

        return reguser;
    }

}
