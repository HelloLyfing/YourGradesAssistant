package com.lyfing.service;

import java.util.ArrayList;

/**
 * 由于本程序涉及GPA的计算等繁杂的数据处理过程，所以在此专门建立一个类，用来进行数据的计算处理操作
 */
public class DoTheMath {
	
	//下面几个ArrayList当中的内容是啥？此页面有解释：http://126.am/tzdNY2
	public ArrayList<ArrayList<String>> alPassedClasses = null;
	public ArrayList<ArrayList<String>> alChengJi = null;
	public ArrayList<ArrayList<String>> alAllFailedClasses = null;
	public ArrayList<ArrayList<String>> alTemp = null;
	public String userInfo = null;
	public String chengJiInfo = "";
	public String beforeAndAfterFailed = "";
	
	/**
	 * 得到已修课程的平均学分绩点，即Grade Point Average.
	 */
	public String getCurrentGPA(){
		//下面的的两个变量，第一个是统计总的学分绩点之和，第二个是统计(每科的学分绩点X学分)之和.
		//CP=ClassPoint, GP = GradePoint.
		float allXueFen = 0;
		float all_XF_Mul_JD = 0;  //XF=学分 JD=绩点
		String name=null;
		float xuefen = 0.0f;
		
		//刚开始用[成绩1]来表示所有成绩值，
		//当成绩为字符串类型，形如"优秀"等，则仍然用[成绩1]来保存成绩值，
		//当成绩为浮点型时，形如"87.6"时，就需要用[成绩2]来保存浮点型的成绩值了.
		String chengji1 = null;
		float chengji2 = 0.0f;
		
		//连接 上述两个ArrayList，存入alTemp当中
		alTemp = new ArrayList<ArrayList<String>>();
		alTemp.addAll(alPassedClasses);
		alTemp.addAll(alChengJi);
		
		//先得到【已修课程】和【本学期成绩】的所有课程(学分X绩点)总和，以及所有课程(学分)的总和信息
		for(int i=0; i<alTemp.size(); i++){
				chengji1 = alTemp.get(i).get(5); //成绩

				//有可能成绩还没出来，为空，所以跳过；
				if (chengji1.equals("") || chengji1.equals(" ")){
					continue;
				}
				xuefen=Float.valueOf(alTemp.get(i).get(3)).floatValue();//课程学分；
				switch( scoreLeverNum(chengji1)){
					case 10 :
						allXueFen += xuefen;
						all_XF_Mul_JD += xuefen * 4.5 ;
						break;
					case 8 :
						allXueFen += xuefen;
						all_XF_Mul_JD += xuefen * 3.5 ;
						break;
					case 7 :
						allXueFen += xuefen;
						all_XF_Mul_JD += xuefen * 2.5 ;
						break;
					case 6 :
						allXueFen += xuefen;
						all_XF_Mul_JD += xuefen * 1.5 ;
						break;
					case 0 :
						//为什么此处不作处理？ 因为没通过的成绩也会出现在这里；而下面会计算所有曾不及格成绩；
						break;
					case 1 :						
						chengji2= (Float.valueOf(alTemp.get(i).get(5))).floatValue();
						if (chengji2 >=60){
							all_XF_Mul_JD += xuefen * ( (chengji2-50)/10 );
							allXueFen += xuefen;						
						}
						break;
				} //switch();
		} //for()
		float f = all_XF_Mul_JD/allXueFen ;
		
		
		
//不算挂科成绩之前的GPA!!!
		f =  (float)( (int)(f*1000+5)/10 )/100 ;
		
		
		
		beforeAndAfterFailed += "------\r\n不算挂科成绩之前，∑(每科学分X绩点)= " + all_XF_Mul_JD + "，∑(每科学分)=" + allXueFen + "\r\n";		
		beforeAndAfterFailed += "不算曾挂科成绩之前GPA为：" + Float.toString( f ) + "\r\n";		
		
		//下面这个部分，得到【曾不及格成绩】的总(学分)，为什么不算绩点了？因为不及格课程绩点肯定为零！
		
		for (int j = 0 ; j < alAllFailedClasses.size() ; j ++ ){
			xuefen = Float.valueOf( alAllFailedClasses.get(j).get(3)).floatValue();	 //课程学分；
			allXueFen += xuefen ;
		}
		f = all_XF_Mul_JD/allXueFen ;
		
		
		
//算上挂科成绩之后的GPA!!!
		f =  (float)( (int)(f*1000+5)/10 )/100 ;  //取小数点前两位，后面的数四舍五入； 
		
		
		
		beforeAndAfterFailed += "算上挂科成绩之后，∑(每科学分X绩点)= " + all_XF_Mul_JD + "，∑(每科学分)=" + allXueFen + "\r\n" ;
		beforeAndAfterFailed += "算上挂科成绩后GPA降为：" + Float.toString( f ) + "\r\n" ;
		return Float.toString( f ) ;  //成绩绩点
	} //getPastGPA();

