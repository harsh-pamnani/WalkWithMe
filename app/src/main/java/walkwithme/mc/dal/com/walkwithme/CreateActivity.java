package walkwithme.mc.dal.com.walkwithme;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateActivity extends AppCompatActivity {

    // Constants for the class
    public static final String LOG_TAG = "HP";
    public static final String URL_START = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
    public static final String URL_END = "&key=AIzaSyCVRI6McPgJ6ZLD3OXZwgtDeFcHH7qByaQ";

    // Declaring all the variables for UI elements
    ImageView photoImageView;
    Integer REQUEST_CAMERA=1, SELECT_FILE=0;

    List<Bitmap> photoList = new ArrayList<Bitmap>();
    EditText dateEditText, edittime, titleEditText, desctiptionEditText;
    Button photoUploadButton, submitButton;

    Calendar myCalendar;
    TextView eventLocationView;
    AutocompleteSupportFragment autocompleteFragment;
    String placeName, placeId;

    StorageReference ref;
    ArrayList<String> firebaseUploadedImagesURLs = new ArrayList<String>();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference fireBaseAuth;

    FirebaseStorage storage;
    StorageReference storageReference;

    private Uri filePath;
    String titleForImage;

    // For storing latitude and longitude
    double latitude, longitude;

    Runnable runnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        // Initializing the firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();
        fireBaseAuth = firebaseDatabase.getReference("WALK_DATA");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Closing the soft keyboard when the screen is launched
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        findViewById(R.id.scrollView).requestFocus();

        // Finding all the elements from UI
        titleEditText = findViewById(R.id.lblTitleEdit);
        myCalendar = Calendar.getInstance();
        dateEditText = findViewById(R.id.lblDateEdit);
        edittime = findViewById(R.id.lblTimeEdit);
        photoImageView = findViewById(R.id.lblUpload);
        photoUploadButton = findViewById(R.id.lblUploadButton);
        submitButton = findViewById(R.id.submitButton);
        eventLocationView = findViewById(R.id.event_location_view);
        desctiptionEditText = findViewById(R.id.lblDescriptionEdit);

        // Initializing the Places object using API key
        Places.initialize(getApplicationContext(), "AIzaSyCVRI6McPgJ6ZLD3OXZwgtDeFcHH7qByaQ");

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCVRI6McPgJ6ZLD3OXZwgtDeFcHH7qByaQ");
        }

        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Event location");

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Place autocomplete listener for Google Places API integration
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                eventLocationView.setText("Event Location");
                placeName = place.getName();
                placeId = place.getId();

                Log.i(LOG_TAG, placeId);

                runnable = new Runnable() {
                    @Override
                    public void run() {
                        getLatLngOfPlace();
                    }
                };

                //retrieve data on separate thread
                Thread thread = new Thread(null, runnable,"background");
                thread.start();

                // https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJ4bBaC4EhWksRgzKOYtziUQo&key=AIzaSyCVRI6McPgJ6ZLD3OXZwgtDeFcHH7qByaQ
                // then go to result.geometry.location
            }

            @Override
            public void onError(Status status) {
                Log.i(LOG_TAG, status.toString());
            }
        });

        // OnClick event for upload button
        photoUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleForImage = titleEditText.getText().toString();
                if(titleForImage.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter event title before uploading an image", Toast.LENGTH_SHORT).show();
                } else {
                    SelectImage();
                }
            }
        });

        // Implementing Set Listener when date is changed.
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(dateEditText, myCalendar);
            }
        };

        // Setting the event title error
        titleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    setTitleError();
                }
            }
        });

        // Implementing date picker
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Implementing time picker
        edittime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        edittime.setText(String.format("%02d",hourOfDay) + ":" + String.format("%02d",minutes));                    }
                }, 0, 0, false);

                timePickerDialog.show();
            }
        });

        // Setting date error
        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    setDateError();
                }
            }
        });

        // Setting time error
        edittime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    setTimeError();
                }
            }
        });

        // OnClick event listener when submit button is pressed
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check all the fields validation before submitting the event
                setTitleError();
                setDateError();
                setTimeError();

                // Getting error
                CharSequence titleError = titleEditText.getError();
                CharSequence dateError = dateEditText.getError();
                CharSequence timeError = edittime.getError();

                // If all the errros are null, then only proceed further
                if(titleError==null && dateError==null && timeError==null) {
                    // Submitting the event date to firebase
                    prepareFormDataforFirebase();

                    // Alert box for event submission
                    AlertDialog alertDialog = new AlertDialog.Builder(CreateActivity.this).create(); //Read Update
                    alertDialog.setTitle("Event Submitted.");
                    alertDialog.setMessage("Your event has been submitted successfully. \n\nGo back to home page.");

                    // Displaying "OK" button for going back to home screen
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK!",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                      //finish() to terminate intent instead of new intent so that onCreate is not called again in HomeActivity
                                      finish();
                                }
                            });

                    alertDialog.show();
                } else {
                    // Displaying the toast if any of the field validations fail
                    Toast toast = Toast.makeText(getApplicationContext(), "Rectify the errors before submitting the form", Toast.LENGTH_SHORT);;
                    toast.show();
                }
            }
        });
    }

    // Method to get latitude and longitude of the place
    private void getLatLngOfPlace() {
        // Request URL for places API
        String requestURL = URL_START + placeId + URL_END;

        RequestQueue queue = Volley.newRequestQueue(this);

        // Creating the JsonObjectRequest for the given URL
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            // Getting teh latitude and longitude from the response
                            JSONObject result = response.getJSONObject("result");
                            JSONObject geometry = result.getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");
                            latitude = location.getDouble("lat");
                            longitude = location.getDouble("lng");
                        } catch (JSONException e) {
                            // Catching the exception
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

    // Method for uploading image to firebase
    private void uploadImage() {
        // Checking file path of image is not null
        if(filePath != null) {
            // Showing progress bar for upload
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Getting the reference for the firebase storage of images
            ref = storageReference.child("images/"+ titleForImage + ";" + UUID.randomUUID().toString());

            // Uploading the given file to firebase storage
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        // Listener when image is successfully uploaded
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Getting the URL of uploade image
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d(LOG_TAG, "onSuccess: uri= "+ uri.toString());
                                    firebaseUploadedImagesURLs.add(uri.toString());
                                }
                            });

                            // Cancelling the dialog for upload
                            progressDialog.dismiss();

                            // Displaying Toast for image uploaded
                            Toast.makeText(CreateActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Listener when image upload fails
                            progressDialog.dismiss();
                            Toast.makeText(CreateActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // Listener when the image is being uploaded to show the progress bar
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });

        }
    }

    // Method for setting the error for Time field
    private void setTimeError() {
        if (TextUtils.isEmpty(edittime.getText())) {
            edittime.setError("Time can not be empty");
        } else {
            edittime.setError(null);
        }
    }

    // Method for setting the error for Title field
    private void setTitleError() {
        if (TextUtils.isEmpty(titleEditText.getText()) || titleEditText.getText().length() < 10) {
            titleEditText.setError("Title can not be less than 10 characters");
        } else {
            titleEditText.setError(null);
        }
    }

    // Method for setting the error for Date field
    private void setDateError() {
        // If no date is entered
        if (TextUtils.isEmpty(dateEditText.getText())) {
            dateEditText.setError("Date can't be past date");
            return;
        }

        // Converting the date to required format.
        String dateString = dateEditText.getText().toString();
        Date enteredDate = null;
        try {
            enteredDate = new SimpleDateFormat("dd/MMM/yyyy").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // If entered date is past date
        if(enteredDate!=null) {
            if (!(enteredDate.compareTo(new Date()) >= 0)) {
                dateEditText.setError("Date can't be past date");
            } else {
                dateEditText.setError(null);
            }
        }
    }

    // Method for displaying options to the user for selecting the image from Camera/Gallery
    private void SelectImage(){
        final CharSequence[] items={"Camera","Gallery", "Cancel"};

        // Alert box displaying the options
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateActivity.this);
        builder.setTitle("Add Image");

        // Alert box having 3 options: Camera, Gallery, and Cancel
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[i].equals("Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILE);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    // Method for updating the format for date
    private void updateLabel(EditText edittext, Calendar myCalendar) {
        String myFormat = "dd/MMM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }

    // This method handles the reuslt of image selection from the user.
    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        // If the response is SUCCESS.
        if(resultCode== Activity.RESULT_OK){
            // If the request was Camera
            if(requestCode==REQUEST_CAMERA){
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                photoList.add(bmp);
                photoImageView.setImageBitmap(photoList.get(0));
            }
            // If the request was Selecting the file from Gallery
            else if(requestCode==SELECT_FILE){
                filePath = data.getData();
                photoImageView.setImageURI(filePath);
                uploadImage();

            }
        }
    }

    // Method for uploading the date to firebase
    private void prepareFormDataforFirebase(){
        // Generating a random string for eventId
        String id = UUID.randomUUID().toString();
        String title = titleEditText.getText().toString();
        String location = placeName;
        String date = dateEditText.getText().toString();
        String time = edittime.getText().toString();
        String description = desctiptionEditText.getText().toString();
        ArrayList<String> uploadImageURL = firebaseUploadedImagesURLs;

        // Setting the default image, if the user has not uploaded any image
        if(uploadImageURL.size()==0) {
            ArrayList<String> defaultArrayList = new ArrayList<String>();
            defaultArrayList.add("https://firebasestorage.googleapis.com/v0/b/walk-8dfad.appspot.com/o/images%2Fdefault%20image.PNG?alt=media&token=8d0683a5-48b0-49f5-935f-152d76bee948");
            uploadImageURL = defaultArrayList;
        }

        double latitudeValue = latitude;
        double longitudeValue = longitude;

        CreateActivityForm crtFrm = new CreateActivityForm(id, title, location, date, time, description, uploadImageURL, latitudeValue, longitudeValue);
        fireBaseAuth.push().setValue(crtFrm);
    }
}