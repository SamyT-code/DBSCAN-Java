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


public class TaxiClusters{

    public static List<List<String>> records = new ArrayList<>();
    public static ArrayList<TripRecord> trips = new ArrayList<TripRecord>();

    public static ArrayList<Cluster> clusters = new ArrayList<Cluster>();
    // public static ArrayList<ArrayList<TripRecord>> clusters = new ArrayList<ArrayList<TripRecord>>();

    public static double epsilon = 0.0003;
    public static int minPts = 5;
    public static String path = "yellow_tripdata_2009-01-15_1hour_clean.csv";
 
    public void extractCSV() throws FileNotFoundException, IOException{
 
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
 
    public void convert(){
 
       for(int i = 0; i < records.size(); i++){
 
          String start_trip = records.get(i).get(4);
 
          double start_lon = Double.parseDouble(records.get(i).get(8));
          double start_lat = Double.parseDouble(records.get(i).get(9));
          GPScoord start = new GPScoord(start_lon, start_lat);
 
          double end_lon = Double.parseDouble(records.get(i).get(12));
          double end_lat = Double.parseDouble(records.get(i).get(13));
          GPScoord end = new GPScoord(end_lon, end_lat);
 
          float dist = Float.parseFloat(records.get(i).get(7));
 
          String label = "undefined";
 
          TripRecord rec = new TripRecord(start_trip, start, end, dist, label);
 
          trips.add(rec);
          // coords.add(new GPScoord(start_lon, start_lat));
          // System.out.println("longitude of point " + i + ": " + coords.get(i).getPickup_Location().getLongitude() );
          // System.out.println("longitude of point " + i + ": " + coords.get(i).getPickup_Location().getLongitude() );
       }
    }
 
    public void printCoords(){
       int n1 = trips.size();
       int n2 = 5;
       for(int i = 0; i < n2; i++ ){
          // System.out.println("coords[" + i + "] longitude: " + coords.get(i).getLongitude());
          // System.out.println("coords[" + i + "] latitude: " + coords.get(i).getLatitude());
          System.out.println("coords[" + i + "] longitude: " + trips.get(i).getPickup_Location().getLongitude());
          System.out.println("coords[" + i + "] latitude: " + trips.get(i).getPickup_Location().getLatitude());
          
          System.out.println();
       }
    }

    public double distance4(double x1, double y1, double x2, double y2) {       
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
     }
  
    public double distance2(GPScoord x, GPScoord y){
        return distance4(x.getLatitude(), x.getLongitude(), y.getLatitude(), y.getLongitude());
    }

    // Pseudocode for DBSCAN from the internet:
    // https://www.researchgate.net/figure/Pseudocode-of-the-DBSCAN-algorithm_fig2_325059373
    public void dbScan(){

        int count = 0;

        for(TripRecord p : trips){ // NOT THE SAME
            if(p.getLabel() != "undefined"){
                continue;
            }

            p.setLabel("visited");

            ArrayList<TripRecord> neighborPTS = rangeQuery(trips, p.getPickup_Location(), epsilon);

            if(neighborPTS.size() < minPts){
                p.setLabel("noise");
                continue;
            } else{
                count++;
                clusters.add(new Cluster());
                expandCluster(trips, p, neighborPTS, count, epsilon, minPts);
            }

        }

    }

    public void expandCluster(ArrayList<TripRecord> trips, TripRecord p, ArrayList<TripRecord> neighborPTS, int count, double epsilon, int minPts){

        Cluster clus = clusters.get(count - 1);
        clus.addTrip(p);

        // for(TripRecord prime : neighborPTS){
        for(int i = 0; i < neighborPTS.size(); i++){
            TripRecord prime = neighborPTS.get(i);
            if(prime.getLabel() == "undefined"){
                prime.setLabel("visited");

                ArrayList<TripRecord> primeNeighbors = rangeQuery(trips, prime.getPickup_Location(), epsilon);
                if(primeNeighbors.size() >= minPts){
                    neighborPTS.addAll(primeNeighbors);
                }

            }

            boolean flag = false;

            for(int j = 0; j < clusters.size(); j++){
                if(clusters.get(j).points.contains(prime)){ // NOT SURE IF THIS IS RIGHT
                    flag = true;
                } 
                
            }

            if(!flag){
                clus = clusters.get(count - 1);
                clus.addTrip(prime);
            }

        }

    }

    public ArrayList<TripRecord> rangeQuery(ArrayList<TripRecord> t, GPScoord q, double e) {

        ArrayList<TripRecord> a = new ArrayList<TripRecord>();
        
        for (int i = 0; i < t.size(); i++) {
            // System.out.println("i = " + i);
            GPScoord p = t.get(i).getPickup_Location();
            // if( (p != q) && (distance2(q, p) <= e) ){
            if( distance2(q, p) <= e) {
               a.add(t.get(i));
            }
        }
        return a;
    }

    public void printCluster(){
        for(int i = 0; i < clusters.size(); i++){
            System.out.println("Cluster ID: " + (i + 1) + ", Longitude: " + clusters.get(i).getLongitudeAverage() 
                + ", Latitude: " + clusters.get(i).getLatitudeAverage() + ", Number of points: " + clusters.get(i).points.size() );
        }
    } 
    
    public static void main(String[] args) throws Exception {
        TaxiClusters tc = new TaxiClusters();

        tc.extractCSV();
        tc.convert();

        // tc.printCoords();
        // System.out.println("The distance between coords[0] and cords[1] is: " +  
        //     tc.distance2(trips.get(0).getPickup_Location(), trips.get(1).getPickup_Location()) );
        
        tc.dbScan();

        System.out.println("Clusters for minPts = " + minPts + " and epsilon = " + String.valueOf(epsilon) + " | Total clusters: " + clusters.size());
        System.out.println();
    
        tc.printCluster();

        int count = 0;
        for(int i = 0; i < clusters.size(); i++){
            count = count + clusters.get(i).points.size(); 
        }

        System.out.println("Total number of: " + count);

        tc.findBiggest(clusters);
        // Supposed to find [2, 6, 9, 14, 16, 20, 56, 62, 92, 94]

    }

    public void findBiggest(ArrayList<Cluster> data) throws Exception {
        
        ArrayList<Integer> numPoints = new ArrayList<Integer>();
        ArrayList<Integer> numPoints2 = new ArrayList<Integer>();

        for(Cluster cluster: data) {
            numPoints.add(cluster.points.size());
            numPoints2.add(cluster.points.size());
        }

        ArrayList<Integer> biggest = new ArrayList<Integer>();
        for(int i = 0; i < 10; i++) {
            Integer toAdd = Collections.max(numPoints);
            int index = numPoints2.indexOf(toAdd);
            biggest.add(index);
            numPoints.remove(toAdd);
        }
        Collections.sort(biggest);
        System.out.println(biggest);
        writeSeparateCSV(biggest, data);
    }

    private void writeSeparateCSV(ArrayList<Integer> biggest, ArrayList<Cluster> data) throws Exception{
        for (int i = 0; i < biggest.size(); i++) {
            OutputStreamWriter outputFile = new OutputStreamWriter(new FileOutputStream("cluster_"+biggest.get(i)+".csv"));
            outputFile.write("unnamed, Latitude, Longitude\n");
            for(TripRecord trip : data.get(biggest.get(i)).getPoints()){
                outputFile.write(data.get(biggest.get(i)).getClusterID()+", "+trip.getPickup_Location().getLatitude() + ", " + trip.getPickup_Location().getLongitude()+"\n");
            }

            outputFile.flush();
            outputFile.close();
        }
    }

}

