package com;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import db.DBUtil;
import entities.DataReturn;
import entities.User;
import mySession.MySessionContext;

@RestController
@RequestMapping("/kaifapingtai")
public class QueryAppsList {
	@RequestMapping(value = "/getAppListByUser.do", method = RequestMethod.POST)
	public DataReturn query(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = MySessionContext.getSession(request.getHeader("sessionId"));
			User user = null;
			if (session != null) {
				user = (User) session.getAttribute("user");
				if (user != null) {
					String userCode = user.getUserCode();
					String sqlApp = "select USER_CODE,USER_NAME,ACTIVEFLAG,LOGINTIME ,CREATTIME from T_USER where  USER_CODE = '"
							+ userCode + "';";
					String sqlAppInfo = "select APP_ID ,USER_ID,APP_NAME ,APP_VERSION ,APP_SIZE,APP_COSURL ,APP_TYPE ,APP_VERIFY ,FIELDID ,FIELDNAME ,FILEPWD ,BUILDVERSION ,UPDATELOG ,ACTIVEFLAG ,UPDATETIME,DOWNCOUNT,ICON,DESC,BUNDLEID from T_APPINFO where USER_ID = '"
							+ userCode + "' and ACTIVEFLAG = '1' order by FIELDID asc ;";
					List appList = DBUtil.selectSql(sqlApp);
					Map userInfo = (Map) appList.get(0);
					List appInfoList = DBUtil.selectSql(sqlAppInfo);
					System.out.println(appInfoList);
					Map<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
					linkedHashMap.put("userInfo", userInfo);
					linkedHashMap.put("appList", appInfoList);
					return new DataReturn("返回app列表成功！", linkedHashMap);
				} else {
					return new DataReturn("111111", "返回app列表失败！", null);
				}

			} else {
				return new DataReturn("111111", "返回app列表失败！", null);
			}

		} catch (Exception e) {
			return new DataReturn("111111", "返回app列表失败！", null);
		}

	}

}
