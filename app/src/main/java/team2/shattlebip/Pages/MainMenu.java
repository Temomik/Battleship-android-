package team2.shattlebip.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import team2.shattlebip.R;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {
   private Button startGameButton;
   private Button showCreditsButton;
   private Button exitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        startGameButton = (Button) findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(this);
        showCreditsButton = (Button) findViewById(R.id.showCreditsButton);
        showCreditsButton.setOnClickListener(this);
        exitButton = (Button) findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);
    }

    private void startGameButtonClick() {
        startActivity(new Intent("team2.shattlebip.Pages.ConnectMenu"));
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
            case R.id.startGameButton:
                startGameButtonClick();
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
