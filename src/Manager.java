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

    public static void showAccounts() {
        getAccounts();
    }

    public static void showTransactions() {
        System.out.println("This is where transactions will go");
    }

    public static void menu() throws IOException {
        System.out.println(
                "Please choose from the following options:\n1. View Accounts\n2. View Transactions\n3. New Account");
        int option = readSelection();
        if (option == 1) {
            showAccounts();
        } else if (option == 2) {
            showTransactions();
        } else if (option == 3) {
            newAccount();
        }
    }

    public static void main(String[] args) throws IOException {
        initDB();
        System.out.println("Welcome to your Bank Manager\n");
        menu();
    }
}
