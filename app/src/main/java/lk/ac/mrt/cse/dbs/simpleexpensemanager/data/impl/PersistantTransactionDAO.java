package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        String Date = formatter.format(date);

        ContentValues cv = new ContentValues();
         cv.put("accountNo", accountNo);
         cv.put("Expense_type", String.valueOf(expenseType));
         cv.put("amount", amount);
         cv.put("date", Date);
        db.insert("transction", null, cv);
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
            ExpenseType expenseType = ExpenseType.valueOf(Expense_type.toUpperCase());

            double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            Date DateObject=null;
            try {
                DateObject = new SimpleDateFormat("MM/dd/yyyy").parse(date);
            } catch (Exception e) {}

            Transaction transaction = new Transaction(DateObject,accountNo,expenseType,amount);
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

