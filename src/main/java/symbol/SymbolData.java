package symbol;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.stream.Collectors;

// Immutable class
public class SymbolData {
    private final ArrayList<String> timeSeriesString;
    private final ArrayList<Double> open;
    private final ArrayList<Double> close;
    private final ArrayList<Double> high;
    private final ArrayList<Double> low;
    private final ArrayList<Double> volume;
    private final String timePeriod;
    private final String responseString;

    public String getTimePeriod() {
        return timePeriod;
    }

    public int totalDataPointCount(){
        return open.size();
    }

    public ArrayList<Double> getClose() {
        return new ArrayList<>(close);
    }

    public ArrayList<Double> getHigh() {
        return new ArrayList<>(high);
    }

    public ArrayList<Double> getOpen(){
        return new ArrayList<>(open);
    }

    public ArrayList<Double> getLow(){
        return new ArrayList<>(low);
    }

    public ArrayList<Double> getVolume(){
        return new ArrayList<>(volume);
    }

    public ArrayList<String> getTimeSeriesString() {
        return new ArrayList<>(timeSeriesString);
    }

    public String getResponseString() { return responseString; }

    public SymbolData(String responseString){
        this.responseString = responseString;
        open = new ArrayList<>();
        close = new ArrayList<>();
        high = new ArrayList<>();
        low = new ArrayList<>();
        volume = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(responseString);
        this.timePeriod = jsonObject.names().getString(0);
        JSONObject data = (JSONObject) jsonObject.get(timePeriod);
        timeSeriesString = (ArrayList<String>) data.keySet().stream().sorted().
                collect(Collectors.toList());
        populateFields(data);
    }

    private void populateFields(JSONObject data){
        try {
            for (String key : timeSeriesString) {
                JSONObject profile = (JSONObject) data.get(key);
                double openValue = Double.parseDouble((String) profile.get("1. open"));
                double highValue = Double.parseDouble((String) profile.get("2. high"));
                double lowValue = Double.parseDouble((String) profile.get("3. low"));
                double closeValue = Double.parseDouble((String) profile.get("4. close"));
                double volumeValue = Double.parseDouble((String) profile.get("5. volume"));
                open.add(openValue);
                high.add(highValue);
                low.add(lowValue);
                close.add(closeValue);
                volume.add(volumeValue);
            }

        }  catch (Exception err){
            err.printStackTrace();
        }
    }
}
