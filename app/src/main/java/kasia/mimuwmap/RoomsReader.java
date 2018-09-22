package kasia.mimuwmap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

public class RoomsReader {
    public static Map<Integer, Room> getRooms(InputStream inputStream) throws IOException {
        Map<Integer, Room> rooms = new TreeMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                Room room = parseCsvLine(csvLine);
                rooms.put(room.getNumber(), room);
            }
        } finally {
            inputStream.close();
        }
        return rooms;
    }

    private static Room parseCsvLine(String csvLine) {
        String[] data = csvLine.split(",");
        Integer number = Integer.parseInt(data[5]);
        Integer floor = Integer.parseInt(data[0]);
        Float left = Float.parseFloat(data[1]);
        Float top = Float.parseFloat(data[2]);
        Float width = Float.parseFloat(data[3]);
        Float height = Float.parseFloat(data[4]);
        return new Room(number, floor, left, top, width, height);
    }
}
