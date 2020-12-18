package org.gridsofts.ourp.model;

import java.io.Serializable;

import org.gridsofts.halo.annotation.Column;
import org.gridsofts.halo.annotation.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户信息
 * 
 * @author lei
 */
@ApiModel(description = "用户信息实体类")
@Table(value = "OURP_USER", primaryKey = { "userId" }, autoGenerateKeys = false)
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("用户ID；主键")
	@Column("USER_ID")
	private String userId; // 用户ID；主键

	@ApiModelProperty("登录密码")
	@Column("USER_PWD")
	private String userPwd; // 登录密码

	@ApiModelProperty("密码摘要算法名称（MD5、SHA1）")
	@Column("PWD_DIGEST_ALGORITHM")
	private String pwdDigestAlgorithm; // 密码摘要算法名称（MD5、SHA1）

	/**************************************************************/
	/** 以下是用户附属资料 ****************************************/
	/**************************************************************/

	@ApiModelProperty("姓")
	@Column("FIRST_NAME")
	private String firstName; // 姓
	@ApiModelProperty("名")
	@Column("LAST_NAME")
	private String lastName; // 名

	@ApiModelProperty("妮称")
	@Column("NICK_NAME")
	private String nickName; // 妮称

	@ApiModelProperty("身份证号")
	@Column("IDCARD")
	private String idcard; // 身份证号

	@ApiModelProperty("电子邮箱")
	@Column("EMAIL")
	private String email; // 电子邮箱
	@ApiModelProperty("电话号码")
	@Column("PHONE")
	private String phone; // 电话号码
	@ApiModelProperty("联系地址")
	@Column("ADDRESS")
	private String address; // 联系地址

	@ApiModelProperty("是否有效（0-无效；1-有效）")
	@Column("IS_VALID")
	private Integer isValid; // 是否有效（0-无效；1-有效）

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getPwdDigestAlgorithm() {
		return pwdDigestAlgorithm;
	}

	public void setPwdDigestAlgorithm(String pwdDigestAlgorithm) {
		this.pwdDigestAlgorithm = pwdDigestAlgorithm;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	@Override
	public String toString() {
		return getUserId() + " [ " + getFirstName() + " " + getLastName() + " ]";
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null || !(obj instanceof User)) {
			return false;
		}

		User target = (User) obj;

		return getUserId() != null && getUserId().equals(target.getUserId());
	}
}
