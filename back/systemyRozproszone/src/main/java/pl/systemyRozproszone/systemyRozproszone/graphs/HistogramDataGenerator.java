package pl.systemyRozproszone.systemyRozproszone.graphs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistogramDataGenerator {

    public static String generate(List<String> columnToCheck, List<String> decissionColumn, int amountOfSectors) {
        List<Double> digitizedList = digitizeColumn(columnToCheck);
        
        double maxValue = getMax(digitizedList);
        double minValue = getMin(digitizedList);
        double sectionWidth;
        HistogramColumnHolder [] sections = new HistogramColumnHolder[amountOfSectors];
        
        //now i have max and min value of the column, now i have to count how wide 
        //the section has to be
        sectionWidth = (maxValue-minValue)/amountOfSectors;
        sections = setSectionsBorders(minValue, maxValue, sectionWidth, amountOfSectors, sections);
        sections = setElementsToSections(sections, decissionColumn, columnToCheck, digitizedList);


        //create jsonobj
        JsonObject histogramDataObject = new JsonObject();

        JsonArray sectionsArray = new JsonArray();
        //add array of sections
        for(int i=0 ; i <sections.length; i++){

            JsonObject sectionObject = new JsonObject();

            JsonArray sectionArray = new JsonArray();
            for(int j=0; j<sections[i].getValues().size();j++){
                JsonObject itemInSection = new JsonObject();
                itemInSection.addProperty("decission", sections[i].getValues().get(j).decission);
                itemInSection.addProperty("value", sections[i].getValues().get(j).value);
                sectionArray.add(itemInSection);
            }
            JsonObject info = new JsonObject();
            info.addProperty("sectorMin", sections[i].sectorMin);
            info.addProperty("sectorMax", sections[i].sectorMax);
            info.addProperty("mean", countMean(sections[i]));
            info.addProperty("amountOfItemsInSector", sections[i].values.size());

            JsonArray itemCount = new JsonArray();
            List<TypeCount> countItems = countItems(decissionColumn, sections[i]);

            for(int k=0; k<countItems.size(); k++){
                JsonObject item = new JsonObject();
                item.addProperty("decission", countItems.get(k).decission);
                item.addProperty("count", countItems.get(k).count);
                itemCount.add(item);
            }

            sectionObject.add("arrayOfElements", sectionArray);
            sectionObject.add("info", info);
            sectionObject.add("itemCount", itemCount);
            sectionsArray.add(sectionObject);

        }
        histogramDataObject.add("items", sectionsArray);

        
        //right now i have width of the section so i have to find out which elements fit into
        //correct sections (indexes of elements to be exact)
        return histogramDataObject.toString();
    }

    private static String countMean(HistogramColumnHolder section) {

        int size = section.getValues().size();
        double sum = 0;
        for(int i=0; i<section.getValues().size(); i++){
            sum+= section.getValues().get(i).value;
        }
        return String.valueOf((sum/size));
    }

    private static List<TypeCount> countItems(List<String> decissionColumn, HistogramColumnHolder section) {

        List<String> decissions = new ArrayList<>();

        //find distinct elements in decissions
        for(int i=1; i<decissionColumn.size(); i++){

            String currentColumnValue = decissionColumn.get(i);
            boolean isThere  = false;

            for(int j=0; j< decissions.size(); j++){

                if(currentColumnValue.equals(decissions.get(j))){
                    isThere = true;
                }
            }

            if(!isThere){
                decissions.add(currentColumnValue);
            }
        }
        //count elements with given decission
        List<TypeCount> typeCounts = new ArrayList<>();
        for(int i=0; i<decissions.size(); i++){
            TypeCount typeCount = new TypeCount();
            typeCount.setDecission(decissions.get(i));
            int counter = 0;
            for(int j=0; j<section.getValues().size(); j++){
                if(section.getValues().get(j).decission.equals(typeCount.decission)){
                    counter++;
                }
            }
            typeCount.setCount(counter);
            typeCounts.add(typeCount);
        }
        return typeCounts;
    }

    private static HistogramColumnHolder[] setElementsToSections(HistogramColumnHolder[] sections, List<String> decissionColumn, List<String> columnToCheck, List<Double> digitizedList) {

        for(int i=0 ; i<digitizedList.size(); i++){

            int rawIndex = i+1;

            for(int j=0; j<sections.length; j++){

                //if it fits between borders add the value
                if(digitizedList.get(i)>=sections[j].sectorMin && digitizedList.get(i)<=sections[j].sectorMax){
                    sections[j].values.add(new HistogramElement(decissionColumn.get(rawIndex), digitizedList.get(i)));
                }

            }

        }
        return sections;
    }

    private static HistogramColumnHolder[] setSectionsBorders(double minValue, double maxValue, double sectionWidth, int amountOfSectors, HistogramColumnHolder[] sections) {

        double smallFraction = 1E-10;
        double currentMin = minValue;
        double currentMax = minValue+sectionWidth;

        for(int i=0; i<sections.length; i++){
            sections[i] = new HistogramColumnHolder();
            sections[i].sectorMin = currentMin;
            sections[i].sectorMax = currentMax;

            currentMin = currentMax+smallFraction;
            currentMax = currentMin+sectionWidth;
        }

        return sections;
    }

    private static double getMin(List<Double> digitizedList) {
        double min = Double.MAX_VALUE;
        
        for(int i=0; i<digitizedList.size(); i++){
            if(digitizedList.get(i)<min){
                min = digitizedList.get(i);
            }
        }
        return min;
    }

    private static double getMax(List<Double> digitizedList) {
        
        double max = Double.MIN_VALUE;
        
        for(int i=0; i< digitizedList.size(); i++){
            if(digitizedList.get(i)>max){
                max = digitizedList.get(i);
            }
        }
        return max;
    }

    private static List<Double> digitizeColumn(List<String> column){
        List<Double> digitizedList = new ArrayList<>();

        for(int i=1; i<column.size(); i++){
            try{
                digitizedList.add(Double.parseDouble(column.get(i)));
            }catch (NumberFormatException e){
                System.out.println("wywalilo sie na digitizowaniu kolumny");
            }
        }
        return digitizedList;
    }
}
