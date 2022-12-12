package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.AccountDAOImpl;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.TransactionDAOImpl;

public class PersistentExpenseManager extends ExpenseManager {
    DBHelper dbHelper;
    public PersistentExpenseManager(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        setup();
    }

    @Override
    public void setup() {
        TransactionDAO implT = new TransactionDAOImpl(dbHelper);
        setTransactionsDAO(implT);

        AccountDAO implA = new AccountDAOImpl(dbHelper);
        setAccountsDAO(implA);
    }
}
