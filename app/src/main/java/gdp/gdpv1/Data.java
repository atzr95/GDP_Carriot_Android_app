package gdp.gdpv1;

public class Data {
    String weather;
    String data;
    int id;

    public Data(String weather , String data) {
        this.weather = weather;
        this.data = data;
    }
    public String getName(){
        return weather;
    }
    public void setWeather(String weather) {
        this.weather= weather;
    }

    public String getData(){
        return data;
    }
    public void setData(String data) {
        this.data= data;
    }

}
