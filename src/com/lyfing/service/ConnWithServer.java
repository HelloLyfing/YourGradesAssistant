package com.lyfing.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


/**
 * 功能：实现和服务器的连接，向服务器发送一些验证性的数据，
 * 并请求相关数据或页面，得到相关Html页面的源代码
 */
public class ConnWithServer {
	HttpHost httpHost = null ;
	DefaultHttpClient httpClient = new DefaultHttpClient();
	HttpPost httpPost = null;
	HttpResponse response = null;
	HttpEntity entity = null;
	CookieStore cookieStore = null;
	
	/**
	 * 第一步，向表单接受页面（处理登录数据的页面），
	 * 发送个人学号、密码请求，
	 * 如果验证成功，会得到一个带有个人信息的Cookie文件，
	 * 验证失败，则没有Cookie文件返回；
	 */
	public boolean getCookies(String forWhat,String stuid,String pwd) throws ParseException, IOException{
		List<NameValuePair> nvps = new ArrayList <NameValuePair>();
		if(forWhat.equals("ChengJi")){
			httpPost = new HttpPost("http://202.207.177.15:7777/pls/wwwbks/bks_login2.login");
			nvps.add(new BasicNameValuePair("stuid", stuid));
			nvps.add(new BasicNameValuePair("pwd", pwd));
		}else if(forWhat.equals("JiaoPing")){
			httpPost = new HttpPost("http://202.207.177.15:8081/jxpg/login.jsp");
			nvps.add(new BasicNameValuePair("userName", stuid));
			nvps.add(new BasicNameValuePair("password", pwd));
			nvps.add(new BasicNameValuePair("identity","student"));
		}else if (forWhat.equals("GuaKe")){
			
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "GB2312"));
		response = httpClient.execute(httpPost);
		entity = response.getEntity();
		
		//这一句太TMD重要了！要是不Consume这个Entity的话，下面的一步就根本没法实施！
		EntityUtils.consume(entity);
        
		cookieStore = httpClient.getCookieStore();
		List<Cookie> cookies = httpClient.getCookieStore().getCookies();
        
		return !cookies.isEmpty();
	} //getCookies();
	
	/**
	 * 开始访问登陆成功后的Html欢迎界面，只为了获得里面的一些诸如学院、学生姓名、学号之类的信息；			
	 */
	public ArrayList<String> getLoginHtml(){
		ArrayList<String> alLoginHtml = null;
		HttpGet httpGet = new HttpGet("/pls/wwwbks/bks_login2.loginmessage");	
		alLoginHtml = getTheHtml(httpGet,7777);
		return alLoginHtml;
	} //getLoginHtml();
						
	/**
	 * 开始访问【已修课程列表】页面，并得到已修成绩的Html源文件，把源文件的每一行代码都存入ArrayList中；
	 */
	public ArrayList<String> getPassedClassesHtml(){
		ArrayList<String> alPastGPHtml = null;
		HttpGet httpGet = new HttpGet("/pls/wwwbks/bkscjcx.yxkc");	
		alPastGPHtml = getTheHtml(httpGet , 7777);
		return alPastGPHtml;
	} //getPastGPHtml()；
		
	/**
	 * 开始访问【本学期成绩】页面，得到并返回存放该页面源代码的ArrayList
	 */
	public ArrayList<String> getChengJiHtml(){
		ArrayList<String> alChengJiHtml = null;
		HttpGet httpGet = new HttpGet("/pls/wwwbks/bkscjcx.curscopre");	
		alChengJiHtml = getTheHtml(httpGet, 7777);
		return alChengJiHtml;
	} //getChengJiHtml()；
		
	/**
	 * 开始访问【曾不及格课程】页面；
	 */
	public ArrayList<String> getAllFailedClassesHtml(){
		ArrayList<String> alAllFailedClassesHtml = null;
		HttpGet httpGet = new HttpGet("/pls/wwwbks/bkscjcx.bjgkc");	
		alAllFailedClassesHtml = getTheHtml(httpGet, 7777);
		return alAllFailedClassesHtml;
	} //getChengJiHtml()；
		
	/**
	 * 开始访问【所有待评估课程】页面，得到其Html源文件
	 */
	public ArrayList<String> getJiaoPingListHtml(){
		ArrayList<String> alJiaoPingListHtml = null;
		HttpGet httpGet = new HttpGet("/jxpg/list_wj.jsp");	
		alJiaoPingListHtml = getTheHtml(httpGet, 8081);
		return alJiaoPingListHtml;
	} //getJiaoPingList()；

	/**
	 * 【公用的】获取相关html源文件的方法，只需给它请求的页面地址即可；
	 */
	public ArrayList<String> getTheHtml(HttpRequest httpRequest,int port){
		httpHost = new HttpHost("202.207.177.15", port, "http");
		String line = null;
		InputStream is = null;
		ArrayList<String> alGetTheHtml = new ArrayList<String>();
		HttpGet httpGet = (HttpGet) httpRequest;	
		try {
			response = httpClient.execute(httpHost,httpGet);
			entity = response.getEntity();				
			is = entity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"GBK"));
			while ((line = br.readLine()) != null) {
				alGetTheHtml.add(line);
			}
			br.close();
			is.close();		
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				EntityUtils.consume(entity) ;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return alGetTheHtml;
	} //getTheHtml();
	
	/**
	 * 好吧，只好单独写一个专门负责用HttpPost方法提交一组数据的方法了↓
	 * 这个方法的作用【负责给各个课程的教学评估的页面提交数据】
	 */
	public String postValues( ArrayList<ArrayList<String>> al ) throws ClientProtocolException, IOException{
		String name = null;
		String halfUrl = null;
		String successClasses = null;
		int num = al.size() + 1 ;  // num用来标识，有多少次提交数据成功了,当num减为0的时候，说明全部提交成功；
		for (int i = 0 ; i < al.size() ; i ++) {
			name = al.get(i).get(0);
			halfUrl= al.get(i).get(1);
			HttpGet httpGet = new HttpGet("/jxpg/" + halfUrl);	
			getTheHtml(httpGet, 8081);
			halfUrl = halfUrl.replaceFirst( "pg", "answer" ) ;
			List<NameValuePair> nvps = new ArrayList <NameValuePair>();;
			httpPost = new HttpPost(new String("http://202.207.177.15:8081" + "/jxpg/" + halfUrl));
			String []s = {"0000000002","0000000004","0000000005","0000000006","0000000007","0000000008","0000000009","0000000010","0000000011","0000000013"};
			for(int j=0 ; j<10 ; j++){
				nvps.add(new BasicNameValuePair(s[j], "1.0"));
			}
			nvps.add(new BasicNameValuePair("zgpj", " "));
			nvps.add(new BasicNameValuePair("kg", "否"));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps,HTTP.UTF_8));
			response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 304 ) {
					num--;
					successClasses = "," + name ;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {e.printStackTrace();}
		} 
		if ( num == 0){
			return successClasses;
		} else {
			return null;
		}
	} //for();
	
	/**
	 * 开源包中说到的很重要的一步，就是在最后一定要释放httpClient占用的资源！
	 */
	public void releaseResouces(){
		httpClient.getConnectionManager().shutdown();
	}
} //class ClientConnWithServer






// 以下是暂时搁置的代码
/*//在教评时需要特别验证一下entity的内容。因为无论账号密码对错，服务器都会发送一个Cookie过来
if( forWhat.equals("JiaoPing") ) {
		if(entity != null){
			String line = null;
			InputStream is = entity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while( (line = br.readLine()) != null ){
				if(line.matches(".*alert.*")){
					return false;
				}
			}
		}else {return false ;}
}*/