package org.gridsofts.ourp.controller;

import java.util.List;

import javax.annotation.Resource;

import org.gridsofts.halo.util.BeanUtil;
import org.gridsofts.halo.util.StringUtil;
import org.gridsofts.ourp.exception.SrvException;
import org.gridsofts.ourp.model.User;
import org.gridsofts.ourp.service.IUserService;
import org.gridsofts.ourp.utils.Encrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户信息控制器
 * 
 * @author lei
 */
@Controller
@RequestMapping("/ourp/user")
public class UserController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name = "ourpUserService")
	private IUserService<User> userService;
	
	/**
	 * 跳转进入用户管理首页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Model model) {

		try {
			// 查询数据库
			model.addAttribute("userList", userService.list());
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}

		return "ourp/user/index";
	}
	
	/**
	 * 获取所有的用户信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<User> list() {

		try {
			return userService.list();
		} catch (SrvException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 查找用户信息
	 * 
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{userId:.+}", method = RequestMethod.GET)
	public User find(@PathVariable String userId) {

		try {
			return userService.find(userId);
		} catch (SrvException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 用户身份认证
	 * 
	 * @param userId
	 * @param userPwd
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/authen", method = RequestMethod.POST)
	public String authenticate(@RequestParam String userId, @RequestParam String userPwd) {

		try {
			if (StringUtil.isNull(userId) || StringUtil.isNull(userPwd)) {
				return "required parameter is null";
			}
			
			// 先查找要修改的用户
			User target = userService.find(userId);
			if (target == null) {
				throw new NullPointerException("not found");
			}
			
			// 验证密码
			String userDigestPwd = Encrypt.md5(userPwd);

			return userDigestPwd.equals(target.getUserPwd()) ? "OK" : "FAIL";
		} catch (SrvException e) {
			return e.getMessage();
		}
	}

	/**
	 * 新建 用户信息
	 * 
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String create(User bean) {

		try {
			if (bean == null) {
				throw new NullPointerException("bean is null");
			}

			// 对密码进行“加密”
			bean.setPwdDigestAlgorithm("MD5");
			bean.setUserPwd(Encrypt.md5(bean.getUserPwd()));

			return userService.create(bean) ? "OK" : "FAIL";
		} catch (SrvException e) {
			return e.getMessage();
		}
	}

	/**
	 * 修改 用户信息；不修改 userId, userPwd, pwdDigestAlgorithm 三个字段
	 * 
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public String update(User bean) {

		try {
			if (bean == null) {
				throw new NullPointerException("bean is null");
			}

			// 先查找要修改的用户
			User target = userService.find(bean.getUserId());
			if (target == null) {
				throw new NullPointerException("not found");
			}

			// 复制用户信息；跳过 userId, userPwd, pwdDigestAlgorithm 三个字段
			BeanUtil.copyProperties(bean, target, new String[] { "userId", "userPwd", "pwdDigestAlgorithm" });

			return userService.update(target) ? "OK" : "FAIL";
		} catch (SrvException e) {
			return e.getMessage();
		}
	}

	/**
	 * 修改 登录密码
	 * 
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pwd", method = RequestMethod.PUT)
	public String updatePwd(User bean) {

		try {
			if (bean == null) {
				throw new NullPointerException("bean is null");
			}

			// 先查找要修改的用户
			User target = userService.find(bean.getUserId());
			if (target == null) {
				throw new NullPointerException("not found");
			}

			// 对密码进行“加密”
			target.setPwdDigestAlgorithm("MD5");
			target.setUserPwd(Encrypt.md5(bean.getUserPwd()));

			return userService.update(target) ? "OK" : "FAIL";
		} catch (SrvException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 批量删除用户信息
	 * 
	 * @param ids 多个用户ID以“,”分隔
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{ids:.+}", method = RequestMethod.DELETE)
	public String delete(@PathVariable String ids) {
		
		try {
			return userService.remove(ids.split("\\s*\\,+\\s*")) ? "OK" : "FAIL";
		} catch (SrvException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 批量停用用户信息
	 * 
	 * @param ids 多个用户ID以“,”分隔
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/disable/{ids:.+}", method = RequestMethod.POST)
	public String disable(@PathVariable String ids) {
		
		try {
			List<User> userList = userService.findAll(ids.split("\\s*,+\\s*"));
			if (userList != null && !userList.isEmpty()) {
				int result = 0;
				
				for (User user : userList) {
					user.setIsValid(0);
					result += userService.update(user) ? 1 : 0;
				}
				
				return result == userList.size() ? "OK" : "FAIL";
			}
		} catch (SrvException e) {
			return e.getMessage();
		}
		
		return "FAIL";
	}
	
	/**
	 * 批量启用用户信息
	 * 
	 * @param ids 多个用户ID以“,”分隔
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/enable/{ids:.+}", method = RequestMethod.POST)
	public String enable(@PathVariable String ids) {
		
		try {
			List<User> userList = userService.findAll(ids.split("\\s*,+\\s*"));
			if (userList != null && !userList.isEmpty()) {
				int result = 0;
				
				for (User user : userList) {
					user.setIsValid(1);
					result += userService.update(user) ? 1 : 0;
				}
				
				return result == userList.size() ? "OK" : "FAIL";
			}
		} catch (SrvException e) {
			return e.getMessage();
		}
		
		return "FAIL";
	}
}
