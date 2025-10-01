package cod;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Cours {
    private StringProperty type;
    private IntegerProperty duree;
    private StringProperty niveau;

    public Cours(StringProperty type, IntegerProperty duree, StringProperty niveau) {
        this.type = type;
        this.duree = duree;
        this.niveau = niveau;
    }

    public StringProperty getType() {
        return this.type;
    }
    public IntegerProperty getDuree() {
        return this.duree;
    }
    public StringProperty getNiveau() {
        return this.niveau;
    }
    
    @Override
    public String toString() {
        return "Cours[type=" + this.type + ", duree=" + this.duree + ", niveau=" + this.niveau + "]";
    }
}