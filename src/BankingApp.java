
import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.sql.*;
import java.util.Scanner;


public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/bankApp";
    private static final String username = "root";
    private static final String password = "your_password";
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            e.getStackTrace();
        }

        try{
            Connection con = DriverManager.getConnection(url,username,password);
            System.out.println("Connection Established Successfully");
            while(true){
                System.out.println("1. Registry");
                System.out.println("2. login");
                System.out.println("3. Exist");
                System.out.print("Enter your Choices : ");
                int choices = sc.nextInt();
                sc.nextLine();
                switch(choices){
                    case 1 :
                        User.register(con,sc);
                        break;

                    case 2 :
                        System.out.print("Enter email : ");
                        String email = sc.next();
                        System.out.print("Enter password : ");
                        String loginPassword = sc.next();
                        boolean b = User.login(con,sc,email,loginPassword);
                        if(b){
                            System.out.println("Login successfully");

                            if(!Account.isAccountExits(con, email)){
                                System.out.print("1. Create Account \n 2. exit \n Enter Your Choices");
                                int ch = sc.nextInt();
                                switch (ch){
                                    case 1: Account.account_open(con,sc,email); break;
                                    case 2: return;
                                    default:
                                        System.out.println("wrong input!!!!");
                                }
                            }else{
                                while(true){
                                    System.out.println("1. Credit Money \n 2. Debit Money \n 3. Transfer Money \n 4. Check Balance \n 5. Log Out \n Enter your choices : ");
                                    int ch = sc.nextInt();
                                    switch (ch){
                                        case 1:
                                            AccountServices.creditMoney(con,sc,email);
                                            break;
                                        case 2:
                                            AccountServices.debitMoney(con,sc,email);break;

                                        case 4:
                                            AccountServices.checkBalance(con,sc,email);break;

                                        case 5: return;
                                        default :
                                            System.out.println("Wrong Input");
                                    }
                                }

                            }

                        }else {
                            System.out.println("Login error");
                        }
                        break;
                    case 3 :
                        System.out.println("Thank You .....");
                        con.close();
                        System.exit(0);

                    default:
                        System.out.println("wrong input");
                }
            }
        }catch (SQLException e){
            e.getStackTrace();
        }
    }

}

class User{
    public static void register(Connection con ,Scanner sc){
        System.out.print("Enter full name: ");
        String name = sc.nextLine();
        System.out.print("Enter email : ");
        String email = sc.next();
        System.out.print("Enter password : ");
        sc.nextLine();
        String password = sc.next();
        String query = "INSERT INTO user(email,full_name,password) VALUES(?,?,?)";
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,email);
            ps.setString(2,name);
            ps.setString(3,password);
            int result = ps.executeUpdate();
            if(result>0) System.out.println("Registration Successfully ");
            else System.out.println("Registration not Successfully ");
        }catch (SQLException e){
            e.getStackTrace();
        }
    }

    public static boolean login(Connection con, Scanner sc,String email, String password){
        boolean ck = false;

        String query = "SELECT full_name FROM user WHERE email = ? AND password = ?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,email);
            ps.setString(2,password);
            ResultSet result = ps.executeQuery();
            while(result.next()){
                ck = true;
            }

        }catch (SQLException e){
            e.getStackTrace();
        }

        return ck;

    }
}


class Account{
    public static int ac_no = 100000;
    public static boolean isAccountExits(Connection con, String email) throws SQLException{
        boolean ck = false;
        String query = "SELECT email FROM accounts WHERE email = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1,email);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) ck = true;

        return ck;

    }
    public static void account_open(Connection con , Scanner sc , String email){
        String query = "INSERT INTO accounts(account_number,full_name,email,balance,security_pin) VALUES(?,?,?,?,?) ";
        System.out.print("Enter your Full Name : " );
        sc.nextLine();
        String name = sc.nextLine();
        System.out.print("Enter initial balance  : " );
        int balance = sc.nextInt();
        System.out.print("Set Security 4 digit PIN: ");
        String pin = sc.next();
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ac_no +=1;
            ps.setInt(1,ac_no);
            ps.setString(2,name);
            ps.setString(3,email);
            ps.setInt(4,balance);
            ps.setString(5,pin);
            int result = ps.executeUpdate();
            if(result>0) System.out.println("Account created  successfully");
            else System.out.println("Account not created");


        }catch (SQLException e){
            e.getStackTrace();
        }


    }
}
class AccountServices{
    AccountServices(Connection con, Scanner sc){

    }

