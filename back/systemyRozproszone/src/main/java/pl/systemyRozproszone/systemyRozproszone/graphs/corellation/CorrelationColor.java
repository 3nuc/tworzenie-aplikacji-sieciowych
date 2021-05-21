package pl.systemyRozproszone.systemyRozproszone.graphs.corellation;

public class CorrelationColor {

    String correlationValue;
    int h;
    int s;
    int l;

    public String getCorrelationValue() {
        return correlationValue;
    }

    public void setCorrelationValue(String correlationValue) {
        this.correlationValue = correlationValue;
    }

    public void setColors(int h, int s, int l){
        this.h=h;
        this.s=s;
        this.l=l;
    }
    public void setColors(ColorsObject obj){
        this.h=obj.getH();
        this.s=obj.getS();
        this.l=obj.getL();
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }
}
