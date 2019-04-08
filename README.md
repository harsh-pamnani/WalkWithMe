# Identification

**WalkWithMe Application**

Team Tech Intellect:

| Name              | Student ID    | E-mail |
| ------            | ------        | ------ |
| Aniruddha Chitley | B00808320     | aniruddha.chitley@dal.ca |
| Deep Shah         | B00796368     | dp371796@dal.ca |
| Harsh Pamnani     | B00802614     | hr340096@dal.ca |
| Nitish Bhardwaj   | B00811535     | nitish_bhardwaj@dal.ca |
| Ueli Haltner      | B00526617     | Ueli.Haltner@dal.ca |

Git repository location: [GitLab/WalkWithMe](https://git.cs.dal.ca/chitley/walkwithme)

# Project Summary
WalkWithMe is a platform where users can find and create nearby walking groups in the city of Halifax, Nova Scotia. The goal is to develop networking events, encourage social experiences, and offer a low cost exercise alternative. The architecture of the application consists of three main screens so that the user can view the list of nearby walking events, create a new walking event, and get additional details about a selected event. Weather details are displayed to the user on the details page if the walk is occurring within the following five days. Additionally, the list of walks is sorted by the shortest distance to the user if the location permissions are accepted. All walks are stored in a Firebase database which is partitioned for walk text data and image files.

**Home Page:** 
The home page will display numerous walking events happening near the user. The application fetches all the nearby events and sorts it according to the distance from the user. A round button on the home page allows the user to create a new event. To promote a minimalistic and user-friendly design, we do not ask the user for any log-in credentials at this time. As soon as the user clicks on a specific event, it will direct them to a new view where the event is mentioned in greater detail along with its location in Google Maps.

<p style= " text-align:center">
  <img width="330" height="600" src="https://firebasestorage.googleapis.com/v0/b/walk-8dfad.appspot.com/o/images%2FHome_Activity.jpeg?alt=media&token=0a53d52f-8c3f-4c75-af0a-8a378dcb9b00" alt="Home Screen">
</p>

**View Event Page:** 
Once users select a specific event on the Home page, the system redirects them to the View Event page. This page displays the details of the event selected by the user. The details include the image of the event, name, date, location, and description. The location of the event is also displayed on the map. Once the user clicks on the location, they are asked to open the exact location in Google Maps. The user can make use of the location features to understand the exact venue details of the event. The user can easily go back to the home page using the Android back button. The minimalistic design approach is followed in designing this page. 

<p style= " text-align:center">
  <img width="330" height="600" src="https://firebasestorage.googleapis.com/v0/b/walk-8dfad.appspot.com/o/images%2FView_Activity.jpeg?alt=media&token=5c3abc5e-eaec-4a91-ba36-2216b2b08e68" alt="View Screen">
</p>

**Create Event Page:** This page allows users to create walking events. The event includes details about the meeting point, date and time of the event, and a description. Also, users can upload the image of the location. The description will allow a user to express in more detail the route, visited landmarks, the type of people they are looking for, and the targeted length of the walk.

<p style= " text-align:center">
  <img width="350" height="600" src="https://firebasestorage.googleapis.com/v0/b/walk-8dfad.appspot.com/o/images%2FCreate_Activity.jpeg?alt=media&token=8ddf423d-0259-42cb-ab1e-002e9ce54852" alt="Create Screen">
</p>

## Libraries
**Google Services Location:** 
This library is part of the Google Play Services Location API (v.16.0.0) and allows the last known location of the users' device to be retrieved. The location is determined using both GPS and Android's Network Location Provider. Source [here](https://developer.android.com/training/location/retrieve-current)

**Universal Image Loader (v.1.9.5):** 
This library allows for loading, caching, and displaying images on Android. The application uses this library to handle many different image formats (URIs), and to display the image in the ListView. Source [here](https://github.com/nostra13/Android-Universal-Image-Loader)

**Google Places (v.1.0.0):**
This library allows the developers to make use of AutoComplete API as well as Places API from Google. The application uses this library to retrieve a list of suggested places when the user is writing the event location. Source [here](https://cloud.google.com/maps-platform/places/)

**Firebase Storage (v.16.1.0):**
This library allows the developers to access Firebase storage. The application uses this library to upload and store images when the user is creating an event. Source [here](https://firebase.google.com/docs/storage/)

**Glide (v.4.9.0):**
Glide library allows for efficient image loading to smoothen the scrolling of images in the application. Glide also supports fetching, decoding and displaying images. The application uses this library while displaying the images on the ViewEvent screen. Source [here](https://bumptech.github.io/glide/)

**Volley (v.1.1.0):**
Volley library helps the developers to make networking for the mobile application easier and faster. The application uses this library for making an HTTP GET request for the places API and to get the coordinates of the place. Source [here](https://developer.android.com/training/volley)

**Firebase Core:**
Firebase Core dependency enables to connect multiple Firebase apps. Source [here](https://firebase.google.com/docs/android/setup)

**Firebase Database:**
Firebase Database library allows access to store text and other data (image) from the android application. Source [here](https://firebase.google.com/docs/android/setup) 

**Picasso (v.):**
Picasso allows hassle-free image load to the application from a URL. Source [here](https://square.github.io/picasso/) 


## Installation Notes
Installation requirments for markers.

- **OS Requirements         :**  Android API level 23 (Android 6.0 Marshmallow)
- **Android Studio Version  :**  3.2.1
- **Gradle Version          :**  3.2.1

**There are no extra dependencies for this application. We are using libraries, which have been included in the build.gradle file.

## Code Examples
The following three examples are problems that the team encountered, and solved, while developing the project.

**Problem 1: AutoComplete API allowed us to select locations but did not return the coordinates**

After selecting the place from Places Autocomplete API, the Place object was returning only Place ID and Place Name for that place. Furthermore, we also required Latitude and Longitude values for storing the walks in the “Home Event” page based on the shortest distance to the user. However, the Autocomplete API was returning null for all values except Id and Name. This is the limitation of Place Autocomplete API. To solve this problem, we are making a second call to the Places API by passing the Place ID. The places API returns every detail for that Place. From this response, we took Latitude and Longitude and stored it in the Walk object. Hence, now the “View Event” page will have access to Latitude and Longitude, and it can sort the data based on the nearest location. Following is the sample from the code:

```
public static final String URL_START = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
public static final String URL_END = "&key=AIzaSyCVRI6McPgJ6ZLD3OXZwgtDeFcHH7qByaQ";

private void getLatLngOfPlace() {
		// Adding the placeId to the URL for place API call
        String requestURL = URL_START + placeId + URL_END;

        RequestQueue queue = Volley.newRequestQueue(this);

		// Creating a new JsonObjectRequest
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            JSONObject result = response.getJSONObject("result");
                            JSONObject geometry = result.getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");
                            latitude = location.getDouble("lat");
                            longitude = location.getDouble("lng");
                        } catch (JSONException e) {
                            Log.i(LOG_TAG, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(LOG_TAG, error.toString());
                    }
                }
        );

        queue.add(getRequest);
}
```

**Problem 2: Images could not be stored alongside text data in the Firebase database**

There is no facility in Firebase to store images with the text. So, we have stored images separately in the storage of the Firebase. Whenever a user uploads an image, we are fetching the URL of that image and saving it in the Firebase along with the walk object. We have implemented the following method to solve this problem.
```
public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
	ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
		@Override
		public void onSuccess(Uri uri) {
			Log.d(LOG_TAG, "onSuccess: uri= "+ uri.toString());
			firebaseUploadedImagesURLs.add(uri.toString());
		}
	});

	progressDialog.dismiss();
	Toast.makeText(CreateActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
}
```

**Problem 3: Bypassing Location Permissions**

One of the roadblocks encountered during testing was that we were able to bypass location permission settings on the emulator but not on a physical device. The method called to retrieve the last known location was continuously declined and resulted in some confusion. We consulted the Android Developers guide to learn more about permission settings and requirements [21]. A solution was found using the Android Developer permission training documentation [14] which allowed us to both check and prompt a permission system dialog box.
```
private void getLocationPermission(){
    // If permission is not granted
    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
        
        //request the location permission by displaying a system dialog
        ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    
            // Result determined in onRequestPermissionResult
    
    //else permission is granted (or has been granted in the past)
    }else{
        mLocationPermissionGranted = true;
        //call method that gets last known location
        getLastLocation();
    }
}        
// Source: "Request App Permission", Android Developer Doc [14]
```
The permission request response is then handled in the onRequestPermissionResult method. If the request is cancelled/denied, the result arrays are empty.
```
@Override
public void onRequestPermissionsResult(int requestCode,
            @NonNull String permissions[], @NonNull int[] grantResults) {
    switch (requestCode) {
        case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
            
            // If request has been granted
            if (grantResults.length > 0
                 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                mLocationPermissionGranted = true;
                // permission was granted! Do the
                // location-related task you need to do.

            //else if the request has been denied
            }else{
                
                mLocationPermissionGranted = false;
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }
    }
}
// Source: "Request App Permission", Android Developer Doc [14]
```

## Feature Section
The following are the main features of our application:

**Sorting using location tracking:** Using Google Maps Location API and the built in GPS, the users last known location is retrieved and stored. When the walks are pulled from the database and added to the list, the constructor also passes the two sets of coordinates (user and walk location) to compute the distance. The list is then sorted using the Java Collections class. The resulting list of walks is sorted by shortest distance to the user.

**Weather:** When a user views an event, the application displays the weather of the location where the event will take place on the specific date. This way it helps the users to make decisions whether they should join the event or not based on the various weather conditions. For example, if there is a high probability of having rain or snow on the event date, the user might not want to attend the event. This application will display the weather of five future days with an image icon in the “View Event” Screen using openweathermap API. If the event is going to happen after five days, then it will display the message that “weather will be available soon” because openweathermap API cannot show the weather forecast past the five days. The original temperature is fetched in Fahrenheit which is converted and displayed in Celsius. Also, to improve the user experience, there is a small icon of the weather, which gives the user more clarity about the weather conditions.

**Place Autocomplete:** This feature provides a list of places when the user tries to type for a particular location. This way the user does not need to type the entire name of the place and prevents users from making typing mistakes. It uses Place Autocomplete API by Google, and so the location names are reliable.

**Camera/Gallery Access:** This feature facilitates a user to upload the image of the location where the event is taking place. The user can attach the photo by accessing their camera. Also, users can upload the picture from their Gallery which will be displayed when the user tries to view the event. 

**Map access on View event page:**
Users are shown the location of the event as text as well as on the map. Google maps[10] are a great way to display the location. The user can click on the embedded Google Map in the application to be redirected to the main Google Navigation system.

**Carousel view for multiple images:**
Using the carousel view[7, 8] users can view multiple images related to the event by swiping on the images from left to right or vice-versa. The view is created to be intuitive and user-friendly to ensure that users can perceive more information with less effort.

## Final Project Status

#### Minimum Functionality
- Creating events (Completed)
- Viewing events and details (Completed)
- GPS integration (Completed)

#### Expected Functionality
- Camera access / Attaching images to events (Completed)
- Weather Details (Completed)

#### Bonus Functionality
- Log-in page (Partially Completed)
- Event notification (Not Implemented)

### Future Work
The next steps in this project if it were to continue would include the following:
- Complete the implementation of the Log-In functionality so that users can keep track of their own posts and update the events.
- Implement a notification functionality to allow SMS or native reminders at a specified time before the walk. This feature would be included in the details screen through a small *bell* icon and give the user the flexibility to decide how often and how soon in advance they would like to be notified.
- Research and develop a design to maintain the database. Currently walks are not deleted past the walk event date and time.
- The application could be scaled to allow use on a global scale. For this, a design would have to be developed to limit the number of walks that are pulled from the database for a user.
- Allow users to upload multiple event images when creating a walking event.


## Sources

#### Programming Tutorials:

[1] M.Tabian, “Custom ListView Adapter For Displaying Multiple Columns,” YouTube.com, 14-Mar-2017. [Video file]. Available: https://www.youtube.com/watch?v=E6vE8fqQPTE.

[2] M.Tabian, “Custom ListView Adapter [With Loading Animation],” YouTube.com, 15-Mar-2017. [Video file]. Available: https://www.youtube.com/watch?v=E6vE8fqQPTE.

[3] M.Tabian, “Google Services, GPS, and Location Permissions,” YouTube.com, 19-Sep-2018. [Video file]. Available: https://www.youtube.com/watch?v=1f4b2-Y_q2A.

[4] M.Tabian, “Retrieving the Device Location,” YouTube.com, 19-Sep-2018. [Video File]. Available: https://www.youtube.com/watch?v=ZXXoIDj2pR0.

[5] Nilanchala, “Universal Image Loader Library in Android,” Stacktips.com, 14-May-2014. [Online]. Available: https://www.stacktips.com/tutorials/android/universal-image-loader-library-in-android.

[6] D. Nugent, “How to get current Location in GoogleMap using FusedLocationProviderClient”, Stack Overflow, 9-Jul-2017. [Online]. Available: https://stackoverflow.com/questions/44992014/how-to-get-current-location-in-googlemap-using-fusedlocationproviderclient.

[7] H. Parsania, "Android Image Slider From URL | Fetch Image From Server URL", DemoNuts, 2019. [Online]. Available: https://demonuts.com/android-image-slider-from-url/. [Accessed: 28- Mar- 2019].

[8] Codepath [Online]. Available: https://guides.codepath.com/android/Displaying-Images-with-the-Picasso-Library. [Accessed: 29- Mar- 2019].

#### Android Libraries:

[9] S.Tarasevich, “Universal Image Loader Library,” GitHub.com, 26-Jan-2016. [Online]. Available: https://github.com/nostra13/Android-Universal-Image-Loader.

[10] "Google Maps Platform  |  Google Developers", Google Developers, 2019. [Online]. Available: https://developers.google.com/maps/documentation/. [Accessed: 01- Apr- 2019].

[11]"5 day weather forecast - OpenWeatherMap", Openweathermap.org, 2019. [Online]. Available: https://openweathermap.org/forecast5.

[12]"Overview  |  Places API  |  Google Developers", Google Developers, 2019. [Online]. Available: https://developers.google.com/places/web-service/intro.

[13]"Place Autocomplete  |  Places API  |  Google Developers", Google Developers, 2019. [Online]. Available: https://developers.google.com/places/web-service/autocomplete.

#### Research Material:

[14] “Request App Permissions,” Android Developer Docs, 2019. [Online]. Available: https://developer.android.com/training/permissions/requesting.html.

[15] “App permissions best practices,” Android Developer Docs, 2019. [Online]. Available: https://developer.android.com/training/permissions/usage-notes.

[16] “Change location settings,” Android Developer Docs, 2019. [Online]. Available: https://developer.android.com/training/location/change-location-settings.html.

[17] “Get the last known location,” Android Developer Docs, 2019. [Online]. Available: https://developer.android.com/training/location/retrieve-current.html.

#### Design Guides:

[18] “Set Up Google Play Services,” Google Developer Docs, 2019. [Online]. Available: https://developers.google.com/android/guides/setup.

[19] “Accessing Google APIs,” Google Developer Docs, 2019. [Online]. Available: https://developers.google.com/android/guides/api-client.

[20] “Understand the Activity Lifecycle,” Android Developer Docs, 2019. [Online]. Available: https://developer.android.com/guide/components/activities/activity-lifecycle.

[21] “Permissions overview,” Android Developer Docs, 2019. [Online]. Available: https://developer.android.com/guide/topics/permissions/overview.

[22] “Location strategies,” Android Developer Docs, 2019. [Online]. Available: https://developer.android.com/guide/topics/location/strategies.

[23] “Java Collections Class,” Android Developer Docs, 2019. [Online]. Available: https://developer.android.com/reference/java/util/Collections

[24] "Accessing the Camera and Stored Media | CodePath Android Cliffnotes", Guides.codepath.com, 2019. [Online]. Available: https://guides.codepath.com/android/Accessing-the-Camera-and-Stored-Media.

[25] Vadim Kotov, and SilentKiller, "Show Error on the tip of the Edit Text Android", Stack Overflow, 2018. [Online]. Available: https://stackoverflow.com/questions/18225365/show-error-on-the-tip-of-the-edit-text-android.

[26] S. Ganesh, س. میرعظیمی and P. Shah, "How to Change color of Button in Android when Clicked?", Stack Overflow, 2012. [Online]. Available: https://stackoverflow.com/questions/3882064/how-to-change-color-of-button-in-android-when-clicked.

[27] A. Singh, J. Person and Y. P, "How to Get Image URL after uploading an image form android to Firebase?", Stack Overflow, 2017. [Online]. Available: https://stackoverflow.com/questions/44686647/how-to-get-image-url-after-uploading-an-image-form-android-to-firebase.

[28] C.Izuchukwu, "How to Upload Images to Firebase from an Android App", Code Envato Tuts+, 2017. [Online]. Available: https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934.

[29] R. Tamada, "Android Material Design Floating Labels for EditText", AndroidHive, 2019. [Online]. Available: https://www.androidhive.info/2015/09/android-material-design-floating-labels-for-edittext/.

[30] "yomiyusuf/InstaLogin", GitHub, 2019. [Online]. Available: https://github.com/yomiyusuf/InstaLogin/.

[31] "Migrating to the New Places SDK Client  |  Places SDK for Android  |  Google Developers", Google Developers, 2019. [Online]. Available: https://developers.google.com/places/android-sdk/client-migration.

[32] A. Jasmin, V. Parihar and O. Damiean, "Detect whether there is an Internet connection available on Android", Stack Overflow, 2019. [Online]. Available: https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android.


#### Stock Images

[33] italianpolitic, "Walk Svg", OnlineWebFonts.COM, 2019. [Online]. Available: https://www.onlinewebfonts.com/icon/366314.