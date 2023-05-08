package com.example.ediaryfx;

import java.io.Serializable;
import java.util.ArrayList;

public class Klass implements Serializable {
    private String klassiNumber;

    private String aine;
    private ArrayList<Õpilane> õpilasteGrupp;


    public Klass(String klassiNumber, ArrayList<Õpilane> õpilasteGrupp, String aine) {
        this.klassiNumber = klassiNumber;
        this.õpilasteGrupp = õpilasteGrupp;
        this.aine = aine;

    }

    /**
     * Klassi numbri get meetod
     * @return klassi number
     */
    public String getKlassiNumber() {
        return klassiNumber;
    }

    /**
     * Aine nimetuse get meetod
     * @return aine nimetus
     */
    public String getAine() {
        return aine;
    }

    /**
     * Õpilaste gruppide get meetod
     * @return õpilaste grupp
     */
    public ArrayList<Õpilane> getÕpilasteGrupp() {
        return õpilasteGrupp;
    }

}
