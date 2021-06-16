package org.gridsofts.ourp.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.gridsofts.halo.crud.service.AbstractCRUDService;
import org.gridsofts.ourp.dao.HaloDAO;
import org.gridsofts.ourp.model.User;
import org.gridsofts.ourp.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * 用户信息服务实现类
 * 
 * @author lei
 */
@Service("ourpUserService")
public class UserServiceImpl extends AbstractCRUDService<User, String> implements IUserService<User> {

	public UserServiceImpl() {
		super(User.class);
	}
	
	@Resource(name = "ourpHaloDAO")
	private HaloDAO haloDAO;
	
	@PostConstruct
	public void initDAO() {
		super.setSuperDAO(haloDAO);
	}
}