	/**
	 * 自定义的【本学期成绩】打印内容
	 * @return
	 */
	public String getChengJiInfo () {
		String isEmptyInfo = "" ;  //储存未出分的课程信息；
		String unPassedInfo = "" ; //储存不及格的课程信息；
		String passedInfo = "" ; //储存及格的课程信息；
		String name = null ;
		String chengJi = null ;
		String xueFen = null ;
		int hasScoreNum = 0 ;  //已出成绩的课程数目；
		int unPassedNum = 0 ; //这个挂科数，可是我最关心的了；
		int passedNum = 0 ;   //通过课程的数目；
		
		for(int i = 0;i<alChengJi.size();i++){
			name = alChengJi.get(i).get(1) ;
			chengJi = alChengJi.get(i).get(5) ;
			xueFen = alChengJi.get(i).get(3) ;
			if ( chengJi.equals(" ") || chengJi.equals("") ){
				isEmptyInfo += name + "\r\n" ;
			} else if ( scoreLeverNum(chengJi) == 0  || scoreLeverNum(chengJi) == 1 && Float.valueOf(chengJi).floatValue() < 60.0 ) {
				hasScoreNum ++ ;
				unPassedNum ++ ;
				unPassedInfo += name + " | 成绩:【" + chengJi + "】\r\n" ;
			} else {
				hasScoreNum ++ ;
				passedNum ++ ;
				passedInfo += name + " | 成绩:【" + chengJi + "】\r\n" ;
			}
		} //for();

		if (hasScoreNum == 0 ){
			chengJiInfo += "目前【所有科目成绩】都【还没出来】了\r\n以下是本学期所有课程：\r\n\r\n" + isEmptyInfo ;
			return chengJiInfo ;
		} else {
			if ( unPassedNum == 0 ) { chengJiInfo += "恭喜！本学期你还没有挂科哦！" + "\r\n\r\n" ; }
			if ( unPassedNum > 0 ) { chengJiInfo += "【不及格】课程成绩如下..." + "\r\n" + unPassedInfo ;}
			if ( passedNum > 0 ) { chengJiInfo += "======\r\n" + "【已及格】课程成绩如下..." + "\r\n" + passedInfo ; }
			if ( hasScoreNum > 0 && !isEmptyInfo.equals("") ) {chengJiInfo += "======\r\n" + "【还没出来】的课程如下..." + "\r\n" + isEmptyInfo ; }
			return chengJiInfo ;
		}
	} //showChengJi();
	
	public String getUserInfo() {
		return userInfo ;
	}
	
	public String getBeforeAndAfterFailed() {
		return beforeAndAfterFailed ;
	}
	public String getPassedClassesInfo () {
		String s = "" ;
		
		s += "以下是所有已通过的课程信息\r\n" ;
		s += "课程内容排序为：课程名【成绩】    学分\r\n\r\n" ;
		for (int i = 0 ; i < alPassedClasses.size() ; i++ ) {
			s += alPassedClasses.get(i).get(1) ;
			s += "【" + alPassedClasses.get(i).get(5) + "】    " ;
			s += alPassedClasses.get(i).get(3) + "分\r\n" ;
		}
		return s ;
	}
	
	public String getAllFailedClassesInfo () {
		String s = "" ;
		s += "------\r\n以下是所有曾不及格课程信息\r\n" ;
		for ( int i = 0 ; i < alAllFailedClasses.size() ; i++ ) {
			s += alAllFailedClasses.get(i).get(1) ;
			s += "【" + alAllFailedClasses.get(i).get(5) + "】    " ;
			s += alAllFailedClasses.get(i).get(3) + "分\r\n" ;
		}
		return s ;
	}
	
	/**
	 * 由于成绩分两种，第一种是字符型的，形如"优秀"、"良好"等，另一种是浮点型的，形如"87.5"
	 * 判断成绩到底是如“优秀”这种字符串型的，还是像“89.7”这种浮点型的	
	 * @param s 实际的成绩值
	 * @return 
	 */
	private int scoreLeverNum (String s) {
		if ( s.equals("优秀") ){
			return 10 ;
		}else if( s.equals("良好") ){
			return 8 ;
		}else if( s.equals("中") ){
			return 7 ;
		}else if( s.equals("及格") ){
			return  6 ;
		}else if( s.equals("不及格")){
			return 0 ;
		}
		return 1 ;  //返回 “1” 代表它是数字类型的成绩，如82；
	}

} //class ProcessAndStoreData{}
