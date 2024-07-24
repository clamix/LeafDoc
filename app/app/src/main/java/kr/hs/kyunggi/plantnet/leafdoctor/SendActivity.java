package kr.hs.kyunggi.plantnet.leafdoctor;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SendActivity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        imageView = findViewById(R.id.imageView);
        Bundle ex = getIntent().getExtras();


        String postUrl = Objects.requireNonNull(ex).getString("URL");
        String uri = Objects.requireNonNull(ex).getString("image");


        runOnUiThread(() -> {
            Bitmap bitmap = BitmapFactory.decodeFile(uri);
            bitmap = rotateImage(bitmap, 90);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;


            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            RequestBody postBodyImage = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", "androidFlask.jpg", RequestBody.create(Objects.requireNonNull(byteArray), MediaType.parse("image/*jpg")))
                    .build();
            imageView.setImageBitmap(bitmap);
            postRequest(postUrl, postBodyImage);

        });
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    void postRequest(String postUrl, RequestBody postBody) {

        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build();

        //new OkHttpClient();
        Request request = new Request.Builder()

                .addHeader("content-type", "charset=utf-8")
                //.addHeader("Connection", "close")
                //.addHeader("Accept-Encoding:", "identity")
                //.addHeader("Transfer-Encoding", "chunked")
                .url(postUrl)
                .post(postBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Cancel the post on failure.
                call.cancel();
                runOnUiThread(() ->
                {
                    Toast.makeText(getApplicationContext(), "Failed to Connect to Server", Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    Intent j = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(j);

                });


            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) {
                if (response.isSuccessful()) {

                    // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                    //Toast.makeText(getApplicationContext(), response.body().string(), Toast.LENGTH_SHORT).show();

                    try {
                        String temp;
                        temp = Objects.requireNonNull(response.body()).string();
                        Intent j = new Intent(getBaseContext(), ResultActivity.class);
                        j.putExtra("result", temp);
                        startActivity(j);


                        //                  } catch (ProtocolException e) {
                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), "Server ERROR!!!!", Toast.LENGTH_SHORT).show();
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                            Intent j = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(j);
                        });
                    }


                }
            }
        });
    }
}


