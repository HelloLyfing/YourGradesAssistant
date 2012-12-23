package com.lyfing.service;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/** 
 * 1,解析从ClientConnWithServer获得的html页面的源代码，
 * 2,【得到Html表格中的数据内容】(从中取出成绩等相关的信息)
 */
public class GetHtmlFormContent {
	ArrayList<String> innerList = null;	
	ArrayList<ArrayList<String>> outerList = null;

	/** 很重要的一步，获得并打印出 登录欢迎界面的个人信息；
	 */
	public String getLoginContent(ArrayList<String> alLoginHtml){
		String loginInfo = "" ;
		boolean gate = true;
		int i=0;
		while (gate && ++i < alLoginHtml.size()){
			if( alLoginHtml.get(i).matches("^<span.*")) {
				loginInfo = alLoginHtml.get(i+1);
				gate = false;
			}
		}
		return loginInfo;
	}
	
	/** 
	 * 得到【已修课程】成绩Html源文件中的所有课程信息；
	 */
	public ArrayList<ArrayList<String>> getPassedClassesContent(ArrayList<String> alPassedGPHtml){				
		/**	遍历的过程为：
				先找到必修课程列表的入口，然后开始进入第一次大循环,等第一次大循环完成；
				同上，开始进行第二组性质的课程大循环；
				同上，开始进行第三种课程性质的大循环；	
		*/		
		class InsertInfo {  //这个Class用来把从【已修课程】源代码中取得的内容，插入、保存起来
			public void insertClassAttri(ArrayList<String> al , int i , Pattern p , int a) {
				int classAttri = 0 ;
				String content = null;
				for(int j=0;j<6;j++){
					content = getOneContent(p,al.get(i+j));
					if (!content.equals("null")){
						innerList.add(content);
						classAttri++;
						   if(classAttri % 6 == 0){
							   innerList.add(Integer.toString(a)); //这里有篇文章说明这些几种INT转String的效率问题，链接：126.am/Ummsz2
							   outerList.add(innerList);
							   innerList = new ArrayList<String>();
							   break;
						   }
					}
				} //for
			} //insertClassAttri();
		}	//class InsertInfo.
		Pattern p1 = Pattern.compile("^<td.*lign.*center\">(.+)</p></td>$",Pattern.CASE_INSENSITIVE); //匹配找出必修课的第一个模式；
		Pattern p2 = Pattern.compile("<TD>(.*)</TD>$",Pattern.CASE_INSENSITIVE); //匹配找出限选、任选课的第二模式；
		boolean one = true;
		boolean two = false;
		boolean three = false;
		String content = null;
		innerList = new ArrayList<String>();
		outerList = new ArrayList<ArrayList<String>>();
		InsertInfo ii = new InsertInfo();
		for(int a=0; a<alPassedGPHtml.size(); a++){
			//为了防止这个bug的出现(戳此：126.am/53t8o2 )，需要遍历 alPassedGPHtml
			if ( alPassedGPHtml.get(a).matches( "^</p></td>$" )) {
				String s = alPassedGPHtml.get(a-1) + "</p></td>";
				alPassedGPHtml.add(a-1, s);
				alPassedGPHtml.remove(a);
				alPassedGPHtml.remove(a);
				break;
			}
		}

		//开始遍历【已修课程】源代码，获得其中的所有课程信息
		for(int i = 0; i<alPassedGPHtml.size() ; i++){
			if (one = true && alPassedGPHtml.get(i).matches("<TR>")){
				//循环得到第一段所有成绩！
				while(!alPassedGPHtml.get(i).matches(".+TABLE>$")){ 
					content = getOneContent( p1,alPassedGPHtml.get(i) );
					if (!content.equals("null")){
						ii.insertClassAttri(alPassedGPHtml , i , p1 , 1); //传1，表示使用的第一个匹配方法
						i+=6;
					}
					i++;
				} //while
				one = false; //关闭第一次成绩的进口，开始第二次的判断；
				two = true;
			}
			if (two == true || three == true && alPassedGPHtml.get(i).matches("^<B>.+B>$")){
				int mm = two==false?3:2;  //确定是第二组或者第三的课程取值
				while(!alPassedGPHtml.get(i).matches(".+TABLE>$") ){				
					content = getOneContent(p2,alPassedGPHtml.get(i));
					if(!content.equals("null")){
						ii.insertClassAttri(alPassedGPHtml , i , p2 , mm);
						i+=6;
					}
					i++;
					if( i>(alPassedGPHtml.size()-2) ){
						break;
					}
				} //while
				two = false;three = true;
			}
		} //for()
		return outerList ;
	} //getPassedChengJiContent();

