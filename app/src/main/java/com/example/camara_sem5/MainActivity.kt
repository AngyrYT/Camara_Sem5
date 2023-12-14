package com.example.camara_sem5

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import java.math.BigInteger
import java.util.HashMap

val REQUEST_IMAGE_CAPTURE=1
var vlGradosGirar:Float=90.0F;
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val vlBtnAbrirCamara= findViewById<Button>(R.id.btnCamara)
        val vlBtnGuardar=findViewById<Button>(R.id.btnAgregar)
        val vlCodigo=findViewById<EditText>(R.id.txtCodigo)
        val vlDireccion=findViewById<EditText>(R.id.txtDireccion)
        val vlTelefono=findViewById<EditText>(R.id.txtTelefono)
        val vlCorreo=findViewById<EditText>(R.id.txtCorreo)
        val vlFecha=findViewById<EditText>(R.id.txtFecha)
        val vlEdad=findViewById<EditText>(R.id.txtEdad)
        val vlProfesion=findViewById<EditText>(R.id.txtProfesion)

        vlBtnAbrirCamara.setOnClickListener()
        {
            val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }

        vlBtnGuardar.setOnClickListener()
        {
            AgregarPerfil(vlCodigo.text.toString(),vlDireccion.text.toString(),vlTelefono.text.toString(),
                vlCorreo.text.toString(),vlFecha.text.toString(),vlEdad.text.toString(),
                vlProfesion.text.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== REQUEST_IMAGE_CAPTURE && resultCode== RESULT_OK)
        {
            val imageBitmap= data?.extras?.get("data") as Bitmap
            val vlImgFoto=findViewById<ImageView>(R.id.imgFoto)
            vlImgFoto.setImageBitmap(imageBitmap)
            val vlMatriz = Matrix();
            vlMatriz.postRotate(vlGradosGirar);
            val rotateBitmap = Bitmap.createBitmap(imageBitmap,0,0, imageBitmap.width,imageBitmap.height, vlMatriz,true)
            vlImgFoto.setImageBitmap(rotateBitmap);
            MediaStore.Images.Media.insertImage(contentResolver,imageBitmap,"Imagen001","Imagen Registrada Desde La App")
        }
    }

    fun AgregarPerfil(pCodigo:String, pDireccion:String, pTelefono:String, pCorreo:String, pFecha:String, pEdad:String, pProfesion:String )
    {
        val queue = Volley.newRequestQueue(applicationContext)
        val URL = "http://192.168.100.139/serviciosWeb/crearperfil.php"
        val StringRequest = object : StringRequest(
            Request.Method.POST,URL,Response.Listener<String>{  response ->
                Toast.makeText(this,"Datos Insertados Correctamente", Toast.LENGTH_SHORT).show()  },
            Response.ErrorListener{error ->  Toast.makeText(this,"Error ${error.message}",Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val params= HashMap<String,String>()
                params["codtrabajador"]=pCodigo;
                params["direccion"]=pDireccion;
                params["telefono"]=pTelefono;
                params["correo"]=pCorreo;
                params["fechaNacimiento"]=pFecha;
                params["edad"]=pEdad;
                params["profesion"]=pProfesion;
                return params
            }
        }
        queue.add(StringRequest)
    }
}