package org.gridsofts.ourp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.gridsofts.halo.annotation.Column;
import org.gridsofts.halo.annotation.Table;
import org.springframework.beans.BeanUtils;

/**
 * 权限
 * 
 * @author lei
 */
@Table(value = "OURP_PERMISSION", primaryKey = { "code" }, autoGenerateKeys = false)
public class Permission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column("CODE")
	private String code; // 权限编码；主键

	@Column("NAME")
	private String name; // 权限名称

	@Column("PRNT_CODE")
	private String prntCode; // 父级权限编码

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrntCode() {
		return prntCode;
	}

	public void setPrntCode(String prntCode) {
		this.prntCode = prntCode;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null || !(obj instanceof Permission)) {
			return false;
		}
		
		Permission target = (Permission) obj;
		
		return getCode() != null && getCode().equals(target.getCode());
	}
	
	/**
	 * 权限树节点
	 * 
	 * @author lei
	 */
	public static class TreeNode extends Permission {
		private static final long serialVersionUID = 1L;

		private List<TreeNode> children = new ArrayList<>(); // 子节点列表
		
		public TreeNode() {
		}
		
		public TreeNode(Permission permission) {
			BeanUtils.copyProperties(permission, this);
		}
		
		public boolean hasChildren() {
			return children.size() > 0;
		}
		
		public void addChild(TreeNode node) {
			children.add(node);
		}

		public List<TreeNode> getChildren() {
			return children;
		}

		public void setChildren(List<TreeNode> children) {
			this.children = children;
		}
	}
}
