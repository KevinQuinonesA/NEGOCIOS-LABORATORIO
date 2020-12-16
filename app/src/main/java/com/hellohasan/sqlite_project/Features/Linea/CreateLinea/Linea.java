package com.hellohasan.sqlite_project.Features.Linea.CreateLinea;

public class Linea {
    private int id;
    private String name;
    private String  State;

    public Linea(int id, String name) {
        this.id = id;
        this.name = name;
        this.State="A";
    }
    public Linea(int id, String name, String state ) {
        this.id = id;
        this.name = name;
        this.State=state;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }


}
