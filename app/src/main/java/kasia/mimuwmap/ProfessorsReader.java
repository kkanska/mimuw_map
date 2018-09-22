package kasia.mimuwmap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProfessorsReader {
    public static List<Professor> getProfessors(InputStream inputStream) throws IOException {
        List<Professor> professors = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                Professor professor = parseCsvLine(csvLine);
                professors.add(professor);
            }
        } finally {
            inputStream.close();
        }
        return professors;
    }

    public static void saveProfessors(List<Professor> professors, OutputStream outputStream)
            throws IOException {
        try {
            StringBuilder sb = new StringBuilder();
            for (Professor professor : professors) {
                sb.append(getCsvLine(professor));
            }
            outputStream.write(sb.toString().getBytes());
        } finally {
            outputStream.close();
        }
    }

    private static Professor parseCsvLine(String csvLine) {
        String[] data = csvLine.split(",");
        Integer room = Integer.parseInt(data[3]);
        return new Professor(data[1], data[2], data[0], room);
    }

    private static String getCsvLine(Professor professor) {
        return String.format(Locale.ENGLISH, "%s,%s,%s,%d\n",
                professor.getTitle(),
                professor.getFirstName(), professor.getLastName(),
                professor.getRoom());
    }
}
