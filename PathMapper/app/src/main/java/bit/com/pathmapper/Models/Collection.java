package bit.com.pathmapper.Models;

import java.util.List;

/**
 * Created by jacksct1 on 20/10/2016.
 */

public class Collection
{
    private int id;
    private String collectionName;
    private List<PointOfInterest> points;

    public Collection() {
    }

    public Collection(int id, String collectionName, List<PointOfInterest> points) {
        this.id = id;
        this.collectionName = collectionName;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public List<PointOfInterest> getPoints() { return points; }

    public void setPoints(List<PointOfInterest> points) { this.points = points; }
}



