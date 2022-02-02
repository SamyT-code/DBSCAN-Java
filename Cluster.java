/* * Samy Touabi - 300184721
   * CSI2520 Projet intégrateur - Partie 1 (OOP avec Java) 
   * Date: Hiver 2022 
   * 
   * On vous demande de programmer l’algorithme DBSCAN afin de grouper les différents enregistrements
   * en utilisant les coordonnées GPS des points de départ. Votre programme doit être une application Java
   * appelée TaxiClusters prenant en paramètre le nom du fichier contenant la base de données à
   * analyser, suivi des paramètres minPts et eps. Le programme produira en sortie la liste des groupes
   * dans un fichier csv donnant, pour chaque groupe, sa position (valeur moyenne des coordonnées de ses
   * points) et son nombre de points. Les points isolés sont ignorés.
   * 
   * */

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

    public List<TripRecord> getPoints() {
        return this.points;
    }

    public void setPoints(List<TripRecord> points) {
        this.points = points;
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