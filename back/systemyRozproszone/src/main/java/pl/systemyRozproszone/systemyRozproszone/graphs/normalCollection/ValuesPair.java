package pl.systemyRozproszone.systemyRozproszone.graphs.normalCollection;

public class ValuesPair {

    String column1;
    String column1Name;
    String column2Name;
    String column2;
    String value;
    int diagramId;

    public ValuesPair(String column1, String column2, String value, int diagramId) {
        this.column1 = column1;
        this.column2 = column2;
        this.value = value;
        this.diagramId = diagramId;
    }

    public ValuesPair(){

    }

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getDiagramId() {
        return diagramId;
    }

    public void setDiagramId(int diagramId) {
        this.diagramId = diagramId;
    }

    public String getColumn1Name() {
        return column1Name;
    }

    public void setColumn1Name(String column1Name) {
        this.column1Name = column1Name;
    }

    public String getColumn2Name() {
        return column2Name;
    }

    public void setColumn2Name(String column2Name) {
        this.column2Name = column2Name;
    }
}
