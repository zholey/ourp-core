package org.gridsofts.ourp.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.gridsofts.halo.bean.Condition;
import org.gridsofts.halo.util.BeanUtil;
import org.gridsofts.ourp.exception.SrvException;

/**
 * 实体对象服务接口
 * 
 * @author lei
 */
public interface IEntityService<T, K> {

	/**
	 * 保存（新增）实体
	 * 
	 * @param bean
	 */
	public boolean create(T bean) throws SrvException;

	/**
	 * 保存（更新）实体
	 * 
	 * @param bean
	 */
	public boolean update(T bean) throws SrvException;

	/**
	 * 根据主键查找实体
	 * 
	 * @param pkid 主键ID
	 * @return
	 */
	public T find(K pkid) throws SrvException;

	/**
	 * 根据主键查找实体
	 * 
	 * @param pkids 主键ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll(K... pkids) throws SrvException;

	/**
	 * 获取全部实体
	 * 
	 * @return
	 */
	public List<T> list() throws SrvException;

	/**
	 * 根据条件查询
	 * 
	 * @param condition
	 * @return
	 */
	public List<T> query(Condition condition);

	/**
	 * 移除指定主键的实体
	 * 
	 * @param pkids 主键ID
	 */
	@SuppressWarnings("unchecked")
	public boolean remove(K... pkids) throws SrvException;

	/***************************************************/
	/** static function ********************************/
	/***************************************************/

	/**
	 * 根据给定的condition构造查询SQL
	 * 
	 * @param entityClass 实体类型
	 * @param condition
	 * @param outParams   调用方传入一个空的list，此方法进行填充
	 * @return
	 */
	public default String getConditionSQL(final Class<?> entityClass, final Condition condition,
			final List<Object> outParams) {
		List<String> sectionSql = new ArrayList<>();

		// 拼接查询条件
		if (condition != null) {
			Iterator<Condition.Param> iteCondition = condition.iterator();
			while (iteCondition.hasNext()) {
				Condition.Param param = iteCondition.next();

				StringBuffer str = new StringBuffer(BeanUtil.getColumnName(param.getName(), entityClass));
				if (Condition.Param.Association.Exact.equals(param.getAssociation())) {
					str.append(" = ? ");
				} else {
					str.append(" LIKE '%?%' ");
				}
				sectionSql.add(str.toString());

				outParams.add(param.getValue());
			}
		}

		if (!sectionSql.isEmpty()) {
			return sectionSql.stream().collect(Collectors.joining(" AND ", " WHERE ", ""));
		}

		return "";
	}
}
