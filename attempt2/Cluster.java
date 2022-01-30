import java.util.ArrayList;
import java.util.List;

public class Cluster{

    public List<TripRecord> points;
    public GPScoord averagePosition;
    public int numPoints;
    public int clusterID;

    public Cluster(List<TripRecord> points){
        this.points = new ArrayList<>(points);
    }

    public Cluster(){
        this.points = new ArrayList<>();
    }

    public List<TripRecord> getCluster() {
        return this.points;
    }

    public void setCluster(List<TripRecord> cluster) {
        this.points = cluster;
    }

    public GPScoord getAveragePosition(){
        return new GPScoord(this.getLongitudeAverage(), this.getLatitudeAverage());
    }

    public double getLongitudeAverage(){
        double lon = 0;
        for(TripRecord trip : this.points){
            lon += trip.getPickup_Location().getLongitude();
        }
        return lon / this.points.size();
    }

    public double getLatitudeAverage(){
        double lat = 0;
        for(TripRecord trip : this.points){
            lat += trip.getPickup_Location().getLatitude();
        }
        return lat / this.points.size();
    }

    public int getClusterID() {
        return this.clusterID;
    }

    public void setClusterID(int clusterID) {
        this.clusterID = clusterID;
    }

    public void addTrip(TripRecord t){
        this.points.add(t);
    }

}