import java.util.Date;
import java.util.Scanner;

public class EvilAskApp {

	public static void main(String[] args) {
		Lists ll = new Lists();
		String minOne = "-1";
		Scanner scan = new Scanner(System.in);

		System.out.println("Welcome to the Corp Savings and Loan");

		// create the initial list , populate from DB.
		Processing.populateListDB(ll);

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
						numIn = Validator.getString(scan,
								"Enter the account#: ");

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
						numIn = Validator.getString(scan,
								"Enter the account#: ");

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
						numIn = Validator.getString(scan,
								"Enter the account#: ");

					}
				}
				tranAcc.setAccNum(numIn);

				ll.setTranAcc(tranAcc);
				Processing.calTotalBal(ll);
				Processing.removeAccount(numIn, ll);
			} else if (tranAcc.getTranType().equalsIgnoreCase("A")) {
				String name = Validator.getString(scan, "Enter the name: ");
				double amount = Validator.getDouble(scan,
						"Enter the initial balance: ", 0);
				Date dateIn = Validator.getDate(scan,
						"Enter the birthdate (mm/dd/yyy): ");
				// sql to add the account
				DBProcess.addAccount(numIn, name, amount, dateIn);
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

		Processing.sortList(ll);
		Processing.calTotalBal(ll);

		for (Account lnewAcc : ll.getInitAcc()) {
			System.out.println("Account# " + lnewAcc.getAccNum());
			System.out.println("Balance: " + lnewAcc.getInitBal() + "\n");
		}

		printAllTran(ll);
		DBProcess.updateDbAllTran(ll);

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
					System.out
							.println("Date of Transaction: "
									+ Processing.processReverseDate(ltranAcc
											.getDate()));
				}

			}
		}
	}

}
