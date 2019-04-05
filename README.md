# Identification

**WalkWithMe Application**

Team Tech Intellect:

| Name              | Student ID    | E-mail |
| ------            | ------        | ------ |
| Aniruddha Chitley | B00808320     | @dal.ca |
| Deep Shah         | B00796368     | @dal.ca |
| Harsh Pamnani     | B00802614     | @dal.ca |
| Nitish Bhardwaj   | B00811535     | @dal.ca |
| Ueli Haltner      | B00526617     | Ueli.Haltner@dal.ca |

Git repository location: [GitLab/WalkWithMe](https://git.cs.dal.ca/chitley/walkwithme)

# Project Summary
WalkWithMe is a platform where users can find and create nearby walking groups in the city of Halifax, Nova Scotia. The goal is to develop networking events, encourage social experiences, and offering low cost exercise alternatives. The architecture of the application consists of three main screens so that the user can view the list of nearby walking events, create a new walking event, and get additional details about a selected event. Weather details are displayed to the user on the details page if the walk is occurring within the following five days. Additionally, the list of walks is sorted by the shorted distance to the user if the location permissions are accepted. All walks are stored in a Firebase database which is partitioned for walk text data and image files.

## Libraries
**Google Services Location:** 
This library is part of the Google Play Services Location API (v.16.0.0) and allows the last known location of the user’s device to be retrieved. The location is determined using both GPS and Android's Network Location Provider. Source [here](https://developer.android.com/training/location/retrieve-current)

**Universal Image Loader (v.1.9.5):** 
This library allows for loading, caching, and displaying images on Android. This application uses this library to handle many different image formats (URIs), and to display the image in the ListView. Source [here](https://github.com/nostra13/Android-Universal-Image-Loader)

...

## Installation Notes
Installation instructions for markers.

OS Requirements: Android API level 23 (Android 6.0 Marshmallow)
...

## Code Examples
You will encounter roadblocks and problems while developing your project. Share 2-3 'problems' that your team solved while developing your project. Write a few sentences that describe your solution and provide a code snippet/block that shows your solution. Example:

**Problem 1: AutoComplete API allowed us to select locations but did not return the coordinates**

A short description.
```
// The method we implemented that solved our problem
public static int fibonacci(int fibIndex) {
    if (memoized.containsKey(fibIndex)) {
        return memoized.get(fibIndex);
    } else {
        int answer = fibonacci(fibIndex - 1) + fibonacci(fibIndex - 2);
        memoized.put(fibIndex, answer);
        return answer;
    }
}

// Source: Wikipedia Java [1]
```

**Problem 2: Images could not be stored alongside text data in the Firebase database**

A short description.
```
// The method we implemented that solved our problem
public static int fibonacci(int fibIndex) {
    if (memoized.containsKey(fibIndex)) {
        return memoized.get(fibIndex);
    } else {
        int answer = fibonacci(fibIndex - 1) + fibonacci(fibIndex - 2);
        memoized.put(fibIndex, answer);
        return answer;
    }
}

// Source: Wikipedia Java [1]
```


## Feature Section
List all the main features of your application with a brief description of each feature.

**Sorting using location tracking:** Using Google Maps Location API and the built in GPS, the users last known location is retrieved and stored. When the walks are pulled from the database and added to the list, the constructor also passes the two sets of coordinates (user and walk location) to compute the distance. The list is then sorted using the Java Collections class. The resulting list of walks is sorted by shortest distance to the user.


...

## Final Project Status
Write a description of the final status of the project. Did you achieve all your goals? What would be the next step for this project (if it were to continue)?

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
- The application could be scaled to allow use on a global scale. For this a design would have to be developed to limit the number of walks that are pulled from the database for a user.

...

## Sources

#### Programming Tutorials:

[] M.Tabian, “Custom ListView Adapter For Displaying Multiple Columns,” YouTube.com, 14-Mar-2017. [Video file]. Available: https://www.youtube.com/watch?v=E6vE8fqQPTE.

[] M.Tabian, “Custom ListView Adapter [With Loading Animation],” YouTube.com, 15-Mar-2017. [Video file]. Available: https://www.youtube.com/watch?v=E6vE8fqQPTE.

[] M.Tabian, “Google Services, GPS, and Location Permissions,” YouTube.com, 19-Sep-2018. [Video file]. Available: https://www.youtube.com/watch?v=1f4b2-Y_q2A.

[] M.Tabian, “Retrieving the Device Location,” YouTube.com, 19-Sep-2018. [Video File]. Available: https://www.youtube.com/watch?v=ZXXoIDj2pR0.

[] Nilanchala, “Universal Image Loader Library in Android,” Stacktips.com, 14-May-2014. [Online]. Available: https://www.stacktips.com/tutorials/android/universal-image-loader-library-in-android.

[] D. Nugent, “How to get current Location in GoogleMap using FusedLocationProviderClient”, Stack Overflow, 9-Jul-2017. [Online]. Available: https://stackoverflow.com/questions/44992014/how-to-get-current-location-in-googlemap-using-fusedlocationproviderclient.

#### Android Libraries:

[] S.Tarasevich, “Universal Image Loader Library,” GitHub.com, 26-Jan-2016. [Online]. Available: https://github.com/nostra13/Android-Universal-Image-Loader.

#### Research Material:

[] “Request App Permissions,” Android Developer Docs, 2019. [Online]. Available: https://developer.android.com/training/permissions/requesting.html.

[] “App permissions best practices,” Android Developer Docs, 2019. [Online]. Available: https://developer.android.com/training/permissions/usage-notes.

[] “Change location settings,” Android Developer Docs, 2019. [Online]. Available: https://developer.android.com/training/location/change-location-settings.html.

[] “Get the last known location,” Android Developer Docs, 2019. [Online]. Available: https://developer.android.com/training/location/retrieve-current.html.

#### Design Guides:

[] “Set Up Google Play Services,” Google Developer Docs, 2019. [Online]. Available: https://developers.google.com/android/guides/setup.

[] “Accessing Google APIs,” Google Developer Docs, 2019. [Online]. Available: https://developers.google.com/android/guides/api-client.

[] “Understand the Activity Lifecycle,” Android Developer Docs, 2019. [Online]. Available: https://developer.android.com/guide/components/activities/activity-lifecycle.

[] “Permissions overview,” Android Developer Docs, 2019. [Online]. Available: https://developer.android.com/guide/topics/permissions/overview.

[] “Location strategies,” Android Developer Docs, 2019. [Online]. Available: https://developer.android.com/guide/topics/location/strategies.

[] “Java Collections Class,” Android Developer Docs, 2019. [Online]. Available: https://developer.android.com/reference/java/util/Collections

#### Stock Images

[] ...