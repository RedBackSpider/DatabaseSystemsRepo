import java.util.ArrayList;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class dbquery
{
    public static void main(String []args)
    {
	String textQuery = "";
        String ps = "";
        int pagesize = 0;
	if(args.length != 2)
	    {
		System.out.println("Incorrect number of Arguments");
		System.exit(0);
	    }
        boolean numFound = false;
	try
	{
	    pagesize = Integer.parseInt(args[0]);
	    textQuery = args[1].toLowerCase();
	    textQuery = textQuery.replaceAll("^\"|\"$", "");
	    numFound = true;
	}
	catch(NumberFormatException e)  
	{  
	    numFound = false;  
	} 
	if(!numFound)
	{
	    try
	    {
		pagesize = Integer.parseInt(args[1]);
		textQuery = args[0].toLowerCase();
		textQuery = textQuery.replaceAll("^\"|\"$", "");
	    }
        catch(NumberFormatException e)
	    {
		System.out.println("Non-numerical pagesize entered");
		System.exit(0);
	    }
	}
        String inputname = "heap." + pagesize;
	// Searching through BNName: textQuery
	// Amount of bytes to read at a time = page size:
	ArrayList<TestPage> UnfilledPages = new ArrayList<>();
        try
	    {
		FileInputStream is = new FileInputStream(inputname);
		long startTime = System.currentTimeMillis();
		long numOfRecords = 0;
		long numOfPages = 0;
		int offset = 0; // Offset for each page
		int buffset = 0; // Offset within page, for each record
		int len = pagesize + 8; // length of a page + 8 is read (includes number of records
		byte[] buffer = new byte[len];
		long reccount = 0;
		TestRecord correctRecord; // record to be assigned
		boolean RecordFound = false;
		while((len = is.read(buffer)) != -1 && !RecordFound) //While there are still bytes to be found and a record has not been found
		{
		    numOfPages++;
		    byte[] slice = Arrays.copyOfRange(buffer, 0, 8);
		    reccount = byteToLong(slice);
		    int recnum = 0;
		    buffset = 8;
		    while(recnum < reccount && !RecordFound) // For each record in the page
		    {
			byte[] slice1 = Arrays.copyOfRange(buffer, buffset, buffset + 14); // Read BUSINESS NAMES from file
			String regname = new String(slice1);
			buffset = buffset + 14;

			byte[] slice2 = Arrays.copyOfRange(buffer, buffset, buffset + 4); // Read namelength to file
			int namelength = byteToInt(slice2);
			buffset = buffset + 4;

			byte[] slice3 = Arrays.copyOfRange(buffer, buffset, buffset + namelength); // read bnname from file
		        String bnname = new String(slice3);
			buffset = buffset + namelength;
		    
			byte[] slice4 = Arrays.copyOfRange(buffer, buffset, buffset + 4); // read status length from file
		        int statuslength = byteToInt(slice4);
			buffset = buffset + 4;

			byte[] slice5 = Arrays.copyOfRange(buffer, buffset, buffset + statuslength); // read bnstatus from file
			String bnstatus = new String(slice5);
			buffset = buffset + statuslength;
			
			byte[] slice6 = Arrays.copyOfRange(buffer, buffset, buffset + 4); // read regdate length from file
		        int reglength = byteToInt(slice6);
			buffset = buffset + 4;
			
			byte[] slice7 = Arrays.copyOfRange(buffer, buffset, buffset + reglength); // read regdt from file
			String bnregdt = new String(slice7);
			buffset = buffset + reglength;
			
			byte[] slice8 = Arrays.copyOfRange(buffer, buffset, buffset + 4); // read cancel date length from file
		        int cancellength = byteToInt(slice8);
			buffset = buffset + 4;

			byte[] slice9 = Arrays.copyOfRange(buffer, buffset, buffset + cancellength); // read bnstatus from file
			String bncanceldt = new String(slice9);
			buffset = buffset + cancellength;
			
			byte[] slice10 = Arrays.copyOfRange(buffer, buffset, buffset + 4); // read renew length from file
		        int renewlength = byteToInt(slice10);
			buffset = buffset + 4;

			byte[] slice11 = Arrays.copyOfRange(buffer, buffset, buffset + renewlength); // read bnstatus from file
			String bnrenewdt = new String(slice11);
			buffset = buffset + renewlength;
			
			byte[] slice12 = Arrays.copyOfRange(buffer, buffset, buffset + 4); // read status length from file
		        int statenumlength = byteToInt(slice12);
			buffset = buffset + 4;

			byte[] slice13 = Arrays.copyOfRange(buffer, buffset, buffset + statenumlength); // read bnstatus from file
			String bnstatenum = new String(slice13);
			buffset = buffset + statenumlength;
			
			byte[] slice14 = Arrays.copyOfRange(buffer, buffset, buffset + 4); // read status length from file
		        int statereglength = byteToInt(slice14);
			buffset = buffset + 4;

			byte[] slice15 = Arrays.copyOfRange(buffer, buffset, buffset + statereglength); // read bnstatus from file
			String bnstatereg = new String(slice15);
			buffset = buffset + statereglength;
			
			byte[] slice16 = Arrays.copyOfRange(buffer, buffset, buffset + 8); // read status length from file
		        long bnabn = byteToLong(slice16);
			buffset = buffset + 8;
			
			String bnnamelower = bnname.toLowerCase();
		        
			if(bnnamelower.contains(textQuery))
			{
			    RecordFound = true;
			    if(bnabn != -1)
				{
				    System.out.println("BN_REG_NAME: " + regname + "\nBN_NAME: " + bnname + "\nBN_STATUS: " + bnstatus + "\nBN_REG_DATE: " + bnregdt + "\nBN_CANCEL_DT: " + bncanceldt + "\nBN_RENEW_DT: " + bnrenewdt + "\nBN_STATE_NUM: " + bnstatenum + "\nBN_STATE_REG: " + bnstatereg + "\nBN_ABN: " + bnabn);
				}
			    else
				{
				    System.out.println("BN_REG_NAME: " + regname + "\nBN_NAME: " + bnname + "\nBN_STATUS: " + bnstatus + "\nBN_REG_DATE: " + bnregdt + "\nBN_CANCEL_DT: " + bncanceldt + "\nBN_RENEW_DT: " + bnrenewdt + "\nBN_STATE_NUM: " + bnstatenum + "\nBN_STATE_REG: " + bnstatereg + "\nBN_ABN: ");
				}
			}
			recnum++;
			numOfRecords++;
		    }
		}
		if(!RecordFound)
		    {
			System.out.println("No record found");
		    }
		is.close();
	        
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime);
		System.out.println("Time taken in milliseconds: " + duration);
		System.out.println("Number of records searched: " + numOfRecords);
		System.out.println("Number of pages searched: " + numOfPages);
	    }
	catch(IOException e)
	    {
		System.exit(0);
	    }
    }
    public static int byteToInt(byte[] slice)
    {
	int num = ((slice[0] & 0xFF) << 24) | ((slice[1] & 0xFF) << 16) | ((slice[2] & 0xFF) << 8) | (slice[3] & 0xFF);
	return num;
    }
    public static long byteToLong(byte[] slice)
    {
	long num = ((slice[0] & 0xFFL) << 56) | ((slice[1] & 0xFFL) << 48) | ((slice[2] & 0xFFL) << 40) | ((slice[3] & 0xFFL) << 32) | ((slice[4] & 0xFFL) << 24) | ((slice[5] & 0xFFL) << 16) | ((slice[6] & 0xFFL) << 8) | (slice[7] & 0xFFL);
	return num;
    }
}
