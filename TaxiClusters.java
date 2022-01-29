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

public class TaxiClusters{

   public static List<List<String>> records = new ArrayList<>();
   public static ArrayList<GPScoord> coords = new ArrayList<GPScoord>();

   public void extract() throws FileNotFoundException, IOException{

      
      try (BufferedReader br = new BufferedReader(new FileReader("yellow_tripdata_2009-01-15_1hour_clean.csv"))) {

         br.readLine();
         String line;

         int count = 0;
         while ((line = br.readLine()) != null && count != -5) {
              String[] values = line.split(",");
              records.add(Arrays.asList(values));
              count++;
         }

      }

   }

   public void coordinates(){

      for(int i = 0; i < records.size(); i++){
         double lon = Double.parseDouble(records.get(i).get(8));
         double lat = Double.parseDouble(records.get(i).get(9));
         coords.add(new GPScoord(lon, lat));
      }
   }

   
   public double distance4(double x1, double y1, double x2, double y2) {       
      return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
   }

   public double distance2(GPScoord x, GPScoord y){
      return distance4(x.getLatitude(), x.getLongitude(), y.getLatitude(), y.getLongitude());
   }

   public void printCoords(){
      int n1 = coords.size();
      int n2 = 5;
      for(int i = 0; i < n2; i++ ){
         System.out.println("coords[" + i + "] longitude: " + coords.get(i).getLongitude());
         System.out.println("coords[" + i + "] latitude: " + coords.get(i).getLatitude());
         System.out.println();
      }
   }

   public static void main(String[] args) throws FileNotFoundException, IOException {

      TaxiClusters tc = new TaxiClusters();

      tc.extract();
      tc.coordinates();

      tc.printCoords();

      System.out.println("The distance between coords[0] and cords[1] is: " +  tc.distance2(coords.get(0), coords.get(1)));

   }

}