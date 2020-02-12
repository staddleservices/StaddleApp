package staddle.com.staddle.fcm;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String USER_CART = "USER_CART";

    // Table columns
    public static final String ID = "id";
    public static final String VID = "vid";
    public static final String MENU_PRICE = "menu_price";
    public static final String MENU_SUBNAME = "menu_subname";
    public static final String MENU_NAME = "menu_name";
    public static final String COUNT = "count";
    public static final String TOTALPRICE ="totalPrice";

    // Database Information
    static final String DB_NAME = "STADDLE_USER_CART.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + USER_CART + "(" + ID
            + " VARCHAR (200) , " + VID + " VARCHAR (200),"+ MENU_PRICE +" VARCHAR (200) ,"+MENU_SUBNAME+" VARCHAR (200) ,"+MENU_NAME+" VARCHAR (200) ,"+ COUNT +" VARCHAR (200),"+ TOTALPRICE +" VARCHAR (200) );";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_CART);
        onCreate(db);
    }
}

