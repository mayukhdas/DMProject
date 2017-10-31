package FileRdr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Random;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;

import ngram.NGramExtractor;
import ngram.getNgram;

public class parseAndLoad {
	static mysqlConnectorCls connector = new mysqlConnectorCls("db.soic.indiana.edu","b565s15_maydas",
			"b565s15_maydas","565+md=my+sql");
	
	//static MultiMap<String,Integer> m = new MultiValueMap<String,Integer>();
	
	public static LinkedList<String> parseFile(String path, String file)
	{
		try {
			
			StringBuilder line1 = new StringBuilder();
            String line;
            //System.out.println("The filename is"+file);
            BufferedReader in = new BufferedReader(new FileReader(path+file));
            while((line=in.readLine())!=null)
			{   
			    line1.append(" ");
            	line1.append(line.substring(9));
			}
            String s=line1.toString().substring(1);
            System.out.println("File read");
            String s1=s.replaceAll("CC CC"," ");
            //String s1=s.replaceAll ("\\s+"," ");
            String s2=s1.replaceAll("\\?\\? \\?\\?"," ");
            //System.out.println("The String is"+s2);
            System.out.println("Junk removed");
            LinkedList<String> ngrams=ngram.getNgram.bld(s2,4);
            in.close();
            return ngrams;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void run_query(String query, Connection con){
		
		try{
			
		    Statement stmt=con.createStatement();
		    stmt.executeUpdate(query);
			
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}		
	
	public static void main(String[] args)
	{
		String path = "E:/My Data/DM project/train/train/";
		Connection con = connector.connect();
		
		try
		{   
			//PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
			//System.setOut(out);
			NGramExtractor extractor = new NGramExtractor();
			String classqry = "SELECT distinct Class,count(1) as cnt FROM file_class_mapping group by class";
			Statement stmt = con.createStatement();
			ResultSet rsClass  = stmt.executeQuery(classqry);
			System.out.println("Query completed---Classes rcvd");
			while(rsClass.next())
			{   
				
				int Class =  rsClass.getInt("Class");
				int cnt = rsClass.getInt("cnt");
				//int Class=1;
				System.out.println("-------------------------***--Class------ :"+Class);
				//----new file
				BufferedWriter bw = new BufferedWriter(new FileWriter("file_class"+Class));
				//--------------
				String classTabDrop="DROP TABLE IF EXISTS class_"+Class;
				run_query(classTabDrop,con);
				String classTabCreate = "CREATE TABLE class_"+Class+" (ngram varchar(100), Fid integer(10))";
				run_query(classTabCreate,con);
				String fileQry = "SELECT Id FROM file_class_mapping"
								+" WHERE class = "+Class;
				Statement stmt2 = con.createStatement();
				ResultSet rs = stmt2.executeQuery(fileQry);
				Random randomGenerator = new Random();
				int randomInt = 0;
				//int numFiles = 5;
				int num=1;
				while(rs.next() && num<=50)
				{
					if(randomInt>0)
					{
						randomInt--;
						continue;
					}
					LinkedList<String> ngrams=null;
					String file=rs.getString("Id");
					System.out.println("The File is"+file);
					if(!(new File(path,file+".bytes")).exists())
						continue;
					ngrams=parseFile(path,file+".bytes");
					for (String s : ngrams){
		                //m.removeMapping(s, num);
		                //m.put(s, num);
						bw.write(s+","+num+"\n");
						
					}
					System.out.println("num is"+num);
					num++;
					if (Class==5)
						randomInt=0;
					else
					     randomInt = randomGenerator.nextInt((int)(cnt/50));
					
					System.out.println(randomInt);
					//numFiles--;
					
				}
				bw.close();
			}
			con.close();
			//System.out.println(m);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	

}
