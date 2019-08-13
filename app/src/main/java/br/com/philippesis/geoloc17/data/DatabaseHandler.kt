package br.com.philippesis.geoloc17.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.com.philippesis.geoloc17.data.models.LocationModel

class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$ID Integer PRIMARY KEY, " +
                "$COLUMN_1 DOUBLE, " +
                "$COLUMN_2 DOUBLE, " +
                "$COLUMN_3 LONG)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun addLocation(locationModel: LocationModel): Boolean {
        // Create and/or open a database that will be used for reading and writing.
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_1, locationModel.latitude)
        values.put(COLUMN_2, locationModel.longitude)
        values.put(COLUMN_3, locationModel.dataTimeMillis)
        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        return (Integer.parseInt("$success") != -1)
    }

    fun getAllLocations(): MutableList<LocationModel> {
        var locations = mutableListOf<LocationModel>()
        val db = readableDatabase
        val selectAllQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectAllQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getString(cursor.getColumnIndex(ID))
                    val latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_1))
                    val longitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_2))
                    val dataTimeMillis = cursor.getLong(cursor.getColumnIndex(COLUMN_3))

                    locations.add(LocationModel(
                            latitude = latitude,
                            longitude = longitude,
                            dataTimeMillis = dataTimeMillis)
                    )

                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return locations
    }

    companion object {
        private const val DB_NAME = "GeoLoc17DB"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "location"
        private const val ID = "id"
        private const val COLUMN_1 = "Latitude"
        private const val COLUMN_2 = "Longitude"
        private const val COLUMN_3 = "DataTime"
    }

}