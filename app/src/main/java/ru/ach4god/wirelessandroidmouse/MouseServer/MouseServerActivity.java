package ru.ach4god.wirelessandroidmouse.MouseServer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ru.ach4god.wirelessandroidmouse.MainActivity;
import ru.ach4god.wirelessandroidmouse.R;

public class MouseServerActivity extends AppCompatActivity implements MouseServerContract.View {

    private MouseServerPresenter presenter;
    private TextView mStatus, mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse_server);
        presenter = new MouseServerPresenter();
        mStatus = findViewById(R.id.tv_status);
        mAddress = findViewById(R.id.tv_address);
        presenter.attachView(this, getApplicationContext());
        presenter.viewIsReady();
    }

    @Override
    public void showAddress(String address) {
        mAddress.setText(getString(R.string.address) + address);
    }

    @Override
    public void showStatus(String status) {
        mStatus.setText(getString(R.string.status) + status);
    }

    public void stopServer(View view) {
        presenter.stopServer();
        presenter.destroy();
        startActivity(new Intent(this, MainActivity.class));
    }
}
