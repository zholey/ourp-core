package org.gridsofts.ourp.service.impl;

import org.gridsofts.ourp.model.User;
import org.gridsofts.ourp.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * 用户信息服务实现类
 * 
 * @author lei
 */
@Service("ourpUserService")
public class UserServiceImpl extends AbstractEntityService<User, String> implements IUserService<User> {

	public UserServiceImpl() {
		super(User.class);
	}
}
