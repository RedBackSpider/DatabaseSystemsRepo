import java.util.ArrayList;

public class HFPage
{
    ArrayList<HFRecord> RecordList = new ArrayList<>();
    int RecordCount = 0;
    int bytesfilled = 0;
    int pagesize;
    public HFPage(int pagesize)
    {
	this.pagesize = pagesize;
    }
    public boolean insertRecord(HFRecord record)
    {
	if(this.bytesfilled + record.getByteSize() > this.pagesize)
	{
	    return false;
	}
	RecordList.add(record);
	this.bytesfilled += record.getByteSize();
	return true;
    }
    public boolean checkEqualPageSize()
    {
	if(this.bytesfilled == this.pagesize)
	{
	    return true;
	}
	return false;
    }
}
