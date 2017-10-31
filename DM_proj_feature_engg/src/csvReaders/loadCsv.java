package csvReaders;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.relique.jdbc.csv.CsvDriver;

public class loadCsv {
	
	static Connection conn;
	static ResultSet results=null;

	public loadCsv(String filename)
	{
		
		try
	    {
	      // Load the driver.
	      Class.forName("org.relique.jdbc.csv.CsvDriver");

	      // Create a connection. The first command line parameter is
	      // the directory containing the .csv files.
	      // A single connection is thread-safe for use by several threads.
	      conn = DriverManager.getConnection("jdbc:relique:csv:" + filename);

	      

	      
	    }
	    catch(Exception e)
	    {
	      e.printStackTrace();
	    }
	}
	public void close() throws SQLException
	{
		conn.close();
	}
	public ResultSet runQry(String query)
	{
		try
		{
			
			// Create a Statement object to execute the query with.
		      // A Statement is not thread-safe.
		      Statement stmt = conn.createStatement();

		      // Select the ID and NAME columns from sample.csv
		      System.out.println(query);
		      results = stmt.executeQuery(query);

		      // Dump out the results to a CSV file with the same format
		      // using CsvJdbc helper function
		     // boolean append = true;
		      //CsvDriver.writeToCsv(results, System.out, append);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return results;
	}
	
	public static void main(String[] args)
	{
		loadCsv ld = new loadCsv("./data");
	}
	
}
