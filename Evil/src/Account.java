
public class Account {

	public int accNum;
	public String Name;
	public double initBal;
	public double amount;
	public long Date;
	public String tranType;
	public double totalBal;
	public boolean overDraft;
	
	public int getAccNum() {
		return accNum;
	}
	public void setAccNum(int accNum) {
		this.accNum = accNum;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	public double getInitBal() {
		return initBal;
	}
	public void setInitBal(double initBal) {
		this.initBal = initBal;
	}
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public double getTotalBal() {
		return totalBal;
	}
	public void setTotalBal(double totalBal) {
		this.totalBal = totalBal;
	}
	public long getDate() {
		return Date;
	}
	public void setDate(long date) {
		Date = date;
	}
	public String getTranType() {
		return tranType;
	}
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	public boolean isOverDraft() {
		return overDraft;
	}
	public void setOverDraft(boolean overDraft) {
		this.overDraft = overDraft;
	}
	
}
