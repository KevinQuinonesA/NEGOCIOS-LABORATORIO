package com.hellohasan.sqlite_project.Features.Planta.ShowPlantaList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.hellohasan.sqlite_project.Database.DatabaseQueryClass;
import com.hellohasan.sqlite_project.Features.Linea.ShowLineaList.LineaListActivity;
import com.hellohasan.sqlite_project.Features.Planta.CreatePlanta.Planta;
import com.hellohasan.sqlite_project.Features.Planta.CreatePlanta.PlantaCreateDialogFragment;
import com.hellohasan.sqlite_project.Features.Planta.CreatePlanta.PlantaCreateListener;
import com.hellohasan.sqlite_project.R;
import com.hellohasan.sqlite_project.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class PlantaListActivity extends AppCompatActivity implements PlantaCreateListener{

    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);

    private List<Planta> plantaList = new ArrayList<>();

    private TextView plantaListEmptyTextView;
    private RecyclerView recyclerView;
    private PlantaListRecyclerViewAdapter plantaListRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planta_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Logger.addLogAdapter(new AndroidLogAdapter());

        recyclerView = (RecyclerView) findViewById(R.id.plantaRecyclerView);
        plantaListEmptyTextView = (TextView) findViewById(R.id.emptyplantaListTextView);

        plantaList.addAll(databaseQueryClass.getAllPlanta());

        plantaListRecyclerViewAdapter = new PlantaListRecyclerViewAdapter(this, plantaList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(plantaListRecyclerViewAdapter);

        viewVisibility();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPlantaCreateDialog();
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
            alertDialogBuilder.setMessage("Are you sure, You wanted to delete all plantas?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            boolean isAllDeleted = databaseQueryClass.deleteAllPlantas();
                            if(isAllDeleted){
                                plantaList.clear();
                                plantaListRecyclerViewAdapter.notifyDataSetChanged();
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
        else if(item.getItemId()==R.id.lineaOpcion){
            Intent i2= new Intent(PlantaListActivity.this, LineaListActivity.class);
            startActivity(i2);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    public void viewVisibility() {
        if(plantaList.isEmpty())
            plantaListEmptyTextView.setVisibility(View.VISIBLE);
        else
            plantaListEmptyTextView.setVisibility(View.GONE);
    }

    private void openPlantaCreateDialog() {
        PlantaCreateDialogFragment plantaCreateDialogFragment = PlantaCreateDialogFragment.newInstance("Create Planta", this);
        plantaCreateDialogFragment.show(getSupportFragmentManager(), Config.CREATE_PLANTA);
    }

    @Override
    public void onPlantaCreated(Planta planta) {
        plantaList.add(planta);
        plantaListRecyclerViewAdapter.notifyDataSetChanged();
        viewVisibility();
        Logger.d(planta.getName());
    }

}
