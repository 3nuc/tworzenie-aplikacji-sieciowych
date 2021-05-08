package pl.systemyRozproszone.systemyRozproszone.nearestNeighbours.kmeans.helpers;

public class NameToNumberPair {

    String number;
    String name;

    public NameToNumberPair(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public NameToNumberPair() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
