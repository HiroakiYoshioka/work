package unit.user.parts;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.*;
import javax.sql.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import user.bean.RegistrantInfo;
import user.parts.RegInfDAO;

public class RegInfDAOTest {
	
	private RegInfDAO regInfDao = null;
	private Connection conn = null;
	private PreparedStatement pstm	= null;
	
	@BeforeClass
	public static void beforeCls(){

	    try {
		    System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
		    System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
	    	
			InitialContext ic = new InitialContext();
		    ic.createSubcontext("java:");
		    ic.createSubcontext("java:comp");
		    ic.createSubcontext("java:comp/env");
		    ic.createSubcontext("java:comp/env/jdbc");
		    //ic.createSubcontext("java:comp/env/jdbc/task");
		    
		    MysqlDataSource ds = new MysqlDataSource();
		    ds.setUser("root");
		    ds.setPassword("root");
		    ds.setURL("jdbc:mysql://localhost/task");
		    ic.bind("java:comp/env/jdbc/task", ds);

		} catch (NamingException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInsert() {		
		RegistrantInfo regInfo = new RegistrantInfo("004", "�����H����", "28");
		
		System.out.println("UT002-001 START");
		regInfDao.insert(regInfo.getrId(), regInfo.getrName(), regInfo.getrAge());
		System.out.println("UT002-001 END");
		
		System.out.println("-------------------------");
		regInfo = null;
	}

	@Test
	public void testUpdate() {
		RegistrantInfo regInfo = new RegistrantInfo("002", "Michael", "29");
		
		System.out.println("UT002-002 START");
		regInfDao.update(regInfo.getrId(), regInfo.getrName(), regInfo.getrAge());
		System.out.println("UT002-002 END");
		
		System.out.println("-------------------------");
		regInfo = null;
	}

	@Test
	public void testDelete() {
		RegistrantInfo regInfo = new RegistrantInfo("001", "��ؑ��Y", "35");
		
		System.out.println("UT002-003 START");
		regInfDao.delete(regInfo.getrId());
		System.out.println("UT002-003 END");
		
		System.out.println("-------------------------");
		regInfo = null;
	}

	@Test
	public void testGetReglist() {
		ArrayList<RegistrantInfo> arrayRegInfo = null;
		RegistrantInfo regInfo = null;
		
		System.out.println("UT002-004 START");
		arrayRegInfo = regInfDao.getReglist();
		
		for(int n=0; n < arrayRegInfo.size(); n++){
			regInfo = arrayRegInfo.get(n);
			System.out.println(regInfo.getrId()+","+regInfo.getrName()+","+regInfo.getrAge());
		}
		System.out.println("UT002-004 END");
	}

	@Test
	public void testGetNextId() {
		// DB���̏����폜
		startDB();
		deleteDB();
		System.out.println("��Before DB List(DB���폜��)");
		selectAllDB();
		closeDB();
		
		System.out.println("UT002-005 START");
		String nextId = regInfDao.getNextId();
		System.out.println("nextId:" + nextId);
		System.out.println("UT002-005 END");
	}
	
	@Before
	public void before(){
		// �����O��DB���̏o�́i���\�b�h�̌Ăяo���j
		System.out.println("=========================");
		System.out.println("��Before DB List");
		startDB();
		selectAllDB();
		closeDB();
		// �C���X�^���X����
		regInfDao = new RegInfDAO();
	}

	@After
	public void after(){		
		// �C���X�^���X���
		regInfDao.close();
		regInfDao = null;

		// �������DB���̏o�́i���\�b�h�Ăяo���j
		System.out.println("��After DB List");
		startDB();
		selectAllDB();
		
		// DB�̍폜(DELETE FROM task.registrants)
		deleteDB();
		
		// DB�̍Đݒ�
		resetDB();
		
		closeDB();
	}
		
	public void startDB(){
		String dbUser = "root";
		String dbPass = "root";
		String url = "jdbc:mysql://localhost:3306/task";
		
		try {
			conn = DriverManager.getConnection(url, dbUser, dbPass);
		} catch (SQLException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}
	}
	
	// DB���o�̓��\�b�h
	public void selectAllDB(){
		ResultSet rslt = null;
		
		try{
			pstm = conn.prepareStatement("SELECT * FROM task.registrants");
			rslt = pstm.executeQuery();
			
			while(rslt.next()){
				System.out.println(rslt.getString("registrant_id")
						+ "," + rslt.getString("registrant_name")
						+ "," + rslt.getString("registrant_age")
						);
			}
			System.out.println("-------------------------");
		}
		catch (SQLException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}
		finally{
			try{
				if(rslt != null){
					rslt.close();
				}
			}
			catch(Exception e){
				
			}
		}
	}
	
	// DB���̏���S�폜���郁�\�b�h
	public void deleteDB(){
		try {
			pstm = conn.prepareStatement("DELETE FROM task.registrants");
			pstm.executeUpdate();
			
		}
		catch (SQLException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}
	}
	
	// DB�̍Đݒ���s�����\�b�h
	public void resetDB(){
		try {
			pstm = conn.prepareStatement("INSERT INTO `registrants` VALUES ('001','��ؑ��Y','35'),('002','Tommy','25'),('003','�R�c�Ԏq','30')");
			pstm.executeUpdate();
			
		}
		catch (SQLException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}
	}
	
	public void closeDB(){
		try{
			
			if(pstm != null){
				pstm.close();
			}
			
			if(conn != null){
				conn.close();
			}
				
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
