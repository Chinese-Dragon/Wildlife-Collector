package edu.drury.mcs.wildlife.JavaClass;

/**
 * Created by yma004 on 12/11/16.
 */

public class Species {

    private String commonName;
    private String scientificName;
    private int group_ID;

    public Species(){

    }

    public Species(String common, String science, int ID) {
        this.commonName = common;
        this.scientificName = science;
        this.group_ID = ID;
    }

    public String getCommonName() {

        return commonName;
    }

    public void setCommonName(String commonName) {

        this.commonName = commonName;
    }

    public String getScientificName() {

        return scientificName;
    }

    public void setScientificName(String scientificName) {

        this.scientificName = scientificName;
    }

    public int getGroup_ID() {
        return group_ID;
    }

    public void setGroup_ID(int group_ID) {
        this.group_ID = group_ID;
    }
}
