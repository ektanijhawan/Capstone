package com.example.ekta.noir.model;

import java.io.Serializable;



public class PaletteData implements Serializable, Comparable{

    private String colorName, colorHexCode;
    private int colorPopulation;

    public PaletteData(){

    }

    public PaletteData(String colorName, String colorHexCode, int colorPopulation){
        this.colorName = colorName;
        this.colorHexCode = colorHexCode;
        this.colorPopulation = colorPopulation;
    }

    public void setColorName(String colorName){
        this.colorName = colorName;
    }
    public String getColorName(){
        return colorName;
    }

    public void setColorHexCode(String colorHexCode){
        this.colorHexCode = colorHexCode;
    }
    public String getColorHexCode(){
        return colorHexCode;
    }

    public void setColorPopulation(int colorPopulation){
        this.colorPopulation = colorPopulation;
    }
    public int getColorPopulation(){
        return colorPopulation;
    }

    @Override
    public int compareTo(Object another) {
        int comparePopulation = ((PaletteData) another).getColorPopulation();
        return comparePopulation - this.colorPopulation;
    }

//    @Override
//    public String toString(){
//        return "[ "
//    }
//    @Override
//    public int compareTo(PaletteData another) {
//        int comparePopulation = ((PaletteData) another).getColorPopulation();
//        return 0;
//    }
}

