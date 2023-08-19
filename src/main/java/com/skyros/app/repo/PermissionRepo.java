package com.skyros.app.repo;


import com.skyros.app.model.Permission;
import com.skyros.app.model.PermissionGroup;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepo extends BaseRepo<Permission> {

    List<Permission> findPermissionByPermissionGroup(PermissionGroup permissionGroup);

}
