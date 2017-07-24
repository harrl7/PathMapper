package bit.com.pathmapper.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import bit.com.pathmapper.Models.Collection;
import bit.com.pathmapper.Models.PointOfInterest;

/**
 * Created by jacksct1 on 20/10/2016.
 */

public class DB_Handler extends SQLiteOpenHelper
{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "pathmapperDB";
    //Table names
    private static final String TABLE_POI = "tblPOI";
    private static final String TABLE_COLLECTION = "tblCollection";
    //POI Table Columns names
    private static final String KEY_POI_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SCI_NAME = "scientificName";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_COLLECTION = "collection";
    //Collection Columns names
    private static final String KEY_COLLECTION_ID = "id";
    private static final String KEY_COLLECTION_NAME = "collectionName";

    public DB_Handler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Creation of the Points of Interest Table
        String CREATE_POI_TABLE = "CREATE TABLE " + TABLE_POI + "("
        + KEY_POI_ID + " INTEGER," + KEY_NAME + " TEXT,"
        + KEY_SCI_NAME + " TEXT," + KEY_LAT + " INTEGER,"
        + KEY_LNG + " INTEGER," + KEY_DESCRIPTION + " TEXT,"
        + KEY_COLLECTION + " INTEGER" + ")";
        db.execSQL(CREATE_POI_TABLE);

