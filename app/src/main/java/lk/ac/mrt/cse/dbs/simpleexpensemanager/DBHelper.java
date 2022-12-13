package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "200411N.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create table Account(" +
                "accountNo TEXT primary key, " +
                "bankName TEXT, " +
                "accountHolderName TEXT," +
                "balance REAL)"
        );
        DB.execSQL("create table Trans(" +
                "transId INTEGER primary key AUTOINCREMENT," +
                "date TEXT, " +
                "accountNo TEXT, " +
                "expenseType TEXT, " +
                "amount REAL)");
        ContentValues values = new ContentValues();
        values.put("1","12345A");
        values.put("2","Yoda Bank");
        values.put("3","Anakin Skywalker");
        values.put("4",10000.0);
        DB.insert("Account",null,values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop table if exists Acount");
        DB.execSQL("drop table if exists Trans");
    }
}
