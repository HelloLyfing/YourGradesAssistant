package com.lyfing.olderInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ReadAndWriteInfoFile {
	File file = new File("./info.ini") ;
	
	public ArrayList<String> readDataFromFile (){
		ArrayList<String> al = new ArrayList<String>() ;
		if ( file.exists() ) {
			FileReader fr = null ;
			BufferedReader br = null ;
			String line = null ;
			try {
				fr = new FileReader( file ) ;
				br = new BufferedReader( fr ) ;
				if ( (line=br.readLine()).matches( "------")) {
					/* 这块写的有点罗嗦，所以需要说一下。
					 * 文件内容一共四行，第一行是“-----”用来作一般校验的；
					 * 第二三四行，分别是班级号、用户名、密码，
					 * 但是第四行的密码又需要转码后，才是真值；
					 * （因为存的时候，是转码保存的，所以取得时候需要转码取回）
					 * */
					while ( (line=br.readLine()) != null) {
						if ( !line.matches( "------" ) ) {
							// 如果不匹配 “ids”，则表明是班级、学号内容；
							// 否则的话，是密码，但密码需要转码后，才能使用；
							if ( ! ( line.split("\\.")[0] ).equals("ids")){
								al.add( (line.split("\\."))[1]);
							}else {
								al.add( disorderChars( line.split("\\.")[1])) ;
							}
						}
					}
				} else if ( line.matches( "classNum.*" )) {
					al.add( line.split("\\.")[1] ) ;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return al ;
		}else { //文件不存在的话；
			return null ;
		}
	}
	
	public boolean writeDataToFile( ArrayList<String> al ) {
		
		FileWriter fw = null ;
		BufferedWriter bw = null ;
		try {
			if ( !file.exists() ) { file.createNewFile() ; }
			fw = new FileWriter( file , false ) ;
			bw = new BufferedWriter( fw ) ;
			if ( al.size() > 1) {
				bw.write( "------" ) ; bw.newLine() ;
				bw.write( "classNum." +al.get(0) ) ; bw.newLine() ;
				bw.write( "stuid." + al.get(1)) ; bw.newLine() ;
				String s = disorderChars( al.get(2) ) ;
				bw.write( "ids." + s) ; bw.newLine() ;
			} else {
				bw.write ("classNum." + al.get(0)) ;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.flush();
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();	
			}
		}
		return true ;	
	} //writeDataToFile();
	
	private static String disorderChars(String source) {
		String result = "" ;
		char[] s1 = new char[source.length()] ;
		s1[0] = source.charAt( source.length()-1 ) ;
		s1[s1.length-1] = source.charAt(0) ;
		for ( int i = 1 ; i<source.length()-1 ; i++ ) {
			s1[i] = source.charAt(i) ;
		}
		int b = 0 ;
		for ( int j= 0 ; j<s1.length ; j ++){
			b=(int)s1[j] ;
			result += (char)(79+79-b) ;
		}
		return result;
	} //disorderChars();
	
	public boolean deleteInfoFile() {
		if ( file.exists() ) {
			file.delete() ;
			return true ;
		} else {
			return false ;
		}
	}
} //class ReadAndWriteInfoFile();
