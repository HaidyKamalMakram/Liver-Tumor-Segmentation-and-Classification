package app.ij.mlwithtensorflowlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class Database {

    public static final String IMAGE = "image";
    private static final String DATABASE_NAME = "user_database";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_ID = "id";
    private static final String KEY_FIRSTNAME = "name";
    private static final String KEY_STAGE = "stage";
    private static final String KEY_DATE = "date";
    private static final String IMAGES_TABLE = "ImagesTable";

    private static final String CREATE_IMAGES_TABLE =
            "CREATE TABLE IF NOT EXISTS " + IMAGES_TABLE + " (" +
                    KEY_ID + " TEXT NOT NULL, "
                    + IMAGE + " BLOB NOT NULL, " + KEY_STAGE + " TEXT NOT NULL, " + KEY_DATE + " TEXT NOT NULL PRIMARY KEY );";

    private static final String CREATE_TABLE_USERDATA = "CREATE TABLE user(name TEXT, age TEXT, password TEXT PRIMARY KEY, email TEXT, state TEXT, phone_number TEXT)";

    private final Context mContext;
    private final DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;


    public Database(Context ctx) {
        mContext = ctx;
        mDbHelper = new DatabaseHelper(mContext);
    }

    public Database open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public Database read() throws SQLException {
        mDb = mDbHelper.getReadableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public Boolean insertData(String name, String age, String password, String email, String state, String phone_number) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("password", password);
        contentValues.put("age", age);
        contentValues.put("email", email);
        contentValues.put("state", state);
        contentValues.put("phone_number", phone_number);

        long result = mDb.insert("user", null, contentValues);

        return result != -1;
    }

    public Boolean checkUser(String email, String password, String state) {

        open();
        Cursor cursor = mDb.rawQuery("SELECT * FROM user WHERE email= ? AND password= ? AND state= ?", new String[]{email, password, state});
        return cursor.getCount() > 0;
    }

    public void addUser(String name, String classImg, String date, Bitmap bmp) {
        open();
        //adding user name in users table
        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, name);

        //Adding Image
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
        byte[] buffer = out.toByteArray();

        ContentValues valuesImage;

        valuesImage = new ContentValues();
        valuesImage.put(KEY_ID, name);
        valuesImage.put(IMAGE, buffer);
        valuesImage.put(KEY_STAGE, classImg);
        valuesImage.put(KEY_DATE, date);
        mDb.insert(IMAGES_TABLE, null, valuesImage);

        close();
    }

    public Cursor getData(String password) {
        open();
        return mDb.rawQuery("SELECT * FROM ImagesTable WHERE id= ? ", new String[]{password}, null);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_IMAGES_TABLE);
            db.execSQL(CREATE_TABLE_USERDATA);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS user");
            db.execSQL("DROP TABLE IF EXISTS '" + IMAGES_TABLE + "'");

            onCreate(db);
        }
    }
}
