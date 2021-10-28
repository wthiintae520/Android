package algonquin.cst2335.yin00043;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {
    TextView textViewTitle = findViewById(R.id.textView);
    Button callNumberButton = findViewById(R.id.buttonCallNumber);
    Button changePictureButton = findViewById(R.id.buttonChangePicture);
    ImageView profileImage = findViewById(R.id.imageProfile);
    EditText phoneNumberEditText = findViewById(R.id.editTextPhone);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        textViewTitle.setText("Welcome back " + emailAddress);

        callNumberButton.setOnClickListener(clk-> {
            Intent call = new Intent(Intent.ACTION_DIAL);
            String phoneNumber = phoneNumberEditText.getText().toString();
            call.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(call);
        } );

        File file = new File( getFilesDir(), "Picture.png");
        if(file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile("Picture.png");
            profileImage.setImageBitmap(theImage);
        }

        changePictureButton.setOnClickListener(clk-> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == Activity.RESULT_OK) {
//                            Intent data = result.getData();
//                            Bitmap thumbnail = data.getParcelableExtra("data");
//                            profileImage.setImageBitmap(thumbnail);
//                        }
//                    }
//                } );
//            cameraResult.launch(cameraIntent);
            final int REQUEST_IMAGE_CAPTURE = 1;
            try {
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            } catch (ActivityNotFoundException e) {
                // display error state to the user
            }

            int requestCode = 1;
            int resultCode = RESULT_OK;

            onActivityResult(requestCode, resultCode, cameraIntent); {
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    Bundle extras = cameraIntent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("cameraIntent");
                    profileImage.setImageBitmap(imageBitmap);
                    FileOutputStream fOut = null;
                    try {
                        fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            startActivity(cameraIntent);
        } );
        SharedPreferences prefs = getSharedPreferences("MyPhone", Context.MODE_PRIVATE);
        String defaultValue = "VariableName";
        prefs.getString("VariableName", "");
        String emailAddress1 = prefs.getString("LoginName", "");
        phoneNumberEditText.setText(emailAddress1);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("StringName", "");
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("MyPhone", Context.MODE_PRIVATE);
        String defaultValue = "VariableName";
        prefs.getString("VariableName", "");
        String emailAddress = prefs.getString("LoginName", "");
        phoneNumberEditText.setText(emailAddress);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("StringName", "");
        editor.apply();
    }
}