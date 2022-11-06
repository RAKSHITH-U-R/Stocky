package symbol.plot;

public class PlotTitles {
    private String mainTitle;
    private String xTitle;
    private String yTitle;

    public PlotTitles(String mainTitle, String xTitle, String yTitle){
        this.mainTitle = mainTitle;
        this.xTitle = xTitle;
        this.yTitle = yTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public void setXTitle(String xTitle) {
        this.xTitle = xTitle;
    }

    public void setYTitle(String yTitle) {
        this.yTitle = yTitle;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public String getXTitle() {
        return xTitle;
    }

    public String getYTitle(){
        return yTitle;
    }
}
