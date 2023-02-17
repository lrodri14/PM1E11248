package com.example.pm1e11248;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class VerFotoActivity extends AppCompatActivity {

    ImageView img;
    Button regresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_foto);

        img = ( ImageView ) findViewById(R.id.fotoPerfil);
        regresar = ( Button ) findViewById(R.id.regresar3);

        Intent intent = getIntent();
        String foto = intent.getStringExtra("foto");
        String nombre = intent.getStringExtra("nombre");
        getSupportActionBar().setTitle("Ver Foto de " + nombre);

        if (foto == null){
            img.setImageResource(R.drawable.user);
        }else{
            Bitmap b = BitmapFactory.decodeFile(foto);
            img.setImageBitmap(b);
        }

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerFotoActivity.this, VerContactosActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}