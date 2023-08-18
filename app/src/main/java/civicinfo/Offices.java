package civicinfo;

import java.io.Serializable;
import java.util.ArrayList;

public class Offices implements Serializable {
    public String name;
    public ArrayList<Integer> officialIndices;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getOfficialIndices() {
        return officialIndices;
    }

    public void setOfficialIndices(ArrayList<Integer> officialIndices) {
        this.officialIndices = officialIndices;
    }

    public Offices(String name, ArrayList<Integer> officialIndices) {
        this.name = name;
        this.officialIndices = officialIndices;
    }
}
