package staddle.com.staddle.fcm;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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

    public void insert(String name, String desc) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.OR_ID, name);
        contentValue.put(DBHelper.STATUS, desc);
        database.insert(DBHelper.CR_ORDS, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DBHelper.OR_ID, DBHelper.STATUS };
        Cursor cursor = database.query(DBHelper.CR_ORDS, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String name, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.OR_ID, name);
        contentValues.put(DBHelper.STATUS, desc);
        int i = database.update(DBHelper.CR_ORDS, contentValues, DBHelper.OR_ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DBHelper.CR_ORDS, DBHelper.OR_ID + "=" + _id, null);
    }

}
