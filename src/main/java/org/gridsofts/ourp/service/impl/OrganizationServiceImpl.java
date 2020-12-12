package org.gridsofts.ourp.service.impl;

import org.gridsofts.ourp.model.Organization;
import org.gridsofts.ourp.service.IOrganizationService;
import org.springframework.stereotype.Service;

/**
 * 组织机构信息服务实现类
 * 
 * @author lei
 */
@Service("ourpOrganizationService")
public class OrganizationServiceImpl extends AbstractEntityService<Organization, String>
		implements IOrganizationService<Organization> {

	public OrganizationServiceImpl() {
		super(Organization.class);
	}
}
