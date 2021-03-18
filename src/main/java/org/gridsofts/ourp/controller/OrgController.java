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
@Api(tags = "组织机构信息控制器")
@Controller("_ourpOrganizationController")
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
	@ApiResponses(@ApiResponse(code = 200, message = "页面跳转进入 ourp/organization/index 视图内"))
	@ApiOperation("跳转进入组织机构管理首页；视图内可以获取  organizationList 类型为  List<Organization>")

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
	 * 获取所有的组织机构信息
	 * 
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "组织机构信息列表；JSON数组"))
	@ApiOperation("获取所有的组织机构信息")

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

	/**
	 * 查找组织机构信息
	 * 
	 * @param orgId
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "组织机构信息；JSON对象"))
	@ApiOperation("查找组织机构信息")

	@ResponseBody
	@RequestMapping(value = "/{orgId:.+}", method = RequestMethod.GET)
	public Organization find(@ApiParam(value = "路径变量；组织机构ID；", required = true) @PathVariable String orgId) {

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
	@ApiResponses(@ApiResponse(code = 200, message = "OK - 创建成功；FAIL - 创建失败；其它异常信息"))
	@ApiOperation("新建 组织机构信息")

	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String create(Organization bean) {

		try {
			if (bean == null) {
				return ("bean is null");
			}

			return orgService.create(bean) ? ("OK") : ("FAIL");
		} catch (SrvException e) {
			logger.error(e.getMessage(), e);

			return e.getMessage();
		}
	}

	/**
	 * 修改 组织机构信息
	 * 
	 * @param bean
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "OK - 修改成功；FAIL - 修改失败；其它异常信息"))
	@ApiOperation("修改 组织机构信息")

	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public String update(Organization bean) {

		try {
			if (bean == null) {
				return ("bean is null");
			}

			// 先查找要修改的组织机构
			Organization target = orgService.find(bean.getOrgId());
			if (target == null) {
				throw new NullPointerException("not found");
			}

			return orgService.update(target) ? ("OK") : ("FAIL");
		} catch (SrvException e) {
			logger.error(e.getMessage(), e);

			return e.getMessage();
		}
	}

	/**
	 * 批量删除组织机构信息
	 * 
	 * @param ids 多个组织机构ID以“,”分隔
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "OK - 删除成功；FAIL - 删除失败；其它异常信息"))
	@ApiOperation("批量删除组织机构信息")

	@ResponseBody
	@RequestMapping(value = "/{ids:.+}", method = RequestMethod.DELETE)
	public String delete(
			@ApiParam(value = "路径变量；组织机构ID；多个ID以“,”分隔", required = true) @PathVariable String ids) {

		try {
			return orgService.remove(ids.split("\\s*\\,+\\s*")) ? ("OK") : ("FAIL");
		} catch (SrvException e) {
			logger.error(e.getMessage(), e);

			return e.getMessage();
		}
	}
}
