package org.gridsofts.ourp.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.gridsofts.halo.crud.SrvException;
import org.gridsofts.halo.crud.controller.AbstractCRUDController;
import org.gridsofts.halo.util.StringUtil;
import org.gridsofts.ourp.model.Organization;
import org.gridsofts.ourp.model.Organization.TreeNode;
import org.gridsofts.ourp.service.IOrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 组织机构信息控制器
 * 
 * @author lei
 */
@Api(tags = "OURP - 组织机构信息控制器")
@Controller("_ourpOrganizationController")
@RequestMapping("/ourp/organization")
public class OrgController extends AbstractCRUDController<Organization, String> {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name = "ourpOrganizationService")
	private IOrganizationService<Organization> orgService;
	
	@Override
	@PostConstruct
	public void setCrudService() {
		super.setCrudService(orgService);
	}

	/**
	 * 跳转进入组织机构管理首页
	 * 
	 * @param model
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "页面跳转进入 ourp/organization/index 视图内"))
	@ApiOperation("MVC视图跳转进入组织机构管理首页；视图内可以获取  organizationList 类型为  List<Organization>")

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(@ApiParam(hidden = true) Model model) {

		try {
			// 查询数据库
			model.addAttribute("organizationList", orgService.list());
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			model.addAttribute("organizationList", null);
		}

		return "ourp/organization/index";
	}
	
	/**
	 * 获取组织机构树
	 * 
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "组织机构树根节点；JSON对象"))
	@ApiOperation("获取组织机构树")

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
}
