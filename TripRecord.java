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

public class TripRecord{

    private String pickup_DateTime;
    private GPScoord pickup_Location;
    private GPScoord dropoff_Location;
    private float trip_Distance;


    public TripRecord(String pickup_DateTime, GPScoord pickuo_Location, GPScoord dropoff_Location, float trip_Distance){

        this.pickup_DateTime = pickup_DateTime;
        this.pickup_Location = pickup_Location;
        this.dropoff_Location = dropoff_Location;
        this.trip_Distance = trip_Distance;

    }

}