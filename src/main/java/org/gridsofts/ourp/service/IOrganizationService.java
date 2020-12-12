package org.gridsofts.ourp.service;

import org.gridsofts.ourp.model.Organization;

/**
 * 组织机构信息服务接口
 * 
 * @author lei
 *
 * @param <T> 组织机构信息类；继承自 org.gridsofts.ourp.model.Organization
 */
public interface IOrganizationService<T extends Organization> extends IEntityService<T, String> {
}
