package com.jacup101.esportstalk;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewPostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatabaseHelper databaseHelper;

    private Spinner typeSpinner;
    private Button post;
    private Button addImage;
    private Button cancel;

    private ImageView imageView;

    private EditText content;
    private EditText title;

    private EditText videoInput;

    private Uri imageUri = null;

    private String community = "null";
    private String type = "null";

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        imageUri = selectedImage;
                        imageView.setImageURI(selectedImage);

                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = new DatabaseHelper(this);

        setContentView(R.layout.activity_new_post);

        typeSpinner = findViewById(R.id.spinner_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setOnItemSelectedListener(this);

        cancel = findViewById(R.id.button_nPCancel);
        cancel.setOnClickListener(v -> cancel(v));

        addImage = findViewById(R.id.button_nPAddImage);
        addImage.setOnClickListener(v -> addImage(v));

        post = findViewById(R.id.button_nPPost);
        post.setOnClickListener(v -> post(v));

        content = findViewById(R.id.editText_nPContent);
        title = findViewById(R.id.editText_nPTitle);

        videoInput = findViewById(R.id.editText_newPVideo);
        videoInput.setVisibility(View.GONE);


        imageView = findViewById(R.id.imageView_nPUploaded);

        community = getIntent().getStringExtra("community");
    }
    public void addImage(View v) {
        if(!type.contains("image")) {
            Toast.makeText(this,"Please select image type",Toast.LENGTH_SHORT).show();
        } else {
            promptImageSelection();

        }

    }
    private boolean checkEmpty() {
        boolean isEmpty = TextUtils.isEmpty(title.getText()) || TextUtils.isEmpty(content.getText());
        if(isEmpty) {
            Toast.makeText(this, "Missing fields", Toast.LENGTH_SHORT).show();
        }
        return isEmpty;
    }


    public String parseYoutubeLink(String youtubeUrl) {
        String pattern = "https?://(?:[0-9A-Z-]+\\.)?(?:youtu\\.be/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|</a>))[?=&+%\\w]*";
        Log.d("youtube_parse","parsing");
        Pattern compiledPattern = Pattern.compile(pattern,
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(youtubeUrl);
        if (matcher.find()) {
            return matcher.group(1);
        }
        Log.d("youtube_parse","failed to parse");

        return "null";
    }
    public void promptImageSelection() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload photo");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    someActivityResultLauncher.launch(takePicture);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    someActivityResultLauncher.launch(pickPhoto);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        imageUri = selectedImage;
                        imageView.setImageURI(selectedImage);
                    }
                    break;
            }
        }
    }

    public void cancel(View v) {
        Intent intent = new Intent(this, CommunityActivity.class);
        intent.putExtra("community",community);
        startActivity(intent);
    }
    public void post(View v) {
        if(checkEmpty()) {
            return;
        }

        if(type.equals("text")) {
            databaseHelper.addPost(title.getText().toString(),"Jacup101",type,content.getText().toString(),community, null, null);
        } if(type.equals("image")) {
            if(imageUri != null) {
                databaseHelper.addPost(title.getText().toString(),"Jacup101",type,content.getText().toString(),community, imageUri, null);
            } else {
                type = "text";
                databaseHelper.addPost(title.getText().toString(),"Jacup101",type,content.getText().toString(),community, null, null);
            }
        } if(type.equals("video")) {
            String parsed = parseYoutubeLink(videoInput.getText().toString());
            Log.d("youtube_parse","Parsed: " + parsed);
            if(!parsed.equals("null")) {
                databaseHelper.addPost(title.getText().toString(),"Jacup101",type,content.getText().toString(), community,null, parsed);
            } else {
                databaseHelper.addPost(title.getText().toString(),"Jacup101",type,content.getText().toString(),community, null, null);
            }
        }
        Intent intent = new Intent(this, CommunityActivity.class);
        intent.putExtra("community",community);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type = (String) parent.getItemAtPosition(position);
        if(type.equals("video")) {
            videoInput.setVisibility(View.VISIBLE);
        } else {
            videoInput.setVisibility(View.GONE);
        } if(!type.equals("image")) {
            imageView.setImageBitmap(null);
        } else {
            if(imageUri != null) imageView.setImageURI(imageUri);
        }

        //Log.d("type_select",type);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }


}
