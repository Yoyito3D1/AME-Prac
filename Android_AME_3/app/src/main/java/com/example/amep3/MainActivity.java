package com.example.amep3;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private EditText etCity;
    private TextView tvResult;
    private ImageView iconWeather;
    private TextView tvTemperature;
    private TextView tvLight;

    private String url = "https://api.openweathermap.org/data/2.5/weather";
    private String appid = "9ef42bd2451859af3c312f7df7f2ea24";
    private DecimalFormat df = new DecimalFormat("#.##");

    // Bluetooth variables
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;

    private static final int MESSAGE_READ = 1;
    private static final int MY_PERMISSIONS_REQUEST_BLUETOOTH_CONNECT = 1;

    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == MESSAGE_READ) {
                String data = (String) msg.obj;
                updateUIWithData(data);
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCity = findViewById(R.id.etCity);
        tvResult = findViewById(R.id.tvResult);
        iconWeather = findViewById(R.id.iconWeather);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvLight = findViewById(R.id.tvLight);

        Button btnGetWeather = findViewById(R.id.btnGetWeather);
        btnGetWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherDetails(v);
            }
        });

        // Initialize Bluetooth
        setupBluetooth();
        // Start reading Bluetooth data
        readBluetoothData();
    }

    private void setupBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Handle the case where the device doesn't support Bluetooth
            return;
        }

        // Call connectBluetooth when a device is selected (e.g., in a button click listener)
        BluetoothDevice selectedDevice = findBluetoothDevice("HMSoft"); // Change to your device name
        if (selectedDevice != null) {
            connectBluetooth(selectedDevice);
        }
    }

    private BluetoothDevice findBluetoothDevice(String deviceName) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, MY_PERMISSIONS_REQUEST_BLUETOOTH_CONNECT);
            return null;
        }

        for (BluetoothDevice pairedDevice : bluetoothAdapter.getBondedDevices()) {
            if (pairedDevice.getName().equals(deviceName)) {
                return pairedDevice;
            }
        }
        return null;
    }

    private void connectBluetooth(BluetoothDevice device) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, MY_PERMISSIONS_REQUEST_BLUETOOTH_CONNECT);
            return;
        }

        try {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            inputStream = bluetoothSocket.getInputStream();

            String deviceAddress = device.getAddress();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getWeatherDetails(View view) {
        String tempUrl = "";
        String city = etCity.getText().toString().trim();

        if (city.equals("")) {
            tvResult.setText("City field cannot be empty!");
        } else {
            tempUrl = url + "?q=" + city + "&appid=" + appid;

            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            processWeatherResponse(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            tvResult.setText("Error fetching weather data");
                        }
                    });

            queue.add(stringRequest);
        }
    }

    private void processWeatherResponse(String response) {
        String output = "";
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonArray = jsonResponse.getJSONArray("weather");
            JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
            String description = jsonObjectWeather.getString("description");
            JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
            double temp = jsonObjectMain.getDouble("temp") - 273.15;
            double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
            float pressure = jsonObjectMain.getInt("pressure");
            int humidity = jsonObjectMain.getInt("humidity");
            JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
            String wind = jsonObjectWind.getString("speed");
            JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
            String clouds = jsonObjectClouds.getString("all");
            JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
            String countryName = jsonObjectSys.getString("country");
            String cityName = jsonResponse.getString("name");

            setWeatherIcon(description.toLowerCase());

            output += "Current weather of " + cityName + " (" + countryName + ")"
                    + "\n Temp: " + df.format(temp) + " °C"
                    + "\n Feels Like: " + df.format(feelsLike) + " °C"
                    + "\n Humidity: " + humidity + "%"
                    + "\n Description: " + description
                    + "\n Wind Speed: " + wind + "m/s (meters per second)"
                    + "\n Cloudiness: " + clouds + "%"
                    + "\n Pressure: " + pressure + " hPa";

            tvResult.setTextColor(Color.rgb(0, 0, 0));
            tvResult.setText(output);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setWeatherIcon(String weatherCondition) {
        if (weatherCondition.contains("cloud")) {
            iconWeather.setImageResource(R.drawable.ic_nubes);
        } else if (weatherCondition.contains("sun") || weatherCondition.contains("clear")) {
            iconWeather.setImageResource(R.drawable.ic_sun);
        } else if (weatherCondition.contains("fog") || weatherCondition.contains("mist")) {
            iconWeather.setImageResource(R.drawable.ic_niebla);
        } else if (weatherCondition.contains("snow")) {
            iconWeather.setImageResource(R.drawable.ic_nieve);
        } else if (weatherCondition.contains("rain")) {
            iconWeather.setImageResource(R.drawable.ic_rain);
        } else if (weatherCondition.contains("wind")) {
            iconWeather.setImageResource(R.drawable.ic_wind);
        }
    }

    private void readBluetoothData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[1024];
                int bytes;

                while (true) {
                    try {
                        bytes = inputStream.read(buffer);
                        String data = new String(buffer, 0, bytes);
                        Message message = handler.obtainMessage(MESSAGE_READ, data);
                        handler.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }).start();
    }

    private void updateUIWithData(String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // Parsea los datos de temperatura y luz desde la cadena recibida
                String[] dataParts = data.split(",");
                if (dataParts.length >= 2) {
                    String temperaturaString = dataParts[1];
                    String luzString = dataParts[0];

                    try {
                        // Convierte las cadenas a valores numéricos
                        float temperatura = Float.parseFloat(temperaturaString);
                        float luz = Float.parseFloat(luzString);

                        // Actualiza TextViews con los datos de temperatura y luz
                        tvTemperature.setText("Temperature: " + temperatura + " °C");
                        tvLight.setText("Light: " + luz);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void closeBluetoothConnection() {
        try {
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeBluetoothConnection();
    }
}
