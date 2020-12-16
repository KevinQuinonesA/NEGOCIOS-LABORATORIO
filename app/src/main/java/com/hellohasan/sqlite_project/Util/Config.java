package com.hellohasan.sqlite_project.Util;

public class Config {

    public static final String DATABASE_NAME = "maquinaria-db";

    //column names of planta table
    public static final String TABLE_PLANTA = "planta";
    public static final String COLUMN_PLANTA_ID = "_id";
    public static final String COLUMN_PLANTA_NAME = "name";
    public static final String COLUMN_PLANTA_STATE = "state";

    //column names of planta table
    public static final String TABLE_LINEAP = "lineap";
    public static final String COLUMN_LINEAP_ID = "_id";
    public static final String COLUMN_LINEAP_NAME = "name";
    public static final String COLUMN_LINEAP_STATE = "state";

    //others for general purpose key-value pair data
    public static final String TITLE = "title";
    public static final String CREATE_PLANTA = "create_planta";
    public static final String UPDATE_PLANTA = "update_planta";
    public static final String CREATE_LINEAP = "create_lineap";
    public static final String UPDATE_LINEAP = "update_lineap";
}
