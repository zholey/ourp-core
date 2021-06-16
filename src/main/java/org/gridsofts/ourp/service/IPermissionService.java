package org.gridsofts.ourp.service;

import org.gridsofts.halo.crud.IEntityService;
import org.gridsofts.ourp.model.Permission;

/**
 * 权限信息服务接口
 * 
 * @author lei
 *
 * @param <T> 权限信息类；继承自 org.gridsofts.ourp.model.Permission
 */
public interface IPermissionService<T extends Permission> extends IEntityService<T, String> {
}
