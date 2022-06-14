package com.example.practicaclasesandroid.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.practicaclasesandroid.R;
import com.example.practicaclasesandroid.adapter.AulaAdapter;
import com.example.practicaclasesandroid.data.DataRoomDB;
import com.example.practicaclasesandroid.model.AulaEntity;

import java.util.ArrayList;
import java.util.List;

public class ListadoClases extends Fragment {

    Toolbar toolbar;

    DataRoomDB database;

    RecyclerView recycler;
    List<AulaEntity> aulaEntities = new ArrayList<>();
    AulaAdapter aulaAdapter;
    LinearLayoutManager llm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = DataRoomDB.getInstance(getContext());
        aulaEntities = database.aulaDao().getAulas();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_listado_clases, container, false);

        // ELEMENTOS RECYCLER
        recycler = v.findViewById(R.id.recycler);
        llm = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(llm);
        aulaAdapter = new AulaAdapter(aulaEntities, getActivity());
        recycler.setAdapter(aulaAdapter);

        // TOOLBAR + LISTENER
        toolbar = v.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.layout_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuAddClase:
                        // CREACION DIALOGO
                        Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.dialog_new_aula);

                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                        dialog.show();
                        dialog.getWindow().setAttributes(lp);

                        // ELEMENTOS DIALOGO
                        EditText editTextNombreAdd = dialog.findViewById(R.id.editTextNombreAdd);
                        EditText editTextUbicacionAdd = dialog.findViewById(R.id.editTextUbicacionAdd);
                        Button btnAdd = dialog.findViewById(R.id.btnAdd);

                        // LISTENER BOTON
                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AulaEntity aula = new AulaEntity();
                                aula.setNombre(editTextNombreAdd.getText().toString());
                                aula.setUbicacion(editTextUbicacionAdd.getText().toString());

                                database.aulaDao().insert(aula);

                                aulaEntities.clear();
                                aulaEntities = database.aulaDao().getAulas();
                                aulaAdapter = new AulaAdapter(aulaEntities, getActivity());
                                recycler.setAdapter(aulaAdapter);

                                dialog.dismiss();
                                Toast.makeText(getContext(), "Aula Introducida", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case R.id.menuEliminarClases:

                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        database.aulaDao().deleteAll();

                                        aulaEntities.clear();
                                        aulaEntities = database.aulaDao().getAulas();
                                        aulaAdapter = new AulaAdapter(aulaEntities, getActivity());
                                        recycler.setAdapter(aulaAdapter);

                                        dialog.dismiss();
                                        Toast.makeText(getActivity(), "Eliminación Total Completada", Toast.LENGTH_SHORT).show();
                                        getFragmentManager().popBackStackImmediate();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("¿Estás seguro?").setPositiveButton("Sí", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                }

                return false;
            }
        });

        return v;
    }
}