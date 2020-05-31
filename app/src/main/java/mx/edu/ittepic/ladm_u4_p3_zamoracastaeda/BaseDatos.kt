package mx.edu.ittepic.ladm_u4_p3_zamoracastaeda

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    //nombreBase = contenido


    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL("CREATE TABLE entrantes (" +
                "Celular VARCHAR(200)," +
                "Plataforma VARCHAR(200)," +
                "Respondido VARCHAR(2)"+
                ")"
        )//CreateTable

        db.execSQL(
            "CREATE TABLE Promociones (" +
                    "Plataforma VARCHAR(200),"+
                    "planes VARCHAR(200))"
        )//create


        db.execSQL(
            "INSERT INTO Promociones (Plataforma, planes) VALUES " +
                    "('NETFLIX','Plan basico $129, Plan Estandar $169, Plan premiun $229'),"+
                    "('SPOTIFY','Plan individual $99.00, Plan duo $129,Plan familiar $149, Plan universitario 49'),"+
                    "('Amazon Prime','Plan $99')"


        )//insert

    }//onCreate

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }//onUpgrade

}//class

