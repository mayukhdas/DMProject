package featureHandlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.util.ArrayList;

import msqldb.getFeatures;

import org.apache.commons.lang.StringUtils;

import csvReaders.loadCsv;

public class enggFeatures {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			BufferedReader bf = new BufferedReader(new FileReader("./data/train_data.csv"));
			String header = bf.readLine();
			bf.close();
			String[] cols = header.split(",");
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("./data/train_datamod.csv",false));
			ArrayList<String> qryCols = getFeatures.get();
			qryCols.add("label");
			//qryCols.add(cols[0]);
			System.out.println(qryCols.size());
			String jcols = StringUtils.join(qryCols, ',');
			bw.write(jcols+"\n");
			String qry = "SELECT "+jcols+" FROM train_data";
			loadCsv ld = new loadCsv("./data");
			ResultSet r = ld.runQry(qry);
			while(r.next())
			{
				String tuple = "";
				for(int i=0;i<qryCols.size();i++)
				{
					if(i==0)
						tuple+=r.getString(qryCols.get(i));
					else
						tuple+=","+r.getString(qryCols.get(i));
				}
				bw.write(tuple+"\n");
			}
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		modTest();
	}
	static void modTest()
	{
		// TODO Auto-generated method stub
				try {
					BufferedReader bf = new BufferedReader(new FileReader("./data/test_data.csv"));
					String header = bf.readLine();
					bf.close();
					String[] cols = header.split(",");
					
					BufferedWriter bw = new BufferedWriter(new FileWriter("./data/test_datamod.csv",false));
					ArrayList<String> qryCols = getFeatures.get();
					qryCols.add("label");
					//qryCols.add(cols[0]);
					System.out.println(qryCols.size());
					String jcols = StringUtils.join(qryCols, ',');
					bw.write(jcols+"\n");
					String qry = "SELECT "+jcols+" FROM test_data";
					loadCsv ld = new loadCsv("./data");
					ResultSet r = ld.runQry(qry);
					while(r.next())
					{
						String tuple = "";
						for(int i=0;i<qryCols.size();i++)
						{
							if(i==0)
								tuple+=r.getString(qryCols.get(i));
							else
								tuple+=","+r.getString(qryCols.get(i));
						}
						bw.write(tuple+"\n");
					}
					bw.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

}
