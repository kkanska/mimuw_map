package kasia.mimuwmap;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

public class ProfessorFinder {

    public static Professor scrapeProfessor(String query) throws IOException {
        String html = getUrl("https://www.mimuw.edu.pl/pracownicy_i_doktoranci/wyszukiwanie?title="
                + query.replace(" ", "+"));
        Document doc = Jsoup.parse(html);
        if (doc.select("div.view-empty").size() > 0)
            return null;
        Elements selected = doc.select("div.views-row");
        try {
            for (Element row : selected) {
                String fullName = row.select("div.views-field-title a").first().text();
                int room = Integer.parseInt(
                        row.select("div.views-field-field-pokoj a").first().text()
                );
                String[] split = fullName.split(" ");
                int size = split.length;
                String firstName = split[size - 2].trim();
                String lastName = split[size - 1].trim();
                String title = StringUtil.join(
                        Arrays.asList(split).subList(0, size - 2),
                        " ")
                        .trim();
                Professor professor = new Professor(firstName, lastName, title, room);
                if (professor.toString().toLowerCase().equals(query.toLowerCase().trim()))
                    return professor;
            }
        } catch (NullPointerException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return null;
    }

    private static String getUrl(String url) throws IOException {
        URL urlObj;
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        urlObj = new URL(url);
        URLConnection urlCon = urlObj.openConnection();

        StringBuilder outputText = new StringBuilder();
        BufferedReader in = new BufferedReader(new
                InputStreamReader(urlCon.getInputStream()));

        String line;
        while ((line = in.readLine()) != null) {
            outputText.append(line);
        }
        in.close();

        return outputText.toString();
    }
}
