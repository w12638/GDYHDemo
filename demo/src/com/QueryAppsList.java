package com;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import db.DBUtil;
@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class QueryAppsList {
	@RequestMapping(value="/queryAppList.do", method=RequestMethod.POST)
	public void query(HttpServletRequest request,HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("User");
			String userid= user.getUserId();
			System.out.println("6666"+userid);
			String sqlApp = "select USER_CODE ,PASSWORD,USER_NAME,ACTIVEFLAG,LOGINTIME ,CREATTIME from T_USER where  USER_NAME= '"+userid+"';";
			String sqlAppInfo = "select APP_ID ,USER_ID,APP_NAME ,APP_VERSION ,APP_SIZE,APP_COSURL ,APP_TYPE ,APP_VERIFY ,FIELDID ,FIELDNAME ,FILEPWD ,BUILDVERSION ,UPDATELOG ,ACTIVEFLAG ,UPDATETIME,DOWNCOUNT,DESC from T_APPINFO where USER_ID = '"+userid+"';";
		    List appList = DBUtil.selectSql(sqlApp);
		    List appInfoList = DBUtil.selectSql(sqlAppInfo);
		    
		} catch (Exception e) {
		}
	}

}
