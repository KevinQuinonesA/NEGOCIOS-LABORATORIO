package com.hellohasan.sqlite_project.Features.Linea.ShowLineaList;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.hellohasan.sqlite_project.Features.Linea.UpdateLineaInfo.LineaUpdateDialogFragment;
import com.hellohasan.sqlite_project.Features.Linea.UpdateLineaInfo.LineaUpdateListener;
import com.hellohasan.sqlite_project.R;
import com.hellohasan.sqlite_project.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.hellohasan.sqlite_project.Features.Linea.CreateLinea.Linea;
import com.hellohasan.sqlite_project.Database.DatabaseQueryClass;
import java.util.List;

public class LineaListRecyclerViewAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private Context context;
    private List<Linea> lineaList;
    private DatabaseQueryClass databaseQueryClass;

    public LineaListRecyclerViewAdapter(Context context, List<Linea> lineaList) {
        this.context = context;
        this.lineaList = lineaList;
        databaseQueryClass = new DatabaseQueryClass(context);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.linea_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final int itemPosition = position;
        final Linea linea = lineaList.get(position);
        Logger.d("Linea nombre: " + linea.getName() );
        holder.nameTextView.setText(linea.getName());
        holder.stateTextView.setText(linea.getState());

        holder.crossButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure, You wanted to delete this linea?");
                        alertDialogBuilder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        deleteLinea(itemPosition);
                                    }
                                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        holder.editButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LineaUpdateDialogFragment lineaUpdateDialogFragment = LineaUpdateDialogFragment.newInstance(linea.getName(), itemPosition, new LineaUpdateListener() {
                    @Override
                    public void onLineaInfoUpdated(Linea linea, int position) {
                        lineaList.set(position, linea);
                        notifyDataSetChanged();
                    }
                });
                lineaUpdateDialogFragment.show(((LineaListActivity) context).getSupportFragmentManager(), Config.UPDATE_LINEAP);
            }
        });
    }

    private void deleteLinea(int position) {
        Linea linea = lineaList.get(position);
        long count = databaseQueryClass.deleteLineaByRegNum(linea.getState());

        if(count>0){
            lineaList.remove(position);
            notifyDataSetChanged();
            ((LineaListActivity) context).viewVisibility();
            Toast.makeText(context, "Linea deleted successfully", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(context, "Linea not deleted. Something wrong!", Toast.LENGTH_LONG).show();

    }

    @Override
    public int getItemCount() {
        return lineaList.size();
    }
}
