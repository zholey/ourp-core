package org.gridsofts.ourp.service.impl;

import org.gridsofts.ourp.model.Role;
import org.gridsofts.ourp.service.IRoleService;
import org.springframework.stereotype.Service;

/**
 * 角色信息服务实现类
 * 
 * @author lei
 */
@Service("ourpRoleService")
public class RoleServiceImpl extends AbstractEntityService<Role, String> implements IRoleService<Role> {

	public RoleServiceImpl() {
		super(Role.class);
	}
}
