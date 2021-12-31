package com.seasongg.user.services.permissions;

import com.seasongg.user.models.permissions.Permission;
import org.springframework.data.repository.CrudRepository;

public interface PermissionRepository extends CrudRepository<Permission, Integer> {

}
