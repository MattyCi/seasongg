package com.seasongg.season.services;

import com.seasongg.season.models.Season;
import com.seasongg.season.models.SeasonResponse;
import com.seasongg.user.models.permissions.Permission;
import com.seasongg.user.services.permissions.PermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Service
public class SeasonDeleteService extends SeasonService {

    @Autowired
    PermissionRepository permissionRepository;

    private final Logger LOG = LoggerFactory.getLogger(SeasonDeleteService.class);

    @Transactional(rollbackFor = Exception.class)
    public SeasonResponse deleteSeason(BigInteger seasonId) throws Exception {

        validateSeasonId(seasonId);

        Season seasonToDelete = findSeason(seasonId);
        seasonRepository.delete(seasonToDelete);

        deleteAllPermissionsForSeason(seasonId);

        return new SeasonResponse(seasonToDelete);

    }

    void deleteAllPermissionsForSeason(BigInteger seasonId) {

        List<Permission> allPermissionsForSeason = permissionRepository.getAllPermissionsForSeason(seasonId);

        for (Permission permissionForSeason : allPermissionsForSeason) {
            LOG.debug("deleting the following permission - {}", permissionForSeason.getPermValue());
        }

        permissionRepository.deleteAll(allPermissionsForSeason);

    }

}
