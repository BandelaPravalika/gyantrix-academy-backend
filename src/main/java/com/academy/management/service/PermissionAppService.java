package com.academy.management.service;

import com.academy.management.dao.PermissionAppDao;
import com.academy.management.model.PermissionApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class PermissionAppService {

    @Autowired
    private PermissionAppDao dao;

    public PermissionApp createPermission(PermissionApp permission) throws SQLException {
        return dao.savePermission(permission);
    }

    public PermissionApp updatePermission(Long id, PermissionApp permission) throws SQLException {
        return dao.updatePermission(id, permission);
    }

    public PermissionApp patchPermission(Long id, PermissionApp permission) throws SQLException {
        return dao.patchPermission(id, permission);
    }

    public boolean deletePermission(Long id) throws SQLException {
        return dao.deletePermission(id);
    }

    public PermissionApp getPermissionById(Long id) throws SQLException {
        return dao.getPermissionById(id);
    }

    public List<PermissionApp> getAllPermissions() throws SQLException {
        return dao.getAllPermissions();
    }
}
