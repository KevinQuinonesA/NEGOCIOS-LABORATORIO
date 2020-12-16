package com.hellohasan.sqlite_project.Features.Planta.UpdatePlantaInfo;

import com.hellohasan.sqlite_project.Features.Planta.CreatePlanta.Planta;

public interface PlantaUpdateListener {
    void onPlantaInfoUpdated(Planta planta, int position);
}
