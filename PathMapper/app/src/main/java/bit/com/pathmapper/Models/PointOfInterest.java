package bit.com.pathmapper.Models;

/**
 * Created by jacksct1 on 20/10/2016.
 */

public class PointOfInterest {

    private int id;
    private String name;
    private String scientificName;
    private double lat;
    private double lng;
    private String description;
    private int collection;

    public PointOfInterest() {

    }

    public PointOfInterest(int id, String name, String scientificName, double lat, double lng, String description, int collection) {
        this.id = id;
        this.name = name;
        this.scientificName = scientificName;
        this.lat = lat;
        this.lng = lng;
        this.description = description;
        this.collection = collection;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }
}
