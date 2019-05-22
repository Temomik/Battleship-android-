package team2.shattlebip.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import team2.shattlebip.R;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {
   private Button playWithBot;
   private Button playWithHuman;
   private Button showCreditsButton;
   private Button exitButton;
    public static int isOnline=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        playWithBot = (Button) findViewById(R.id.playBotButton);
        playWithBot.setOnClickListener(this);
        playWithHuman = (Button) findViewById(R.id.playOnlineButton);
        playWithHuman.setOnClickListener(this);
        showCreditsButton = (Button) findViewById(R.id.showCreditsButton);
        showCreditsButton.setOnClickListener(this);
        exitButton = (Button) findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);
    }

    private void playOnlineButtonClick() {
        startActivity(new Intent("team2.shattlebip.Pages.ConnectMenu"));
    }
    private void playWithBotButtonClick() {
        startActivity(new Intent("team2.shattlebip.Pages.MainActivity"));
    }

    private void showCreditsButtonClick() {
        startActivity(new Intent("team2.shattlebip.Pages.Credits"));
    }

    private void exitButtonClick() {
        finishAffinity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playOnlineButton:
                isOnline=1;
                playOnlineButtonClick();
                break;
            case R.id.playBotButton:
                playWithBotButtonClick();
                break;
            case R.id.showCreditsButton:
                showCreditsButtonClick();
                break;
            case R.id.exitButton:
                exitButtonClick();
                break;
        }
    }
}
