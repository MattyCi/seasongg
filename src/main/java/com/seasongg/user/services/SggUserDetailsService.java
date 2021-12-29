package com.seasongg.user.services;

import com.seasongg.user.models.Reguser;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SggUserDetailsService implements UserDetailsService {

    @Autowired
    private ReguserRepository reguserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Reguser reguser = reguserRepository.findByUsernameIgnoreCase(username);

        if (reguser == null) {
            throw new UsernameNotFoundException(username);
        } else {
            Hibernate.initialize(reguser.getUserPermissions());
        }

        return reguser;
    }

}
