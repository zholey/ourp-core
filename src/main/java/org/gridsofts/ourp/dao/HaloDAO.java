package org.gridsofts.ourp.dao;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
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
			}
		});

		// 自动建表
		String tablesPolicy = System.getProperty("ourp.tables.autocreate", "true");
		if ("true".equalsIgnoreCase(tablesPolicy)) {
			initTables();
		}
	}

	public void initTables() {

		if (logger.isInfoEnabled()) {
			logger.info("OURP 自动建表...");
		}

		try (BufferedInputStream inStream = new BufferedInputStream(
				getClass().getResourceAsStream("/org/gridsofts/ourp/model/init-tables.sql"));
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();) {

			byte[] buffer = new byte[4096];
			int len = 0;
			while ((len = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, len);
			}
			outStream.flush();

			String createTablesSql = new String(outStream.toByteArray(), Charset.defaultCharset());

			// 执行建表语句
			String[] sqlAry = createTablesSql.split("\\s*;\\s*");
			if (sqlAry != null && sqlAry.length > 0) {
				for (int i = 0; i < sqlAry.length; i++) {
					executeUpdate(sqlAry[i]);

					if (logger.isDebugEnabled()) {
						logger.debug(sqlAry[i]);
					}
				}
			}

			if (logger.isInfoEnabled()) {
				logger.info("OURP 数据表创建完毕");
			}
		} catch (Throwable e) {
			logger.error("OURP 数据表初始化异常", e);
		}
	}
}
