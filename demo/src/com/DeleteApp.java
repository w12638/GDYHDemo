package com;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import db.DBUtil;
@Controller
public class DeleteApp {
	@RequestMapping("deleteApp.do")
	public String query(String appId,HttpServletRequest request) {
		try {
			
			String sql = "delete from App where  APP_ID='"+appId+"';";
		    DBUtil.operationSql(sql);
		    String sqlPlist = "delete from Plist where  APP_ID='"+appId+"';";
		    DBUtil.operationSql(sqlPlist);
		    
		} catch (Exception e) {
			return "error";
		}
		return "success";
	}

}
