import service.AlphaVantageHelper;
import symbol.plot.PlotTitles;
import symbol.SymbolData;
import symbol.plot.SymbolPlotter;

public class PlotTest {
    public static void main(String[] args) {
        AlphaVantageHelper helper = new AlphaVantageHelper();
        String res = helper.latestTimeSeriesIntraday("IBM", 5);

        SymbolData data = new SymbolData(res);
        PlotTitles titles = new PlotTitles("Main Title", "X - Axis Title", "Y - Axis Title");
        String imgPath = SymbolPlotter.buildLineChart(data, titles, 1600, 800);
        System.out.println(imgPath);
    }
}
