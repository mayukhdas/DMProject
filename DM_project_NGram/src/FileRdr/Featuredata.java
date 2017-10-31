package FileRdr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;

import ngram.NGramExtractor;

public class Featuredata {
	
	static mysqlConnectorCls connector = new mysqlConnectorCls("db.soic.indiana.edu","b565s15_maydas",
			"b565s15_maydas","565+md=my+sql");
	
	public static String parseFile(String path, String file)
	{
		try {
			
			StringBuilder line1 = new StringBuilder();
            String line;
            BufferedReader in = new BufferedReader(new FileReader(path+file));
            while((line=in.readLine())!=null)
			{   
			    line1.append(" ");
            	line1.append(line.substring(9));
			}
            String s=line1.toString().substring(1);
            System.out.println("File read");
            String s1=s.replaceAll("CC (CC )+","");
            //String s1=s.replaceAll ("\\s+"," ");
            String s2=s1.replaceAll("\\?\\? (\\?\\? )+","");
            //System.out.println("The String is"+s2);
            System.out.println("Junk removed");
            //LinkedList<String> ngrams=ngram.getNgram.bld(s2,4);
            in.close();
            return s2;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		String path = "E:/My Data/DM project/train/train/";
		Connection con = connector.connect();
		String filename="D:/feature.csv";
		
	  try{	
		  String classqry = "SELECT Id,Class from file_class_mapping";
		  String headerquery="Select ngram from final_feature";
		  Statement stmt = con.createStatement();
		  ResultSet rsClass1  = stmt.executeQuery(headerquery);
		  StringBuilder Featureheader=new StringBuilder();
		  Featureheader.append("Id,");
		  ArrayList<String> row=new ArrayList<String>();
		  while(rsClass1.next()){
			  String Feature=rsClass1.getString("ngram");
			  row.add(Feature);
			  Featureheader.append(Feature+",");
		  }
		  Featureheader.append("label");
		  FileWriter fileWriter = null;
		  fileWriter = new FileWriter(filename);
		  fileWriter.write(Featureheader.toString()+"\n");
		  fileWriter.close();
		 // fileWriter.close();
		  stmt = con.createStatement();
		  ResultSet rsClass  = stmt.executeQuery(classqry);
		  System.out.println("Query completed---Classes rcvd");
		  while(rsClass.next())
		  { 
			  fileWriter = new FileWriter(filename,true);
			  int Class =  rsClass.getInt("Class");
			  String File = rsClass.getString("Id");
			  System.out.println("The file is "+File+".bytes");
			  String ngrams=parseFile(path,File+".bytes");
			  StringBuilder Featurevec=new StringBuilder();
			  Featurevec.append(File+",");

			   for (int i=0; i<row.size();i++){

				   String ngram=row.get(i);
				   
			       if (ngrams.contains(ngram))
					   Featurevec.append("1,");
			       else
			    	   Featurevec.append("0,");
			  }
			  Featurevec.append(Class);	
			  fileWriter.write(Featurevec.toString()+"\n");
			  fileWriter.close();
			  System.out.println("Class---------: "+Class);
		   }
	  }
	  catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
