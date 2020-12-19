package org.gridsofts.ourp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.gridsofts.halo.annotation.Column;
import org.gridsofts.halo.annotation.Table;
import org.gridsofts.halo.util.BeanUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 组织机构
 * 
 * @author lei
 */
@ApiModel(description = "组织机构实体类")
@Table(value = "OURP_ORGANIZATION", primaryKey = { "orgId" }, autoGenerateKeys = false)
public class Organization implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("机构ID；主键")
	@Column("ORG_ID")
	private String orgId; // 机构ID；主键

	@ApiModelProperty("父级机构ID；外键；可空")
	@Column("PRNT_ID")
	private String prntId; // 父级机构ID；外键；可空

	@ApiModelProperty("机构名称")
	@Column("ORG_NAME")
	private String orgName; // 机构名称

	@ApiModelProperty("机构简称")
	@Column("SHORT_NAME")
	private String shortName; // 机构简称

	@ApiModelProperty("机构性质")
	@Column("ORG_NATURE")
	private String orgNature; // 机构性质

	@ApiModelProperty("简介")
	@Column("INTRODUCTION")
	private String introduction; // 简介

	@ApiModelProperty("地址")
	@Column("ADDRESS")
	private String address; // 地址

	@ApiModelProperty("邮编")
	@Column("POSTCODE")
	private String postcode; // 邮编

	@ApiModelProperty("联系人")
	@Column("CNTCT_PERSON")
	private String cntctPerson; // 联系人

	@ApiModelProperty("联系电话")
	@Column("CNTCT_PHONE")
	private String cntctPhone; // 联系电话

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getPrntId() {
		return prntId;
	}

	public void setPrntId(String prntId) {
		this.prntId = prntId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getOrgNature() {
		return orgNature;
	}

	public void setOrgNature(String orgNature) {
		this.orgNature = orgNature;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCntctPerson() {
		return cntctPerson;
	}

	public void setCntctPerson(String cntctPerson) {
		this.cntctPerson = cntctPerson;
	}

	public String getCntctPhone() {
		return cntctPhone;
	}

	public void setCntctPhone(String cntctPhone) {
		this.cntctPhone = cntctPhone;
	}

	@Override
	public String toString() {
		return getOrgName();
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null || !(obj instanceof Organization)) {
			return false;
		}

		Organization target = (Organization) obj;

		return getOrgId() != null && getOrgId().equals(target.getOrgId());
	}

	/**
	 * 组织机构树节点
	 * 
	 * @author lei
	 */
	@ApiModel(value = "Organization.TreeNode", description = "组织机构树节点")
	public static class TreeNode extends Organization {
		private static final long serialVersionUID = 1L;

		@ApiModelProperty(value = "子节点列表", dataType = "List<Organization.TreeNode>")
		private List<TreeNode> children = new ArrayList<>(); // 子节点列表

		public TreeNode() {
		}

		public TreeNode(Organization organization) {
			BeanUtil.copyProperties(organization, this);
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
