public class HFRecord
{
    String bn_name;
    String register_name;
    String bn_status;
    String bn_reg_dt;
    String bn_cancel_dt;
    String bn_renew_dt;
    String bn_state_num;
    String bn_state_of_reg;
    Long bn_abn;
    int bytesize = 0;
    public HFRecord(String nregister_name, String nbn_name, String nbn_status, String nbn_reg_dt,String nbn_cancel_dt,String nbn_renew_dt,String nbn_state_num, String nbn_state_of_reg, Long nbn_abn)
    {
	this.bn_name = nbn_name;
	this.register_name = nregister_name;
	this.bn_status = nbn_status;
	this.bn_reg_dt = nbn_reg_dt;
	this.bn_cancel_dt = nbn_cancel_dt;
	this.bn_renew_dt = nbn_renew_dt;
	this.bn_state_num = nbn_state_num;
	this.bn_state_of_reg = nbn_state_of_reg;
	this.bn_abn = nbn_abn;	
	this.bytesize = this.getBNNameSize() + this.getRegNameSize() + this.getStatusSize() + this.getRegDtSize() + this.getCancelDtSize() + this.getRenewDtSize() + this.getStateNumSize() + this.getStateRegSize() + 8 + 7 * 4; //longsize
    }
    public int getByteSize()
    {
	return this.bytesize;
    }
    public String getBNName()
    {
	return this.bn_name;
    }
    public int getBNNameSize()
    {
	return this.bn_name.length();
    }
    public String getRegName()
    {
	return this.register_name;
    }
    public int getRegNameSize()
    {
	return this.register_name.length();
    }
    public String getStatus()
    {
	return this.bn_status;
    }
    public int getStatusSize()
    {
	return this.bn_status.length();
    }
    public String getRegDt()
    {
	return this.bn_reg_dt;
    }
    public int getRegDtSize()
    {
	return this.bn_reg_dt.length();
    }
    public String getCancelDt()
    {
	return this.bn_cancel_dt;
    }
    public int getCancelDtSize()
    {
	return this.bn_cancel_dt.length();
    }
    public String getRenewDt()
    {
	return this.bn_renew_dt;
    }
    public int getRenewDtSize()
    {
	return this.bn_renew_dt.length();
    }
    public String getStateNum()
    {
	return this.bn_state_num;
    }
    public int getStateNumSize()
    {
	return this.bn_state_num.length();
    }
    public String getStateReg()
    {
	return this.bn_state_of_reg;
    }
    public int getStateRegSize()
    {
	return this.bn_state_of_reg.length();
    }
    public Long getABN()
    {
	return this.bn_abn;
    }
}
