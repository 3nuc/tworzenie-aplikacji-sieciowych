package pl.systemyRozproszone.systemyRozproszone.graphs.normalCollection;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class NormalCollectionGenerator {


    public static String generatedListJson(List<List<String>> dataDigitized, List<String> columnNames, List<String> decissionClass){

        List<List<String>> data = dataDigitized;
        List<String> columns = columnNames;
        List<String> decission = decissionClass; //member to iterate +1 index
        List<ValuesPair> createdValue = new ArrayList<>();
        List<NameIndexPair> decisionPairs = new ArrayList<>();
        List<List<ValuesPair>> listOfLists = new ArrayList<>();

        int graphCounter = 0;

        for(int i=0; i<columnNames.size(); i++){

            List<String> currentBaseColumn = data.get(i);

            for(int j=0; j<columnNames.size(); j++){

                List<String> currentPairedColumn = data.get(j);

                List<ValuesPair> oneGraph = new ArrayList<>();
                for(int k=1; k<data.get(0).size();k++){
                    ValuesPair pair = new ValuesPair();
                    pair.setColumn1(currentBaseColumn.get(k));
                    pair.setColumn2(currentPairedColumn.get(k));
                    pair.setColumn1Name(currentBaseColumn.get(0));
                    pair.setColumn2Name(currentPairedColumn.get(0));
                    pair.setDiagramId(graphCounter);
                    pair.setValue(decission.get(k));
                    createdValue.add(pair);
                    oneGraph.add(pair);
                }
                listOfLists.add(oneGraph);
                graphCounter++;
            }
        }

        int decissionClassCounter = 0;
        for(int i=1;i<decissionClass.size();i++){
            String currentClass = decissionClass.get(i);

            boolean doesThisNameExist = false;
            for(int j=0;j<decisionPairs.size();j++){
                if(decisionPairs.get(j).getValue().equals(currentClass)){
                    doesThisNameExist = true;
                }
            }
            if(!doesThisNameExist){
                decisionPairs.add(new NameIndexPair(currentClass, decissionClassCounter));
                decissionClassCounter++;
            }
        }

        JsonObject response = new JsonObject();

        JsonArray graphs = new JsonArray();

        for(int i=0; i<listOfLists.size();i++){
            JsonObject graphToReturn = new JsonObject();

            JsonArray oneGraphArray = new JsonArray();

            List<ValuesPair> graph = listOfLists.get(i);
            double minX = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE;
            double minY = Double.MAX_VALUE;
            double maxY = Double.MIN_VALUE;

            for(int k=0; k<graph.size(); k++){

                if(Double.parseDouble(graph.get(k).getColumn1()) > maxX){
                    maxX = Double.parseDouble(graph.get(k).getColumn1());
                }
                if(Double.parseDouble(graph.get(k).getColumn1()) < minX){
                    minX = Double.parseDouble(graph.get(k).getColumn1());
                }
                if(Double.parseDouble(graph.get(k).getColumn2()) > maxY){
                    maxY = Double.parseDouble(graph.get(k).getColumn2());
                }
                if(Double.parseDouble(graph.get(k).getColumn2()) < minY){
                    minY = Double.parseDouble(graph.get(k).getColumn2());
                }

                ValuesPair graphItem = graph.get(k);

                JsonObject graphItemObject = new JsonObject();
                graphItemObject.addProperty("row", graphItem.getColumn1());
                graphItemObject.addProperty("col", graphItem.getColumn2());
                graphItemObject.addProperty("val", graphItem.getValue());
                graphItemObject.addProperty("graphId", graphItem.getDiagramId());
                oneGraphArray.add(graphItemObject);

            }
            graphToReturn.add("items", oneGraphArray);

            JsonObject axisProps = new JsonObject();
            axisProps.addProperty("minX", minX);
            axisProps.addProperty("minY", minY);
            axisProps.addProperty("maxX", maxX);
            axisProps.addProperty("maxY", maxY);
            axisProps.addProperty("rowName",graph.get(0).getColumn1Name());
            axisProps.addProperty("colName",graph.get(0).getColumn2Name());
            graphToReturn.add("properties", axisProps);

            graphs.add(graphToReturn);

        }
        response.add("graphs", graphs);
        return response.toString();
    }
}
