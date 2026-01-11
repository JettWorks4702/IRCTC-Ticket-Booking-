package ticket.booking.services;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainService {


    private List<Train> trainList;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String TRAINS_PATH = "../localDB/trains.json";

       public TrainService() throws IOException {
           loadTrains();
    }

    public List<User> loadTrains() throws IOException
    {
        File trains = new File(TRAINS_PATH);
        return objectMapper.readValue(trains , new TypeReference<List<User>>() {});

    }
    public List<Train> searchTrains(String source, String destination){
      return trainList.stream().filter(train -> validTrain(train, source, destination)).collect(Collectors.toList());
    }


    public void addTrain(Train newTrain){
           //Check if train already exists

        Optional<Train> existingTrain = trainList.stream().filter(train -> train.getTrainId().equalsIgnoreCase(newTrain.getTrainId())).findFirst();
        if(existingTrain.isPresent())
        {
         updateTrain(newTrain);
        }
        else{
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }

    public void updateTrain(Train updatedTrain) {

        // Find the index of the train with the same trainId
        OptionalInt index = IntStream.range(0, trainList.size())
                .filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(updatedTrain.getTrainId()))
                .findFirst();

        if (index.isPresent()) {
            // If found, replace the existing train with the updated one
            trainList.set(index.getAsInt(), updatedTrain);
            saveTrainListToFile();
        } else {
            // If not found, treat it as adding a new train
            addTrain(updatedTrain);
        }
    }
    public boolean validTrain(Train train, String src, String dest)
    {
        List<String> stationOrder = train.getStations();
        int srcIndex = stationOrder.indexOf(src.toLowerCase());
        int destIndex = stationOrder.indexOf(dest.toLowerCase());

        return srcIndex != -1 && destIndex != -1 && srcIndex < destIndex ;
    }

    private void saveTrainListToFile() {
        try {
            objectMapper.writeValue(new File(TRAINS_PATH), trainList);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception based on your application's requirements
        }
    }
}