package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantTransactionDAO;

public class PersistantDemoExpenseManager extends ExpenseManager{
    private Context context;
    public PersistantDemoExpenseManager(Context context) {
        this.context = context;
        setup();
    }
    @Override
    public void setup() {
        TransactionDAO persistantTransactionDAO = new PersistantTransactionDAO(context);
        setTransactionsDAO(persistantTransactionDAO);

        AccountDAO persistantAccountDAO = new PersistantMemoryAccountDAO(context);
        setAccountsDAO(persistantAccountDAO);

    }
}
