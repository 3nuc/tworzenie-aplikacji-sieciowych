package pl.systemyRozproszone.systemyRozproszone.nearestNeighbours;

public class DistanceHolder {

    private double distance;
    private int id;


    public DistanceHolder() {
    }

    public DistanceHolder(double distance, int id) {
        this.distance = distance;
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
