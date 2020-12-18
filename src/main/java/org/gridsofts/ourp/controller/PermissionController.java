package org.gridsofts.ourp.controller;

import java.util.List;

import javax.annotation.Resource;

import org.gridsofts.halo.util.StringUtil;
import org.gridsofts.ourp.exception.SrvException;
import org.gridsofts.ourp.model.Permission;
import org.gridsofts.ourp.model.Permission.TreeNode;
import org.gridsofts.ourp.service.IPermissionService;
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
 * 权限信息控制器
 * 
 * @author lei
 */
@Api(tags = "权限信息控制器")
@Controller
@RequestMapping("/ourp/permission")
public class PermissionController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name = "ourpPermissionService")
	private IPermissionService<Permission> permissionService;

	/**
	 * 跳转进入权限管理首页
	 * 
	 * @param model
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "页面跳转进入 ourp/permission/index 视图内"))
	@ApiOperation("跳转进入权限管理首页；视图内可以获取  permissionList 类型为  List<Permission>")

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
	 * 获取所有的权限信息
	 * 
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "权限信息列表；JSON数组"))
	@ApiOperation("获取所有的权限信息")

	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<Permission> list() {

		try {
			return permissionService.list();
		} catch (SrvException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
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

	/**
	 * 查找权限信息
	 * 
	 * @param permissionId
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "权限信息；JSON对象"))
	@ApiOperation("查找权限信息")

	@ResponseBody
	@RequestMapping(value = "/{permissionId:.+}", method = RequestMethod.GET)
	public Permission find(@ApiParam(value = "路径变量；权限ID；", required = true) @PathVariable String permissionId) {

		try {
			return permissionService.find(permissionId);
		} catch (SrvException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 新建 权限信息
	 * 
	 * @param bean
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "OK - 创建成功；FAIL - 创建失败；其它异常信息"))
	@ApiOperation("新建 权限信息")

	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String create(Permission bean) {

		try {
			if (bean == null) {
				throw new NullPointerException("bean is null");
			}

			return permissionService.create(bean) ? "OK" : "FAIL";
		} catch (SrvException e) {
			return e.getMessage();
		}
	}

	/**
	 * 修改 权限信息
	 * 
	 * @param bean
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "OK - 修改成功；FAIL - 修改失败；其它异常信息"))
	@ApiOperation("修改 权限信息")

	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public String update(Permission bean) {

		try {
			if (bean == null) {
				throw new NullPointerException("bean is null");
			}

			// 先查找要修改的权限
			Permission target = permissionService.find(bean.getCode());
			if (target == null) {
				throw new NullPointerException("not found");
			}

			return permissionService.update(target) ? "OK" : "FAIL";
		} catch (SrvException e) {
			return e.getMessage();
		}
	}

	/**
	 * 批量删除权限信息
	 * 
	 * @param ids 多个权限ID以“,”分隔
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "OK - 删除成功；FAIL - 删除失败；其它异常信息"))
	@ApiOperation("批量删除权限信息")

	@ResponseBody
	@RequestMapping(value = "/{ids:.+}", method = RequestMethod.DELETE)
	public String delete(@ApiParam(value = "路径变量；权限ID；多个ID以“,”分隔", required = true) @PathVariable String ids) {

		try {
			return permissionService.remove(ids.split("\\s*\\,+\\s*")) ? "OK" : "FAIL";
		} catch (SrvException e) {
			return e.getMessage();
		}
	}
}