        //Creation of the Collection Table
        String CREATE_COLLECTION_TABLE = "CREATE TABLE " + TABLE_COLLECTION + "("
        + KEY_COLLECTION_ID + " INTEGER," + KEY_COLLECTION_NAME + " TEXT" + ")";
        db.execSQL(CREATE_COLLECTION_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTION);
        // Creating tables again
        onCreate(db);
    }

    //Add new POI
    public void addPOI (PointOfInterest pointOfInterest)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_POI_ID, pointOfInterest.getId()); // POI Name
        values.put(KEY_NAME, pointOfInterest.getName()); // POI Name
        values.put(KEY_SCI_NAME, pointOfInterest.getScientificName()); // POI Scientific Name
        values.put(KEY_LAT, pointOfInterest.getLat()); // POI Latitude
        values.put(KEY_LNG, pointOfInterest.getLng()); // POI Longitude
        values.put(KEY_DESCRIPTION, pointOfInterest.getDescription()); // POI Description
        values.put(KEY_COLLECTION, pointOfInterest.getCollection()); // POI Collection

        // Inserting Row
        db.insert(TABLE_POI, null, values);
        db.close(); // Closing database connection
    }

    //Add new Collection
    public void addCollection (Collection collection)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COLLECTION_ID, collection.getId());
        values.put(KEY_COLLECTION_NAME, collection.getCollectionName()); // Collection Name

        // Inserting Row
        db.insert(TABLE_COLLECTION, null, values);
        db.close(); // Closing database connection
    }

    //checks for existing POI from ID
    public boolean checkExistingPOI(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_POI + " where " + KEY_POI_ID + " = " + id;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            db.close(); // Closing database connection
            return false;
        }
        cursor.close();
        db.close(); // Closing database connection
        return true;
    }

    //checks for Collection from ID
    public boolean checkExistingCollection(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_COLLECTION + " where " + KEY_COLLECTION_ID + " = " + id;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            db.close(); // Closing database connection
            return false;
        }
        cursor.close();
        db.close(); // Closing database connection
        return true;
    }

    // Getting one shop
    public PointOfInterest getPOI(int id) {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_POI, new String[] { KEY_POI_ID,
                            KEY_NAME, KEY_SCI_NAME, KEY_LAT, KEY_LNG, KEY_DESCRIPTION, KEY_COLLECTION }, KEY_POI_ID + "=?",
                    new String[] { String.valueOf(id) }, null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();
            PointOfInterest poi = new PointOfInterest(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getString(5), cursor.getInt(2));
            db.close();
            // return poi
            return poi;
        }
        catch(IndexOutOfBoundsException e)
        {
            return null;
        }

    }


    // Getting All POIs
    public List<PointOfInterest> getAllPOI() {
        List<PointOfInterest> poiList = new ArrayList<PointOfInterest>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_POI;

      try {
          SQLiteDatabase db = this.getWritableDatabase();
          Cursor cursor = db.rawQuery(selectQuery, null);
          // looping through all rows and adding to list
          if (cursor.moveToFirst()) {
              do {
                  PointOfInterest poi = new PointOfInterest();
                  poi.setId(Integer.parseInt(cursor.getString(0)));
                  poi.setName(cursor.getString(1));
                  poi.setScientificName(cursor.getString(2));
                  poi.setLat(cursor.getDouble(3));
                  poi.setLng(cursor.getDouble(4));
                  poi.setDescription(cursor.getString(5));
                  poi.setCollection(cursor.getInt(6));
                  // Adding contact to list
                  poiList.add(poi);
              } while (cursor.moveToNext());
              db.close();
          }
      }
      catch (NullPointerException e)
      {

      }

        // return poi list
        return poiList;
    }

    // Getting All POIs from a passed ocllection
    public List<PointOfInterest> getAllCollectionPOI(int id) {
        List<PointOfInterest> poiList = new ArrayList<PointOfInterest>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_POI + " WHERE " + KEY_COLLECTION + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PointOfInterest poi = new PointOfInterest();
                poi.setId(Integer.parseInt(cursor.getString(0)));
                poi.setName(cursor.getString(1));
                poi.setScientificName(cursor.getString(2));
                poi.setLat(cursor.getDouble(3));
                poi.setLng(cursor.getDouble(4));
                poi.setDescription(cursor.getString(5));
                poi.setCollection(cursor.getInt(6));
                // Adding contact to list
                poiList.add(poi);

            } while (cursor.moveToNext());
        }
        db.close();
        // return poi list
        return poiList;
    }

    // Getting All Collections
    public List<Collection> getAllCollections() {
        List<Collection> collectionList = new ArrayList<Collection>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_COLLECTION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Collection collection = new Collection();
                collection.setId(Integer.parseInt(cursor.getString(0)));
                collection.setCollectionName(cursor.getString(1));
                // Adding contact to list
                collectionList.add(collection);
            } while (cursor.moveToNext());
        }
        db.close();
        // return collections list
        return collectionList;
    }

    // Updating a POI
    public int updatePOI(PointOfInterest pointOfInterest) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, pointOfInterest.getName()); // POI Name
        values.put(KEY_SCI_NAME, pointOfInterest.getScientificName()); // POI Scientific Name
        values.put(KEY_LAT, pointOfInterest.getLat()); // POI Latitude
        values.put(KEY_LNG, pointOfInterest.getLng()); // POI Longitude
        values.put(KEY_DESCRIPTION, pointOfInterest.getDescription()); // POI Description
        values.put(KEY_COLLECTION, pointOfInterest.getCollection()); // POI Collection
        // updating row
        return db.update(TABLE_POI, values, KEY_POI_ID + " = ?",
                new String[]{String.valueOf(pointOfInterest.getId())});
    }

    // Updating a Collection
    public int updateCollection(Collection collection) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, collection.getCollectionName()); // Collection Name
        // updating row
        return db.update(TABLE_POI, values, KEY_POI_ID + " = ?",
                new String[]{String.valueOf(collection.getId())});

    }

    // Deleting a POI
    public void deletePOI(PointOfInterest pointOfInterest) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POI, KEY_POI_ID + " = ?",
                new String[] { String.valueOf(pointOfInterest.getId()) });
        db.close();
    }

    // Deleting a Collection
    public void deleteCollection(Collection collection) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COLLECTION, KEY_COLLECTION_ID + " = ?",
                new String[] { String.valueOf(collection.getId()) });
        db.close();
    }

}
