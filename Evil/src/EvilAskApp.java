import java.awt.List;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class EvilAskApp {

	public static void main(String[] args) throws SQLException {
		 //URL of Oracle database server
        String url = "jdbc:oracle:thin:system/password@localhost"; 
      
        //properties for creating connection to Oracle database
        Properties props = new Properties();
        props.setProperty("user", "testdb");
        props.setProperty("password", "password");
      
        //creating connection to Oracle database using JDBC
        Connection conn = DriverManager.getConnection(url, props);

		Lists ll = new Lists();
		
		String minOne = "-1";
		int accountNum = 0;
		String remve_Acc_num="";
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Welcome to the Corp Savings and Loan");
		
//		while(Num != -1){
//			if(Num != -1)
//				accountNum = Num;
//			else
//				break;
//			Account newAcc = new Account();
//			newAcc.setAccNum(accountNum);
//			
//			System.out.println("Enter name of the account# " +accountNum );
//			String name = scan.next();
//			newAcc.setName(name);
//			
//			System.out.println("enter the balance for account# " +accountNum);
//			double initBal = scan.nextDouble();
//			newAcc.setInitBal(initBal);
//			
//			newAcc.getAccNum();
//			newAcc.getName();
//			newAcc.getInitBal();
//			
//			// set the init array list with values
//			ll.setInitAcc(newAcc);
//			System.out.println("Enter an account # or -1 to stop entering accounts");
//			Num = scan.nextInt();	
//			
//		}
		
		String tranType = Validator.getType(scan, "Enter a transaction type (Add an account[A], Check[C], Debit card[D], Deposit[DEP], Remove an account[R], Withdrawal[W] or -1 to finish");
		while(!tranType.equals(minOne)){
			Account tranAcc = new Account();
			tranAcc.setTranType(tranType);
			
			System.out.print("Enter the account#: ");
			String numIn = Validator.getString(scan, "Enter the account#: ");
			tranAcc.setAccNum(numIn);			
			
			if (tranAcc.getTranType().equalsIgnoreCase("C")
					|| tranAcc.getTranType().equalsIgnoreCase("D")
					|| tranAcc.getTranType().equalsIgnoreCase("W")) {
				double amount = -Validator.getDouble(scan, "Enter the amount: ", 0);
				scan.nextLine();

				tranAcc.setAmount(amount);
				Date dateIn = Validator.getDate(scan, "Enter the date (mm/dd/yyy): ");
				tranAcc.setDate(dateIn);
				ll.setTranAcc(tranAcc);

			} else if (tranAcc.getTranType().equalsIgnoreCase("DEP")) {
				double amount = Validator.getDouble(scan, "Enter the amount: ", 0);
				tranAcc.setAmount(amount);
				Date dateIn = Validator.getDate(scan, "Enter the date (mm/dd/yyy): ");
				tranAcc.setDate(dateIn);
				ll.setTranAcc(tranAcc);
			} else if(tranAcc.getTranType().equalsIgnoreCase("R")){
				ll.setTranAcc(tranAcc);
				remve_Acc_num = findAccount(tranAcc.getAccNum(), ll);
			}
			else {
				System.out.println("Done transaction questions");
				Date dateIn = Validator.getDate(scan, "Enter the date (mm/dd/yyy): ");
				tranAcc.setDate(dateIn);
			}
			
		
			tranType = Validator.getType(scan, "Enter a transaction type (Add an account[A], Check[C], Debit card[D], Deposit[DEP], Remove an account[R], Withdrawal[W] or -1 to finish");
		}
		
		//calculate total tax
		calTotalBal(ll);
		// remove account
		removeAccount(remve_Acc_num, ll);
			
		//print the total
		printDetails(ll);
		// print all the transactions
		printAllTran(ll);

		// remove an account if the balance is less than zero
		
	}

	private static String findAccount(String Num, Lists ll) {
		for(int i=0; i<ll.getInitAcc().size(); i++){
			if(ll.getInitAcc().get(i).getAccNum().equals(Num)){
				return Num;
			}
		}
		return Num;
		
	}

	public static void removeAccount(String remv_Acc_num, Lists ll){
				
		for(int i=0; i<ll.getInitAcc().size();i++){
			if(ll.getInitAcc().get(i).getAccNum().equals(remv_Acc_num)){
				ll.getInitAcc().remove(i);
				System.out.println("Removed from the account# " +remv_Acc_num);
			}
		}
	}
		
	
	private static void removeAccount(Lists ll) {
		for(int i = 0; i< ll.getInitAcc().size(); i++){
			if(ll.getInitAcc().get(i).getTotalBal() <= 0){
				ll.getInitAcc().get(i).getAccNum();
				
			}
		}
	}


	private static void printAllTran(Lists ll) {
		System.out.println("Printing the Transaction Summary");
		for(Account ltranAcc : ll.getTranAcc()){
			for(Account lnewAcc : ll.getInitAcc()){
				if(ltranAcc.getAccNum() == lnewAcc.getAccNum()){
					System.out.println("\n");
					System.out.println("Account# " + ltranAcc.getAccNum());
					System.out.println("Account Holder's Name: " + lnewAcc.getName());
					System.out.println("Transaction Type: " +ltranAcc.getTranType());
					System.out.println("Amount of Transaction: " + ltranAcc.getAmount());
					System.out.println("Date of Transaction: " + processReverseDate(ltranAcc.getDate()));
				}
			}
		}
	}


	private static String processReverseDate(Date date) {
		SimpleDateFormat formatDate = new SimpleDateFormat("M/d/yyyy");
		return formatDate.format(date);
	}


	private static void calTotalBal(Lists ll) {
		for(Account ltranAcc : ll.getTranAcc()){
			for(Account lnewAcc : ll.getInitAcc()){
				if(ltranAcc.getAccNum() == lnewAcc.getAccNum()){
					lnewAcc.totalBal = ltranAcc.getAmount()+lnewAcc.getInitBal(); 
					if(Math.signum(lnewAcc.totalBal)==-1.0){
						lnewAcc.setOverDraft(true);
						lnewAcc.totalBal += (-35.0);
					}
					lnewAcc.setTotalBal(lnewAcc.totalBal);
				}
			}
		}
	}

	
	private static void printDetails(Lists lst) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		
		for(Account listacc : lst.getInitAcc()){
			if(listacc.overDraft==true){
				System.out.println("The balance for account# " + listacc.getAccNum() + "\t" 
						+ "is " + "-" + formatter.format(listacc.getTotalBal()));
			}else{
				System.out.println("The balance for account# " + listacc.getAccNum() + "\t" 
						+ "is " + formatter.format(listacc.getTotalBal()));
			}			
		}
	}

	
}
