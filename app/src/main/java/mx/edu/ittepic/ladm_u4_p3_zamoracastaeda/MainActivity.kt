package mx.edu.ittepic.ladm_u4_p3_zamoracastaeda

import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.database.getStringOrNull
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var hilo : Hilo? = null
    //Nombre base -->contenido
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLista.setOnClickListener {

            cargarLista()

        }//btnLista

        btnVerifica.setOnClickListener {
            verificarSMS()
        }

        hilo = Hilo(this)
        hilo!!.start()

    }//onCreate


    fun cargarLista(){

        try {

            var con = mensajesEntrantes("","","")
            con.asignarPuntero(this)
            var data = con.mostrarTodo()

            if(data.size == 0){
                if (con.error==2){
                    mensaje("Error en la tabla/vacia")
                }
            }


            var total = data.size-1
            var vector = Array<String>(data.size, {""})

            (0..total).forEach {

                var sms = data[it]
                var item = sms.numero+"\n"+sms.plataforma+"\n"+sms.respondido
                vector[it] = item
            }//forEach
            lista.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, vector)

        }catch (e : SQLiteException) {

        }

    }//cargarLista


    fun verificarSMS() {

        try{

            var base = BaseDatos(this,"contenido",null, 1)
            var select = base.readableDatabase
            var SQL = "SELECT * FROM entrantes"
            var cursor = select.rawQuery(SQL,null)


            if(cursor.moveToFirst()){

                do {

                    var numero = cursor.getString(0)
                    var plataforma = cursor.getString(1)
                    var estado = cursor.getString(2)
                    plataforma = plataforma.toUpperCase()

                    when(plataforma){
                        "NETFLIX" ->{

                            if(estado == "N"){

                                var datos = traerDatos("NETFLIX")
                                //mensaje(estado)
                                enviarSMS(datos,numero)
                                actualizaEstado(numero)

                            }//if-estado

                        }//netFlix

                        "SPOTIFY" ->{

                            if(estado == "N"){

                                var datos = traerDatos("SPOTIFY")
                                //mensaje(estado)
                                enviarSMS(datos,numero)
                                actualizaEstado(numero)

                            }//if-estado

                        }//hbo

                        "AMAZON PRIME"->{

                            if(estado=="N"){

                                var datos = traerDatos("AMAZON PRIME")
                                //mensaje(estado)
                                enviarSMS(datos,numero)
                                actualizaEstado(numero)

                            }//if-estado

                        }//disney

                        else->{
                            var mensaje = "La aplicacion solo tiene promociones de NETFLIX, SPOTIFY y AMAZON PRIME. Todas en Mayusculas"
                            enviarSMS(mensaje,numero)
                        }//else

                    }//when-platafomra

                }while (cursor.moveToNext())

            }//if-cursorMoveToFirst

        }catch(e: SQLiteException){
            mensaje(e.message.toString())
        }

    }//verificarSMS


    fun actualizaEstado(numero: String){
        try {

            var base = BaseDatos(this,"contenido",null,1)
            var update = base.writableDatabase
            var SQL = "UPDATE entrantes SET Respondido = 'S' WHERE Celular = ?"
            var parametro = arrayOf(numero)

            update.execSQL(SQL,parametro)
            base.close()

        }catch (e:SQLiteException){
            mensaje(e.message.toString())
        }
    }//actualizaEstado


    fun traerDatos(plataforma:String) : String{
        var datos = ""
        try {

            var plataformaBusqueda = arrayOf(plataforma)
            var base = BaseDatos(this, "contenido", null, 1)
            var select = base.readableDatabase
            var SQL = "SELECT * FROM Promociones WHERE Plataforma = ?"
            var cursor = select.rawQuery(SQL,plataformaBusqueda)

            if (cursor.moveToFirst()){
                do {

                    datos +=cursor.getString(1)+"\n"

                }while (cursor.moveToNext())
            }

            base.close()

        }catch (e : SQLiteException){

        }

        return  datos

    }//traerDatos


    fun enviarSMS(mensaje:String, numero:String){

        SmsManager.getDefault().sendTextMessage(numero,null,mensaje,null,null)

    }//enviarSMS


    fun mensaje(m : String){
        Toast.makeText(this,m,Toast.LENGTH_LONG).show()
    }//mensaje



}//class
