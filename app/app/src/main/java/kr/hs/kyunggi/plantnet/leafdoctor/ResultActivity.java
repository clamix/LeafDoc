package kr.hs.kyunggi.plantnet.leafdoctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("CallToPrintStackTrace")
public class ResultActivity extends AppCompatActivity {

    TextView titleTextView;
    TextView causeTextView;
    TextView remediesTextView;
    Button checkAnother;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle ex = getIntent().getExtras();
        String disease;

            if (ex != null) {
                disease = ex.getString("result");
            }
            else
            {
                disease = "";
            }

//        String disease = "Tomato Early Blight";

        titleTextView = findViewById(R.id.diseaseTitleTextView);
        causeTextView = findViewById(R.id.causeTextView);
        remediesTextView = findViewById(R.id.remediesTextView);
        checkAnother = findViewById(R.id.checkAnother);

        checkAnother.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        });

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(getApplicationContext()));
//            System.out.println(obj);
            JSONArray dis = obj.getJSONArray(disease);
            String causeString = dis.getJSONObject(0).getString("Causes");
            String remediesString = dis.getJSONObject(1).getString("Remedies");
            titleTextView.setText(disease);
            causeTextView.setText(causeString);
            remediesTextView.setText(remediesString);
        } catch (JSONException e) {

                    e.printStackTrace();
        }
    }

    public String loadJSONFromAsset(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("remedies.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            int read = is.read(buffer);

            if (read != -1) {
                is.close();
            }

            json = new String(buffer, StandardCharsets.UTF_8);


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}


