package org.gridsofts.ourp.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Stream;

import org.gridsofts.halo.annotation.Column;
import org.gridsofts.halo.annotation.Table;

/**
 * 角色
 * 
 * @author lei
 */
@Table(value = "OURP_ROLE", primaryKey = { "roleId" }, autoGenerateKeys = false)
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column("ROLE_ID")
	private String roleId; // 角色ID；主键

	@Column("ROLE_NAME")
	private String roleName; // 角色名称

	@Column("AUTH_PERMISSIONS")
	private String authPermissions; // 授权权限编码串；以“,”分隔

	@Column("AUTH_ORGANIZATIONS")
	private String authOrganizations; // 授权机构ID串；以“,”分隔

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * 检查是否包含指定的权限编码
	 * 
	 * @param code
	 * @return
	 */
	public boolean containsPermission(String code) {
		
		if (code == null || getAuthPermissions() == null) {
			return false;
		}

		Stream<String> permissionCodeStream = Arrays.stream(getAuthPermissions().split("\\s*\\,+\\s*"));
		return permissionCodeStream.filter(eachId -> code.equals(eachId)).findAny().isPresent();
	}

	public String getAuthPermissions() {
		return authPermissions;
	}

	public void setAuthPermissions(String authPermissions) {
		this.authPermissions = authPermissions;
	}

	/**
	 * 检查是否包含指定的机构ID
	 * 
	 * @param orgId
	 * @return
	 */
	public boolean containsOrganization(String orgId) {
		
		if (orgId == null || getAuthOrganizations() == null) {
			return false;
		}

		Stream<String> orgIdStream = Arrays.stream(getAuthOrganizations().split("\\s*\\,+\\s*"));
		return orgIdStream.filter(eachId -> orgId.equals(eachId)).findAny().isPresent();
	}

	public String getAuthOrganizations() {
		return authOrganizations;
	}

	public void setAuthOrganizations(String authOrganizations) {
		this.authOrganizations = authOrganizations;
	}
	
	@Override
	public String toString() {
		return getRoleName();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null || !(obj instanceof Role)) {
			return false;
		}
		
		Role target = (Role) obj;
		
		return getRoleId() != null && getRoleId().equals(target.getRoleId());
	}
}
