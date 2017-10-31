package msqldb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class getFeatures {

	public static ArrayList<String> get() {
		// TODO Auto-generated method stub
		ArrayList<String> finalCols = new ArrayList<String>();
		try
		{
			mysqlConnectorCls connector  = new mysqlConnectorCls("db.soic.indiana.edu","b565s15_maydas",
				"b565s15_maydas","565+md=my+sql");
			Connection con = connector.connect();
			String qry = "Select * from final_feature";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(qry);
			while(rs.next())
			{
				String feature = rs.getString(1);
				String domClass = rs.getString(2);
				
				domClass = domClass.replaceAll("\\[|\\]| ", "");
				String[] domClsarr = domClass.split(",");
				if(domClsarr.length==1)
					finalCols.add(feature.replaceAll(" ", "_"));
				
				System.out.println(feature+"-----"+domClass);
			}
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return finalCols;
	}

}
