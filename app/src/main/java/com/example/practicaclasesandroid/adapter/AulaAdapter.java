package com.example.practicaclasesandroid.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicaclasesandroid.R;
import com.example.practicaclasesandroid.data.DataRoomDB;
import com.example.practicaclasesandroid.model.AulaEntity;

import java.util.List;

public class AulaAdapter extends RecyclerView.Adapter<AulaAdapter.ViewHolder> {

    private List<AulaEntity> aulaEntityList;
    private Activity context;

    private DataRoomDB database;

    public AulaAdapter(List<AulaEntity> aulaEntityList, Activity context) {
        this.aulaEntityList = aulaEntityList;
        this.context = context;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.aula_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AulaEntity item = aulaEntityList.get(position);

        holder.textViewAulaFull.setText("Nombre: " + item.getNombre() + " | Ubicación: " + item.getUbicacion());

        database = DataRoomDB.getInstance(context);

        holder.imageEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AulaEntity a = aulaEntityList.get(holder.getAdapterPosition());

                int sId = a.getId();
                String sNombre = a.getNombre();
                String sUbicacion = a.getUbicacion();

                // CREAR DIALOGO
                Dialog dialog = new Dialog(context);
                // Construir el contenido
                dialog.setContentView(R.layout.dialog_editar_aula);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                // Activar dialogo
                dialog.show();
                dialog.getWindow().setAttributes(lp);

                //inicializar variables
                EditText editTextEditNombre = dialog.findViewById(R.id.editTextEditNombre);
                EditText editTextEditUbicacion = dialog.findViewById(R.id.ediTextEditUbicacion);
                Button btnEdit = dialog.findViewById(R.id.btnEdit);

                // Poner datos seleccionados
                editTextEditNombre.setText(sNombre);
                editTextEditUbicacion.setText(sUbicacion);

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        // Obtener modificaciones
                        String uNombre = editTextEditNombre.getText().toString();
                        String uUbicacion = editTextEditUbicacion.getText().toString();

                        // Modificar BBDD
                        database.aulaDao().updateNombre(sId, uNombre);
                        database.aulaDao().updateUbicacion(sId, uUbicacion);

                        aulaEntityList.clear();
                        aulaEntityList.addAll(database.aulaDao().getAulas());
                        Toast.makeText(context, "Aula Editada", Toast.LENGTH_SHORT).show();

                        notifyDataSetChanged();
                    }
                });
            }
        });

        holder.imageBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                int position = holder.getAdapterPosition();
                                AulaEntity nAula = aulaEntityList.get(position);
                                database.aulaDao().delete(nAula);

                                aulaEntityList.remove(position);
                                notifyItemRemoved(position);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Aula Eliminada", Toast.LENGTH_SHORT).show();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("¿Estás seguro?").setPositiveButton("Sí", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }


        });
    }

        @Override
        public int getItemCount () {
            return aulaEntityList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView textViewAulaFull;
            ImageView imageEditar, imageBorrar;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                textViewAulaFull = itemView.findViewById(R.id.textViewAulaFull);
                imageEditar = itemView.findViewById(R.id.imageEditar);
                imageBorrar = itemView.findViewById(R.id.imageBorrar);
            }
        }
    }

