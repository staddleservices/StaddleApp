package staddle.com.staddle.fcm;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.ResultSet;

public class DBManager {

    private DBHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String id, String vid,String menu_price,String menu_name,String count,String total) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.ID, id);
        contentValue.put(DBHelper.VID, vid);
        contentValue.put(DBHelper.MENU_PRICE,menu_price);

        contentValue.put(DBHelper.MENU_NAME,menu_name);
        contentValue.put(DBHelper.COUNT,count);
        contentValue.put(DBHelper.TOTALPRICE,total);
        database.insert(DBHelper.USER_CART, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DBHelper.ID, DBHelper.VID, DBHelper.MENU_PRICE,DBHelper.MENU_NAME,DBHelper.COUNT,DBHelper.TOTALPRICE };
        Cursor cursor = database.query(DBHelper.USER_CART, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(String id, String vid,String menu_price,String menu_name,String count,String total) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.ID, id);
        contentValues.put(DBHelper.VID, vid);
        contentValues.put(DBHelper.MENU_PRICE, menu_price);

        contentValues.put(DBHelper.MENU_NAME, menu_name);
        contentValues.put(DBHelper.COUNT,count);
        contentValues.put(DBHelper.TOTALPRICE, total);
        int i = database.update(DBHelper.USER_CART, contentValues, DBHelper.ID + " = " + id, null);
        return i;
    }

    public void delete(String _id) {
        database.delete(DBHelper.USER_CART, DBHelper.ID + "=" + _id, null);
    }

    public void truncate(){
        database.delete(DBHelper.USER_CART,null,null);
        database.close();
    }



    public float getTotal() {
        float total = 0;
        //String[] columns = new String[] { DBHelper.ID, DBHelper.VID, DBHelper.MENU_PRICE,DBHelper.MENU_NAME,DBHelper.COUNT,DBHelper.TOTALPRICE };
        Cursor cursor = database.rawQuery("SELECT SUM("+DBHelper.TOTALPRICE+") FROM "+ DBHelper.USER_CART,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }



        if(cursor.moveToFirst()){
            do{

                total = cursor.getFloat(0);
            }while(cursor.moveToNext());
        }
        return total;
    }


    public void updateQuantity(String id,String count,String totalprice){



        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COUNT,count);
        contentValues.put(DBHelper.TOTALPRICE,totalprice);

        database.update(DBHelper.USER_CART,contentValues,DBHelper.ID+" = "+id,null);

    }


    public Boolean lookup() {
        String[] columns = new String[] { DBHelper.ID, DBHelper.VID, DBHelper.MENU_PRICE,DBHelper.MENU_NAME,DBHelper.COUNT,DBHelper.TOTALPRICE };
        Cursor cursor = database.query(DBHelper.USER_CART, columns, null, null, null, null, null);
        if (cursor != null) {
            return false;
        }
        return true;
    }



}
