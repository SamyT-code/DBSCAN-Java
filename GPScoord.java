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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;

public class GPScoord{

    private double start_longitude;
    private double start_latitude;
    
    public GPScoord(double start_longitude, double start_latitude){
        this.start_longitude = start_longitude;
        this.start_latitude = start_latitude;
    }

    public double getLongitude(){
        return this.start_longitude;
    }

    public void setLongitude(double longitude){
        this.start_longitude = longitude;
    }

    public double getLatitude(){
        return this.start_latitude;
    }

    public void setLatitude(double latitude){
        this.start_latitude = latitude;
    }

}