    private static boolean isSufficent(Connection con, String email, String pin,int amount) throws SQLException{
        String query = "SELECT balance FROM accounts WHERE email = ? AND security_pin = ?";
        boolean ck = false;
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1,email);
        ps.setString(2,pin);
        ResultSet result = ps.executeQuery();
        while(result.next()){
            if(result.getInt("balance") > amount && result.getInt("balance")>0)
                ck = true;
        }
        return ck;
    }
    //credit money
    public static void creditMoney(Connection con,Scanner sc, String email) throws SQLException{
        String query = "UPDATE accounts SET balance = balance + ? WHERE email = ? AND security_pin = ?";
        System.out.print("Enter Amount : ");
        int amount = sc.nextInt();
        System.out.print("Enter Security Pin: ");
        String pin = sc.next();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1,amount);
        ps.setString(2,email);
        ps.setString(3,pin);
        int rs = ps.executeUpdate();
        if(rs>0) System.out.println(amount + " credited successfully ");
        else System.out.println("Credit not successfully");
    }

    public static void debitMoney(Connection con,Scanner sc, String email) throws SQLException{
        String query = "UPDATE accounts SET balance = balance- ? WHERE email = ? AND security_pin = ?";
        System.out.print("Enter Amount : ");
        int amount = sc.nextInt();
        System.out.print("Enter Security Pin: ");
        String pin = sc.next();
        if (!isSufficent(con, email, pin, amount)) {
            System.out.println("❌ Insufficient balance");
            return;
        }
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1,amount);
        ps.setString(2,email);
        ps.setString(3,pin);
        int rs = ps.executeUpdate();
        if(rs>0) System.out.println(amount + " credited successfully ");
        else System.out.println("Credit not successfully");

    }

    public static void money_transfer(Connection con, Scanner sc, String email) throws SQLException{
        con.setAutoCommit(false);
        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE email = ? AND security_pin = ?";
        String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account = ? ";
        System.out.print("Enter Transfer account number : ");
        int creditAC = sc.nextInt();
        System.out.print("Enter amount : ");
        int amount = sc.nextInt();
        System.out.print("Enter Security Pin: ");
        String pin = sc.next();
        if (!isSufficent(con, email, pin, amount)) {
            System.out.println("❌ Insufficient balance");
            return;
        }
        PreparedStatement psDebit = con.prepareStatement(debit_query);
        psDebit.setInt(1,amount);
        psDebit.setString(2,email);
        psDebit.setString(3,pin);
        PreparedStatement psCredit = con.prepareStatement(credit_query);
        psCredit.setInt(1,amount);
        psCredit.setInt(2,creditAC);
        int resultDebit = psDebit.executeUpdate();
        int resultCredit = psCredit.executeUpdate();

        if( resultDebit == 1 && resultCredit == 1 ){
            con.commit();
            System.out.println("Money transfer Successfully");
        }

        else {
            con.rollback();
            System.out.println("Money transfer not Successfully");
        }



    }


    public static void checkBalance(Connection con, Scanner sc, String email) throws SQLException{
        String query = "SELECT balance FROM accounts WHERE email = ? AND security_pin = ?";

        System.out.print("Enter Security Pin: ");
        String pin = sc.next();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1,email);
        ps.setString(2,pin);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            System.out.println("Your Balance is : " + rs.getString("balance"));

        }


    }

}
