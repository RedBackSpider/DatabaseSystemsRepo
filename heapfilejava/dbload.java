import java.lang.Integer;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.util.Arrays;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Comparator;
import java.io.ByteArrayOutputStream;


public class dbload
{
    public static void main(String[] args)
    {
	String filename = "";
	String ps = "";
	if(args.length != 3)
	{
	    System.out.println("Incorrect number of Arguments");
	    System.exit(0);
	}
	if(args[0].equals("-p"))
	{
	    ps = args[1];
	    filename = args[2];
	}
	else if(args[1].equals("-p"))
	{
	    ps = args[2];
	    filename = args[0];
	}
	else
	{
	    System.out.println("Wrong Argument Format: Aguments must have a -p flag");
	    System.exit(0);
	}
	int pagesize = 0;
	try
	{
	    pagesize = Integer.parseInt(ps);
	
	}
	catch(NumberFormatException e)
	{
	    System.out.println("Non-numerical pagesize entered");
	    System.exit(0);
	}
	String outputname = "heap." + ps;
	ArrayList<TestPage> UnfilledPages = new ArrayList<>();
	TestPage paged = new TestPage(pagesize);
	try
	{
	    Scanner sc = new Scanner(new File(filename));
	    DataOutputStream os = new DataOutputStream(new FileOutputStream(outputname));
	    while(sc.hasNextLine())
	    {
		String line = sc.nextLine();
		ArrayList<String> linetokens = new ArrayList<String>(Arrays.asList(line.split("\t", -1)));
		System.out.println(linetokens.get(0).length());
		for(int i = 0; i < linetokens.size(); i++)
		{
		    if(linetokens.get(i).charAt(0) == '"')
		    {
			linetokens.set(i, linetokens.get(i).substring(1,linetokens.get(i).length()));
		    }
		    if(linetokens.get(i).charAt(linetokens.get(i).length()-1) == '"')
		    {
			linetokens.set(i, linetokens.get(i).substring(0,linetokens.get(i).length()-1));
		    }
		}
		System.out.println(linetokens.get(0).length());
		TestRecord test = new TestRecord(linetokens.get(0), linetokens.get(1), linetokens.get(2));
		if(test.getByteSize() > pagesize)
		{
		    System.out.println("Record cannot fit into page structure, increase page size");
		    sc.close();
		    os.close();
		    System.exit(0);
		}
		System.out.println(linetokens.get(0) + " "  + linetokens.get(1) + " "  +  linetokens.get(2) + " " + test.getByteSize());
		Iterator<TestPage> iter = UnfilledPages.iterator();
		boolean inserted = false;
		while (iter.hasNext() && !inserted)
		{
		    TestPage page = iter.next();
		    inserted = page.insertRecord(test);
		}
		if(!inserted)
		{
		    TestPage page = new TestPage(pagesize);
		    page.insertRecord(test);
		    UnfilledPages.add(page);
		}
		Collections.sort(UnfilledPages, new Comparator<TestPage>() {
			public int compare(TestPage p1, TestPage p2) {
			    return p2.getBytesFilled() - p1.getBytesFilled();
			}
		});

		
		/*if(linetokens.get(8).length() != 0)
		  {
		      abn = Long.parseLong(linetokens.get(8));
		  }*/
		// Token 0 = id
		// Token 1 = code
		// Token 2 = country
	    }
	    Iterator<TestPage> it = UnfilledPages.iterator();
	    while(it.hasNext())
	    {
		TestPage page = it.next();
		System.out.println("New Page Bytes:" + page.getBytesFilled());
		ArrayList<TestRecord> records = page.getRecords();
		Iterator<TestRecord> it2 = records.iterator();
		int pageByteCount = page.getBytesFilled();
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		byte[] writeBytes = new byte[pagesize];
		System.out.println(writeBytes.length);
		while(it2.hasNext())
		{
		    TestRecord record = it2.next();
		    byte[] recordBytes;
		    //System.out.println(record.getID());
		    //System.out.println(record.getCode());
		    //System.out.println(record.getName());
		    byte[] id = record.getID().getBytes();
		    byte[] code = record.getCode().getBytes();
		    byte[] name = record.getName().getBytes();
		    byte[] namelength = new byte[4];
		    int nl = record.getName().length();
		    namelength[0] = (byte) (nl >> 24);
		    namelength[1] = (byte) (nl >> 16);
		    namelength[2] = (byte) (nl >> 8);
		    namelength[3] = (byte) (nl);
		    bytestream.write(id);
		    bytestream.write(code);
		    bytestream.write(namelength);
		    bytestream.write(name);
		}
		writeBytes = bytestream.toByteArray();
		byte[] paddingBytes = new byte[pagesize-writeBytes.length]; 
		os.write(writeBytes,0,writeBytes.length);
		os.write(paddingBytes,0,paddingBytes.length);
		//os.writeBytes(linetokens.get(0).length()+linetokens.get(0)+linetokens.get(1).length()+linetokens.get(1)+linetokens.get(2).length()+linetokens.get(2));
	    }
	    sc.close();
	    os.close();
	}
	catch(FileNotFoundException e)
	{
	    e.printStackTrace();
	}
	catch(IOException e)
	{
	    e.printStackTrace();
	}
    }
}
