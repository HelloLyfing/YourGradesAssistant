package com.lyfing.beans;

/**
 * [课程]实体类
 * 每个课程都是一个实体
 * 
 * 如果有时间和精力的话，
 * 可以把所有 List<List<String>> 换成 List< ClassBean >
 * 意即，用包含每门课程各种信息的【实体类】ClassBean，
 * 替换同样包含每门课程各种信息的【集合类】 List<String>
 *  
 */
public class ClassBean {
	
	//详见该页面信息>  http://126.am/tzdNY2
	public String ClassID; //课程号
	public String ClassName; //课程名
	public String ClassNumber; //课程序号
	public String ClassXueFen; //课程学分
	public String ClassTestIime; //课程考试时间
	public String ClassChengJi; //课程成绩
	public String ClassAttr; //课程属性(必修，选修，考查)
	
	public String getClassID() {
		return ClassID;
	}
	public void setClassID(String classID) {
		ClassID = classID;
	}
	public String getClassName() {
		return ClassName;
	}
	public void setClassName(String className) {
		ClassName = className;
	}
	public String getClassNumber() {
		return ClassNumber;
	}
	public void setClassNumber(String classNumber) {
		ClassNumber = classNumber;
	}
	public String getClassXueFen() {
		return ClassXueFen;
	}
	public void setClassXueFen(String classXueFen) {
		ClassXueFen = classXueFen;
	}
	public String getClassTestIime() {
		return ClassTestIime;
	}
	public void setClassTestIime(String classTestIime) {
		ClassTestIime = classTestIime;
	}
	public String getClassChengJi() {
		return ClassChengJi;
	}
	public void setClassChengJi(String classChengJi) {
		ClassChengJi = classChengJi;
	}
	public String getClassAttr() {
		return ClassAttr;
	}
	public void setClassAttr(String classAttr) {
		ClassAttr = classAttr;
	}
}
