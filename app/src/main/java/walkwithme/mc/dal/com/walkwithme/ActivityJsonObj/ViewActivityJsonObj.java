package walkwithme.mc.dal.com.walkwithme.ActivityJsonObj;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ViewActivityJsonObj {

    // Method to create JSON objects from JSON String.
    //Accepts String Object as method parameter
    public JSONObject saveData(String result) {
        JSONObject eventObj = null;
        try {
            eventObj= (JSONObject) new JSONTokener(result).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return eventObj;
    }

}
