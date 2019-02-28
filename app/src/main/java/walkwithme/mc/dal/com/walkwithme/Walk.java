package walkwithme.mc.dal.com.walkwithme;

import android.graphics.Picture;

public class Walk {
    private String eventName;
    private String eventDatetime;
    private String eventLocation;
    private Picture eventPicture;

    public Walk(String name, String datetime, String location) {

        this.eventName = name;
        this.eventDatetime = datetime;
        this.eventLocation = location;

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
}



