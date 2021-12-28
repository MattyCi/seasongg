package com.seasongg.season.services;

import com.seasongg.season.models.Season;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;

public interface SeasonRepository extends CrudRepository<Season, BigInteger> {

}
