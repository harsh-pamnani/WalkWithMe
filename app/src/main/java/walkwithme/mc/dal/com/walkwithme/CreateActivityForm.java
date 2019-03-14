package walkwithme.mc.dal.com.walkwithme;

public class CreateActivityForm {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;


    // Constructor for Create Activity form
    public CreateActivityForm(String title) {
        this.title = title;
    }
}
