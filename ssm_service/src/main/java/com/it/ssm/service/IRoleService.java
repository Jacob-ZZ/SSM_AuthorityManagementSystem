package com.it.ssm.service;

import com.it.ssm.domain.Permission;
import com.it.ssm.domain.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface IRoleService {

    public List<Role> findAll() throws Exception;

    void save(Role role) throws Exception;

    Role findById(String roleId) throws Exception;

    void deleteRoleById(String roleId) throws Exception;

    List<Permission> findOtherPermissions(String roleId) throws Exception;


    void addPermissionToRole(String roleId, String[] permissionIds) throws Exception;
}
