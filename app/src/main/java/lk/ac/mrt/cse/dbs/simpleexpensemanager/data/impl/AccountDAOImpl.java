package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class AccountDAOImpl implements AccountDAO {
    DBHelper dbHelper;

    public AccountDAOImpl(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    @Override
    public List<String> getAccountNumbersList() {
        ArrayList<String> list = new ArrayList<>();
        Cursor data = getData();
        data.moveToFirst();
        while (data.moveToNext()) {
            list.add(data.getString(1));
        }
        return list;
    }

    @Override
    public List<Account> getAccountsList() {
        ArrayList<Account> list = new ArrayList<>();
        Cursor data = getData();
        data.moveToFirst();
        while (data.moveToNext()) {
            Account account = new Account(
                    data.getString(1),
                    data.getString(2),
                    data.getString(3),
                    data.getDouble(4));
            list.add(account);
        }
        return list;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase DB = dbHelper.getWritableDatabase();
        Cursor data = DB.rawQuery("Select * from Account where accountNo=?", new String[]{accountNo}, null);
        if (!data.isNull(1)) {
            data.moveToFirst();
            Account account = null;
            while (data.moveToNext()) {
                account = new Account(
                        data.getString(1),
                        data.getString(2),
                        data.getString(3),
                        data.getDouble(4));
            }
            return account;
        }
        throw new InvalidAccountException("Account " + accountNo + " is invalid.");
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase DB = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("accountNo", account.getAccountNo());
        values.put("bankName", account.getBankName());
        values.put("accountHolderName", account.getAccountHolderName());
        values.put("balance", account.getBalance());
        DB.insert("Account", null, values);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase DB = dbHelper.getWritableDatabase();
        Cursor data = DB.rawQuery("Select * from Account where accountNo=?", new String[]{accountNo}, null);
        if (data.isNull(1)) {
            throw new InvalidAccountException("Account " + accountNo + " is invalid.");
        }
        DB.delete("Account", "accountNo=?", new String[]{accountNo});
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase DB = dbHelper.getWritableDatabase();
        Cursor data = DB.rawQuery("Select * from Account where accountNo=?", new String[]{accountNo}, null);
        if (!data.isNull(0)) {
            ContentValues values = new ContentValues();
            values.put("bankName", data.getString(2));
            values.put("accountHolderName", data.getString(3));
            double value = data.getDouble(4);
            switch (expenseType) {
                case EXPENSE:
                    value -= amount;
                    break;
                case INCOME:
                    value += amount;
                    break;
            }
            values.put("accountHolderName", value);
            long result = DB.update("Account", values, "accountNo=?", new String[]{accountNo});
            if(result == -1){
                throw new InvalidAccountException("Error occurred during the account update!");
            }
        }
        throw new InvalidAccountException("Account " + accountNo + " is invalid.");

    }

    public Cursor getData() {
        SQLiteDatabase DB = dbHelper.getWritableDatabase();
        return DB.rawQuery("Select * from Account", null);
    }
}
