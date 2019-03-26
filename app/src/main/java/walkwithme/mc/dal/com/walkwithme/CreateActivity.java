package walkwithme.mc.dal.com.walkwithme;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONObject;

public class CreateActivity extends AppCompatActivity {

    ImageView photoImageView;
    Integer REQUEST_CAMERA=1, SELECT_FILE=0;

    private final static String TAG = "HP";

    List<Bitmap> photoList = new ArrayList<Bitmap>();
    EditText dateEditText;
    EditText edittime;
    EditText titleEditText;
    EditText desctiptionEditText;
    AutoCompleteTextView venueAutoComplete;
    Calendar myCalendar;
    Button photoUploadButton;
    Button submitButton;
    TextView eventLocationView;
    AutocompleteSupportFragment autocompleteFragment;
    String placeName;
    File hpFile;
    Uri hpFileUri;
    StorageReference ref;
    ArrayList<String> firebaseUploadedImagesURLs = new ArrayList<String>();
    ArrayList<Uri> imageURIs = new ArrayList<Uri>();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference fireBaseAuth;

    FirebaseStorage storage;
    StorageReference storageReference;

    private Uri filePath;
    String titleForImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        //FirebaseApp.initializeApp(CreateActivity.this);
        // Creating the instance of firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance();
        fireBaseAuth = firebaseDatabase.getReference("WALK_DATA");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        findViewById(R.id.scrollView).requestFocus();

        titleEditText = findViewById(R.id.lblTitleEdit);
        myCalendar = Calendar.getInstance();
        dateEditText = findViewById(R.id.lblDateEdit);
        edittime = findViewById(R.id.lblTimeEdit);
        photoImageView = findViewById(R.id.lblUpload);
        photoUploadButton = findViewById(R.id.lblUploadButton);
        submitButton = findViewById(R.id.submitButton);
        eventLocationView = findViewById(R.id.event_location_view);
        desctiptionEditText = findViewById(R.id.lblDescriptionEdit);

        Places.initialize(getApplicationContext(), "AIzaSyCVRI6McPgJ6ZLD3OXZwgtDeFcHH7qByaQ");

        PlacesClient placesClient = Places.createClient(this);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCVRI6McPgJ6ZLD3OXZwgtDeFcHH7qByaQ");
        }

        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Event location");

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                eventLocationView.setText("Event Location");
                placeName = place.getName();
                Log.i("HP", "Place: " + place.getName() + ", " + place.getId());
                // https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJ4bBaC4EhWksRgzKOYtziUQo&key=AIzaSyCVRI6McPgJ6ZLD3OXZwgtDeFcHH7qByaQ
                // then go to result.geometry.location
            }

            @Override
            public void onError(Status status) {
                Log.i("HP", "An error occurred: " + status);
            }
        });

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

        titleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    setVenueError();
                }
            }
        });

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edittime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        edittime.setText(hourOfDay + ":" + minutes);
                    }
                }, 0, 0, false);

                timePickerDialog.show();
            }
        });

        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    setDateError();
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVenueError();
                setLocationError();
                setDateError();
                setTimeError();

                CharSequence titleError = titleEditText.getError();
                CharSequence dateError = dateEditText.getError();
                CharSequence timeError = edittime.getError();

                if(titleError==null && dateError==null && timeError==null) {
                    prepareFormDataforFirebase();

                    AlertDialog alertDialog = new AlertDialog.Builder(CreateActivity.this).create(); //Read Update
                    alertDialog.setTitle("Event Submitted.");
                    alertDialog.setMessage("Your event has been submitted successfully. \n\nGo back to home page.");

                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK!",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent createIntent= new Intent(CreateActivity.this, HomeActivity.class);
                                    startActivity(createIntent);
                                }
                            });

                    alertDialog.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Rectify the errors before submitting the form", Toast.LENGTH_SHORT);;
                    toast.show();
                }
            }
        });
    }

    private void uploadImage() {
        // Add the image upload code here
    }

    private void setLocationError() {

    }

    private void setTimeError() {
        if (TextUtils.isEmpty(edittime.getText())) {
            edittime.setError("Time can not be empty");
        } else {
            edittime.setError(null);
        }
    }

    private void setVenueError() {
        if (TextUtils.isEmpty(titleEditText.getText()) || titleEditText.getText().length() < 10) {
            titleEditText.setError("Title can not be less than 10 characters");
        } else {
            titleEditText.setError(null);
        }
    }

    private void setDateError() {
        if (TextUtils.isEmpty(dateEditText.getText())) {
            dateEditText.setError("Date can't be past date");
            return;
        }

        String dateString = dateEditText.getText().toString();
        Date enteredDate = null;
        try {
            enteredDate = new SimpleDateFormat("dd/MMM/yyyy").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(enteredDate!=null) {
            if (!(enteredDate.compareTo(new Date()) >= 0)) {
                dateEditText.setError("Date can't be past date");
            } else {
                dateEditText.setError(null);
            }
        }
    }

    private void SelectImage(){
        final CharSequence[] items={"Camera","Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateActivity.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[i].equals("Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, SELECT_FILE);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    private void updateLabel(EditText edittext, Calendar myCalendar) {
        String myFormat = "dd/MMM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){
            if(requestCode==REQUEST_CAMERA){
                Log.i(TAG, "entered in request camera access");

                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                photoList.add(bmp);
                photoImageView.setImageBitmap(photoList.get(0));
            } else if(requestCode==SELECT_FILE){
                Log.i(TAG, "entered in select file access");
                ClipData clipData = data.getClipData();

                //Log.i(TAG, data.getDataString());

                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri tempPath = clipData.getItemAt(i).getUri();
                    imageURIs.add(tempPath);
                }

                filePath = clipData.getItemAt(0).getUri();
                photoImageView.setImageURI(filePath);
                uploadImage();

            }
        }
    }

    private void prepareFormDataforFirebase(){
        // Add code here to submit the event data to firebase
    }
}