import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.lang.StringBuilder;
import java.util.StringTokenizer;
import java.util.Iterator;
import java.io.FileWriter;

public class App
{
    public static void main( String[] args )
    {
        try
	    {
		String csvFile1 = "/home/ec2-user/BUSINESS_NAMES_201803.csv";
		Scanner sc = new Scanner(new File(csvFile1));
		String csvFile2 = "/home/ec2-user/BUSINESS_NAMES_201803-1.csv";
		FileWriter writer = new FileWriter(csvFile2);
		boolean bool = false;
		while(sc.hasNextLine())
		    {
			String line = sc.nextLine();
			StringTokenizer st = new StringTokenizer(line, "\t");
			ArrayList<String> linetokens = new ArrayList<String>(Arrays.asList(line.split("\t", -1)));
			StringBuilder writeline = new StringBuilder();
			Iterator<String> itr = linetokens.iterator();
			while(itr.hasNext())
			    {
				String value = itr.next();
				if(bool)
				    {
					if(value.length() < 2) // if double quotes cannot be around value
					    {
						value = "\"" + value + "\"";
					    }
					else if(value.charAt(0) != '"' && value.charAt(value.length() - 1) != '"')
					    {
						value = "\"" + value + "\"";
					    }
				    }
				if(itr.hasNext())
				    {
					writeline.append(value+",");
				    }
				else
				    {
					writeline.append(value+"\n");
				    }
			    }
			bool = true;
			writer.append(writeline.toString());
		    }
		writer.flush();
		writer.close();
	    }
        catch (Exception e)
	    {
		e.printStackTrace();
	    }
    }
}
