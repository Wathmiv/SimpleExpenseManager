package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {

    private static final String Account_Table = "Account";
    private static final String accountNo  = "accountNo";
    private static final String bankName  = "bankName";
    private static final String accountHolderName  = "accountHolderName";
    private static final String balance  = "balance";
    private static final String Transaction_Table  = "transction";
    private static final String accountNoTransaction  = "accountNo";
    private static final String Expense_type  = "Expense_type";
    private static final String amount  = "amount";
    private static final String date  = "date";




    public DBhelper (Context context){
        super(context,"200646M.db",null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableAccount = "CREATE TABLE "+Account_Table+ " (ID integer primary key autoincrement, " + accountNo + " text, " + bankName + " text, "+ accountHolderName + " text, " + balance+" double)";
        String createTableTransaction = "CREATE TABLE "+Transaction_Table +" (ID integer primary key autoincrement, " + accountNoTransaction + " text, " + Expense_type+" text, " + amount +" double, " + date+" integer)  ";
        db.execSQL(createTableAccount);
        db.execSQL(createTableTransaction);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
