package com.seasongg.users.services;

import com.seasongg.users.Reguser;
import com.seasongg.users.ReguserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
