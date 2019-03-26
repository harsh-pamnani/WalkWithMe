package walkwithme.mc.dal.com.walkwithme;

import java.util.ArrayList;

public class CreateActivityForm {
   String date;
   String description;
   String id;
   ArrayList<String> imageURL;
   String location;
   String time;
   String title;


    public CreateActivityForm(String date, String description, String id, ArrayList<String> imageURL, String location, String time, String title) {
        this.date = date;
        this.description = description;
        this.id = id;
        this.imageURL = imageURL;
        this.location = location;
        this.time = time;
        this.title = title;
    }

    public CreateActivityForm() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getImageURL() {
        return imageURL;
    }

    public void setImageURL(ArrayList<String> imageURL) {
        this.imageURL = imageURL;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
