package com.seasongg.user.services.permissions;

import com.seasongg.user.models.permissions.UserPermission;
import org.springframework.data.repository.CrudRepository;

public interface UserPermissionRepository extends CrudRepository<UserPermission, Integer> {

}
