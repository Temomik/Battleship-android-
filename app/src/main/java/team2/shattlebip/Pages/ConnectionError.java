package team2.shattlebip.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import team2.shattlebip.R;

public class ConnectionError extends AppCompatActivity implements View.OnClickListener {

    private Button exitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_error);
        exitButton = (Button) findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);
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
            case R.id.showCreditsButton:
                showCreditsButtonClick();
                break;
            case R.id.exitButton:
                exitButtonClick();
                break;
        }
    }
}
