package com.seasongg.user.services;

import com.seasongg.user.models.Reguser;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;

public interface ReguserRepository extends CrudRepository<Reguser, BigInteger> {

    Reguser findByUsername(String username);

    Reguser findByUsernameIgnoreCase(String username);

}
