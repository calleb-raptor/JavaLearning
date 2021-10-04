import java.sql.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Manager {

    public static void initDB() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:testDB.db");
            System.out.println("Opened db...");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Accounts" + "(ID INT PRIMARY KEY NOT NULL," + "AccountName TEXT,"
                    + "DateOpened TEXT," + "DateClosed TEXT)";

            stmt.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS Trans (ID INTEGER PRIMARY KEY NOT NULL," + "AccountID INT NOT NULL,"
                    + "Amount REAL," + "Date TEXT);";

            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static int readSelection() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int menuOption = Integer.parseInt(reader.readLine());

        return menuOption;
    }

    public static void insertNewAccount(int AccountNumber, String AccountName, String DateOpened) {
        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:testDB.db");
            System.out.println("Opened db...");

            // stmt = c.createStatement();
            String sql = "INSERT INTO Accounts (ID, AccountName, DateOpened) VALUES (?, ?, ?);";
            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, AccountNumber);
            pstmt.setString(2, AccountName);
            pstmt.setString(3, DateOpened);
            pstmt.executeUpdate();
            pstmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void getAccounts() {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:testDB.db");

            String sql = "SELECT * FROM Accounts;";
            stmt = c.createStatement();
            ResultSet res = stmt.executeQuery(sql);
            while (res.next()) {
                int AccountNumber = res.getInt("ID");
                String AccountName = res.getString("AccountName");

                System.out.println("Account Number: " + AccountNumber + "\nName: " + AccountName);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void newAccount() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please fill in the details below to set up a new account");
        System.out.println("AccountNumber:\n");
        int accountNumber = Integer.parseInt(reader.readLine());

        System.out.println("Thank you, now please enter Account Name:\n");
        String accountName = reader.readLine();

        String dateOpened = "02/10/2021";
        insertNewAccount(accountNumber, accountName, dateOpened);

        System.out.println("Thank you, your account has been set up with the below details:\nAccount Number: "
                + accountNumber + "\nAccount Name: " + accountName);
        menu();
    }

    public static void showAccounts() throws IOException {
        getAccounts();
        System.out.println(
                "If you would like to view transactions for a particular account\nplease input the account number below:\n");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int accountNumber = Integer.parseInt(reader.readLine());

        if (accountNumber > 0) {
            showTransactions(accountNumber);
        } else {
            System.out.println("Invalid account number...");
        }
    }

    public static void getTransactions(int AccountNumber) {
        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:testDB.db");

            String sql = "SELECT COUNT(ID) FROM Trans WHERE AccountID = ?;";
            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, AccountNumber);

            ResultSet count = pstmt.executeQuery();
            while (count.next()) {
                int numberOfTrans = count.getInt(1);
                System.out.println("Account " + AccountNumber + " has " + numberOfTrans + " transaction(s)\n");
            }

            sql = "SELECT * FROM Trans WHERE AccountID = ?;";
            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, AccountNumber);

            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                int id = res.getInt(1);
                float amount = res.getFloat(3);
                String date = res.getString(4);

                System.out.println("Transaction ID: " + id + "\nAmount: " + amount + "\nDate: " + date + "\n");
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static String optionReader() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String option = reader.readLine();

        return option;
    }

    public static void insertTransaction(int AccountNumber, float amount, String Date) {
        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:testDB.db");
            System.out.println("Opened db...");

            // stmt = c.createStatement();
            String sql = "INSERT INTO Trans (AccountID, Amount, Date) VALUES (?, ?, ?);";
            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, AccountNumber);
            pstmt.setFloat(2, amount);
            pstmt.setString(3, Date);
            pstmt.executeUpdate();
            pstmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void newTransaction(int AccountNumber) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Add new Transaction:");
        System.out.println("Please input details below:");
        System.out.println("Amount:\n");
        float amount = Float.parseFloat(reader.readLine());
        System.out.println("Thank you, now please enter date:\n");
        String date = reader.readLine();
        insertTransaction(AccountNumber, amount, date);
        showTransactions(AccountNumber);
    }

    public static void showTransactions(int AccountNumber) throws IOException {
        System.out.println("Showing transactions for account: " + AccountNumber);

        getTransactions(AccountNumber);

        System.out.println("Would you like to input new transactions?\n");

        String option = optionReader();

        switch (option) {
            case "y":
            case "Y":
                newTransaction(AccountNumber);
                break;
            case "n":
            case "N":
                menu();
                break;
            default:
                System.out.println("Invalid Option...");
        }
    }

    public static void menu() throws IOException {
        System.out.println("Please choose from the following options:\n1. View Accounts\n2. New Account");
        int option = readSelection();
        switch (option) {
            case 1:
                showAccounts();
                break;
            case 2:
                newAccount();
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    public static void main(String[] args) throws IOException {
        initDB();
        System.out.println("Welcome to your Bank Manager\n");
        menu();
    }
}
