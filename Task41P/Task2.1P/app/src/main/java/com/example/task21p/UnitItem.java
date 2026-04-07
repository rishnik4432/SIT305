package com.example.task21p;

public class UnitItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_UNIT = 1;
    private int type;
    private String name;
    public UnitItem(int type, String name) {
        this.type = type;
        this.name = name;
    }
    public int getType(){
        return type;
    }
    public String getName() {
        return name;
    }
}
