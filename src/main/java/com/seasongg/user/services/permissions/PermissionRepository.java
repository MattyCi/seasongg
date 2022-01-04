package com.seasongg.user.services.permissions;

import com.seasongg.user.models.permissions.Permission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface PermissionRepository extends CrudRepository<Permission, Integer> {

    @Query("SELECT p FROM Permission p WHERE p.permValue like CONCAT('season\\:', '%\\:', :seasonId)")
    List<Permission> getAllPermissionsForSeason(@Param("seasonId") BigInteger seasonId);

}
