package org.gridsofts.ourp.service;

import org.gridsofts.ourp.model.Role;

/**
 * 角色信息服务接口
 * 
 * @author lei
 *
 * @param <T> 角色信息类；继承自 org.gridsofts.ourp.model.Role
 */
public interface IRoleService<T extends Role> extends IEntityService<T, String> {
}
