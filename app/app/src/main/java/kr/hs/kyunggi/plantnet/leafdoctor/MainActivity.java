package kr.hs.kyunggi.plantnet.leafdoctor;

import static android.icu.text.DateFormat.getDateTimeInstance;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;





public class MainActivity extends AppCompatActivity {


    Button button;
    File photoFile = null;
    String currentPhotoPath = "";
    Uri photoURI = null;
    ActivityResultLauncher<Intent> startCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        //

        button.setOnClickListener(v -> captureImage());


        startCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                uploadImageToServer(photoFile.getAbsolutePath());
            }
        });

        runOnUiThread(() -> {
            TextView textView = findViewById(R.id.textView);
            TextView textView2 =findViewById(R.id.textView2);
            TextView textView4 =findViewById(R.id.textView4);
            //noinspection ConstantValue
            if (BuildConfig.TEAM != null)
                textView.setText(BuildConfig.TEAM);
            //noinspection ConstantValue
            if (BuildConfig.AUTHOR != null)
                textView2.setText(BuildConfig.AUTHOR);
            //noinspection ConstantValue
            if (BuildConfig.DESC != null)
                textView4.setText(BuildConfig.DESC);
        });
    }
    private void captureImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {

            Intent takepictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takepictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                try {

                    photoFile = createImageFile();

                    // Continue only if the File was successfully created
                    if (photoFile.exists()) {
                        photoURI = FileProvider.getUriForFile(this,
                                "kr.hs.kyunggi.plantnet.leafdoctor.fileprovider",
                                photoFile);
                        takepictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        //startActivityForResult(takepictureIntent, CAPTURE_IMAGE_REQUEST);
                        startCamera.launch(takepictureIntent);


                    }
                } catch (IOException ex) {
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp;
        timeStamp = getDateTimeInstance().toString();
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */


        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void uploadImageToServer(String uri) {

        String url = BuildConfig.SERVER;
        Intent j = new Intent(getBaseContext(), SendActivity.class);
        //noinspection ConstantValue
        if (url == null) {
            Toast.makeText(this, "SERVER is Null(local.properties)", Toast.LENGTH_SHORT).show();
            return;
        }
        j.putExtra("URL", url);
        j.putExtra("image", uri);
        startActivity(j);

    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
