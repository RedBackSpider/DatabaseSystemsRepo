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
	this.RecordCount++;
	return true;
    }
    public int getNumRecords()
    {
	return this.RecordCount;
    }
    public void setNumRecords(int num)
    {
	this.RecordCount = num;
    }
    public HFRecord getRecord(int index)
    {
	return this.RecordList.get(index);
    }
    public int getBytesFilled()
    {
	return this.bytesfilled;
    }
    public ArrayList<HFRecord> getRecords()
    {
	return this.RecordList;
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
