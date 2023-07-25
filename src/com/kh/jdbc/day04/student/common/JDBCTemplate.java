package com.kh.jdbc.day04.student.common;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.util.Properties;

public class JDBCTemplate {
	
	private Properties prop;
	
	// 디자인 패턴 : 각기 다른 소프트웨어 모듈이나 기능을 가진 응용 SW를
	// 개발할때 공통되는 설계 문제를 해결하기 위하여 사용되는 패턴임.
	// ==> 효율적인 방식을 위함!!
	// 패턴의 종류 : 생성패턴, 구조패턴, 행위패턴,...
	// 1. 생성패턴 : 싱글톤 패턴, 추상팩토리, 팩토리매서드, ...
	// 2. 구조패턴 : 컴포지트, 데코레이트, ...
	// 3. 행위패턴 : 옵저버, 스테이브, 전략, 템플릿 메서드, ...

	/*
	 * public class Singletone{ 
	 * 		private static Sinletone instance;
	 * 
	 * 		private Singletone(){}
	 * 
	 * 		public static Singleton getInstance(){ 
	 * 			if(instance == null){ 
	 * 				instance = new Sigletone(); 
	 * 			}
	 * 		 }
	 * }
	 */

	// 무조건 딱 한번만 생성되고 없을때에만 생성한다.
	// 이미 존재하면 존재하는 객체를 사용함
	private static JDBCTemplate instance;
	private static Connection conn;

	private JDBCTemplate() {}

	public static JDBCTemplate getInstance() {
		// 이미 만들어져 있는지 체크하고
		if (instance == null) {
			// 안만들어져 있으면 만들어서 사용
			instance = new JDBCTemplate();
		}
		// 만들어져 있으면 그거 사용해
		return instance;

	}

	// DBCP(DataBase Connection Pool)
	public Connection createConnection() {
		try {
			prop = new Properties();
			Reader reader = new FileReader("resources/dev.proerties");
			prop.load(reader);
			String driverName = prop.getProperty("driverName");
			String url = prop.getProperty("url");
			String user = prop.getProperty("user");
			String password = prop.getProperty("password");
			if (conn == null || conn.isClosed()) {
				Class.forName("driverName");
				conn = DriverManager.getConnection(url, user, password);
				conn.setAutoCommit(false);  // 오토 커밋 풀어주세요!
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void commit(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void rollback(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		if (conn != null) {
			try {
				if (!conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
