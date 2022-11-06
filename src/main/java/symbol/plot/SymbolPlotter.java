package symbol.plot;

import symbol.SymbolData;
import org.knowm.xchart.*;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.knowm.xchart.style.theme.MatlabTheme;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class SymbolPlotter {

    public static String buildLineChart(SymbolData data, PlotTitles titles, int imgWidth, int imgHeight){
        ArrayList<Double> x = getXLinspace(data.totalDataPointCount());

        XYChart chart = new XYChart(imgWidth, imgHeight);
        chart.getStyler().setTheme(new MatlabTheme());
        XYSeries series = chart.addSeries("Stock Movement", x, data.getOpen());
        series.setMarker(SeriesMarkers.NONE);
        setChartTitles(chart, titles);

        return saveImageInFileSystem(chart);
    }

    public static String buildOHLCChart(SymbolData data, PlotTitles titles, int imgWidth, int imgHeight){
        ArrayList<Double> x = getXLinspace(data.totalDataPointCount());

        OHLCChart chart = new OHLCChart(imgWidth, imgHeight);
        chart.getStyler().setTheme(new MatlabTheme());
        OHLCSeries series = chart.addSeries("Price", x, data.getOpen(), data.getHigh(),
                data.getLow(), data.getClose(), data.getVolume());
        series.setMarker(SeriesMarkers.NONE);

        setChartTitles(chart, titles);

        return saveImageInFileSystem(chart);
    }

    private static void setChartTitles(Chart chart, PlotTitles titles){
        chart.setTitle(titles.getMainTitle());
        chart.setXAxisTitle(titles.getXTitle());
        chart.setYAxisTitle(titles.getYTitle());
    }

    private static ArrayList<Double> getXLinspace(int size){
        ArrayList<Double> time = new ArrayList<>();
        for(double i = 1; i <= size; i++)
            time.add(i);

        return time;
    }

    private static String saveImageInFileSystem(Chart chart){
        String outPath = "src/main/resources/plot_images/" + System.currentTimeMillis() + ".png";
        File outfile = new File(outPath);
        try{
            BufferedImage img = BitmapEncoder.getBufferedImage(chart);
            ImageIO.write(img, "png", outfile);
        } catch (Exception e){
            e.printStackTrace();
        }

        return outPath;
    }
}
