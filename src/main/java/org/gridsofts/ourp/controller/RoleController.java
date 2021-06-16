package org.gridsofts.ourp.controller;

import javax.annotation.Resource;

import org.gridsofts.halo.crud.controller.AbstractCRUDController;
import org.gridsofts.ourp.model.Role;
import org.gridsofts.ourp.service.IRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
public class RoleController extends AbstractCRUDController<Role, String> {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name = "_ourpRoleService")
	private IRoleService<Role> roleService;

	@Override
	public void setCrudService() {
		super.setCrudService(roleService);
	}

	/**
	 * 跳转进入角色管理首页
	 * 
	 * @param model
	 * @return
	 */
	@ApiResponses(@ApiResponse(code = 200, message = "页面跳转进入 ourp/role/index 视图内"))
	@ApiOperation("MVC视图跳转进入角色管理首页；视图内可以获取  roleList 类型为  List<Role>")

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
}
