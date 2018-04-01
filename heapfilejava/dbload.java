import java.lang.Integer;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.util.Arrays;
import java.lang.Long;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Comparator;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

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
	ArrayList<HFPage> UnfilledPages = new ArrayList<>();
	try
	{
	    Scanner sc = new Scanner(new File(filename));
	    DataOutputStream os = new DataOutputStream(new FileOutputStream(outputname));
	    long startTime = System.currentTimeMillis();
	    long numOfRecords = 0;
	    sc.nextLine(); // Skip first line
	    while(sc.hasNextLine() && numOfRecords < 100)
	    {
		String line = sc.nextLine();
		ArrayList<String> linetokens = new ArrayList<String>(Arrays.asList(line.split("\t", -1)));
		System.out.println(line.length());
		if(linetokens.size() != 9)
		{
		    System.out.println("Wrong number of tokens");
		    sc.close();
		    os.close();
		    System.exit(0);
		}
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
		if (linetokens.get(8).isLong())
		{
		    System.out.println("Last Token is not a Long value");
		    sc.close();
		    os.close();
		    System.exit(0);
		}
		long abn = Long.parseLong(linetokens.get(8));
		//TestRecord test = new TestRecord(linetokens.get(0), linetokens.get(1), linetokens.get(2));
		HFRecord test = HFRecord(linetokens.get(0), linetokens.get(1), linetokens.get(2), linetokens.get(3), linetokens.get(4), linetokens.get(5), linetokens.get(6), linetokens.get(7));
		if(test.getByteSize() > pagesize)
		{
		    System.out.println("Record cannot fit into page structure, increase page size");
		    sc.close();
		    os.close();
		    System.exit(0);
		}
		Iterator<HFPage> iter = UnfilledPages.iterator();
		boolean inserted = false;
		while (iter.hasNext() && !inserted)
		{
		    HFPage page = iter.next();
		    inserted = page.insertRecord(test);
		}
		if(!inserted)
		{
		    HFPage page = new HFPage(pagesize);
		    page.insertRecord(test);
		    UnfilledPages.add(page);
		}
		Collections.sort(UnfilledPages, new Comparator<HFPage>() {
			public int compare(HFPage p1, HFPage p2) {
			    return p2.getBytesFilled() - p1.getBytesFilled();
			}
		});
		numOfRecords++;
	    }
	    Iterator<HFPage> it = UnfilledPages.iterator();
	    while(it.hasNext())
	    {
		HFPage page = it.next();
		int pageByteCount = page.getBytesFilled();
		long pageNumRecords = page.getNumRecords();
		System.out.println("New Page Bytes:" + pageByteCount);
		System.out.println("New Page Record Count:" + pageNumRecords);
		ArrayList<HFRecord> records = page.getRecords();
		Iterator<HFRecord> it2 = records.iterator();
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
	        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(pageNumRecords);
		byte[] writeBytes;
		bytestream.write(buffer.array());
		while(it2.hasNext())
		{
		    HFRecord record = it2.next();
		    byte[] recordBytes;
		    byte[] registername;
		    byte[] bnname;
		    byte[] bnstatus;
		    byte[] bnregdate;
		    byte[] bncanceldate;
		    byte[] bnrenewdate;
    		    byte[] bndate;


		    /*byte[] id = record.getID().getBytes();
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
		    bytestream.write(name);*/
		}
		writeBytes = bytestream.toByteArray();
		byte[] paddingBytes = new byte[pagesize+8-writeBytes.length]; 
		os.write(writeBytes,0,writeBytes.length);
		os.write(paddingBytes,0,paddingBytes.length);
	    }
	    long endTime = System.currentTimeMillis();
	    long duration = (endTime - startTime);
	    System.out.println("Time taken in milliseconds: " + duration);
	    System.out.println("Number of records inserted: " + numOfRecords);
	    System.out.println("Numbeer of pages made: " + UnfilledPages.size());
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
    public boolean isLong(String number)
    {
	try
	{
	    long l = Long.parseLong(number);
	}
	catch(NumberFormatException e)
	{
	    return false;
	}
	return true;
    }
}
