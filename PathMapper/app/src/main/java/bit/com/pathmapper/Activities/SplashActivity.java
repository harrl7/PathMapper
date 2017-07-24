package bit.com.pathmapper.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import bit.com.pathmapper.Utilities.DB_Sync;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(this, "Connecting to Server", Toast.LENGTH_LONG).show();
        DB_Sync backendSync = new DB_Sync(getApplicationContext());

        //Loads and runs the main PathMapper activity
            Intent intent = new Intent(this, PathMapperActivity.class);
            startActivity(intent);
            finish();
    }
}
