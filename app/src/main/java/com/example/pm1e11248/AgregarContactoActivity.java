package com.example.pm1e11248;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm1e11248.conexion.SQLiteConexion;
import com.example.pm1e11248.transacciones.Contactos;
import com.example.pm1e11248.transacciones.Transacciones;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AgregarContactoActivity extends AppCompatActivity {

    Button tomarFoto, guardar, regresar;
    EditText nombre, numero, nota;
    Spinner paises;
    ImageView foto, logo;

    Contactos contacto;
    Boolean actualizacionActiva;
    String ubicacion;
    static final  int captura = 1;
    static final  int acceso_camara = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);

        actualizacionActiva = false;

        tomarFoto = ( Button ) findViewById(R.id.agregarfoto);
        guardar =  ( Button ) findViewById(R.id.guardar);
        regresar = ( Button ) findViewById(R.id.regresar);

        nombre = ( EditText ) findViewById(R.id.nombre);
        paises = ( Spinner ) findViewById(R.id.pais);
        numero = ( EditText ) findViewById(R.id.numero);
        nota = ( EditText ) findViewById(R.id.nota);

        foto = ( ImageView ) findViewById(R.id.foto);
        logo = ( ImageView ) findViewById(R.id.logo);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);

        logo.setImageResource(R.drawable.logouth);

        if (id != -1){
            actualizacionActiva = true;
            getSupportActionBar().setTitle("Actualizar Contacto");
            extraerContacto(String.valueOf(id));
        }else{
            foto.setImageResource(R.drawable.user);
            getSupportActionBar().setTitle("Agregar Contacto");
        }

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgregarContactoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirFolder();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nombre.getText().toString().equals("")){
                    Toast.makeText(AgregarContactoActivity.this, "Agregue un nombre valido", Toast.LENGTH_LONG).show();
                    return;
                }

                if (numero.getText().toString().equals("")){
                    Toast.makeText(AgregarContactoActivity.this, "Agregue un numero valido", Toast.LENGTH_LONG).show();
                    return;
                }

                if (nota.getText().toString().equals("")){
                    Toast.makeText(AgregarContactoActivity.this, "Agregue una nota valida", Toast.LENGTH_LONG).show();
                    return;
                }

                if (id == -1){
                    agregarContacto(nombre.getText().toString(),
                            paises.getSelectedItem().toString(),
                            numero.getText().toString(),
                            nota.getText().toString(),
                            ubicacion.toString());
                }else{
                    actualizarContacto(id,
                            nombre.getText().toString(),
                            paises.getSelectedItem().toString(),
                            numero.getText().toString(),
                            nota.getText().toString(),
                            ubicacion.toString());
                }
            }
        });

        paises.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String paisSeleccionado = adapterView.getItemAtPosition(i).toString();

                if (!actualizacionActiva){
                    switch (paisSeleccionado){
                        case "Costa Rica":
                            numero.setText("+506");
                            break;
                        case "El Salvador":
                            numero.setText("+502");
                            break;
                        case "Guatemala":
                            numero.setText("+503");
                            break;
                        default:
                            numero.setText("+504");
                            break;
                    }
                }

                actualizacionActiva = false;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

    }

    public void agregarContacto(String nombre, String pais, String numero, String nota, String imagen){
        try{

            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
            SQLiteDatabase bd = conexion.getWritableDatabase();
            Contactos contacto = new Contactos(nombre, pais, numero, nota, imagen);
            ContentValues valores = new ContentValues();
            valores.put("nombre", contacto.getNombre());
            valores.put("pais", contacto.getPais());
            valores.put("numero", contacto.getNumero());
            valores.put("nota", contacto.getNota());
            valores.put("image", contacto.getImagen());
            bd.insert(Transacciones.tablacontactos, "id", valores);
            Toast.makeText(this, "Se ha agregado " + nombre + " a tus contactos", Toast.LENGTH_SHORT).show();
            limpiar();

        }catch(Exception ex){
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void actualizarContacto(int id, String nombre, String pais, String numero, String nota, String imagen){
        try{

            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
            SQLiteDatabase bd = conexion.getWritableDatabase();
            Contactos contacto = new Contactos(nombre, pais, numero, nota, imagen);
            ContentValues valores = new ContentValues();
            valores.put("nombre", contacto.getNombre());
            valores.put("pais", contacto.getPais());
            valores.put("numero", contacto.getNumero());
            valores.put("nota", contacto.getNota());
            valores.put("image", contacto.getImagen());
            String[] idActualizacion = new String[] {String.valueOf(id)};
            bd.update(Transacciones.tablacontactos, valores,"id=?", idActualizacion);
            Toast.makeText(this, "Se ha actualizado " + nombre + " de tus contactos", Toast.LENGTH_SHORT).show();
            limpiar();

            Intent intent = new Intent(AgregarContactoActivity.this, VerContactosActivity.class);
            startActivity(intent);

        }catch(Exception ex){
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void extraerContacto(String id){
        try{
            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
            SQLiteDatabase bd = conexion.getWritableDatabase();
            String[] busqueda = {String.valueOf(id)};
            String[] campos = {"id", "nombre", "pais", "numero", "nota", "image"};
            Cursor cursor =  bd.query(Transacciones.tablacontactos, campos, "id=?", busqueda, null, null, null);
            cursor.moveToFirst();
            contacto = new Contactos(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5));
            nombre.setText(contacto.getNombre());
            paises.setSelection(((ArrayAdapter<String>)paises.getAdapter()).getPosition(contacto.getPais()));
            numero.setText(contacto.getNumero());
            nota.setText(contacto.getNota());
            ubicacion = contacto.getImagen();
            Bitmap b = BitmapFactory.decodeFile(contacto.getImagen());
            foto.setImageBitmap(b);
        }catch(Exception ex){
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void permisos()
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},acceso_camara);
        }
        else
        {
            fotoDispatch();
        }
    }

    @Override
    public void onRequestPermissionsResult(int codigo, @NonNull String[] permisos, @NonNull int[] resultados) {
        super.onRequestPermissionsResult(codigo, permisos, resultados);

        if(codigo == acceso_camara)
        {
            if(resultados.length > 0 && resultados[0] == PackageManager.PERMISSION_GRANTED)
            {
                fotoDispatch();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Acceso de Camara Denegado",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int codigo, int resultado, @Nullable Intent data) {
        super.onActivityResult(codigo, resultado, data);

        if(codigo == captura && resultado == RESULT_OK)
        {
            try {
                File archivoFoto = new File(ubicacion);
                foto.setImageURI(Uri.fromFile(archivoFoto));
            }
            catch (Exception ex)
            {
                ex.toString();
            }
        }
    }

    private File crearImagen() throws IOException {
        String fecha = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File archivos = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(
                fecha,
                ".jpg",
                archivos
        );

        ubicacion = imagen.getAbsolutePath();
        return imagen;
    }
    private void fotoDispatch() {
        Intent fotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (fotoIntent.resolveActivity(getPackageManager()) != null) {
            File archivo = null;
            try {
                archivo = crearImagen();
            } catch (IOException ex) {
                ex.toString();
            }
            if (archivo != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.pm1e11248.fileprovider",
                        archivo);
                fotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(fotoIntent, captura);
            }
        }
    }

    public void abrirFolder(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse("/storage/emulated/0/Android/data/com.example.pm1e11248/files/Pictures/");
        intent.setDataAndType(uri, "*/*");
        startActivity(Intent.createChooser(intent, "Abrir Folder"));
    }

    public void limpiar() {
        nombre.setText(Transacciones.Empty);
        numero.setText(Transacciones.Empty);
        paises.setSelection(0);
        nota.setText(Transacciones.Empty);
        foto.setImageResource(android.R.color.transparent);
    }
}