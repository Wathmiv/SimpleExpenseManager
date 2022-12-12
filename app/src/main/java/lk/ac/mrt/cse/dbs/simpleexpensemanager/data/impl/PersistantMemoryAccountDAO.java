package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.DBhelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistantMemoryAccountDAO implements AccountDAO {
    private DBhelper dbHelper;
    private SQLiteDatabase db;

    public PersistantMemoryAccountDAO(Context context){
        dbHelper = new DBhelper(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> AccountNoList = new ArrayList<String>();
        db = dbHelper.getReadableDatabase();
        String query = "select accountNo from Account";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()){
            String AccountNo = cursor.getString(cursor.getColumnIndex("accountNo"));
            AccountNoList.add(AccountNo);
        }
        cursor.close();
        db.close();
        return AccountNoList;
    }



    @Override
    public List<Account> getAccountsList() {
        List<Account> AccountList = new ArrayList<Account>();
        db = dbHelper.getReadableDatabase();
        String query = "select * from Account";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()){
            String AccountNo = cursor.getString(1);
            String bankName = cursor.getString(2);
            String accountHolderName = cursor.getString(3);
            double balance = cursor.getDouble(4);

            Account account = new Account(AccountNo,bankName,accountHolderName,balance);
            AccountList.add(account);

        }
        cursor.close();
        db.close();
        return AccountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account = null;
        db = dbHelper.getReadableDatabase();
        String query = "select * from Account where accountNo = " + accountNo;
        Cursor cursor = db.rawQuery(query, new String[]{accountNo});
        while (cursor.moveToNext()) {

            int ID = cursor.getInt(cursor.getColumnIndex("ID"));
            String accountNo1 = cursor.getString(cursor.getColumnIndex("accountNo"));
            String bankName = cursor.getString(cursor.getColumnIndex("bankName"));
            String accountHolderName = cursor.getString(cursor.getColumnIndex("accountHolderNAme"));
            double balance = cursor.getDouble(cursor.getColumnIndex("balance"));
            account = new Account(accountNo1, bankName, accountHolderName, balance);

        }
        return account;

    }

    @Override
    public void addAccount(Account account) {
        db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("accountNo",account.getAccountNo());
        cv.put("bankName",account.getBankName());
        cv.put("accountHolderName", account.getAccountHolderName());
        cv.put("balance", account.getBalance());

        db.insert("Account", null, cv);
        db.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        db = dbHelper.getWritableDatabase();
        String query = "delete from Account where accountNo = "+ accountNo;
        Cursor cursor = db.rawQuery(query,null);
        if (!cursor.moveToFirst()) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        db = dbHelper.getWritableDatabase();
        String query = "select * from Account where accountNo = " + accountNo;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            double balance = cursor.getDouble(cursor.getColumnIndex("balance"));
            switch (expenseType) {
                case EXPENSE:
                    balance -= amount;
                    break;
                case INCOME:
                    balance += amount;
                    break;
            }
            String query2 = "update Account set balance = " + balance + " where accountNo = " + accountNo;
            db.execSQL(query2, new Object[]{cursor.getInt(cursor.getColumnIndex("ID")),
                    cursor.getString(cursor.getColumnIndex("accountNo")),
                    cursor.getString(cursor.getColumnIndex("bankName")),
                    cursor.getString(cursor.getColumnIndex("accountHolderName")),
                    cursor.getDouble(cursor.getColumnIndex("balance"))});
            cursor.close();
        }
        else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }
}

