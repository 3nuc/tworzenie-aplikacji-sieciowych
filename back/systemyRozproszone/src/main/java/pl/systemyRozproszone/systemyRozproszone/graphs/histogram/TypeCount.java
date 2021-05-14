package pl.systemyRozproszone.systemyRozproszone.graphs.histogram;

public class TypeCount {

    String decission;
    int count;

    public TypeCount() {
    }

    public TypeCount(String decission, int count) {
        this.decission = decission;
        this.count = count;
    }

    public String getDecission() {
        return decission;
    }

    public void setDecission(String decission) {
        this.decission = decission;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
