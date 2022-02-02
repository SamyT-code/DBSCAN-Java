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

// Un TripRecord enregistre plusiers attributs importants de chaque voyage (surtout pickup_Location)
public class TripRecord{

    public String pickup_DateTime;
    public GPScoord pickup_Location;
    public GPScoord dropoff_Location;
    public float trip_Distance;
    public String label; // label sert a identifier si un TripRecord a été visité ou non. Ensuite, il monre si un TripRecord est de type "noise" ou non

    public TripRecord(String pickup_DateTime, GPScoord pickup_Location, GPScoord dropoff_Location, float trip_Distance, String label){
        this.pickup_DateTime = pickup_DateTime;
        this.pickup_Location = pickup_Location;
        this.dropoff_Location = dropoff_Location;
        this.trip_Distance = trip_Distance;
        this.label = label;
    }

    public String getPickup_DateTime() {
        return this.pickup_DateTime;
    }

    public void setPickup_DateTime(String pickup_DateTime) {
        this.pickup_DateTime = pickup_DateTime;
    }

    public GPScoord getPickup_Location() {
        return this.pickup_Location;
    }

    public void setPickup_Location(GPScoord pickup_Location) {
        this.pickup_Location = pickup_Location;
    }

    public GPScoord getDropoff_Location() {
        return this.dropoff_Location;
    }

    public void setDropoff_Location(GPScoord dropoff_Location) {
        this.dropoff_Location = dropoff_Location;
    }

    public float getTrip_Distance() {
        return this.trip_Distance;
    }

    public void setTrip_Distance(float trip_Distance) {
        this.trip_Distance = trip_Distance;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}