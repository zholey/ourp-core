package org.gridsofts.ourp.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.gridsofts.halo.annotation.Table;
import org.gridsofts.halo.bean.Condition;
import org.gridsofts.ourp.dao.HaloDAO;
import org.gridsofts.ourp.exception.SrvException;
import org.gridsofts.ourp.model.User;
import org.gridsofts.ourp.service.IEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实体信息“增删改查”服务的抽象基类
 * 
 * @author lei
 */
public abstract class AbstractEntityService<T, K> implements IEntityService<T, K> {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name = "ourpHaloDAO")
	protected HaloDAO haloDAO;

	private Class<T> beanClass;

	public AbstractEntityService(Class<T> beanClass) {
		this.beanClass = beanClass;
	}

	@Override
	public boolean create(T bean) throws SrvException {
		return haloDAO.save(beanClass, bean) != null;
	}

	@Override
	public boolean update(T bean) throws SrvException {
		return haloDAO.update(bean) == 1;
	}

	@Override
	public T find(K pkid) throws SrvException {
		return haloDAO.find(beanClass, pkid);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(K... pkids) throws SrvException {

		if (pkids == null) {
			return null;
		}

		return Arrays.stream(pkids).map(pkid -> {
			return haloDAO.find(beanClass, pkid);
		}).collect(Collectors.toList());
	}

	@Override
	public List<T> list() throws SrvException {
		return haloDAO.list(beanClass);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean remove(K... pkids) throws SrvException {

		if (pkids != null) {
			int result = 0;
			
			List<K> pkidList = Arrays.asList(pkids);
			for (K pkid : pkidList) {
				try {
					result += haloDAO.delete(find(pkid));
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
				}
			}

			return result == pkidList.size();
		}
		return false;
	}

	@Override
	public List<T> query(Condition condition) {
		Table tableAnnotation = beanClass.getAnnotation(Table.class);
		if (tableAnnotation == null) {
			throw new NullPointerException("tableAnnotation is null");
		}

		StringBuffer conditionSql = new StringBuffer("SELECT T.* FROM " + tableAnnotation.value() + " T");
		List<Object> queryParams = new ArrayList<>();

		// 拼接查询条件
		if (condition != null) {
			conditionSql.append(getConditionSQL(User.class, condition, queryParams));
		}

		return haloDAO.executeQuery(beanClass, conditionSql.toString(), queryParams);
	}
}
