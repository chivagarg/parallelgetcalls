import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Scratch {

    ConcurrentHashMap<String, String> responses = new ConcurrentHashMap<>();

    private static final String GET_URL = "https://postman-echo.com/get";
    class HttpRequestDispatcher implements Runnable {

        HttpRequestDispatcher(String query) {
            this.query = query;
        }

        String query;

        @Override
        public void run() {
            //URLConnection connection = null;
            try {
                URL url = new URL(GET_URL + "?" + query);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Accept", "application/json");

                InputStream response = con.getInputStream();
                StringWriter writer = new StringWriter();
                IOUtils.copy(response, writer, "UTF-8");
                String responseString = writer.toString();
                JSONObject jsonObject = new JSONObject(responseString);
                responses.put(query, jsonObject.getString("url"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    List<String> getResponses(List<String> queries) throws InterruptedException {
        List<Thread> runners = new ArrayList<>();

        for (String query : queries) {
            Thread t =  new Thread(new HttpRequestDispatcher(query));
            t.start();
            runners.add(t);
        }

        for (Thread t : runners) {
            t.join();
        }

        return queries
                .stream()
                .map(q -> responses.get(q))
                .collect(Collectors.toList());

    }
}
