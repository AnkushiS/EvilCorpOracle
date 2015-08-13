import java.util.Date;

public class Account {

	public String accNum;
	public String Name;
	public double initBal;
	public double amount;
	public Date date;
	public String tranType;
	public double totalBal;
	public boolean overDraft;
	
	public String getAccNum() {
		return accNum;
	}
	public void setAccNum(String accNum) {
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
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
