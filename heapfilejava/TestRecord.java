public class TestRecord
{
    String id;
    String code;
    String name;
    int bytesize = 0;
    public TestRecord(String id, String code, String name)
    {
	this.id = id;
	this.code = code;
	this.name = name;
	this.bytesize = id.length() + code.length() + name.length();
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
