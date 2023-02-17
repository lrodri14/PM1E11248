package com.example.pm1e11248.transacciones;
import java.sql.Blob;

public class Contactos {

    private int id;
    private String nombre;
    private String pais;
    private String numero;
    private String nota;
    private String imagen;

    public Contactos(){

    }

    public Contactos(int id, String nombre, String pais, String numero, String nota, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.numero = numero;
        this.nota = nota;
        this.imagen = imagen;
    }

    public Contactos(String nombre, String pais, String numero, String nota, String imagen) {
        this.nombre = nombre;
        this.pais = pais;
        this.numero = numero;
        this.nota = nota;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
