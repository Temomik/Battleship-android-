package team2.shattlebip.Pages;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import team2.shattlebip.R;

public class ConnectMenu extends AppCompatActivity implements View.OnClickListener {
    Button createGameButton;
    Button connectGameButton;
    Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_menu);
        createGameButton = (Button) findViewById(R.id.createGame);
        createGameButton.setOnClickListener(this);
        connectGameButton = (Button) findViewById(R.id.connectGame);
        connectGameButton.setOnClickListener(this);
        backButton = (Button) findViewById(R.id.back);
        backButton.setOnClickListener(this);
    }

    private void createGameButtonClick() {
        startActivity(new Intent("team2.shattlebip.Pages.MainActivity"));
    }

    private void connectGameButtonClick() {
    }

    private void backButtonClick() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createGame:
                createGameButtonClick();
                break;
            case R.id.connectGame:
                connectGameButtonClick();
                break;
            case R.id.back:
                backButtonClick();
                break;
        }
    }
}

