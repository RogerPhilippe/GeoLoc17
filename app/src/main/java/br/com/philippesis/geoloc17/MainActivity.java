package br.com.philippesis.geoloc17;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;

import br.com.philippesis.geoloc17.data.DatabaseHandler;
import br.com.philippesis.geoloc17.data.models.LocationModel;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends Activity {

    private Button btnLocation;
    private TextView txtLocation;
    private RecyclerView historyList;
    private FusedLocationProviderClient client;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLocation = findViewById(R.id.idBtnGetLocation);

        txtLocation = findViewById(R.id.idTxtLocation);

        historyList = findViewById(R.id.idHistoryList);

        requestPermission();

        client = LocationServices.getFusedLocationProviderClient(this);

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            txtLocation.setText(getString(
                                    R.string.location_label,
                                    String.valueOf(location.getLatitude()),
                                    String.valueOf(location.getLongitude())
                            ));

                            databaseHandler = new DatabaseHandler(MainActivity.this);

                            LocationModel locationModel = new LocationModel(
                                    0,
                                    location.getLatitude(),
                                    location.getLongitude(),
                                    Calendar.getInstance().getTimeInMillis());

                            boolean success = databaseHandler.addLocation(locationModel);

                            String msg = "Erro ao tentar salvar";

                            if (success) msg = "Salvo com sucesso!";

                            setToast(msg, Toast.LENGTH_SHORT);

                        }

                    }
                });

            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {ACCESS_FINE_LOCATION}, 1);
    }

    private void setToast(String msg, int timeShow) {

        Toast.makeText(this, msg,timeShow).show();

    }

}
