package com.hellohasan.sqlite_project.Features.Linea.UpdateLineaInfo;

import com.hellohasan.sqlite_project.Features.Linea.CreateLinea.Linea;

public interface LineaUpdateListener {
    void onLineaInfoUpdated(Linea linea, int position);
}
