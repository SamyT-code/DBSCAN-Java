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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;
import static java.util.Comparator.comparing;


public class TaxiClusters{

    // records contiendra une liste 2D de toutes les infos de yellow_tripdata de manière non-traitée
    public static List<List<String>> records = new ArrayList<>(); 

    // trips est une liste de type TripRecord que se créer quand chaque trip a été traité
    public static ArrayList<TripRecord> trips = new ArrayList<TripRecord>();

    // clusters est une liste de type cluster qui contient une liste de TripRecord. Chaque Cluster dans clusters
    // est un regroupement de plusiers TripRecords qui sont suffisament proche les uns des autres.
    public static ArrayList<Cluster> clusters = new ArrayList<Cluster>();

    // epsilon est la distance enclidienne minimale entre 2 points pour qu'ils sont considérés des voisins
    public static double epsilon = 0.0003;

    // minPts est le nombre minimal de voisins qu'un point doit avoir pour former un Cluster
    public static int minPts = 5;

    // path est simplement le nom du document CSV duquel on extrait l'information
    public static String path = "yellow_tripdata_2009-01-15_1hour_clean.csv";


    // Cette méthode extrait l'information de yellow_tripdata de manière non-traité dans la liste 2D records.
    // Le code pour cette méthode a été pris de: https://www.baeldung.com/java-csv-file-array. 
    public void extractCSV() throws FileNotFoundException, IOException{
 
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
 
            br.readLine(); // On saute la première ligne car elle ne contient pas des données TripRecord
            String line;
          
            while ( (line = br.readLine()) != null ) {
               String[] values = line.split(",");
               records.add(Arrays.asList(values));
            }
        }
    }

    // Cette méthode convertit chaque record en TripRecord
    public void convert(){

        // ,Unnamed: 0,Unnamed: 0.1,vendor_name,Trip_Pickup_DateTime,Trip_Dropoff_DateTime,Passenger_Count,Trip_Distance,Start_Lon,Start_Lat,
        //        1           2          3               4                      5                 6            7              8       9         
       for(int i = 0; i < records.size(); i++){
 
          String start_trip = records.get(i).get(4);
 
          double start_lon = Double.parseDouble(records.get(i).get(8));
          double start_lat = Double.parseDouble(records.get(i).get(9));
          GPScoord start = new GPScoord(start_lon, start_lat); // Créer un objet GPScoord pour start
 
          double end_lon = Double.parseDouble(records.get(i).get(12));
          double end_lat = Double.parseDouble(records.get(i).get(13));
          GPScoord end = new GPScoord(end_lon, end_lat); // Créer un objet GPScoord pour end
 
          float dist = Float.parseFloat(records.get(i).get(7));
 
          String label = "undefined";
 
          TripRecord rec = new TripRecord(start_trip, start, end, dist, label); // Créer l.objet TripRecord
 
          trips.add(rec); // Ajouter ce record à la liste de TripRecords
       }
    }

    // Cette méthode imprime tous les éléments dans trips.
    public void printCoords(){
       for(int i = 0; i < trips.size(); i++ ){
          System.out.println("coords[" + i + "] longitude: " + trips.get(i).getPickup_Location().getLongitude());
          System.out.println("coords[" + i + "] latitude: " + trips.get(i).getPickup_Location().getLatitude());
          System.out.println();
       }
    }

    // Cette méthode trouve la distance euclidienne entre 2 points en 2D.
    public double distance4(double x1, double y1, double x2, double y2) {       
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
     }

    // Cette méthode ce sert de la méthode distance4(...) pour trouver la distance entre 2 GPScoord.
    public double distance2(GPScoord x, GPScoord y){
        return distance4(x.getLatitude(), x.getLongitude(), y.getLatitude(), y.getLongitude());
    }

    // Cette méthode crée une sorte de base de donnée (en réalité c'est une liste de Clusters) qui regroupe
    // ensemble des GPScoord. Elle se sert de d'autres méthodes plus bas pour l'aider.
    // Pseudocode pour DBSCAN de l'internet:
    // https://www.researchgate.net/figure/Pseudocode-of-the-DBSCAN-algorithm_fig2_325059373
    public void dbScan(){

        int count = 0; // Ce compteur servira, en partie, à identifier des Clusters

        for(TripRecord p : trips){ // Itérer à travers trips

            if(p.getLabel() != "undefined"){ // Si le point p n'est pas "undefined", alors il a déjà été visité.
                continue; // on peut donc passer au prochain point 
            }

            p.setLabel("visited"); // Signaler que ce point est visité (puisqu'il ne l'a pas encore été)

            // Créer une liste qui contiendra tous les voisins de p selon les contraites
            ArrayList<TripRecord> neighborPTS = rangeQuery(trips, p.getPickup_Location(), epsilon); 

            if(neighborPTS.size() < minPts){ // Si p n'a pas suffisament de voisins, alors c'est un point "noise"
                p.setLabel("noise");
                continue;
            } else{ // Si p a suffisament de voisin, alors lui et ses voisins forment un nouveau Cluster
                count++;
                clusters.add(new Cluster());
                expandCluster(p, neighborPTS, count, epsilon, minPts, trips);
            }

        }

    }

    public void expandCluster(TripRecord p, ArrayList<TripRecord> neighborPTS, int count, double epsilon, int minPts, ArrayList<TripRecord> trips){

        Cluster clus = clusters.get(count - 1); // Chercher le derner Cluster de la liste
        clus.addTrip(p); // Ajouter à ce Cluster le TripRecord p

        for(int i = 0; i < neighborPTS.size(); i++){ // Itérer à travers les voisins de p
            TripRecord prime = neighborPTS.get(i);
            if(prime.getLabel() == "undefined"){ // Si le voisin de p n'est pas visité,

                prime.setLabel("visited"); // Indiquer qu'il est visité

                // primeNeighbors est une liste du voisin de p sélectionné
                ArrayList<TripRecord> primeNeighbors = rangeQuery(trips, prime.getPickup_Location(), epsilon);
                
                if(primeNeighbors.size() >= minPts) // Si le voisin de p a suffisament de voisins,
                    neighborPTS.addAll(primeNeighbors); // ajouter ces voisins du voisin de p comme voisins de p
            }

            // Si prime ne fait pas encore parti d'un Cluster, l'ajouter à clus
            boolean check = false;
            outerloop:
            for(int j = 0; j < clusters.size(); j++){ // Vérifier s'il fait partie d'un Cluster
                if(clusters.get(j).points.contains(prime)){
                    check = true;
                    break outerloop; // Arrêter de vérifier s'il fait partie d'un Cluster, on sait déjà que oui
                }
                     
            }

            if(!check) //Si non, l'ajouter à clus
                clus.addTrip(prime);
        }

        // Pour une raison que j'ignore, avec epsilon = 0.0003 et minPTS = 5,
        // quelques clusters ont 4 points. Pour regler ce problème, il fait executer expandCluster une autre fois.
        if(clus.points.size() < 5) 
            expandCluster(p, neighborPTS, count, epsilon, minPts, trips);
    }

    // Cette méthode détermine qui sont les points voisins d'un point
    public ArrayList<TripRecord> rangeQuery(ArrayList<TripRecord> t, GPScoord q, double e) {

        ArrayList<TripRecord> a = new ArrayList<TripRecord>(); // Créer une liste de TripRecord
        
        for (int i = 0; i < t.size(); i++) {
            GPScoord p = t.get(i).getPickup_Location();
            // if( (p != q) && (distance2(q, p) <= e) ) // Cette ligne de code est utile pour voir si on a le même output que la section anglaise
            if( distance2(q, p) <= e)
               a.add(t.get(i));
        }
        return a; // Retourner une liste de tous les voisins de q
    }

    // Cette méthode imprime tous les clusters ainsi que la moyenne des latitudes-longitudes.
    public void printCluster(){
        for(int i = 0; i < clusters.size(); i++){
            System.out.println("Cluster ID: " + (i + 1) + ", Longitude: " + clusters.get(i).getLongitudeAverage() 
                + ", Latitude: " + clusters.get(i).getLatitudeAverage() + ", Number of points: " + clusters.get(i).points.size() );
        }
    } 
    
    public static void main(String[] args) throws Exception {

        System.out.println("\nCe programme ne devrait pas prendre plus que 10 secondes a executer. Veuillez patienter...");

        TaxiClusters tc = new TaxiClusters(); // Créer un objet TC afin d'appeler les méthodes non-statiques

        tc.extractCSV(); // Extraire les données du fichier CSV

        tc.convert(); // Convertir ces données en liste de TripRecords

        tc.dbScan(); // Créer des Clusters à partir de la liste des TripRecords

        System.out.println("Clusters for minPts = " + minPts + " and epsilon = " + String.valueOf(epsilon) + " | Total clusters: " + clusters.size());
        System.out.println();
    
        // tc.printCluster();

        tc.makeCSV(clusters);       
        // tc.make10CSV(clusters); // Supposed to find [2, 6, 9, 14, 16, 20, 56, 62, 92, 94]
        

    }

    // Cette méthode créer un CSV de tous les clusters avec leur longitude/latitude moyenne en ordre décroissant.
    // J'ai utilisé LambdaJ pour trier clusters
    // https://stackoverflow.com/questions/2784514/sort-arraylist-of-custom-objects-by-property
    public void makeCSV(ArrayList<Cluster> clusters) throws Exception{
        int n = clusters.size();
        OutputStreamWriter outputFile = new OutputStreamWriter(new FileOutputStream("clustersBIG.csv"));
        outputFile.write("ID, Average Latitude, Average Longitude, Number of points\n");

        List<Cluster> biggest = new ArrayList<Cluster>(clusters);
        Collections.sort(biggest, new Comparator<Cluster>() {
                public int compare(Cluster c1, Cluster c2) {
                return Integer.valueOf(c2.points.size()).compareTo(c1.points.size());
                }
        });
        
        
        for(int i = 0; i < n - 1; i++){
            outputFile.write("Cluster ID: " + (i + 1) + ", Latitude: " + biggest.get(i).getLatitudeAverage() 
                + ", Longitude: " + biggest.get(i).getLongitudeAverage() + ", Number of points: " + biggest.get(i).points.size() + "\n" );
        }

        outputFile.write("Cluster ID: " + (n) + ", Longitude: " + biggest.get(n - 1).getLongitudeAverage() 
                + ", Latitude: " + biggest.get(n - 1).getLatitudeAverage() + ", Number of points: " + biggest.get(n - 1).points.size() );

        outputFile.flush();
        outputFile.close();        
    } 


    // Cette méthode créer 10 CSV de tous les points des 10 plus gros Clusters.
    // J'ai utilisé LambdaJ pour trier clusters
    // https://stackoverflow.com/questions/2784514/sort-arraylist-of-custom-objects-by-property
    public void make10CSV(ArrayList<Cluster> clusters) throws Exception{
  
        List<Cluster> biggest = new ArrayList<Cluster>(clusters);
        Collections.sort(biggest, new Comparator<Cluster>() {
                public int compare(Cluster c1, Cluster c2) {
                return Integer.valueOf(c2.points.size()).compareTo(c1.points.size());
                }
        });

        for (int i = 0; i < 10; i++) {
            OutputStreamWriter outputFile = new OutputStreamWriter(new FileOutputStream("cluster_"+(i+1)+".csv"));
            outputFile.write("Order, Latitude, Longitude\n");
            for(TripRecord trip : biggest.get(i).getPoints()){
                outputFile.write((i+1) + ", "+trip.getPickup_Location().getLatitude() + ", " + trip.getPickup_Location().getLongitude()+"\n");
            }            

            outputFile.flush();
            outputFile.close();
        }

    }

}

