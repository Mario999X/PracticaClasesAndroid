package com.example.practicaclasesandroid.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.practicaclasesandroid.model.AulaEntity;

import java.util.List;

@Dao
public interface AulaDao {

    @Insert
    long insert(AulaEntity a);

    @Delete
    void delete(AulaEntity a);

    @Query("DELETE FROM tabla_aulas")
    void deleteAll();

    @Query("SELECT * FROM tabla_aulas")
    List<AulaEntity> getAulas();

    @Query("UPDATE tabla_aulas set nombre = :sNombre where id = :sId")
    void updateNombre(Integer sId, String sNombre);

    @Query("UPDATE tabla_aulas set ubicacion = :sUbicacion where id = :sId")
    void updateUbicacion(Integer sId, String sUbicacion);
}
