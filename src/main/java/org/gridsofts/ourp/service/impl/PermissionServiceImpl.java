package org.gridsofts.ourp.service.impl;

import javax.annotation.Resource;

import org.gridsofts.halo.crud.service.AbstractCRUDService;
import org.gridsofts.ourp.dao.OurpDAO;
import org.gridsofts.ourp.model.Permission;
import org.gridsofts.ourp.service.IPermissionService;
import org.springframework.stereotype.Service;

/**
 * 权限信息服务实现类
 * 
 * @author lei
 */
@Service("_ourpPermissionService")
public class PermissionServiceImpl extends AbstractCRUDService<Permission, String>
		implements IPermissionService<Permission> {

	@Resource(name = "_ourpDAO")
	private OurpDAO haloDAO;
	
	@Override
	public void setSuperDAO() {
		super.setSuperDAO(haloDAO);
	}
}
