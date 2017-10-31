package FileRdr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Testdata {
	
	static mysqlConnectorCls connector = new mysqlConnectorCls("db.soic.indiana.edu","b565s15_maydas",
			"b565s15_maydas","565+md=my+sql");

	public static void main(String[] args) {
		String masterfile="D:/Training_data.csv";
		String trainfile="D:/train_data.csv";
		String testfile="D:/test_data.csv";
		FileWriter fileWriter1 = null;
		FileWriter fileWriter2 = null;
		String line;
		Connection con = connector.connect();
		try{
			BufferedReader br = new BufferedReader(new FileReader(masterfile));
			fileWriter1 = new FileWriter(trainfile);
			fileWriter2 = new FileWriter(testfile);
			int Class=1;
			boolean flag=true;
			int testcnt=0;
			boolean classchng=true;
			int count=0;
			int testcount=0;
			while((line = br.readLine()) != null) {
				if (flag){
				   fileWriter1.write(line+"\n");
				   fileWriter2.write(line+"\n");
				   flag=false;
				   continue;
				}
				if (classchng){
				  String query = "SELECT count(1) as count from file_class_mapping where class="+Class;
				  System.out.println(query);
				  Statement stmt = con.createStatement();
				  ResultSet rsClass  = stmt.executeQuery(query);
				  while(rsClass.next()){
				  count=rsClass.getInt("count");
				  testcount=(int) (0.05*count);
				  classchng=false;
				  }
				}
				  if (testcnt< testcount){
				   fileWriter2.write(line+"\n");
			   	   testcnt++;
				  }
				  else if (testcnt < count){
					  fileWriter1.write(line+"\n");
				      testcnt++;
				      if (testcnt==count){
				    	  testcnt=0;
						  classchng=true;
						  Class++;
			
				      }
				  }   
			}
			br.close();
			fileWriter1.close();
			fileWriter2.close();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
