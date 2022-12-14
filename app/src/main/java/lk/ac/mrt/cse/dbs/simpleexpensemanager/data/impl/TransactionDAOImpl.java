package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class TransactionDAOImpl implements TransactionDAO {
    DBHelper dbHelper;

    public TransactionDAOImpl(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase DB = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date.toString());
        values.put("accountNo", accountNo);
        values.put("expenseType", expenseType.toString());
        values.put("amount", amount);
        DB.insert("Trans", null, values);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        ArrayList<Transaction> list = new ArrayList<>();
        Cursor data = getData();
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                try {
                    list.add(new Transaction(
                            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy").parse(data.getString(1)),
                            data.getString(2),
                            ExpenseType.valueOf(data.getString(3)),
                            data.getDouble(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (data.moveToNext());
        }
        return list;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> allTransactionLogs = getAllTransactionLogs();
        int size = allTransactionLogs.size();
        return (size > limit) ? allTransactionLogs.subList(size - limit, size) :
                allTransactionLogs;
    }

    public Cursor getData() {
        SQLiteDatabase DB = dbHelper.getWritableDatabase();
        return DB.rawQuery("Select * from Trans", null);
    }
}
