import java.util.ArrayList;
import java.util.List;

public class TestPage
{
    ArrayList<TestRecord> RecordList = new ArrayList<>();
    int RecordCount = 0;
    int bytesfilled = 0;
    int pagesize;
    public TestPage(int pagesize)
    {
        this.pagesize = pagesize;
    }
    public boolean insertRecord(TestRecord record)
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
    public TestRecord getRecord(int index)
    {
	return this.RecordList.get(index);
    }
    public int getBytesFilled()
    {
	return this.bytesfilled;
    }
    public ArrayList<TestRecord> getRecords()
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

