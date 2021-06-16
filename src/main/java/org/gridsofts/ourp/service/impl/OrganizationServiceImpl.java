package org.gridsofts.ourp.service.impl;

import javax.annotation.Resource;

import org.gridsofts.halo.crud.service.AbstractCRUDService;
import org.gridsofts.ourp.dao.OurpDAO;
import org.gridsofts.ourp.model.Organization;
import org.gridsofts.ourp.service.IOrganizationService;
import org.springframework.stereotype.Service;

/**
 * 组织机构信息服务实现类
 * 
 * @author lei
 */
@Service("_ourpOrganizationService")
public class OrganizationServiceImpl extends AbstractCRUDService<Organization, String>
		implements IOrganizationService<Organization> {

	@Resource(name = "_ourpDAO")
	private OurpDAO haloDAO;
	
	@Override
	public void setSuperDAO() {
		super.setSuperDAO(haloDAO);
	}
}
