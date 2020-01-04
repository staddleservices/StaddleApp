package staddle.com.staddle.fcm;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String CR_ORDS = "current_orders";

    // Table columns
    public static final String OR_ID = "order_id";
    public static final String STATUS = "order_status";

    // Database Information
    static final String DB_NAME = "STADDLE_ORDERS.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + CR_ORDS + "(" + OR_ID
            + " VARCHAR (200) , " + STATUS + " VARCHAR (10));";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CR_ORDS);
        onCreate(db);
    }
}

