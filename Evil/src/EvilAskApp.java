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
		// URL of Oracle database server
		String url = "jdbc:oracle:thin:testuser/password@localhost";

		// properties for creating connection to Oracle database
		Properties props = new Properties();
		props.setProperty("user", "testdb");
		props.setProperty("password", "password");

		// creating connection to Oracle database using JDBC
		Connection conn = DriverManager.getConnection(url, props);

		Lists ll = new Lists();

		String minOne = "-1";
		int accountNum = 0;
		String remve_Acc_num = "";

		Scanner scan = new Scanner(System.in);

		System.out.println("Welcome to the Corp Savings and Loan");

		// create the initial list , populate from DB.
		populateListDB(ll, conn);
		
		for (Account lnewAcc : ll.getInitAcc()) {
			System.out.println("Account# " + lnewAcc.getAccNum());
			System.out.println("Balance: " + lnewAcc.getInitBal() + "\n");
		}
		
		String tranType = Validator
				.getType(
						scan,
						"Enter a transaction type (Add an account[A], Check[C], "
								+ "Debit card[D], Deposit[DEP], "
								+ "Remove an account[R], Withdrawal[W] or -1 to finish: ");
		while (!tranType.equals(minOne)) {
			
			Account tranAcc = new Account();
			tranAcc.setTranType(tranType);

			String numIn = Validator.getString(scan, "Enter the account#: ");
			boolean isValid = false;

			
			if (tranAcc.getTranType().equalsIgnoreCase("C")
					|| tranAcc.getTranType().equalsIgnoreCase("D")
					|| tranAcc.getTranType().equalsIgnoreCase("W")) {
				while (isValid == false) {
					for (int i = 0; i < ll.getInitAcc().size(); i++) {
						if (ll.getInitAcc().get(i).getAccNum().equals(numIn)) {
							isValid = true;
							break;
						}
					}
					if (isValid == false) {
						System.out.println("account not in the system");
						numIn = Validator.getString(scan, "Enter the account#: ");

					}
				}
				tranAcc.setAccNum(numIn);

				double amount = -Validator.getDouble(scan,
						"Enter the amount: ", 0);
				tranAcc.setAmount(amount);
				Date dateIn = Validator.getDate(scan,
						"Enter the date (mm/dd/yyy): ");
				tranAcc.setDate(dateIn);
				ll.setTranAcc(tranAcc);

			} else if (tranAcc.getTranType().equalsIgnoreCase("DEP")) {
				while (isValid == false) {
					for (int i = 0; i < ll.getInitAcc().size(); i++) {
						if (ll.getInitAcc().get(i).getAccNum().equals(numIn)) {
							isValid = true;
							break;
						}
					}
					if (isValid == false) {
						System.out.println("account not in the system");
						numIn = Validator.getString(scan, "Enter the account#: ");

					}
				}
				tranAcc.setAccNum(numIn);

				double amount = Validator.getDouble(scan, "Enter the amount: ",
						0);
				tranAcc.setAmount(amount);
				Date dateIn = Validator.getDate(scan,
						"Enter the date (mm/dd/yyy): ");
				tranAcc.setDate(dateIn);
				ll.setTranAcc(tranAcc);
			} else if (tranAcc.getTranType().equalsIgnoreCase("R")) {
				while (isValid == false) {
					for (int i = 0; i < ll.getInitAcc().size(); i++) {
						if (ll.getInitAcc().get(i).getAccNum().equals(numIn)) {
							isValid = true;
							break;
						}
					}
					if (isValid == false) {
						System.out.println("account not in the system");
						numIn = Validator.getString(scan, "Enter the account#: ");

					}
				}
				tranAcc.setAccNum(numIn);

				ll.setTranAcc(tranAcc);
				calTotalBal(ll);
				removeAccount(numIn, ll);
			} else if (tranAcc.getTranType().equalsIgnoreCase("A")) {
				String name = Validator
						.getString(scan, "Enter the name: ");
				double amount = Validator.getDouble(scan,
						"Enter the initial balance: ", 0);
				Date dateIn = Validator.getDate(scan,
						"Enter the birthdate (mm/dd/yyy): ");

				// sql to add the account
				String sql = "INSERT INTO accounts (ACCT_ID,NAME,Init_bal,birthdate) VALUES ( "
						+ "'"
						+ numIn
						+ "'"
						+ ", "
						+ "'"
						+ name
						+ "'"
						+ ", "
						+ amount
						+ ", "
						+ "TO_DATE("
						+ "'"
						+ processReverseDate(dateIn)
						+ "'"
						+ ","
						+ "'MM/DD/YYYY'))";
				PreparedStatement preStatement = conn.prepareStatement(sql);
				preStatement.executeQuery();
				conn.commit();

			} else {
				System.out.println("Done transaction questions");
				Date dateIn = Validator.getDate(scan,
						"Enter the date (mm/dd/yyy): ");
				tranAcc.setDate(dateIn);
			}

			
			tranType = Validator
					.getType(
							scan,
							"Enter a transaction type (Add an account[A], Check[C], "
									+ "Debit card[D], Deposit[DEP], "
									+ "Remove an account[R], Withdrawal[W] or -1 to finish");
			
		}
		sortList(ll);
		calTotalBal(ll);
		
		for (Account lnewAcc : ll.getInitAcc()) {
			System.out.println("Account# " + lnewAcc.getAccNum());
			System.out.println("Balance: " + lnewAcc.getInitBal() + "\n");
		}
		
		printAllTran(ll);
		updateDbAllTran(ll);

	}

	private static void sortList(Lists ll) {
		for (int i = 0; i < ll.getTranAcc().size(); i++) {
			for (int j = i; j < ll.getTranAcc().size(); j++) {
				// Sort the date first.
				if (ll.getTranAcc().get(i).getDate().compareTo(ll.getTranAcc().get(j).getDate()) > 0) {
					Account temp3 = ll.getTranAcc().get(i);
					ll.getTranAcc().set(i, ll.getTranAcc().get(j));
					ll.getTranAcc().set(j, temp3);
				}
				
				// If the dates are the same, compare the transaction types and amounts.
				if (ll.getTranAcc().get(i).getDate().compareTo(ll.getTranAcc().get(j).getDate()) == 0) {
					// If the second transaction type is "add an account", swap the order.
					if (ll.getTranAcc().get(j).getTranType().equals("A")) {
						Account temp1 = ll.getTranAcc().get(i);
						ll.getTranAcc().set(i, ll.getTranAcc().get(j));
						ll.getTranAcc().set(j, temp1);
						
					// If the second transaction type is "deposit" and the first one is any of the other types, keep the same order.
					} else if ((!ll.getTranAcc().get(i).getTranType().equals("DEP")) && ll.getTranAcc().get(j).getTranType().equals("DEP")) {
						continue;
					
					// if the first transaction type is "deposit" and the second one is any of the other types, swap the order.
					} else if (ll.getTranAcc().get(i).getTranType().equals("DEP") && (!ll.getTranAcc().get(j).getTranType().equals("DEP"))){
						Account temp2 = ll.getTranAcc().get(i);
						ll.getTranAcc().set(i, ll.getTranAcc().get(j));
						ll.getTranAcc().set(j, temp2);
						
					// If both of the transactions are not "deposit", put the one with larger amount first.
					} else if (ll.getTranAcc().get(i).getAmount() < ll.getTranAcc().get(j).getAmount()) {
						Account temp3 = ll.getTranAcc().get(i);
						ll.getTranAcc().set(i, ll.getTranAcc().get(j));
						ll.getTranAcc().set(j, temp3);
					}
				}
			}
		}
	}
		
	

	private static void updateDbAllTran(Lists ll) {
		String url = "jdbc:oracle:thin:testuser/password@localhost";
		Properties props = new Properties();
		props.setProperty("user", "testdb");
		props.setProperty("password", "password");
		try {
			Connection conn = DriverManager.getConnection(url, props);

			for (Account ltranDet : ll.getTranAcc()) {
				String sql = "insert into transactions (tran_id, acct_id, Amount, Tran_date, tran_type) values "
						+ "(tran_id_seq.nextval , "
						+ "'"
						+ ltranDet.getAccNum()
						+ "'"
						+ ","
						+ ltranDet.getAmount()
						+ ","
						+ "TO_DATE("
						+ "'"
						+ processReverseDate(ltranDet.getDate())
						+ "'"
						+ ","
						+ "'MM/DD/YYYY')" + ","
						+ "'" + ltranDet.getTranType() + "')";

				PreparedStatement preStatement1 = conn.prepareStatement(sql);
				preStatement1.executeQuery();
				conn.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void populateListDB(Lists ll, Connection conn) {
		try {
			String sql = "select Acct_id, name, init_bal from Accounts";
			PreparedStatement preStatement = conn.prepareStatement(sql);
			ResultSet result = preStatement.executeQuery();
			while (result.next()) {
				Account acc = new Account();
				acc.setAccNum(result.getString("acct_id"));
				acc.setName(result.getString("name"));
				acc.setInitBal(result.getDouble("Init_bal"));
				ll.setInitAcc(acc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static String findAccount(String Num, Lists ll) {
		for (int i = 0; i < ll.getInitAcc().size(); i++) {
			if (ll.getInitAcc().get(i).getAccNum().equals(Num)) {
				return Num;
			}
		}
		return Num;

	}

	public static void removeAccount(String remv_Acc_num, Lists ll) {

		for (int i = 0; i < ll.getInitAcc().size(); i++) {
			if (ll.getInitAcc().get(i).getAccNum().equals(remv_Acc_num)) {
				ll.getInitAcc().remove(i);
				break;
			}
		}

		for (int i = 0; i < ll.getTranAcc().size(); i++) {
			if (ll.getTranAcc().get(i).getTranType().equalsIgnoreCase("R")) {
				ll.getTranAcc().remove(i);
				break;
			}
		}

		removeFromDB(remv_Acc_num);
	}

	private static void removeFromDB(String remv_Acc_num) {
		String url = "jdbc:oracle:thin:testuser/password@localhost";
		Properties props = new Properties();
		props.setProperty("user", "testdb");
		props.setProperty("password", "password");
		try {
			Connection conn = DriverManager.getConnection(url, props);

			String sql = "select init_bal from Accounts where acct_id=" + "'"
					+ remv_Acc_num + "'";
			PreparedStatement accountBal = conn.prepareStatement(sql);
			ResultSet balance = accountBal.executeQuery();

			while (balance.next()) {
				if (balance.getDouble("init_bal") == 0) {
					// remove the account
					String sqlRmv = "delete from accounts where acct_id=" + "'"
							+ remv_Acc_num + "'";
					PreparedStatement accountRmvd = conn
							.prepareStatement(sqlRmv);
					accountRmvd.executeQuery();
					conn.commit();
				} else {
					System.out.println("Cannot close the account# "
							+ remv_Acc_num);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void printAllTran(Lists ll) {
		System.out.println("Printing the Transaction Summary");
		for (Account ltranAcc : ll.getTranAcc()) {
			for (Account lnewAcc : ll.getInitAcc()) {
				if (ltranAcc.getAccNum().equals(lnewAcc.getAccNum())) {
					System.out.println("\n");
					System.out.println("Account# " + ltranAcc.getAccNum());
					System.out.println("Account Holder's Name: "
							+ lnewAcc.getName());
					System.out.println("Transaction Type: "
							+ ltranAcc.getTranType());
					System.out.println("Amount of Transaction: "
							+ ltranAcc.getAmount());
					System.out.println("Date of Transaction: "
							+ processReverseDate(ltranAcc.getDate()));
				}

			}
		}
	}

	private static String processReverseDate(Date date) {
		SimpleDateFormat formatDate = new SimpleDateFormat("M/d/yyyy");
		return formatDate.format(date);
	}

	private static void calTotalBal(Lists ll) {

		for (Account ltranAcc : ll.getTranAcc()) {

			for (Account lnewAcc : ll.getInitAcc()) {
				if (ltranAcc.getAccNum().equals(lnewAcc.getAccNum())) {

					lnewAcc.setInitBal(ltranAcc.getAmount()
							+ lnewAcc.getInitBal());
					if (lnewAcc.getInitBal() < 0) {
						lnewAcc.setInitBal(lnewAcc.getInitBal() + (-35));
					}

				}

			}
			updateTotalBalDB(ll);

		}

	}

	private static void updateTotalBalDB(Lists ll) {

		String url = "jdbc:oracle:thin:testuser/password@localhost";

		// properties for creating connection to Oracle database
		Properties props = new Properties();
		props.setProperty("user", "testdb");
		props.setProperty("password", "password");

		// creating connection to Oracle database using JDBC
		try {
			Connection conn = DriverManager.getConnection(url, props);

			for (Account lttBal : ll.getInitAcc()) {
				String sql = "update accounts set init_bal="
						+ lttBal.getInitBal() + "where acct_id="
						+ lttBal.getAccNum();
				PreparedStatement preStatement = conn.prepareStatement(sql);
				preStatement.executeQuery();
				conn.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
