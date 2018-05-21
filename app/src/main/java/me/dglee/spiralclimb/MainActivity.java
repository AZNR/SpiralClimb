package me.dglee.spiralclimb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    }

    public void moveTo(View view) {
        Intent intent;
        String location = view.getTag().toString();

        // changing activities
        switch (location) {
            case "CLIMB":
                intent = new Intent(this, ClimbActivity.class);
                break;
            case "LEADERBOARD":
                intent = new Intent(this, LeaderboardActivity.class);
                break;
            case "OPTIONS":
                intent = new Intent(this, OptionsActivity.class);
                break;
            default: // exit the app
                finish();
                System.exit(0);
                return;
        }

        startActivity(intent);
    }
}
