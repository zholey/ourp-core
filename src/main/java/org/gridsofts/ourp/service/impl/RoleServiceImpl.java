package org.gridsofts.ourp.service.impl;

import javax.annotation.Resource;

import org.gridsofts.halo.crud.service.AbstractCRUDService;
import org.gridsofts.ourp.dao.HaloDAO;
import org.gridsofts.ourp.model.Role;
import org.gridsofts.ourp.service.IRoleService;
import org.springframework.stereotype.Service;

/**
 * 角色信息服务实现类
 * 
 * @author lei
 */
@Service("ourpRoleService")
public class RoleServiceImpl extends AbstractCRUDService<Role, String> implements IRoleService<Role> {

	@Resource(name = "ourpHaloDAO")
	private HaloDAO haloDAO;
	
	@Override
	public void setSuperDAO() {
		super.setSuperDAO(haloDAO);
	}
}
