package org.gridsofts.ourp.controller;

import java.util.List;

import javax.annotation.Resource;

import org.gridsofts.halo.util.StringUtil;
import org.gridsofts.ourp.exception.SrvException;
import org.gridsofts.ourp.model.Organization;
import org.gridsofts.ourp.model.Organization.TreeNode;
import org.gridsofts.ourp.service.IOrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 组织机构信息控制器
 * 
 * @author lei
 */
@Controller
@RequestMapping("/ourp/organization")
public class OrgController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name = "ourpOrganizationService")
	private IOrganizationService<Organization> orgService;

	/**
	 * 跳转进入组织机构管理首页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Model model) {

		try {
			// 查询数据库
			model.addAttribute("organizationList", orgService.list());
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}

		return "ourp/organization/index";
	}

	/**
	 * 获取所有的组织机构信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<Organization> list() {

		try {
			return orgService.list();
		} catch (SrvException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 获取组织机构树
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public TreeNode tree() {
		TreeNode rootNode = new TreeNode();
		rootNode.setOrgId("_ROOT");
		rootNode.setOrgName("ROOT");

		try {
			List<Organization> orgList = orgService.list();
			if (orgList != null && !orgList.isEmpty()) {

				orgList.parallelStream().filter(org -> StringUtil.isNull(org.getPrntId())).forEach(topNode -> {
					_buildTree(rootNode, topNode, orgList);
				});
			}
		} catch (SrvException e) {
			logger.error(e.getMessage(), e);
		}

		return rootNode;
	}

	private void _buildTree(TreeNode rootNode, Organization child, List<Organization> orgList) {
		TreeNode childNode = new TreeNode(child);
		rootNode.addChild(childNode);

		if (orgList != null && !orgList.isEmpty()) {
			orgList.parallelStream().filter(org -> child.getOrgId().equals(org.getPrntId())).forEach(org -> {
				_buildTree(childNode, org, orgList);
			});
		}
	}

	/**
	 * 查找组织机构信息
	 * 
	 * @param orgId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{orgId:.+}", method = RequestMethod.GET)
	public Organization find(@PathVariable String orgId) {

		try {
			return orgService.find(orgId);
		} catch (SrvException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 新建 组织机构信息
	 * 
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String create(Organization bean) {

		try {
			if (bean == null) {
				throw new NullPointerException("bean is null");
			}

			return orgService.create(bean) ? "OK" : "FAIL";
		} catch (SrvException e) {
			return e.getMessage();
		}
	}

	/**
	 * 修改 组织机构信息
	 * 
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public String update(Organization bean) {

		try {
			if (bean == null) {
				throw new NullPointerException("bean is null");
			}

			// 先查找要修改的组织机构
			Organization target = orgService.find(bean.getOrgId());
			if (target == null) {
				throw new NullPointerException("not found");
			}

			return orgService.update(target) ? "OK" : "FAIL";
		} catch (SrvException e) {
			return e.getMessage();
		}
	}

	/**
	 * 批量删除组织机构信息
	 * 
	 * @param ids 多个组织机构ID以“,”分隔
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{ids:.+}", method = RequestMethod.DELETE)
	public String delete(@PathVariable String ids) {

		try {
			return orgService.remove(ids.split("\\s*\\,+\\s*")) ? "OK" : "FAIL";
		} catch (SrvException e) {
			return e.getMessage();
		}
	}
}
