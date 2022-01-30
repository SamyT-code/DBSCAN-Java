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

public class Cluster{

    // In cluster class we need to store a list of TripRecords?
    // private int id;
    // private TripRecord record;
    // private int nPoints;
    public ArrayList<TripRecord> cluster;

    public Cluster(ArrayList<TripRecord> cluster){
        this.cluster = cluster;
    }

    public ArrayList<TripRecord> getCluster(){
        return this.cluster;
    }

    public double getLongitudeAverage(){
        double sum = 0;
        for(TripRecord t : this.getCluster()){
            sum += t.getPickup_Location().getLongitude();
        }
        return sum / this.cluster.size();
    }

    public double getLatitudeAverage(){
        double sum = 0;
        for(TripRecord t : this.getCluster()){
            sum += t.getPickup_Location().getLatitude();
        }
        return sum / this.cluster.size();
    }

    public void printClustInfo(){
        for(int i = 0; i < cluster.size(); i++){
            // System.out.println("Cluster ID: " + (i + 1) + ", Longitude: " + cluster.get(i).getLongitudeAverage());
        }
    }

    // Average long & lat
    // iterate through triprecord list in every cluster and find averae long and lat


}