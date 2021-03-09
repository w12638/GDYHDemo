package com;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import db.DBUtil;
import entities.DataReturn;
import entities.User;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;




@RestController
public class LoginAction {

	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public DataReturn login(@RequestBody Map map, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			String code = (String) map.get("code");
			String password = (String) map.get("password");
			HttpSession session = request.getSession();
			String sessionid = session.getId();
			String sql = "SELECT USER_CODE, USER_NAME FROM T_user where USER_CODE = '" + code + "' and PASSWORD = '"
					+ password + "';";
			List pd = DBUtil.selectSql(sql);
			if (!pd.isEmpty()) {
				User user = new User((String) (((Map) pd.get(0)).get("UserCode")),
						(String) (((Map) pd.get(0)).get("UserName")));
				session.setAttribute("user", user);
				Map<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
				linkedHashMap.put("sessionid", sessionid);
				Date d = new Date();
				SimpleDateFormat sbf = new SimpleDateFormat("yyyyMMddHHmmss");
				String sql2 = "update T_USER set LOGINTIME = '"+sbf.format(d)+"' where USER_CODE = '"+code+"'" ;
				DBUtil.operationSql(sql2);
				return new DataReturn("登录成功!", linkedHashMap);
			} else {
				return new DataReturn("111111", "请输入用户名和密码!", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new DataReturn("111111", "登录失败!", null);
		}

	}

}
