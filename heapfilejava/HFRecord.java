public class HFRecord
{
    String bn_name;
    String register_name;
    String bn_state;
    String bn_reg_dt;
    String bn_cancel_dt;
    String bn_renew_dt;
    String bn_state_num;
    String bn_state_of_reg;
    Long bn_abn;
    public heapfilejava(String nbn_name, String nregister_name, String nbn_state, String nbn_reg_dt,String nbn_cancel_dt,String nbn_renew_dt,String nbn_state_num, String bn_state_of_reg, Long bn_abn)
    {
	this.bn_name = nbn_name;
	this.register_name = nregister_name;
	this.bn_state = nbn_state;
	this.bn_reg_dt = nbn_reg_dt;
	this.bn_cancel_dt = nbn_cancel_dt;
	this.bn_renew_dt = nbn_renew_dt;
	this.bn_state_num = nbn_state_num;
	this.bn_state_of_reg = nbn_state_of_reg;
	this.bn_abn = nbn_abn;	
    }
    public String getBNName()
    {
	return bn_name;
    }
    public String getBNNameSize()
    {
	return bn_name.length();
    }
    public String getRegName()
    {
	return register_name;
    }
    public String getRegName()
    {
	return register_name.length();
    }
    public String getState()
    {
	return bn_state;
    }
    public String getState()
    {
	return bn_state.length();
    }
    public String getRegDT()
    {
	return bn_reg_dt;
    }
    public String getRenewDt()
    {
	return bn_reg_dt.length();
    }
    public String getCancelDt()
    {
	return bn_cancel_dt;
    }
    public String getRenewDt()
    {
	return bn_cancel_dt.length();
    }
    public String getRenewDt()
    {
	return bn_renew_dt;
    }
    public String getRenewDt()
    {
	return bn_renew_dt.length();
    }
    public String getStateNum()
    {
	return bn_state_num;
    }
    public String getStateNum()
    {
	return bn_state_num.length();
    }
    public String getStateReg()
    {
	return bn_state_of_reg;
    }
    public String getStateReg()
    {
	return bn_state_of_reg.length();
    }
    public String getStateReg()
    {
	return bn_abn;
    }
}
