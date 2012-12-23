package com.lyfing.api;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import com.lyfing.service.ConnWithServer;
import com.lyfing.service.GetHtmlFormContent;


public class DataService {
	private static ConnWithServer cws = new ConnWithServer() ;
	private static GetHtmlFormContent ghfc = new GetHtmlFormContent() ;
	private static boolean ChengJi_YanZhengPassed = false;
	private static ArrayList<ArrayList<String>> list = null;
	
	/**
	 * 登录验证
	 * @param stuid 你懂得
	 * @param pwd 你懂得
	 * @param forWhat (初期可填任意值)分别为"本科生成绩系统验证"，"教学评估系统验证"、"挂科补考系统验证"
	 * @return 验证通过，返回true，否则返回false.
	 */
	public static boolean yanZheng(String stuid, String pwd, String forWhat){
		try {
			if (cws.getCookies( "ChengJi" , stuid , pwd )) {
				ChengJi_YanZhengPassed = true;
				return true ;
			} else {
				return false ;
			}
		} catch (ParseException e1) {e1.printStackTrace();}
		  catch (IOException e2) { e2.printStackTrace();
		}
		finally {
			return false;
		}
	} //yanZheng();	
	
	/**
	 * 一旦验证通过，就可以获得用户信息了
	 * @return 用户信息
	 */
	public static String getUserInfo(){
		if( ChengJi_YanZhengPassed){	
			String s = null;
			s = ghfc.getLoginContent( cws.getLoginHtml());
			return s;
		} else {
			return "用户名密码不正确，不可进行本步操作";
		}
	}
	
	/**
	 * 获得所有[已修课程]的成绩信息
	 */
	public static ArrayList<ArrayList<String>> getPassedClassesData(){
		list = ghfc.getPassedClassesContent( cws.getPassedClassesHtml());
		return list;
	}
	
	/**
	 * 获得所有[本学期成绩]
	 */
	public static ArrayList<ArrayList<String>> getChengJiData(){
		list = ghfc.getChengJiContent( cws.getChengJiHtml());
		return list;
	}
	
	/**
	 * 一键教评. 注意：该方法为耗时操作，请务必新建线程运行之！
	 * @return 成功，则返回[已教评的所有课程的名称]，失败返回[null];
	 */
	public static String yiJianJiaoPing(){
		String s = null;
		try {
			s = cws.postValues( ghfc.getJiaoPingList( cws.getJiaoPingListHtml()));
		} catch (ClientProtocolException e) { e.printStackTrace();}
		  catch (IOException e) { e.printStackTrace();
		}
		return s;
	}
	
	
	

	
/**
 * ！！！
 * 注意：使用前，请先在下方填写学号和密码！！！
 * ！！！
 * 以下模块为获取数据的演示操作，可删除
 */
	public static void main(String[] args) {
		ArrayList<ArrayList<String>> list = null;
		
		String stuid = "填写你的学号"; String pwd = "填写你的成绩管理系统的密码";
		
		yanZheng(stuid, pwd, "dd");

System.out.print( getUserInfo());
System.out.println("\r\n");

		list = getPassedClassesData();
		printlist(list);
		
System.out.println("\r\n" + "============本学期成绩============");		
		list = getChengJiData();
		printlist(list);
		
		cws.releaseResouces();
	}

	private static void printlist( ArrayList<ArrayList<String>> list){
		for(int i = 0; i<list.size(); i++){
			for(int j = 0; j<list.get(i).size(); j++){
				System.out.print(list.get(i).get(j) + "，");
			}
			System.out.println();
		}
	}
}