	/**
	 * 你给我
	 * 【一行字符串】和【一个正则表达式】，我就给你返回
	 * 该行字符串中【你需要的内容】(由正则表达式表示你需要的内容)
	 */
	private String getOneContent(Pattern p,String s ){
		Matcher m = p.matcher(s);
		if(m.find()){
			return m.group(1);
		}else {return "null";}		
	}	//getClassesContent
	
	/** 
	 * 在该方法第一次的使用试用中，将得到的课程信息，一共六个，全部存入allClasses里面； 
	 * 【i】说明当前字符串数组的位置，【Pattern】说明使用的匹配模式
	 * 不知道当初给哪里做的注释
	 */	

	/**
	 * 【得到教评的List目录】
	 * 突然发现处理Html源文件的方法太没有适用性了，弄得现在只好再写一个处理测评页面的了
	 */
	public ArrayList<ArrayList<String>> getJiaoPingList(ArrayList<String> al){
		outerList = new ArrayList<ArrayList<String>>();
		innerList = new ArrayList<String>();
		String content = null;
		Pattern p = null;
		int a = 1;
		boolean gate = true;
		for(int i = 0; i<al.size(); i++){
			if(al.get(i).matches(".*<td><font\\scolor=\"red\".*")){
				while(gate){
					if(a == 1){
						p = Pattern.compile(".*<td>(.+)</td>$");  //得到尚未评价的课程名
						content = getOneContent(p, al.get(i-1) );
						innerList.add(content);
						a = 2;
					}else if(a == 2){
						p = Pattern.compile(".*<td><a\\shref=\"(.+)\"\\sonclick.*</td");
						content = getOneContent(p,al.get(i+1));
						innerList.add(content);
						a = 3;
					}else if(a == 3)
						gate = false;
				} //while();
				outerList.add(innerList);
				innerList = new ArrayList<String>();
				i++;
				a = 1;
				gate = true ;
			} //if
		}
		return outerList ;
	} //getJiaoPingList
	
	/**
	 * 得到【本学期成绩】表格中的数据；
	 */ 
	public ArrayList<ArrayList<String>> getChengJiContent(ArrayList<String> al){
		outerList = new ArrayList<ArrayList<String>>();
		innerList = new ArrayList<String>();
		Pattern p = Pattern.compile("^<td\\sbgcolor=\"#EAE2F3\"><p\\salign=\"center\">(.*)</p></td>$");
		String content = null;
		int i = 0 ;
		while(i<al.size()) {
			if(al.get(i).matches("<td\\sbgcolor=\"#E2D8EF\"><p\\salign=\"center\"><strong>.*")){
					while(!al.get(i).matches("^</table>$")){
						content = getOneContent(p, al.get(i)) ;
						if(!content.equals("null")){
							for(int j=0;j<7;j++){
								content = getOneContent(p, al.get(i+j+1));
								innerList.add(content);
							}
							outerList.add(innerList);
							i += 7;
							innerList = new ArrayList<String>();
						} // if(!content.);
						i++;
					} // while();
			}
			i++;
		} // while(true);
		return outerList;
	} // getChengJiContent();
	
	/**
	 * 得到【All曾不及格课程】表格中的数据；其实下面的方法，
	 * 直接拷贝于上一个方法(getChengJiContent)；，也就是这两个方法基本一样的
	 */
	public ArrayList<ArrayList<String>> getAllFailedClassesContent(ArrayList<String> al){
		outerList = new ArrayList<ArrayList<String>>();
		innerList = new ArrayList<String>();
		Pattern p = Pattern.compile("^<td\\sbgcolor=\"#EAE2F3\"><p\\salign=\"center\">(.*)</p></td>$");
		String content = null;
		int i = 0 ;
		while(i<al.size()) {
			if(al.get(i).matches("<td\\sbgcolor=\"#E2D8EF\"><p\\salign=\"center\"><strong>.*")){
					while(!al.get(i).matches("^</TABLE>$")){
						content = getOneContent(p, al.get(i)) ;
						if(!content.equals("null")){
							for(int j=0;j<6;j++){
								content = getOneContent(p, al.get(i+j));
								innerList.add(content);
							}
							outerList.add(innerList);
							i += 6;
							innerList = new ArrayList<String>();
						} // if(!content.);
						i++;
					} // while();
			}
			i++;
		} // while( i<al.size() );
		return outerList;
	} // getAllFailedClassesContent();
	
} //GetHtmlFormContent