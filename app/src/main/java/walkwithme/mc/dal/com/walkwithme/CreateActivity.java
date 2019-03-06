package walkwithme.mc.dal.com.walkwithme;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateActivity extends AppCompatActivity {

    ImageView photoImageView;
    Integer REQUEST_CAMERA=1, SELECT_FILE=0;

    ArrayList<Bitmap> photoList = new ArrayList<Bitmap>();
    EditText dateEditText;
    EditText edittime;
    EditText titleEditText;
    AutoCompleteTextView venueAutoComplete;
    Calendar myCalendar;
    Button photoUploadButton;
    Button submitButton;

    String TAG = "HP";

    AutoCompleteTextView text;
    String[] places ={"Dalhousie ","SMU","Duncan Street","Park Victoria","Robbie Street","Cunnard Street","Downtown","Walmart","HSC","Spring Garden"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        text = findViewById(R.id.autoCompleteTextView1);
        titleEditText = findViewById(R.id.lblTitleEdit);
        venueAutoComplete = findViewById(R.id.autoCompleteTextView1);
        myCalendar = Calendar.getInstance();
        dateEditText = findViewById(R.id.lblDateEdit);
        edittime = findViewById(R.id.lblTimeEdit);
        photoImageView = findViewById(R.id.lblUpload);
        photoUploadButton = findViewById(R.id.lblUploadButton);
        submitButton = findViewById(R.id.submitButton);

        ArrayAdapter adapter = new
                ArrayAdapter(this,android.R.layout.simple_list_item_1, places);

        text.setAdapter(adapter);
        text.setThreshold(1);

        photoUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
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
                    if (titleEditText.getText().length() < 10) {
                        titleEditText.setError("Title can not be less than 10 characters");
                    }
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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence titleError = titleEditText.getError();

                if(titleError==null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(CreateActivity.this).create(); //Read Update
                    alertDialog.setTitle("Event Submitted.");
                    alertDialog.setMessage("Your event has been submitted successfully. \n\nGo back to home page.");

                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK!",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Go to home screen
                                    dialog.dismiss();
                                }
                            });

                    alertDialog.show();  //<-- See This!
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Rectify the errors", Toast.LENGTH_SHORT);;
                    toast.show();
                }
            }
        });
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
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                photoList.add(bmp);
                photoImageView.setImageBitmap(photoList.get(0));
            }else if(requestCode==SELECT_FILE){
                ClipData clipData = data.getClipData();

                Uri selectedImageUri;
                //for (int i = 0; i < clipData.getItemCount(); i++) {
                selectedImageUri = clipData.getItemAt(0).getUri();
                //}

                photoImageView.setImageURI(selectedImageUri);
                Log.i(TAG, "8");
            }
        }
    }
}