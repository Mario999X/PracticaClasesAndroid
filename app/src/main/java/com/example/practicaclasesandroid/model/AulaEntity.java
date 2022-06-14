package com.example.practicaclasesandroid.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabla_aulas")
public class AulaEntity {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String nombre;
    private String ubicacion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
