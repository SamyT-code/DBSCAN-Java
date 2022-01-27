import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {

    public static void main(String[] args){

        String path = "yellow_tripdata_2009-01-15_1hour_clean.csv";
        String line = "";

        int counter = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));

            while((line = br.readLine()) != null){
                String[] values = line.split(",");
                System.out.println("Longitude: " + values[8] + ", Latitude: " + values[9]);
                counter++;
                if(counter == 10)
                    break;
            }


        } 
        
        catch (FileNotFoundException e) {
            // Auto-generated catch block
            e.printStackTrace();
        }

        catch (IOException e){
            e.printStackTrace();
        }


    }
    
}
