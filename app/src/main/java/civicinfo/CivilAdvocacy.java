package civicinfo;

import java.io.Serializable;
import java.util.ArrayList;

public class CivilAdvocacy implements Serializable {
    private NormalizedInput NormalizedInputObject;
    private String kind;
    private ArrayList<Offices> offices;
    private ArrayList<Officials> officials;

    public NormalizedInput getNormalizedInputObject() {
        return NormalizedInputObject;
    }

    public void setNormalizedInputObject(NormalizedInput normalizedInputObject) {
        NormalizedInputObject = normalizedInputObject;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public ArrayList<Offices> getOffices() {
        return offices;
    }

    public void setOffices(ArrayList<Offices> offices) {
        this.offices = offices;
    }

    public ArrayList<Officials> getOfficials() {
        return officials;
    }

    public void setOfficials(ArrayList<Officials> officials) {
        this.officials = officials;
    }


    public CivilAdvocacy(NormalizedInput normalizedInputObject, String kind, ArrayList<Offices> offices, ArrayList<Officials> officials) {
        NormalizedInputObject = normalizedInputObject;
        this.kind = kind;
        this.offices = offices;
        this.officials = officials;
    }
}
