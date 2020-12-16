package com.hellohasan.sqlite_project.Features.Linea.ShowLineaList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hellohasan.sqlite_project.Features.Linea.CreateLinea.*;
import com.hellohasan.sqlite_project.Features.Planta.ShowPlantaList.PlantaListActivity;
import com.hellohasan.sqlite_project.R;
import com.hellohasan.sqlite_project.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.hellohasan.sqlite_project.Database.DatabaseQueryClass;

import java.util.ArrayList;
import java.util.List;

public class LineaListActivity extends AppCompatActivity implements LineaCreateListener{

    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);

    private List<Linea> lineaList = new ArrayList<>();

    private TextView lineaListEmptyTextView;
    private RecyclerView recyclerView;
    private LineaListRecyclerViewAdapter lineaListRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linea_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Logger.addLogAdapter(new AndroidLogAdapter());

        recyclerView = (RecyclerView) findViewById(R.id.lineaRecyclerView);
        lineaListEmptyTextView = (TextView) findViewById(R.id.emptylineaListTextView);

        lineaList.addAll(databaseQueryClass.getAllLinea());

        lineaListRecyclerViewAdapter = new LineaListRecyclerViewAdapter(this, lineaList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(lineaListRecyclerViewAdapter);

        viewVisibility();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLineaCreateDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_delete){

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure, You wanted to delete all lineas?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            boolean isAllDeleted = databaseQueryClass.deleteAllLineas();
                            if(isAllDeleted){
                                lineaList.clear();
                                lineaListRecyclerViewAdapter.notifyDataSetChanged();
                                viewVisibility();
                            }
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
        else if(item.getItemId()==R.id.plantaOpcion){
            Intent i2= new Intent(LineaListActivity.this, PlantaListActivity.class);
            startActivity(i2);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void viewVisibility() {
        if(lineaList.isEmpty())
            lineaListEmptyTextView.setVisibility(View.VISIBLE);
        else
            lineaListEmptyTextView.setVisibility(View.GONE);
    }

    private void openLineaCreateDialog() {
        LineaCreateDialogFragment lineaCreateDialogFragment = LineaCreateDialogFragment.newInstance("Create Linea", this);
        lineaCreateDialogFragment.show(getSupportFragmentManager(), Config.CREATE_LINEAP);
    }

    @Override
    public void onLineaCreated(Linea linea) {
        lineaList.add(linea);
        lineaListRecyclerViewAdapter.notifyDataSetChanged();
        viewVisibility();
        Logger.d(linea.getName());
    }

}
