// This class has a race condition in it.
public class BankAccount {

    private int balance = 0;

    // Abstraction function
    //   AF(balance) = a bank account containing balance (in dollars)
    // Rep invariant
    //   true

    /**
     * Modifies this account by depositing 1 dollar.
     */
    public void deposit() {
        balance = balance + 1;
    }

    /**
     * Modifies this account by withdrawing 1 dollar.
     */
    public void withdraw() {
        balance = balance - 1;
    }
    
    /**
     * @return amount of money in this account, in dollars
     */
    public int balance() {
        return balance;
    }
    
    // simulate a network of cash machines, handling customer transactions concurrently
    private static final int NUMBER_OF_CASH_MACHINES = 2;
    private static final int TRANSACTIONS_PER_MACHINE = 250;
    
    public static void main(String[] args) {
        final BankAccount account = new BankAccount();
        System.out.println("starting balance is $" + account.balance());

        System.out.println("... then " 
                            + NUMBER_OF_CASH_MACHINES 
                            + " cash machines do "
                            + TRANSACTIONS_PER_MACHINE
                            + " $1-deposit/$1-withdrawal transactions each...");

        // simulate each cash machine with a thread
        for (int i = 0; i < NUMBER_OF_CASH_MACHINES; ++i) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    // each ATM does a bunch of transactions that should
                    // leave the account balance unchanged
                    for (int j = 0; j < TRANSACTIONS_PER_MACHINE; ++j) {
                        account.deposit(); // put a dollar in
                        account.withdraw(); // take it back out
                        //System.out.println(account.balance()); // uncomment this to make the bug magically disappear! (it's not really gone though)
                    }
                    System.out.println("cash machine done, balance is " + account.balance());
                }
            });
            t.start(); // don't forget to start the thread!
        }
        
    }
}
