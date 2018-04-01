public class TestRecord
{
    String id;
    String code;
    String name;
    int namelength = 0;
    int bytesize = 0;
    public TestRecord(String id, String code, String name)
    {
	this.id = id;
	this.code = code;
	this.name = name;
	this.namelength = name.length();
	this.bytesize = id.length() + code.length() + name.length() + 4;
    }
    public String getName()
    {
	return this.name;
    }
    public String getCode()
    {
	return this.code;
    }
    public String getID()
    {
	return this.id;
    }
    public int getByteSize()
    {
	return this.bytesize;
    }
}
