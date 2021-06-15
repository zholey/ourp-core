package org.gridsofts.ourp.controller;

import java.util.List;

import javax.annotation.Resource;

import org.gridsofts.ourp.exception.SrvException;
import org.gridsofts.ourp.model.Role;
import org.gridsofts.ourp.service.IRoleService;
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
 * 角色信息控制器
 * 
 * @author lei
 */
@Api(tags = "OURP - 角色信息控制器")
@Controller("_ourpRoleController")
@RequestMapping("/ourp/role")
public class RoleController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name = "ourpRoleService")
	private IRoleService<Role> roleService;

	/**
	 * 跳转进入角色管理首页
	 * 
	 * @param model
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "页面跳转进入 ourp/role/index 视图内"))
	@ApiOperation("跳转进入角色管理首页；视图内可以获取  roleList 类型为  List<Role>")

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(@ApiParam(hidden = true) Model model) {

		try {
			// 查询数据库
			model.addAttribute("roleList", roleService.list());
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}

		return "ourp/role/index";
	}

	/**
	 * 获取所有的角色信息
	 * 
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "角色信息列表；JSON数组"))
	@ApiOperation("获取所有的角色信息")

	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<Role> list() {

		try {
			return roleService.list();
		} catch (SrvException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 查找角色信息
	 * 
	 * @param roleId
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "角色信息；JSON对象"))
	@ApiOperation("查找角色信息")

	@ResponseBody
	@RequestMapping(value = "/{roleId:.+}", method = RequestMethod.GET)
	public Role find(@ApiParam(value = "路径变量；角色ID；", required = true) @PathVariable String roleId) {

		try {
			return roleService.find(roleId);
		} catch (SrvException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 新建 角色信息
	 * 
	 * @param bean
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "OK - 创建成功；FAIL - 创建失败；其它异常信息"))
	@ApiOperation("新建 角色信息")

	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String create(Role bean) {

		try {
			if (bean == null) {
				return ("bean is null");
			}

			return roleService.create(bean) ? ("OK") : ("FAIL");
		} catch (SrvException e) {
			logger.error(e.getMessage(), e);

			return e.getMessage();
		}
	}

	/**
	 * 修改 角色信息
	 * 
	 * @param bean
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "OK - 修改成功；FAIL - 修改失败；其它异常信息"))
	@ApiOperation("修改 角色信息")

	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public String update(Role bean) {

		try {
			if (bean == null) {
				return ("bean is null");
			}

			// 先查找要修改的角色
			Role target = roleService.find(bean.getRoleId());
			if (target == null) {
				return ("bean not found");
			}

			return roleService.update(target) ? ("OK") : ("FAIL");
		} catch (SrvException e) {
			logger.error(e.getMessage(), e);

			return e.getMessage();
		}
	}

	/**
	 * 批量删除角色信息
	 * 
	 * @param ids 多个角色ID以“,”分隔
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "OK - 删除成功；FAIL - 删除失败；其它异常信息"))
	@ApiOperation("批量删除角色信息")

	@ResponseBody
	@RequestMapping(value = "/{ids:.+}", method = RequestMethod.DELETE)
	public String delete(@ApiParam(value = "路径变量；角色ID；多个ID以“,”分隔", required = true) @PathVariable String ids) {

		try {
			return roleService.remove(ids.split("\\s*\\,+\\s*")) ? ("OK") : ("FAIL");
		} catch (SrvException e) {
			logger.error(e.getMessage(), e);

			return e.getMessage();
		}
	}
}
