package com.gs.spider.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 关于程序本身的一些信息
 * @author Hokis
 * 
 */
public final class AppInfo {

	/*名称*/
	public static String APP_NAME;
	
	/*版本*/
	public static String APP_VERSION;
	
	/*在线文档*/
	public static String ONLINE_DOCUMENTATION;
	
	
	static {
		Properties appinfo = new Properties();
		try(
				InputStreamReader isr = new InputStreamReader(AppInfo.class.getResourceAsStream("/appinfo"),"UTF-8"); 
				) {
			appinfo.load(isr);
			APP_NAME = appinfo.getProperty("appName");
			APP_VERSION= appinfo.getProperty("appVersion");
			ONLINE_DOCUMENTATION= appinfo.getProperty("onlineDocumentation");
		} catch (IOException e) {
		}
	}
	
	
}
