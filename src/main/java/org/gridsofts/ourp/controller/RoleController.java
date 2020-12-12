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

/**
 * 角色信息控制器
 * 
 * @author lei
 */
@Controller
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
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Model model) {

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
	@ResponseBody
	@RequestMapping(value = "/{roleId:.+}", method = RequestMethod.GET)
	public Role find(@PathVariable String roleId) {

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
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String create(Role bean) {

		try {
			if (bean == null) {
				throw new NullPointerException("bean is null");
			}

			return roleService.create(bean) ? "OK" : "FAIL";
		} catch (SrvException e) {
			return e.getMessage();
		}
	}

	/**
	 * 修改 角色信息
	 * 
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public String update(Role bean) {

		try {
			if (bean == null) {
				throw new NullPointerException("bean is null");
			}

			// 先查找要修改的角色
			Role target = roleService.find(bean.getRoleId());
			if (target == null) {
				throw new NullPointerException("not found");
			}

			return roleService.update(target) ? "OK" : "FAIL";
		} catch (SrvException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 批量删除角色信息
	 * 
	 * @param ids 多个角色ID以“,”分隔
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{ids:.+}", method = RequestMethod.DELETE)
	public String delete(@PathVariable String ids) {
		
		try {
			return roleService.remove(ids.split("\\s*\\,+\\s*")) ? "OK" : "FAIL";
		} catch (SrvException e) {
			return e.getMessage();
		}
	}
}
