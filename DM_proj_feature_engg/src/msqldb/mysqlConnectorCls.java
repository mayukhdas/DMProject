package msqldb;

import java.sql.Connection;
import java.sql.DriverManager;

public class mysqlConnectorCls {
	
	String hostUrl;
	String dbName;
	String userName;
	String pwd;
	String driver="com.mysql.jdbc.Driver";


	//
	public mysqlConnectorCls(String host, String db, String user, String password)
	{
		hostUrl = "jdbc:mysql://"+host+":3306/";
		dbName = db;	
		userName = user;
		pwd = password;
		}

	public Connection connect()
	{
		Connection conn=null;
			try
			{
				Class.forName(driver).newInstance();
		        conn = DriverManager.getConnection(hostUrl+dbName,userName,pwd);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			return conn;
	}

}
