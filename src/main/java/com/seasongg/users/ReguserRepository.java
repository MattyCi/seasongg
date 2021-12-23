package com.seasongg.users;

import org.springframework.data.repository.CrudRepository;

public interface ReguserRepository extends CrudRepository<Reguser, Long> {

    Reguser findByUsername(String username);

    Reguser findByUsernameIgnoreCase(String username);

}
