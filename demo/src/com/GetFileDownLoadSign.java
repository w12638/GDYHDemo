package com;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import db.DBUtil;
import entities.DataReturn;
import util.MD5;

@RestController
public class GetFileDownLoadSign {
	@Value("${cosurl}")
    private String cosurl;
	@Value("${plistUrl}")
	private String plistUrl;
	@Value("${plistUrlReturn}")
	private String plistUrlReturn;
	@Value("${customKey}")
	private String customKey;
	@PostMapping("/getFileDownLoadSign")
	public DataReturn getCredential(@RequestBody Map<String, Object> map) {
		try {
			String appId = (String)map.get("appId");
			String filePwd = (String)map.get("filePwd");
			if (appId == null || filePwd == null) {
				return new DataReturn("11111111","请输入文件ID或提取码!",null);
			}
		
			String sql = "select a.filePwd,a.APP_COSURL,a.DOWNCOUNT, a.FIELDNAME as FIELD_NAME,a.APP_SIZE as file_Size,a.APP_TYPE as file_Type,a.UPDATETIME,a.APP_VERSION as version,b.USER_NAME as upload_User, a.ICON, a.UPDATELOG,a.BUILDVERSION,a.BUNDLEID,a.APP_VERIFY as verify_Type,a.APP_NAME,a.FILE_NAME from T_APPINFO a inner join T_USER b on a.USER_ID = b.USER_CODE   where APP_ID = '"+appId+"'";
			String filePwd2;
		
			List appList = DBUtil.selectSql(sql);
			if (appList==null||appList.get(0)==null) {
				return new DataReturn("11111111", "提取码输入错误！", null);
			}
			filePwd2 = (String)((Map) appList.get(0)).get("Filepwd");
			if (!filePwd.equals(filePwd2)) {
				return new DataReturn("11111111", "提取码输入错误！", null);
			}
			//生成16进制时间戳
			Date date = new Date();
			date.setTime(date.getTime() + 10*60*1000);
			Long ab = date.getTime()/1000;
		    String a = Long.toHexString(ab);
		    //包访问的路径加密
		    System.out.println("===customKey==="+customKey);
		    Map map2 = (Map)appList.get(0);
		    String appCosurl = (String)map2.get("AppCosurl");  
//		    String fileName = "/"+appCosurl.substring(appCosurl.length()-33, appCosurl.length());
//		    System.out.println("fileName====="+fileName);
//		    String bbb = customKey + fileName + a;
		    String bbb = customKey + appCosurl + a;
		    String customMD5 = MD5.stringToMD5(bbb);
//		    String appDownLoadUrl = cosurl+"/"+customMD5+"/"+a+fileName;
		    String appDownLoadUrl = cosurl+"/"+customMD5+"/"+a+appCosurl;
		    
		   
		    //如果是ios包，则生成plist文件
		    if (map2.get("FileType").equals("ios")) {
//		    	String fileName2 = "/"+appCosurl.substring(appCosurl.length()-26, appCosurl.length());
		    	String fileName = "/"+(String)map2.get("FileName");
//		    	String plistUrl2 = plistUrl+fileName2+".plist";
//		    	String plistUrl3 = plistUrlReturn+fileName2+".plist";	
		    	String plistUrl2 = plistUrl+fileName+".plist";
		    	String plistUrl3 = plistUrlReturn+fileName+".plist";	
		    	boolean bl = createPlist(plistUrl2,appDownLoadUrl, (String)map2.get("Icon"), null,(String)map2.get("Bundleid"), (String)map2.get("Buildversion") ,(String)map2.get("AppName"));
		    	if (bl == false) {
		    		return new DataReturn("11111111", "生成plist文件失败!", null);
				}
		    	
		    	map2.put("plistUrl", plistUrl3);
			}
		  
		    map2.put("appDownLoadUrl", appDownLoadUrl);
		    
		    map2.remove("Filepwd");
		    map2.remove("AppCosurl");
		    map2.remove("FileName");
		    
		    //更新下载次数
		    String  downcount =  (String)((Map)appList.get(0)).get("Downcount"); 
		    int downcountInt = Integer.parseInt(downcount)+1;
		    String downcountString = String.valueOf(downcountInt);
		    
		    String sql2 = "update T_APPINFO set DOWNCOUNT = '"+downcountString+"' where APP_ID = '"+appId+"' ";
		    DBUtil.operationSql(sql2);
		    return new DataReturn( "获取文件下载签名成功！", map2);
		    
		} catch (Exception e) {
			e.printStackTrace();
			return new DataReturn("11111111", "获取文件下载签名异常！", null);
		} 
		
	}
	
	
	/**
	 * 生成plist文件 
	 * @param title
	 * @param path
	 * @param bundleId
	 * @param fileUrl
	 * @param versionCode
	 * @return
	 * @throws Exception
	 */
	public boolean createPlist( String path,String appUrl, String displayImageUrl,String fullSizeImageUrl,String userName,String versionCode,String title) throws Exception {
        System.out.println("开始创建plist文件");
        System.out.println(path);
        System.out.println(title);
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            	e.printStackTrace();
            	return false;
            }
        }
        String plist = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n"
                + "<plist version=\"1.0\">\n" 
                +"<dict>\n"
                + "<key>items</key>\n"
                + "<array>\n"
                + "<dict>\n"
                + "<key>assets</key>\n"
                + "<array>\n"
                + "<dict>\n"
                + "<key>kind</key>\n"
                + "<string>software-package</string>\n"
                + "<key>url</key>\n"
                // 之前所上传的ipa文件路径（必须是https，否则无法下载！）
                + "<string>" +  appUrl+ "</string>\n"
                + "</dict>\n"
                + "<dict>\n"
                + "<key>kind</key>\n"
                + "<string>display-image</string>\n"
                + "<key>needs-shine</key>\n"
                + "<true/>\n"
                + "<key>url</key>\n"
                + "<string>"+displayImageUrl+"</string>\n"
                + "</dict>\n"
				+ "<dict>\n"
				+ "<key>kind</key>\n"
                + "<string>full-size-image</string>\n"
                + "<key>needs-shine</key>\n"
                + "<true/>\n"
                + "<key>url</key>\n"
                + "<string>https://fullSizeImageUrl</string>\n"
                + " </dict>\n"
                + "</array>\n"
                + "<key>metadata</key>\n"
                + "<dict>\n"
                + "<key>bundle-identifier</key>\n"
                // 这个是开发者账号用户名，也可以为空，为空安装时看不到图标，完成之后可以看到
                + "<string>" + userName + "</string>\n"
                + "<key>bundle-version</key>\n"
                // 版本号
                + "<string>"+ versionCode +"</string>\n"
                + "<key>kind</key>\n"
                + "<string>software</string>\n"
                + "<key>title</key>\n"
                // 一定要有title，否则无法正常下载
                + "<string>"+ title +"</string>\n"
                + "</dict>\n"
                + "</dict>\n"
                + "</array>\n"
                + "</dict>\n"
                + "</plist>";
        try {
            FileOutputStream output = new FileOutputStream(file);
            OutputStreamWriter writer;
            writer = new OutputStreamWriter(output, "UTF-8");
            writer.write(plist);
            writer.close();
            output.close();
        } catch (Exception e) {
        	e.printStackTrace();
        	return false;
        }
        System.out.println("成功创建plist文件");
        return true;
    }

}
