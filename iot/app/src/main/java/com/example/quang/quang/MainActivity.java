package com.example.quang.quang;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.quang.quang.MVVM.VM.NPNHomeViewModel;
import com.example.quang.quang.MVVM.View.NPNHomeView;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */

public class MainActivity extends Activity implements NPNHomeView {

    float temperature = 0;
    float humidity = 0;


    float Tcond = 0;
    float Hcond = 0;


    TextView text;
    TextView text2;
    TextView text3;

    NPNHomeViewModel mHomeViewModel;

    String url="http://192.168.0.109/";
    //String url1="http://172.16.21.1/";
    String url3="https://iot-assignment.herokuapp.com/mcu/123?fbclid=IwAR3trLWJXjTz5Kc9cb1RZ4hi6usf_thX_0klx4hLa9d0KH6icjPcIZVBecc";

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int INTERVAL_BETWEEN_BLINKS_MS = 5000;

    private Handler mHandler = new Handler();
    private Gpio mLedGpio;
    private boolean mLedState = false;
    private static String pin1 = "BCM6";

    int a= 24;
    int b= 35;

    //String url = getResources().getString(R.string."http://172.16.1.131/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView)findViewById(R.id.text);
        text2 = (TextView)findViewById(R.id.text2);
        text3 = (TextView)findViewById(R.id.text3);

        mHomeViewModel = new NPNHomeViewModel();
        mHomeViewModel.attach(this, this);

        Log.i(TAG, "Starting BlinkActivity");

        PeripheralManager manager = PeripheralManager.getInstance();
        try {
            mLedGpio = manager.openGpio(pin1);
            mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            Log.i(TAG, "Start blinking LED GPIO pin");
            // Post a Runnable that continuously switch the state of the GPIO, blinking the
            // corresponding LED
            mHandler.post(mBlinkRunnable);
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove pending blink Runnable from the handler.
        mHandler.removeCallbacks(mBlinkRunnable);
        // Close the Gpio pin.
        Log.i(TAG, "Closing LED GPIO pin");
        try {
            mLedGpio.close();
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        } finally {
            mLedGpio = null;
        }
    }

    public void condition(){ StringRequest stringRequest = new StringRequest(Request.Method.GET, url3, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
//                text.setText(response);
            //System.out.println("RESPONSE: " + response);
            String[] parts = response.split(",\"temp_condition\":");
            String[] temp_cond = parts[1].split(",\"humi_condition\":");
            //System.out.println("1111111111111 " + temp_cond[0]);
            String[] humid_cond = temp_cond[1].split(",\"timestamp\":");
            //System.out.println("2222222222222 " + humid_cond[0]);
            //System.out.println("3333333333333 " + humid_cond[1]);
            try {
                Tcond = Float.parseFloat(temp_cond[0]);
                Hcond = Float.parseFloat(humid_cond[0]);
            } catch(NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }

            //System.out.println("ddddddddd " + parts[1]);

            System.out.println("CONDITION VALUEEEEEEE " + Tcond + " and " + Hcond);
            if(temperature > Tcond || humidity >Hcond){
                System.out.println("WATER IS NEED");
            }else {
                System.out.println("FINEEEEEEEEE");
            }

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
        }
    }
    );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void request() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                text.setText(response);
                String[] parts = response.split("meow=\"nhiet-do\">");
                String[] temp = parts[1].split("</div><div class=\"col-md-3\">Do am: </div><div class=\"col-md-3\">");
               // System.out.println("ccccccccc: " + temp[0]);
                text.setText(temp[0]);


                String[] humid = temp[1].split("</div></div>");
               // System.out.println("eeeeeeeee: " + humid[0]);
                text2.setText(humid[0]);

                try {
                    temperature = Float.parseFloat(temp[0]);
                    humidity = Float.parseFloat(humid[0]);
                } catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }

                System.out.println("FLOAT VALUEEEEEEE " + temperature + " and " + humidity);


                //up data to server
                String urlSalinity = "";
                urlSalinity = "https://iot-assignment.herokuapp.com/send?temporary=" + temp[0] + "&humidity=" + humid[0] + "&mcu=123";
               // Log.d(TAG, "VALUEEEEEEEEEEE: " + urlSalinity);
                mHomeViewModel.updateToServer(urlSalinity);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*public void request1() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                text.setText(response);
                String[] parts = response.split("meow=\"nhiet-do\">");
                String[] temp = parts[1].split("</div></div>");
                System.out.println("ccccccccc: " + temp[0]);
                text3.setText(temp[0]);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/


    private Runnable mBlinkRunnable = new Runnable() {
        @Override
        public void run() {
            // Exit Runnable if the GPIO is already closed
            if (mLedGpio == null) {
                return;
            }
            try {

                // Toggle the GPIO state
                mLedState = !mLedState;
                mLedGpio.setValue(mLedState);
               // Log.d(TAG, "State set to " + mLedState);
                request();
                condition();
                //request1();
                // Reschedule the same runnable in {#INTERVAL_BETWEEN_BLINKS_MS} milliseconds
                mHandler.postDelayed(mBlinkRunnable, INTERVAL_BETWEEN_BLINKS_MS);


            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
    };

    @Override
    public void onSuccessUpdateServer(String message) {
        Log.d(TAG, "Request server is successful " + message);
    }

    @Override
    public void onErrorUpdateServer(String message) {
        Log.d(TAG, "Request server is fail");
    }
}




/*
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends Activity {
    private static final String FILENAME = "ip.txt";
    static TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView)findViewById(R.id.text);


    }

    public static void main(String[] args) {
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            for(int i=0; i < 256; i++){
                String content = "172.16.18." + Integer.toString(i) + "\n";

                fw = new FileWriter(FILENAME);
                bw = new BufferedWriter(fw);
                bw.write(content);
                System.out.println("Xong");

                text.setText(content);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

*/