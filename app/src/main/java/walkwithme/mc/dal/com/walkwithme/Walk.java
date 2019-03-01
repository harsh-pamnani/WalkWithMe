package walkwithme.mc.dal.com.walkwithme;

public class Walk {
    //Elements used for the Home Screen
    private String eventName;
    private String eventDatetime;
    private String eventLocation;
    private String eventImageURL;
    //Remaining elements for Create and View Activity
    private int eventId;
    private String eventCoordinateLang;
    private String eventCoordinateLong;
    private String eventDescription;
    private String eventWeather;

    public Walk(String name, String datetime, String location, String imgURL) {

        this.eventName = name;
        this.eventDatetime = datetime;
        this.eventLocation = location;
        this.eventImageURL = imgURL;

    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDatetime() {
        return eventDatetime;
    }

    public void setEventDatetime(String eventDatetime) {
        this.eventDatetime = eventDatetime;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventImageURL() {
        return eventImageURL;
    }

    public void setEventImageURL(String eventImageURL) {
        this.eventImageURL = eventImageURL;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventCoordinateLang() {
        return eventCoordinateLang;
    }

    public void setEventCoordinateLang(String eventCoordinateLang) {
        this.eventCoordinateLang = eventCoordinateLang;
    }

    public String getEventCoordinateLong() {
        return eventCoordinateLong;
    }

    public void setEventCoordinateLong(String eventCoordinateLong) {
        this.eventCoordinateLong = eventCoordinateLong;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventWeather() {
        return eventWeather;
    }

    public void setEventWeather(String eventWeather) {
        this.eventWeather = eventWeather;
    }
}



