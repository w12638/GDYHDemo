package com;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import db.DBUtil;
import entities.DataReturn;
import entities.User;
import mySession.MySessionContext;
import sun.misc.BASE64Decoder;

@RestController
@RequestMapping("/kaifapingtai")
public class OnUploadSuccess {	
	@Value("${imageUrl}")
	private String imageUrl;
	@Value("${imageUrlReturn}")
	private String imageUrlReturn;
	@PostMapping("onUploadSuccess")
	public DataReturn getCredential(HttpServletRequest request,@RequestBody Map<String, Object> map) {
		System.out.println(map);
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		User user = (User)MySessionContext.getSession(request.getHeader("sessionId")).getAttribute("user");
		String userCode = user.getUserCode();
		String appName = (String)map.get("appName")==null?"":(String)map.get("appName");
		String appVersion = (String)map.get("appVersion")==null?"":(String)map.get("appVersion");
		Integer size = (Integer)map.get("size");
		String sizeSting = String.valueOf(size);
		String cosUrl = (String)map.get("cosUrl");
		String type = (String)map.get("type");
		String verifyType = (String)map.get("verifyType")==null?"":(String)map.get("verifyType");
		String fieldId = (String)map.get("fieldId");
		String fieldName = (String)map.get("fieldName");
		String filePwd = (String)map.get("filePwd");
		String buildVersion = (String)map.get("buildVersion");
		String updateLog = (String)map.get("updateLog")==null?"":(String)map.get("updateLog");
		String updatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String bundleId = (String)map.get("bundleId");
		String icon = (String)map.get("icon")==null?"":(String)map.get("icon");
		String fileName = (String)map.get("fileName");
		
		if (cosUrl==null||size==null||type==null||fieldId==null||fieldName==null||filePwd==null||bundleId==null) {
			return new DataReturn("11111111", "有必填项为未填，请检查!", null);
		}
		String iconUrl = "";
		if(icon != null && !"".equals(icon)) {
			//图片类型
			System.out.println(icon.indexOf(";base64,"));
			String imageType = icon.substring(11,icon.indexOf(";base64,"));
			String fileBase64String;
			fileBase64String = icon.substring(icon.indexOf(";base64,")+8);
			//图片名称
			String fileName2 = fileName+"."+imageType;
			File file = convertBase64ToFile(fileBase64String, imageUrl, fileName2);
			iconUrl = String.valueOf(file).equals("null")?"":imageUrlReturn+"/"+fileName2;
			
		}
		
		String sql = "update T_APPINFO set ACTIVEFLAG = '0' where USER_ID = '"+userCode+"' and fieldId = '"+fieldId+"' ";
		String sql2 = "INSERT INTO T_APPINFO (APP_ID, USER_ID,APP_NAME,APP_VERSION,APP_SIZE,APP_COSURL,APP_TYPE,APP_VERIFY,FIELDID,FIELDNAME,FILEPWD,BUILDVERSION,UPDATELOG,ACTIVEFLAG,UPDATETIME,DOWNCOUNT,LOCKFLAG,DESC,BUNDLEID,ICON,FILE_NAME)" + 
				" VALUES ('"+uuid+"','"+userCode+"','"+appName+"','"+appVersion+"','"+sizeSting+"','"+cosUrl+"','"+type+"','"+verifyType+"',"
						+ "'"+fieldId+"','"+fieldName+"','"+filePwd+"','"+buildVersion+"','"+updateLog+"','1','"+updatetime+"','0','0','','"+bundleId+"','"+iconUrl+"','"+fileName+"')";
		try {
			DBUtil.operationSql(sql);
			DBUtil.operationSql(sql2);
		} catch (Exception e) {
			e.printStackTrace();
			return new DataReturn("11111111", "更新app文件信息失败!", null);
		} 
		
		Map<String, String> mapReturn = new HashMap<String, String>();
		mapReturn.put("filedId", uuid);
		return new DataReturn("保存app文件信息成功!", mapReturn);
		
		
	}
	
	/**
     * 将base64字符串，生成文件
     */
    public static File convertBase64ToFile(String fileBase64String, String imageUrl, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(imageUrl);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bfile = decoder.decodeBuffer(fileBase64String);

            file = new File(imageUrl + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    
   
}
