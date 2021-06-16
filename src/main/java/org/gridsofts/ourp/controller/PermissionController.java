package org.gridsofts.ourp.controller;

import java.util.List;

import javax.annotation.Resource;

import org.gridsofts.halo.crud.SrvException;
import org.gridsofts.halo.crud.controller.AbstractCRUDController;
import org.gridsofts.halo.util.StringUtil;
import org.gridsofts.ourp.model.Permission;
import org.gridsofts.ourp.model.Permission.TreeNode;
import org.gridsofts.ourp.service.IPermissionService;
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
 * 权限信息控制器
 * 
 * @author lei
 */
@Api(tags = "OURP - 权限信息控制器")
@Controller("_ourpPermissionController")
@RequestMapping("/ourp/permission")
public class PermissionController extends AbstractCRUDController<Permission, String> {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name = "ourpPermissionService")
	private IPermissionService<Permission> permissionService;

	@Override
	public void setCrudService() {
		super.setCrudService(permissionService);
	}

	/**
	 * 跳转进入权限管理首页
	 * 
	 * @param model
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "页面跳转进入 ourp/permission/index 视图内"))
	@ApiOperation("MVC视图跳转进入权限管理首页；视图内可以获取  permissionList 类型为  List<Permission>")

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(@ApiParam(hidden = true) Model model) {

		try {
			// 查询数据库
			model.addAttribute("permissionList", permissionService.list());
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}

		return "ourp/permission/index";
	}

	/**
	 * 获取权限信息树
	 * 
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "权限信息树根节点；JSON对象"))
	@ApiOperation("获取权限信息树")

	@ResponseBody
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public TreeNode tree() {
		TreeNode rootNode = new TreeNode();
		rootNode.setCode("_ROOT");
		rootNode.setName("ROOT");

		try {
			List<Permission> permissionList = permissionService.list();
			if (permissionList != null && !permissionList.isEmpty()) {

				permissionList.parallelStream().filter(permission -> StringUtil.isNull(permission.getPrntCode()))
						.forEach(topNode -> {
							_buildTree(rootNode, topNode, permissionList);
						});
			}
		} catch (SrvException e) {
			logger.error(e.getMessage(), e);
		}

		return rootNode;
	}

	private void _buildTree(TreeNode rootNode, Permission child, List<Permission> permissionList) {
		TreeNode childNode = new TreeNode(child);
		rootNode.addChild(childNode);

		if (permissionList != null && !permissionList.isEmpty()) {
			permissionList.parallelStream().filter(permission -> child.getCode().equals(permission.getPrntCode()))
					.forEach(permission -> {
						_buildTree(childNode, permission, permissionList);
					});
		}
	}
}
