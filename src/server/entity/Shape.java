package server.entity;

import java.io.Serializable;
import java.util.List;

public class Shape implements Serializable{

//    private location
//    private
    private List<List<Coordinate>> coordinates;
    private boolean isRecognized;
    private String type;
    private Coordinate barycenter;


    public Shape(List<List<Coordinate>> coordinates) {
        this.coordinates = coordinates;
        this.barycenter = calculateBarycenter(coordinates);
    }

    public Shape(List<List<Coordinate>> coordinates, boolean isRecognized) {
        this.coordinates = coordinates;
        this.isRecognized = isRecognized;
        this.barycenter = calculateBarycenter(coordinates);
    }

    public List<List<Coordinate>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<Coordinate>> coordinates) {
        this.coordinates = coordinates;
    }

    public boolean isRecognized() {
        return isRecognized;
    }

    public void setRecognized(boolean recognized) {
        isRecognized = recognized;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Coordinate calculateBarycenter(List<List<Coordinate>> coordinatesAll){
        int sizeCoordinates = 0;
        double xAll = 0;
        double yAll = 0;
        for (int i = 0 ; i <coordinatesAll.size();i++){
            List<Coordinate> line = coordinatesAll.get(i);
            for (int j = 0; j <line.size();j++ ){
                Coordinate single = line.get(j);
                xAll += single.getCoX();
                yAll += single.getCoY();
                sizeCoordinates++;
            }
        }

        Coordinate barycenter = new Coordinate(xAll/sizeCoordinates,yAll/sizeCoordinates);
        return barycenter;
    }


    public Coordinate getBarycenter() {
        return barycenter;
    }

    public void setBarycenter(Coordinate barycenter) {
        this.barycenter = barycenter;
    }
}
