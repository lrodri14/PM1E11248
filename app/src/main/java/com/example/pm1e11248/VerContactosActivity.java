package com.example.pm1e11248;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.Manifest;

import com.example.pm1e11248.conexion.SQLiteConexion;
import com.example.pm1e11248.transacciones.Contactos;
import com.example.pm1e11248.transacciones.Transacciones;

import java.util.ArrayList;

public class VerContactosActivity extends AppCompatActivity {

    SQLiteConexion conexion;
    ArrayList<Contactos> listaContactos;
    ArrayList<String> arregloContactos;
    ArrayAdapter adp;

    Button regresar, actualizar, compartir, eliminar, verfoto, llamar;

    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_contactos);
        getSupportActionBar().setTitle("Lista de Contactos");
        conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);


        regresar = ( Button ) findViewById(R.id.regresar2);
        actualizar = ( Button ) findViewById(R.id.actualizar);
        compartir = ( Button ) findViewById(R.id.compartir);
        eliminar = ( Button ) findViewById(R.id.eliminar);
        verfoto = ( Button ) findViewById(R.id.verfoto);
        llamar = ( Button ) findViewById(R.id.llamar);

        lista = ( ListView ) findViewById(R.id.lista);

        ObtenerListaContactos();

        adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arregloContactos);
        lista.setAdapter(adp);

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerContactosActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int color = Color.parseColor("#abaaad");
                view.setBackgroundColor(color);
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int seleccionado = lista.getCheckedItemPosition();
                if (seleccionado != ListView.INVALID_POSITION){
                    int id = listaContactos.get(seleccionado).getId();
                    Eliminar(id);
                    ObtenerListaContactos();
                    adp = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, arregloContactos);
                    lista.setAdapter(adp);
                }
            }
        });

        compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int seleccionado = lista.getCheckedItemPosition();
                if (seleccionado != ListView.INVALID_POSITION){
                    int id = listaContactos.get(seleccionado).getId();
                    String nombre = listaContactos.get(seleccionado).getNombre();
                    String numero = listaContactos.get(seleccionado).getNumero();
                    String nota = listaContactos.get(seleccionado).getNota();
                    Compartir(id, nombre, numero, nota);
                }
            }
        });

        llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int seleccionado = lista.getCheckedItemPosition();
                if (seleccionado != ListView.INVALID_POSITION){
                    String nombre = listaContactos.get(seleccionado).getNombre();
                    String numero = listaContactos.get(seleccionado).getNumero();
                    mostrarDialog(nombre, numero);
                }
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int seleccionado = lista.getCheckedItemPosition();
                if (seleccionado != ListView.INVALID_POSITION){
                    int id = listaContactos.get(seleccionado).getId();
                    Actualizar(id);
                }
            }
        });

        verfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int seleccionado = lista.getCheckedItemPosition();
                if (seleccionado != ListView.INVALID_POSITION){
                    String nombre = listaContactos.get(seleccionado).getNombre();
                    String foto = listaContactos.get(seleccionado).getImagen();
                    Intent intent = new Intent(VerContactosActivity.this, VerFotoActivity.class);
                    intent.putExtra("foto", foto);
                    intent.putExtra("nombre", nombre);
                    startActivity(intent);
                }
            }
        });
    }

    public void ObtenerListaContactos()
    {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Contactos contacto = null;
        listaContactos = new ArrayList<Contactos>();

        // Cursor
        Cursor cursor = db.rawQuery("SELECT * FROM contactos", null );

        while(cursor.moveToNext())
        {
            contacto = new Contactos();

            contacto.setId(cursor.getInt(0));
            contacto.setNombre(cursor.getString(1));
            contacto.setPais(cursor.getString(2));
            contacto.setNumero(cursor.getString(3));
            contacto.setNota(cursor.getString(4));
            contacto.setImagen(cursor.getString(5));

            listaContactos.add(contacto);
        }

        Toast.makeText(this, "Contactos en Total: " + listaContactos.size(), Toast.LENGTH_SHORT).show();

        cursor.close();
        Filling();
    }

    public void Filling()
    {
        arregloContactos = new ArrayList<String>();
        for(int i = 0; i < listaContactos.size(); i++)
        {
            arregloContactos.add(listaContactos.get(i).getId() + " | "+
                    listaContactos.get(i).getNombre() + " | "+
                    listaContactos.get(i).getNumero() + " | " +
                    listaContactos.get(i).getPais());
        }
    }

    public void Eliminar(int id){
        SQLiteDatabase db = conexion.getReadableDatabase();
        String[] argumentos = { String.valueOf(id) };
        String condicion = "id = ?";
        db.delete(Transacciones.tablacontactos, condicion, argumentos);
        Toast.makeText(this, "Registro Eliminado Correctamente", Toast.LENGTH_SHORT).show();
        db.close();
    }

    public void Compartir(int id, String nombre, String numero, String nota){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, id  + " | " + nombre +  " | " + numero + " | " + nota);
        startActivity(Intent.createChooser(intent, "Compartir por:"));
    }

    public void Llamar(String numero){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel: " + numero.substring(4)));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            return;
        }
        startActivity(intent);
    }

    public void Actualizar(int id){
        Intent intent = new Intent(VerContactosActivity.this, AgregarContactoActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void mostrarDialog(String nombre, String numero){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Desea llamar a: " + nombre + "?");
        builder.setMessage(numero);
        builder.setPositiveButton("Llamar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Llamar(numero);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog confirmacion = builder.create();
        confirmacion.show();
    }
}