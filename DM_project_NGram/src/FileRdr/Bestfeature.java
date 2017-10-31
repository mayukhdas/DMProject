package FileRdr;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Bestfeature {
	
	static mysqlConnectorCls connector = new mysqlConnectorCls("db.soic.indiana.edu","b565s15_maydas",
			"b565s15_maydas","565+md=my+sql");
	
   public static ResultSet run_query(String query, Connection con){
	   
		ResultSet rsClass=null;
		try{
			
		    Statement stmt=con.createStatement();
		    rsClass=stmt.executeQuery(query);
		    return rsClass;
			
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   }
	   return null;
      }	
   
	public static void main(String[] args) {
		
		Connection con = connector.connect();
		
		try
		{
			int xvar=0;
			String dropquery="DROP TABLE IF EXISTS final_feature";
     	    Statement stmt = con.createStatement();
			stmt.executeUpdate(dropquery);
			String crquery="create table final_feature( ngram varchar(100),domclass varchar(100))";
			stmt = con.createStatement();
			stmt.executeUpdate(crquery);
			String classqry = "SELECT distinct Class FROM file_class_mapping group by class";
		    ResultSet rsClass=run_query(classqry,con);
		    System.out.println("Query completed-----------Classes rcvd");
		    while(rsClass.next()){
		    	
		    	int Class =  rsClass.getInt("Class");
		    	System.out.println("Fetching features for Class"+Class);
		    	if (Class==2)
		    		xvar=45;
		    	else
		    		xvar=35;
		    	
		    	String tbselect="select ngram from class"+Class+"_summary where cnt > "+ xvar;
		    	ResultSet rsngram=run_query(tbselect,con);
		    	//System.out.println("Select executed");
		    	while (rsngram.next()){
		    	   int ngclscnt=1;
		    	   String rngram=rsngram.getString("ngram");
		    	   ArrayList<Integer> domclass=new ArrayList<Integer>();
		    	   domclass.add(Class);
		    	   for (int i=1;i<=9;i++){
		    		   
		    		   if (i==Class)
		    			   continue;
		    		   
		    		   String tbcheck="Select ngram,cnt from class"+i+"_summary where ngram='"+rngram+"'";
		    		   ResultSet ncheck=run_query(tbcheck,con);
		    		  
		    		   if (ncheck.next() &&  ncheck.getInt("cnt") > 10){
		    			   System.out.println("For ngram "+rngram);
		    			   System.out.println("Class is "+i+" count is "+ ncheck.getInt("cnt"));
		    			   ngclscnt++;
		    			   domclass.add(i);
		    		   }	    	
		    	   }
		    	   
		           if (ngclscnt<=3 && ngclscnt > 0){
		        	   domclass.toString();	
		        	   String query="Select ngram from final_feature where ngram='"+rngram+"'";
		        	   ResultSet checkrow=run_query(query,con);
		        	   if (!checkrow.next()){
		        	      String tbinsert="Insert into final_feature values ('"+rngram+"','"+domclass+"')";
		        	      stmt = con.createStatement();
		   			      stmt.executeUpdate(tbinsert);
		        	  }
		           }
		         }
		    				
		      }   
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
