package walkwithme.mc.dal.com.walkwithme;

import android.location.Location;

import java.util.ArrayList;

public class Walk {
    //Elements used for the Home Screen
    private String eventName;
    private String eventDatetime;
    private String eventLocation;
    private String eventImageURL;

    public Walk() {
    }

    //Remaining elements for Create and View Activity
    private String eventId;
    private Double eventCoordinateLat;
    private Double eventCoordinateLong;
    private String eventDescription;
    private String eventWeather;
    private ArrayList<String> carouselImages;

    private Float distanceToUser; //distance in km

    public Walk(double userLatitude, double userLongitude, double latitude, double longitude, String id, String name, String date, String time, String location, String imgURL, ArrayList<String> carouselImages,String eventDescription) {

        this.eventName = name;
        this.eventDatetime = date + " | " + time;
        this.eventLocation = location;
        this.eventImageURL = imgURL;

        this.eventId = id;
        this.eventCoordinateLat = latitude;
        this.eventCoordinateLong = longitude;

        this.eventDescription = eventDescription;
        this.eventWeather = "";
        this.carouselImages = carouselImages;


        this.distanceToUser = calculateDistance(userLatitude, userLongitude, latitude, longitude);

    }

    private Float calculateDistance(double userLatitude, double userLongitude, double walkLatitude, double walkLongitude) {

        Location userLocation = new Location("user");
        Location walkLocation = new Location("walk");

        userLocation.setLatitude(userLatitude);
        userLocation.setLongitude(userLongitude);

        walkLocation.setLatitude(walkLatitude);
        walkLocation.setLongitude(walkLongitude);

        float distance = userLocation.distanceTo(walkLocation);

        //convert meters to kilometers
        distance = distance / 1000;

        return distance;

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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Double getEventCoordinateLat() {
        return eventCoordinateLat;
    }

    public void setEventCoordinateLat(Double eventCoordinateLat) {
        this.eventCoordinateLat = eventCoordinateLat;
    }

    public Double getEventCoordinateLong() {
        return eventCoordinateLong;
    }

    public void setEventCoordinateLong(Double eventCoordinateLong) {
        this.eventCoordinateLong = eventCoordinateLong;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public ArrayList<String> getCarouselImages() {
        return carouselImages;
    }

    public void setCarouselImages(ArrayList<String> carouselImages) {
        this.carouselImages = carouselImages;
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

    public Float getDistanceToUser() {
        return distanceToUser;
    }

    public void setDistanceToUser(Float distanceToUser) {
        this.distanceToUser = distanceToUser;
    }



}



