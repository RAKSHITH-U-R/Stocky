package service;
import database.RedisAdapter;
import io.github.cdimascio.dotenv.Dotenv;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class AlphaVantageHelper {
    String baseUri = "https://www.alphavantage.co/query?";
    private final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(20))
            .build();
    private String APIKey;
    private final RedisAdapter cache;
    private static final Dotenv dotenv = Dotenv.configure()
            .directory(".")
            .ignoreIfMalformed()
            .ignoreIfMissing()
            .load();

    public AlphaVantageHelper(){
        cache = new RedisAdapter();
        cache.connect();
        readTokenFromEnv();
    }

    private void readTokenFromEnv(){
        String apiKey = dotenv.get("ALPHA_VANTAGE_API_KEY");
        setAPIKey(apiKey);
    }

    public void setAPIKey(String APIKey){
        this.APIKey = APIKey;
    }

    public String sendRequest(String endPoint, String searchKey){

        if (cache.get(searchKey) != null) {
            System.out.println("In Cache");
            return cache.get(searchKey);
        }

        String reqURL = baseUri + endPoint;
        System.out.println("Making request to " + endPoint);

        HttpResponse<String> response;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(reqURL))
                .build();

        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch(Exception e){
            System.err.println(e.getMessage());
            return null;
        }

        String errorString = "Error Message";

        // check if error in stock symbol
        if (response.body().toLowerCase().contains(errorString.toLowerCase()))
            return null;

        if (response.statusCode() == 200) {
            String responseBody = response.body();
            cache.set(searchKey, responseBody);
            cache.expire(searchKey, 1000);
            return responseBody;
        }

        return null;
    }

    // latest 100 data points with interval as specified minutes
    public String latestTimeSeriesIntraday(String symbol, int interval){
        String endPoint = "function=TIME_SERIES_INTRADAY&symbol=" + symbol + "" +
                "&interval=" + interval + "min&" +
                "apikey=" + APIKey;
        return sendRequest(endPoint, symbol + "-" + interval);
    }

    // latest 100 data points with interval as day
    public String dailyTimeSeries(String symbol){
        String endPoint = "function=TIME_SERIES_DAILY&symbol=" + symbol + "&apikey=" + APIKey;
        return sendRequest(endPoint, symbol + "-d");
    }

    // last 20 years weekly data
    public String weeklyTimeSeries(String symbol){
        String endPoint = "function=TIME_SERIES_WEEKLY&symbol=" + symbol + "&apikey=" + APIKey;
        return sendRequest(endPoint,symbol + "-w");
    }

    // last 20 years monthly data
    public String monthlyTimeSeries(String symbol){
        String endPoint = "function=TIME_SERIES_MONTHLY&symbol=" + symbol + "&apikey=" + APIKey;
        return sendRequest(endPoint,symbol + "-m");
    }

}
