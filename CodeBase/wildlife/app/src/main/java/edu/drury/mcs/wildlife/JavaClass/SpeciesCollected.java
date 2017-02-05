package edu.drury.mcs.wildlife.JavaClass;

/**
 * Created by yma004 on 2/4/17.
 */

public class SpeciesCollected {
    private int quantity;
    private String scientificName;

    public SpeciesCollected() {

    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }
}
