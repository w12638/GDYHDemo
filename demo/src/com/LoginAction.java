package com;

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

import util.SHA1;
import db.DBUtil;
import entities.DataReturn;
import entities.User;

@RestController
public class LoginAction {

	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public DataReturn login(@RequestBody Map map, HttpServletRequest request, HttpServletResponse response) {
		try {
			String code = (String) map.get("code");
			String password = (String) map.get("password");
			String timeStamp = (String) map.get("timeStamp");
			long endTime =System.currentTimeMillis();
			long beginTime = Long.parseLong(timeStamp);
			long time = endTime - beginTime;
			if(time>SetPwdAction.validTimeStamp){
				return new DataReturn("111111", "时间戳已失效", null);
			}
			HttpSession session = request.getSession();
			String sessionid = session.getId();
			String sql = "SELECT USER_CODE, USER_NAME,ACTIVEFLAG,PASSWORD,LOGINTIME FROM T_user where USER_CODE = '" + code + "';";
			List pd = DBUtil.selectSql(sql);
			if (!pd.isEmpty()) {
				User user = new User((String) (((Map) pd.get(0)).get("UserCode")),
						(String) (((Map) pd.get(0)).get("UserName")));
				session.setAttribute("user", user);
				String flag = (String) (((Map) pd.get(0)).get("Activeflag"));
				String pwd = (String) (((Map) pd.get(0)).get("Password"));
				String lt = (String) (((Map) pd.get(0)).get("Logintime"));
				String shaPwd = SHA1.stringToSHA(pwd+"+"+timeStamp);
				Date d = new Date();
				
				SimpleDateFormat sbf = new SimpleDateFormat("yyyyMMddHHmmss");
				Date loginDate = sbf.parse(lt);
				long loginTime = loginDate.getTime();
				int num =1;
				if("5".equals(flag)){
					if(loginTime>SetPwdAction.validLoginTime){//判断是否超过俩个小时
						if(!password.equals(shaPwd)){
							String sql2 = "update T_USER set LOGINTIME = '"+sbf.format(d)+"' where USER_CODE = '"+code+"'" ;
							DBUtil.operationSql(sql2);
							return new DataReturn("111111", "密码连续输错3次，请俩小时后在登陆!", null);
						}
					}else{
						return new DataReturn("111111", "密码已锁定，请俩小时后在登陆重试!", null);
					}
				}else{
					if(!password.equals(shaPwd)){
						num = Integer.parseInt(flag)+1; //ACTIVEFLAG 1：正常 、2：密码输错1次、2：密码输错1次、3：密码输错2次、4：密码输错3次、5：密码锁定俩小时后重试；
						String sql2 = "update T_USER set LOGINTIME = '"+sbf.format(d)+"' , ACTIVEFLAG = '"+num+"' where USER_CODE = '"+code+"'" ;
						DBUtil.operationSql(sql2);
						return new DataReturn("111111", "密码错误!", null);
					}
					
				}
				Map<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
				linkedHashMap.put("sessionid", sessionid);
				
				String sql2 = "update T_USER set LOGINTIME = '"+sbf.format(d)+"' , ACTIVEFLAG = '"+num+"' where USER_CODE = '"+code+"'" ;
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
