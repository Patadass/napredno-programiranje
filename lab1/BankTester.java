import java.util.Objects;
import java.util.Random;
import java.util.*;
import java.util.stream.Collectors;

class Account{
    private String name;
    private long id;
    private double balance;
    
    public Account(String name, double balace){
        this.id = new Random().nextLong();
        this.name = name; 
        this.balance = balace;
    }

    public double getBalance(){
        return balance;
    }

    public String getName(){
        return name;
    }

    public long getId(){
        return id;
    }

    public void setBalance(double balance){
        this.balance = balance;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("");
        sb.append("Name: " + name.toString() + "\n");
        sb.append("Balance: " + String.format("%.2f", balance) + "$\n");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        Account acc = (Account) o;
        return acc.getId() == this.getId();
    }
}

abstract class Transaction{
    final private long fromId, toId;
    final String description;
    final private double amount;

    public Transaction(long fromId, long toId, String description, double amount){
        this.fromId = fromId;
        this.toId = toId;
        this.description = description;
        this.amount = amount;
    }

    final public double getAmount(){
        return amount;
    }

    final public long getFromId(){
        return fromId;
    }
    
    final public long getToId(){
        return toId;
    }

    final public String getDescription(){
        return description;
    }

    public int getType(){
        return 0;
    }

    @Override
    public int hashCode(){
        return Objects.hash(fromId, toId, description, amount);
    }
}

class FlatAmountProvisionTransaction extends Transaction{
    private double flatProvision;

    public FlatAmountProvisionTransaction(long fromId, long toId, double amount, double flatProvision){
        super(fromId, toId, "FlatAmount", amount);
        this.flatProvision = flatProvision;
    }

    public double getFlatAmount(){
        return flatProvision;
    }

    @Override
    public int getType(){
        return 1;
    }

    @Override
    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        FlatAmountProvisionTransaction fa = (FlatAmountProvisionTransaction) o;
        return fa.getFromId() == this.getFromId() && fa.getToId() == this.getToId() && fa.getAmount() == this.getAmount() && fa.getFlatAmount() == this.getFlatAmount();
    }
}

class FlatPercentProvisionTransaction extends Transaction{
    private int centsPerDolar;

    public FlatPercentProvisionTransaction(long fromId, long toId, double amount, int centsPerDolar){
        super(fromId, toId, "FlatPercent", amount);
        this.centsPerDolar = centsPerDolar;
    }

    public int getPercent(){
        return centsPerDolar;
    }

    @Override
    public int getType(){
        return 2;
    }

    @Override
    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        FlatPercentProvisionTransaction fa = (FlatPercentProvisionTransaction) o;
        return fa.getFromId() == this.getFromId() && fa.getToId() == this.getToId() && fa.getAmount() == this.getAmount() && fa.getPercent() == this.getPercent();
    }
}

class Bank{
    private String name;
    private Account[] accounts;
    private double totalTransers;
    private double totalProvision;

    public Bank(String name, Account[] accounts){
        this.name = name;
        this.accounts = accounts.clone();
        this.totalTransers = 0;
        this.totalProvision = 0;
    }

    public boolean makeTransaction(Transaction t){
        Account from = null, to = null;
        for(Account acc : accounts){
            if(acc.getId() == t.getFromId()){
                from = acc;
            }
            if(acc.getId() == t.getToId()){
                to = acc;
            }
        }

        if(from == null || to == null){
            return false;
        }

        if(from.getBalance() < t.getAmount()){
            return false;
        }

        double provision = 0;
        if(t.getType() == 1){
            FlatAmountProvisionTransaction fa = (FlatAmountProvisionTransaction)t;
            provision = fa.getFlatAmount();
            to.setBalance(to.getBalance() + fa.getAmount());
        }
        if(t.getType() == 2){
            FlatPercentProvisionTransaction fp = (FlatPercentProvisionTransaction)t;
            provision = fp.getAmount() * fp.getPercent()/100;
            to.setBalance(to.getBalance() + fp.getAmount());
        }
        from.setBalance(from.getBalance() - t.getAmount() - provision);
        totalTransers += t.getAmount();
        totalProvision += provision;
        return true;
    }

    public double totalTransfers(){
        return this.totalTransers;
    }

    public double totalProvision(){
        return this.totalProvision;
    }

    public Account[] getAccounts(){
        return accounts;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("");
        sb.append("Name: " + name + "\n\n");
        for(Account acc : accounts){
            sb.append(acc.toString());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o){
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Double.compare(this.totalTransfers(), bank.totalTransfers()) == 0 && Double.compare(totalProvision, bank.totalProvision) == 0 && Objects.equals(name, bank.name) && Objects.deepEquals(accounts, bank.accounts);    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }
}

public class BankTester {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static double parseAmount (String amount){
        return Double.parseDouble(amount.replace("$",""));
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", 20.0);
        Account a2 = new Account("Andrej", 20.0);
        Account a3 = new Account("Andrej", 30.0);
        Account a4 = new Account("Gajduk", 20.0);
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1)&&!a1.equals(a2)&&!a2.equals(a1)&&!a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, 20.0, 10.0);
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, 20.0, 10.0);
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, 20.0, 10.0);
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, 50.0, 50.0);
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, 20.0, 10.0);
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, 20.0, 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, 20.0, 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, 20.0, 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, 50.0, 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, 20.0, 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, 20.0, 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, 3.0, 3.0);
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(),  parseAmount(jin.nextLine()));
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    double amount = parseAmount(jin.nextLine());
                    double parameter = parseAmount(jin.nextLine());
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + String.format("%.2f$",t.getAmount()));
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + String.format("%.2f$",bank.totalProvision()));
                    System.out.println("Total transfers: " + String.format("%.2f$",bank.totalTransfers()));
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, double amount, double o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, (int) o);
        }
        return null;
    }


}
