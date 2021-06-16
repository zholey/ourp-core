package org.gridsofts.ourp.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Stream;

import org.gridsofts.halo.annotation.Column;
import org.gridsofts.halo.annotation.Table;
import org.gridsofts.halo.crud.IEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 角色
 * 
 * @author lei
 */
@ApiModel(description = "角色信息实体类")
@Table(value = "OURP_ROLE", primaryKey = { "roleId" }, autoGenerateKeys = false)
public class Role implements Serializable, IEntity<String> {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("角色ID；主键")
	@Column("ROLE_ID")
	private String roleId;

	@ApiModelProperty("角色名称")
	@Column("ROLE_NAME")
	private String roleName;

	@ApiModelProperty("授权权限编码串；以“,”分隔")
	@Column("AUTH_PERMISSIONS")
	private String authPermissions;

	@ApiModelProperty("授权机构ID串；以“,”分隔")
	@Column("AUTH_ORGANIZATIONS")
	private String authOrganizations;

	@Override
	@ApiModelProperty(hidden = true)
	public String getPK() {
		return getRoleId();
	}

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
