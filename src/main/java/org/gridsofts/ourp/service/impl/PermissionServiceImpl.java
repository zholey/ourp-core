package org.gridsofts.ourp.service.impl;

import org.gridsofts.ourp.model.Permission;
import org.gridsofts.ourp.service.IPermissionService;
import org.springframework.stereotype.Service;

/**
 * 权限信息服务实现类
 * 
 * @author lei
 */
@Service("ourpPermissionService")
public class PermissionServiceImpl extends AbstractEntityService<Permission, String>
		implements IPermissionService<Permission> {

	public PermissionServiceImpl() {
		super(Permission.class);
	}
}
