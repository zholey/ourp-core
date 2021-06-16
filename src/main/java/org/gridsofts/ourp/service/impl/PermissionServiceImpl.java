package org.gridsofts.ourp.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.gridsofts.halo.crud.service.AbstractCRUDService;
import org.gridsofts.ourp.dao.HaloDAO;
import org.gridsofts.ourp.model.Permission;
import org.gridsofts.ourp.service.IPermissionService;
import org.springframework.stereotype.Service;

/**
 * 权限信息服务实现类
 * 
 * @author lei
 */
@Service("ourpPermissionService")
public class PermissionServiceImpl extends AbstractCRUDService<Permission, String>
		implements IPermissionService<Permission> {

	public PermissionServiceImpl() {
		super(Permission.class);
	}
	
	@Resource(name = "ourpHaloDAO")
	private HaloDAO haloDAO;
	
	@PostConstruct
	public void initDAO() {
		super.setSuperDAO(haloDAO);
	}
}
