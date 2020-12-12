package org.gridsofts.ourp.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.gridsofts.halo.SuperDAO;
import org.gridsofts.halo.exception.ConnectionException;
import org.gridsofts.halo.itf.IConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 数据访问对象
 * 
 * @author lei
 */
@Component("ourpHaloDAO")
public class HaloDAO extends SuperDAO {
	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name = "dataSource")
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * DAO实例化后，要进行初始化
	 */
	@PostConstruct
	public void init() {
		setFactory(new IConnectionFactory() {

			@Override
			public Connection getConnection() throws ConnectionException {
				
				if (dataSource == null) {
					throw new NullPointerException("dataSource is null");
				}
				
				try {
					Connection conn = dataSource.getConnection();
					
					// 设置 autoCommit = true
					if (!conn.getAutoCommit()) {
						conn.setAutoCommit(true);
					}
					
					return conn;
				} catch (SQLException e) {
					throw new ConnectionException(e.getMessage());
				}
			}

			@Override
			public void release(Connection conn) {

				try {
					conn.close();
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
				}
			}});
	}
}
