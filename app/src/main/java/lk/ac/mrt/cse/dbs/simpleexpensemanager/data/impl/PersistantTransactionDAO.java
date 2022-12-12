package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.DBhelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistantTransactionDAO implements TransactionDAO {
    private DBhelper dbHelper;
    private SQLiteDatabase db;

    public PersistantTransactionDAO(Context context){
        dbHelper = new DBhelper(context);
    }
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        db = dbHelper.getWritableDatabase();
        String expenseType1;
        if (expenseType == ExpenseType.EXPENSE){
            expenseType1 = "Expense";
        }
        else{
            expenseType1 = "Income";
        }

        ContentValues cv = new ContentValues();
         cv.put("accountNo", accountNo);
         cv.put("Expense_type", expenseType1);
         cv.put("amount", amount);
         cv.put("date", date.getTime());
        db.insert("Transaction", null, cv);
        db.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> TransactionList = new ArrayList<Transaction>();
        db = dbHelper.getReadableDatabase();
        String query = "select * from transction";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()){
            String accountNo = cursor.getString(cursor.getColumnIndex("accountNo"));
            String Expense_type = cursor.getString(cursor.getColumnIndex("Expense_type"));
            double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
            Date date = new Date(cursor.getInt(cursor.getColumnIndex("date")));
            ExpenseType expenseType;
            if (Expense_type == "Expense"){
                expenseType = ExpenseType.EXPENSE;
            }
            else{
                expenseType = ExpenseType.INCOME;
            }

            Transaction transaction = new Transaction(date,accountNo,expenseType,amount);
            TransactionList.add(transaction);

        }
        cursor.close();
        db.close();
        return TransactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionlist = new ArrayList<Transaction>();
        transactionlist = this.getAllTransactionLogs();
        int size = transactionlist.size();
        if (size <= limit) {
            return transactionlist;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactionlist.subList(size - limit, size);
    }
}

