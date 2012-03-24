package com.cs301w01.meatload.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import com.cs301w01.meatload.model.querygenerators.AlbumQueryGenerator;
import com.cs301w01.meatload.model.querygenerators.PictureQueryGenerator;
import com.cs301w01.meatload.model.querygenerators.QueryGenerator;
import com.cs301w01.meatload.model.querygenerators.TagQueryGenerator;


/**
 * This class is helper for dealing with SQLite in Android. It provides a variety of useful 
 * methods for creating, updating, deleting, and selecting data. 
 * @see <a href="http://www.codeproject.com/Articles/119293/Using-SQLite-Database-with-Android">
   	http://www.codeproject.com/Articles/119293/Using-SQLite-Database-with-Android
 * </a>
 */
public class SQLiteDBManager extends SQLiteOpenHelper implements DBManager /**implements Serializable*/ {

	private static final long serialVersionUID = 1L;
    private static String logTag = "DBMANAGER";
    private static final String DB_NAME = "skindexDB";
    
    private static final int DATABASE_VERSION = 2;
    
    private Context myContext;


    public SQLiteDBManager(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        
        //myContext = context;

        //uncomment if any changes have been made to tables to reset
        //resetDB();

    }

    @Override
    public void onCreate(SQLiteDatabase newDB) {

        createTables(newDB);

    }

    private void createTables(SQLiteDatabase db) {

        db.execSQL(PictureQueryGenerator.getCreateTableQuery());
        Log.d(logTag, PictureQueryGenerator.getTableName() + " generated.");

        db.execSQL(AlbumQueryGenerator.getCreateTableQuery());
        Log.d(logTag, AlbumQueryGenerator.getTableName() + " generated.");

        db.execSQL(TagQueryGenerator.getCreateTableQuery());
        Log.d(logTag, TagQueryGenerator.getTableName() + " generated.");
        
        db.execSQL(QueryGenerator.TABLE_NAME_ALBUMTAGS);
        Log.d(logTag, QueryGenerator.TABLE_NAME_ALBUMTAGS + " generated.");

        Log.d(logTag, "DB generated.");

    }

    private void dropTables(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS " + PictureQueryGenerator.getTableName());
        Log.d(logTag, PictureQueryGenerator.getTableName() + " dropped.");

        db.execSQL("DROP TABLE IF EXISTS " + AlbumQueryGenerator.getTableName());
        Log.d(logTag, AlbumQueryGenerator.getTableName() + " dropped.");

        db.execSQL("DROP TABLE IF EXISTS " + TagQueryGenerator.getTableName());
        Log.d(logTag, TagQueryGenerator.getTableName() + " dropped.");
        
        db.execSQL("DROP TABLE IF EXISTS " + QueryGenerator.TABLE_NAME_ALBUMTAGS);
        Log.d(logTag, QueryGenerator.TABLE_NAME_ALBUMTAGS + " dropped.");

        Log.d(logTag, "DB generated.");
        
    }
    
    /**
     * Used to force changes to update in the sql table during testing
     */
    public void resetDB() {

        SQLiteDatabase db = this.getWritableDatabase();

        dropTables(db);
        createTables(db);
        
        //TODO: test db.close();
        
        Log.d(logTag, "TABLES RESET.");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        dropTables(sqLiteDatabase);
        onCreate(sqLiteDatabase);

    }


    
    /**
     * Performs a raw SQL Select query. Returns a cursor set to the first result.
     * @param query Query to be sent to database
     * @return Cursor to first result of query, or null if query is empty
     */
    public Cursor performRawQuery(String query) {
        
    	SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery(query, null);

        Log.d(logTag, "Raw query executed: " + query);
        	
        if (c.getCount() == 0) {
        	
        	db.close();
        	
        	return null;
        }
        
        c.moveToFirst();

        //TODO: test 
        db.close();
        
        return c;

    }

	public Cursor query(boolean b, String tableName, String[] selectColumns,
			String string, Object object, Object object2, Object object3,
			Object object4, Object object5) {
		
		return this.query(b, tableName, selectColumns, string, object, 
									object2, object3, object4, object5);
		
	}

	public int update(String tableName, ContentValues cv, String string,
			Object object) {
		
		return this.update(tableName, cv, string, object);
		
	}

	public long insert(String tableName, String colId, ContentValues cv) {
		
		return this.insert(tableName, colId, cv);
		
	}

}