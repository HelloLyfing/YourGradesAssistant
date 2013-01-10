package com.lyfing.olderInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.ParseException;

import com.lyfing.service.ConnWithServer;
import com.lyfing.service.DoTheMath;
import com.lyfing.service.GetHtmlFormContent;
import com.lyfing.olderInterface.ReadAndWriteInfoFile;

public class TheMain  {
	
	static String classNum = null ; //【班级号】
	static String stuid = "" ; // 此处填【用户名】
	static String pwd = "" ;  // 【密码】
	static ConnWithServer cws = new ConnWithServer() ;
	static DoTheMath doTheMath = null ;
	static GetHtmlFormContent ghfc = new GetHtmlFormContent() ;
	static UserInterface ui = new UserInterface("hello") ;
	static ReadAndWriteInfoFile rwif = new ReadAndWriteInfoFile() ;
	private static boolean yanZheng( ConnWithServer cws , int forWhat , String stuid , String pwd ){
		try {
			if (cws.getRightCookies( cws.CookieForWhat_ChengJi , stuid , pwd )) {
				return true ;
			} else {
				return false ;
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		} catch (IOException e2)  {
			e2.printStackTrace();
		}
		return false;
	}  //yanZheng();
	
	public static class NewListner implements ActionListener {
		boolean isChengJiShow = false ;
		boolean isPassedGPAShow = false ;
		String chengJiInfo = "" ;
		String passedClasses = "" ;
		
		public void actionPerformed(ActionEvent e) {			
			if ( e.getSource().equals( ui.butLogin )) {			
				if( !stuid.equals( ui.stuid.getText() ) ) {
					classNum = ui.classNum.getText() ;
					stuid = classNum + ui.stuid.getText() ;
					pwd = new String( ui.pwd.getPassword() ) ;
				}
				boolean b = yanZheng( cws , cws.CookieForWhat_ChengJi , stuid , pwd) ;
				if ( b ) {
					ArrayList<String> al = new ArrayList<String>() ;
					if ( ui.jcb.isSelected()) {
						stuid = stuid.substring( 8 ) ; // 取学号最后两位;
						al.add( classNum ) ; al.add( stuid ) ; al.add( pwd ) ;
						rwif.writeDataToFile( al ) ;
					} else {
						al.add( classNum ) ;
						rwif.writeDataToFile( al ) ;
					}
					ui.frame.setVisible( false ) ;
					ui = ui.createGUI() ;
					NewListner linstner = new NewListner() ;
					ui.butChengJi.addActionListener( linstner ) ;
					ui.butCurrentGPA.addActionListener( linstner ) ;
					ui.menuItemLogout.addActionListener( linstner ) ;
					//给 pasd.userInfo 赋值，也就是取出用户信息，存放到userInfo里面；
					doTheMath = new DoTheMath() ;
					doTheMath.userInfo = ghfc.getLoginContent( cws.getLoginHtml() ) ;
					ui.taInfo.setText( doTheMath.getUserInfo() + "，你好 ！\r\n==========\r\n");	
				}else {
					ui.taIsLegal.setVisible( true ) ;
					ui.taIsLegal.setText( "用户名或密码错误 ！ " );
				}
			} else if ( e.getSource().equals( ui.butReset )) {
				ui.stuid.setText("") ;
				ui.pwd.setText("") ;
				ui.taIsLegal.setVisible( false ) ;
			} else if ( e.getSource().equals( ui.butChengJi )){
				if ( isChengJiShow == false ) {  //决定是否响应该按钮，例如你上一个按钮就是点击的该按钮，当然就不需要再响应该按钮；
					if ( doTheMath.alChengJi == null ) {
						doTheMath.alChengJi = ghfc.getChengJiContent( cws.getChengJiHtml() ) ;
					}
					if ( chengJiInfo.equals( "" )){
						chengJiInfo += ( doTheMath.getUserInfo() + "，你好 ！\r\n==========\r\n");
						chengJiInfo += ( doTheMath.getChengJiInfo() ) ;
						chengJiInfo += ("==========") ;
					}
					ui.taInfo.setText( "" ) ;
					ui.taInfo.append( chengJiInfo ) ;
					ui.taInfo.setCaretPosition( 0 ) ;  //让滚动条的位置，总在最顶端；
					isChengJiShow = true ;
					isPassedGPAShow = false ;
				}
			} else if ( e.getSource().equals( ui.butCurrentGPA )) {
				if ( isPassedGPAShow == false ) {
					if ( doTheMath.alPassedClasses == null ){
						doTheMath.alPassedClasses = ghfc.getPassedClassesContent( cws.getPassedClassesHtml()) ;				
					}
					if ( doTheMath.alChengJi == null){
						doTheMath.alChengJi = ghfc.getChengJiContent( cws.getChengJiHtml());
					}
					if ( doTheMath.alAllFailedClasses == null ) {
						doTheMath.alAllFailedClasses = ghfc.getAllFailedClassesContent( cws.getAllFailedClassesHtml() ) ;
					}
					if ( passedClasses.equals( "" )){
						passedClasses += ( doTheMath.getUserInfo() + "，你好 ！\r\n==========\r\n");
						passedClasses += ( "你目前的平均学分绩点是：" + doTheMath.getCurrentGPA() + "\r\n(本文最下面有更详尽的GPA处理信息)\r\n") ;
						passedClasses += ( doTheMath.getPassedClassesInfo() ) ;
						passedClasses += ( doTheMath.getAllFailedClassesInfo() ) ;
						passedClasses += ( doTheMath.getBeforeAndAfterFailed() ) ;
						passedClasses += ( "==========" ) ;
					}
					ui.taInfo.setText( "" ) ;
					ui.taInfo.append( passedClasses ) ;
					ui.taInfo.setCaretPosition( 0 ) ;  //让滚动条的位置，总在最顶端；
					isPassedGPAShow = true ;
					isChengJiShow = false ;
				}
			} else if ( e.getSource().equals(ui.menuItemLogout) ) {
				cws.releaseResouces() ; //释放资源；
				cws = new ConnWithServer() ; //重新弄一个客户端模型;
				NewListner alistner = new NewListner() ;
				UserInterface.frame.setVisible( false ) ;
				
				ui = new UserInterface("hello") ;
				ui.butLogin.addActionListener( alistner ) ;
				ui.butReset.addActionListener( alistner ) ;
				ui.classNum.setText( classNum ) ;
				ui.stuid.requestFocus() ;
				ui.getRootPane().setDefaultButton( ui.butLogin ) ;
			}
		} //actionPerformed();
	}
	
	public static void main(String[] args) {
		//从文件读取用户信息；
		if( rwif.readDataFromFile()!= null ) {
			ArrayList<String> al = rwif.readDataFromFile() ;
			if (al.size() >1 ) {
				classNum = al.get(0) ;
				stuid = classNum + al.get(1) ;
				pwd = al.get(2) ;
				ui.classNum.setText( al.get(0)) ;
				ui.stuid.setText( al.get(1)) ;
				ui.pwd.setText( al.get(2) ) ;
				ui.jcb.setSelected( true ) ;
			} else if ( al.size() == 1 ) {
				classNum = al.get(0) ;
				ui.classNum.setText( al.get(0) ) ;
			}
		}
		NewListner alistner = new NewListner() ;
		ui.butLogin.addActionListener( alistner ) ;
		ui.butReset.addActionListener( alistner ) ;
		ui.stuid.requestFocus() ;
		ui.getRootPane().setDefaultButton(ui.butLogin);
   	 	
	} //main();	
}  //theMain();
