package com;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mySession.MySessionContext;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import util.SHA1;
import db.DBUtil;
import entities.DataReturn;
import entities.User;

@RestController
@RequestMapping("/kaifapingtai")
public class SetPwdAction {
	public static long validTimeStamp= 10*60*1000;//时间错有效期10分钟 10*60*1000
	public static long validLoginTime= 2*60*60*1000;//登陆密码连错三次 俩小时后登陆
	@RequestMapping(value = "/setPwd.do", method = RequestMethod.POST)
	public DataReturn login(@RequestBody Map map, HttpServletRequest request, HttpServletResponse response) {
		try {
			String oldPwd = (String) map.get("oldPwd");
			String newPwd = (String) map.get("newPwd");
			User user = (User)MySessionContext.getSession(request.getHeader("sessionId")).getAttribute("user");
	        long endTime =System.currentTimeMillis();
			String userCode = user.getUserCode();
			String sql = "SELECT USER_CODE, USER_NAME,ACTIVEFLAG,PASSWORD,LOGINTIME FROM T_user where USER_CODE = '" + userCode + "';";
			List pd = DBUtil.selectSql(sql);
			if (!pd.isEmpty()) {
				String flag = (String) (((Map) pd.get(0)).get("Activeflag"));
				String pwd = (String) (((Map) pd.get(0)).get("Password"));
				String lt = (String) (((Map) pd.get(0)).get("Logintime"));
				Date d = new Date();
				SimpleDateFormat sbf = new SimpleDateFormat("yyyyMMddHHmmss");
				Date loginDate = sbf.parse(lt);
				long loginTime = loginDate.getTime();
				long time = endTime - loginTime;
				int num =1;   
				if("5".equals(flag)&&time<validLoginTime){
					if(loginTime>SetPwdAction.validLoginTime){//判断是否超过俩个小时
						if(!oldPwd.equals(pwd)){
							String sql2 = "update T_USER set LOGINTIME = '"+sbf.format(d)+"' where USER_CODE = '"+userCode+"'" ;
							DBUtil.operationSql(sql2);
							return new DataReturn("111111", "密码连续输错3次，请俩小时后在登陆!", null);
						}
					}else{
						return new DataReturn("111111", "密码已锁定，请俩小时后在登陆重试!", null);
					}
				}else{
					if(!oldPwd.equals(pwd)){
						num = Integer.parseInt(flag)+1; //ACTIVEFLAG 1：正常 、2：密码输错1次、2：密码输错1次、3：密码输错2次、4：密码输错3次、5：密码锁定俩小时后重试；
						String sql2 = "update T_USER set LOGINTIME = '"+sbf.format(d)+"' , ACTIVEFLAG = '"+num+"' where USER_CODE = '"+userCode+"'" ;
						DBUtil.operationSql(sql2);
						return new DataReturn("111111", "密码错误!", null);
					}
					
				}
				String sql2 = "update T_USER set LOGINTIME = '"+sbf.format(d)+"' , ACTIVEFLAG = '"+num+"',PASSWORD ='"+newPwd+"' where USER_CODE = '"+userCode+"'" ;
				DBUtil.operationSql(sql2);
				return new DataReturn("重置密码成功!", null);
			} else {
				return new DataReturn("111111", "请输入密码!", null);
			} 

		} catch (Exception e) {
			e.printStackTrace();
			return new DataReturn("111111", "重置密码失败!", null);
		}

	}
}
