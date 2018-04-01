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
	UnfilledPages.add(paged);
	try
	{
	    Scanner sc = new Scanner(new File(filename));
	    DataOutputStream os = new DataOutputStream(new FileOutputStream(outputname));
	    sc.nextLine();
	    while(sc.hasNextLine())
	    {
		String line = sc.nextLine();
		int id = 0;
		ArrayList<String> linetokens = new ArrayList<String>(Arrays.asList(line.split("\t", -1)));
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
		//os.writeBytes(linetokens.get(0).length()+linetokens.get(0)+linetokens.get(1).length()+linetokens.get(1)+linetokens.get(2).length()+linetokens.get(2));
		
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
		while(it2.hasNext())
		{
		    TestRecord record = it2.next();
		    System.out.println(record.getID());
		    System.out.println(record.getCode());
		    System.out.println(record.getName());
		    Bytes[] bytes = 
		}
		System.out.println("");
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
