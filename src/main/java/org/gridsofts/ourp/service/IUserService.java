package org.gridsofts.ourp.service;

import org.gridsofts.ourp.model.User;

/**
 * 用户信息服务接口
 * 
 * @author lei
 *
 * @param <T> 用户信息类；继承自 org.gridsofts.ourp.model.User
 */
public interface IUserService<T extends User> extends IEntityService<T, String> {
}
