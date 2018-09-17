package com.example.filip.weatherfinal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView upisiIme;

    Button gumbDaj;
    Button gumbReset;
    TextView result;
    EditText cityName;
    DownloadTask task;

    int randomBroj;

    ImageView filip;
    ImageView anja;

    String city;
    JSONObject main;
    JSONObject vjetar;
    JSONObject sys;

    JSONObject mainForecast;

    String proba;

    Date dateObject;
    String sunrise;
    String sunset;
    long sunriseInt;
    long sunsetInt;

    JSONObject jsonObjectForecast = null;
    JSONObject jsonObject = null;

    List<Forecaast> forcastList;

    List<String> delaloBu;
    String delaloBuString;

    String vjetarBrzina;
    String temp;
    String tempMax;
    String tempMin;
    String desc;
    String list;
    String ime;
    String drzava;

    public class DownloadTask extends AsyncTask<String, Void, StringBuffer> {


        @Override
        protected StringBuffer doInBackground(String... urls) {


            StringBuffer buffer = new StringBuffer();

            try {

                URL url = new URL(urls[0]);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.connect();
                int resCode = con.getResponseCode();

                if (resCode == HttpURLConnection.HTTP_OK) {

                    InputStream is = con.getInputStream();

                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = br.readLine()) != null) {
                        buffer.append(line);

                    }

                    is.close();
                    br.close();
                    con.disconnect();
                }


            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    //errorResponse = "Check your internet connection";
                }
            }
            return buffer;

        }

        @Override
        protected void onPostExecute(StringBuffer html) {

            super.onPostExecute(html);


            try {


                if (jsonObjectForecast == null) {
                    jsonObjectForecast = new JSONObject(html.toString());
                } else {
                    jsonObject = new JSONObject(html.toString());
                }


                Log.i("glavni", html.toString());
                if (jsonObject == null) {

                    Log.i("bagreh", "harhhahrh");
                } else {
                    JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                    JSONArray jsonArrayList = jsonObjectForecast.getJSONArray("list");


                    List<JSONObject> listOfObjects = new ArrayList<>();

                    for (int i = 0; i < jsonArrayList.length(); i++) {
                        mainForecast = jsonArrayList.getJSONObject(i);

                        listOfObjects.add(mainForecast);

                    }
                    forcastList = new ArrayList<>();
                    for (JSONObject prognoza : listOfObjects
                            ) {
                        JSONObject mainObject = prognoza.getJSONObject("main");
                        JSONArray jsonArrayListWeather = prognoza.getJSONArray("weather");

                        Forecaast forecaast = new Forecaast();
                        forecaast.setTemp(mainObject.getString("temp"));
                        forecaast.setTempMax(mainObject.getString("temp_min"));
                        forecaast.setTempMin(mainObject.getString("temp_max"));
                        forecaast.setDtTxt(prognoza.getString("dt_txt"));
                        for (int i = 0; i < jsonArrayListWeather.length(); i++) {
                            forecaast.setDescriptionFor(jsonArrayListWeather.getJSONObject(i).getString("description"));

                        }

                        forcastList.add(forecaast);




                        }
                    Log.i("testttt", forcastList.get(1).getDescriptionFor());


                    delaloBu = new ArrayList<>();
                    for (int i =0; i<forcastList.size();i++){

                        delaloBu.add(forcastList.get(i).getDt_txt()+"\n"+"Srednja temperatura: "+forcastList.get(i).getTemp()+"°C"+
                                "\n"+"Opis: "+forcastList.get(i).getDescriptionFor()+"\n\n");
               //         delaloBu.add("\n");
                   //     delaloBu.add(forcastList.get(i).getTemp());
                   //     delaloBu.add("\n");
                  //      delaloBu.add(forcastList.get(i).getDescriptionFor());


                    }

                    //noinspection MalformedRegex
                    delaloBuString = delaloBu.toString().replaceAll(",","").replaceAll("]","").replaceAll("\\[","");



                    proba = "\n"+forcastList.get(1).getDescriptionFor();


                    Log.i("delalobu",delaloBu.toString());




                    vjetar = jsonObject.getJSONObject("wind");
                    main = jsonObject.getJSONObject("main");
                    sys = jsonObject.getJSONObject("sys");


                    ime = jsonObject.getString("name");
                    vjetarBrzina = vjetar.getString("speed");
                    temp = main.getString("temp");
                    tempMin = main.getString("temp_min");
                    tempMax = main.getString("temp_max");
                    drzava = sys.getString("country");
                    sunrise = sys.getString("sunrise");
                    sunset = sys.getString("sunset");


                    sunriseInt = Integer.parseInt(sunrise) * 1000;
                    sunsetInt = Integer.parseInt(sunset) * 100;


                    Log.i("brzina", vjetarBrzina);

                    for (int i = 0; i < jsonArrayWeather.length(); i++) {

                        desc = ((JSONObject) jsonArrayWeather.get(i)).get("description").toString();

                        Log.i("description2", desc);


                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (html.length() > 0) {
                print(html);
            }

        }


    }


    @SuppressLint("SetTextI18n")
    private void print(StringBuffer html) {


        result.setText("Država: " + drzava + "\n" + "Grad: " + ime + "\n" + "Brzina vjetra: " + vjetarBrzina + "m/s" + "\n" +
                        "Temperatura: " + temp + "°C" + "\n" +
                        "Maksimalna temperatura: " + tempMax + "°C" + "\n" +
                        "Minimalna temperatura: " + tempMin + "°C" + "\n" +
                        "Opis vremena: " + desc+

                      "\n\n" + "PROGNOZA ZA UBUDUĆE" + "\n\n"+ delaloBuString
                //datum


                //  "Sunrise: " + sunriseDate() +  "\n" +
                // "Sunset:"  + sunsetDate() +  "\n" +
        );


        Log.i("proba", html.toString());




    }


    public void weather(View view) {


        anja.animate().rotation(520f).translationXBy(-1700f).setDuration(2800);
        filip.animate().rotation(-520f).translationXBy(1700f).setDuration(2800);
        cityName.setVisibility(View.INVISIBLE);


        city = cityName.getText().toString().replaceAll("č","c").replaceAll("ć","c").replaceAll("ž","z");
        Log.i("grad=", city);
        cityName.getText().clear();
        InputMethodManager mng = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mng.hideSoftInputFromWindow(cityName.getWindowToken(), 0);
        List<String> ulrsList = new ArrayList<>();
        ulrsList.add("http://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=8dc646b47c4013059004c801afe6f31d&units=metric");
        ulrsList.add("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=8dc646b47c4013059004c801afe6f31d&units=metric");


        for (String url : ulrsList
                ) {
            startThread(url);
        }




    }


    public String sunriseDate() {
        dateObject = new Date(sunriseInt);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    public String sunsetDate() {
        dateObject = new Date(sunsetInt);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");

        return timeFormat.format(dateObject);
    }


    public void restart(View view) {


        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }


    public void aGumb(View view) {

        Random randomGenerator = new Random();
        randomBroj = randomGenerator.nextInt(6);
        int randomDuration = randomGenerator.nextInt(800) + 2000;
        float randomRotation = randomGenerator.nextInt(1100) - 700;
        float distance = randomGenerator.nextInt(1200) - 700;

        double direction = Math.random() * 2 * Math.PI;
        int translationX = (int) (Math.cos(direction) * distance);
        int translationY = (int) (Math.sin(direction) * distance);


        if (randomBroj == 0) {

            int randomSlika = randomGenerator.nextInt(2);
            if (randomSlika == 0) {

                anja.setBackgroundResource(R.drawable.anja1);
            } else {

                anja.setBackgroundResource(R.drawable.anja2);
            }
            anja.animate().setDuration(randomDuration).rotation(randomRotation).start();
        } else if (randomBroj == 1) {
            int randomSlika = randomGenerator.nextInt(2);
            if (randomSlika == 0) {

                anja.setBackgroundResource(R.drawable.anja1);
            } else {

                anja.setBackgroundResource(R.drawable.anja2);
            }
            anja.animate().setDuration(randomDuration).scaleX(0.5f).scaleY(0.5f).start();
        } else if (randomBroj == 2) {
            int randomSlika = randomGenerator.nextInt(2);
            if (randomSlika == 0) {

                anja.setBackgroundResource(R.drawable.anja1);
            } else {

                anja.setBackgroundResource(R.drawable.anja2);
            }

            anja.animate().setDuration(randomDuration).scaleX(1.2f).scaleY(1.2f).start();
        } else if (randomBroj == 3) {
            int randomSlika = randomGenerator.nextInt(2);
            if (randomSlika == 0) {

                anja.setBackgroundResource(R.drawable.anja1);
            } else {

                anja.setBackgroundResource(R.drawable.anja2);
            }
            anja.animate().translationX(translationX).translationY(translationY).setDuration(randomDuration).start();

        } else if (randomBroj == 4) {
            int randomSlika = randomGenerator.nextInt(2);
            if (randomSlika == 0) {

                anja.setBackgroundResource(R.drawable.anja1);
            } else {

                anja.setBackgroundResource(R.drawable.anja2);
            }
            anja.animate().translationX(translationX).translationY(translationY).setDuration(randomDuration).rotation(randomRotation).start();

        } else if (randomBroj == 5) {
            int randomSlika = randomGenerator.nextInt(2);
            if (randomSlika == 0) {

                anja.setBackgroundResource(R.drawable.anja1);
            } else {

                anja.setBackgroundResource(R.drawable.anja2);
            }
            anja.animate().translationY(translationY).setDuration(randomDuration).rotation(randomRotation).start();

        }
    }


    public void fGumb(View view) {

        {

            Random randomGenerator = new Random();
            randomBroj = randomGenerator.nextInt(6);
            int randomDuration = randomGenerator.nextInt(800) + 2000;
            float randomRotation = randomGenerator.nextInt(400) + 1900;
            float distance = randomGenerator.nextInt(1200) - 700;

            double direction = Math.random() * 2 * Math.PI;
            int translationX = (int) (Math.cos(direction) * distance);
            int translationY = (int) (Math.sin(direction) * distance);


            if (randomBroj == 0) {

                int randomSlika = randomGenerator.nextInt(2);
                if (randomSlika == 0) {

                    filip.setBackgroundResource(R.drawable.filip1);
                } else {

                    filip.setBackgroundResource(R.drawable.filip2);
                }
                filip.animate().setDuration(randomDuration).rotation(randomRotation).start();
            } else if (randomBroj == 1) {
                int randomSlika = randomGenerator.nextInt(2);
                if (randomSlika == 0) {

                    filip.setBackgroundResource(R.drawable.filip1);
                } else {

                    filip.setBackgroundResource(R.drawable.filip2);
                }
                filip.animate().setDuration(randomDuration).scaleX(0.5f).scaleY(0.5f).start();
            } else if (randomBroj == 2) {
                int randomSlika = randomGenerator.nextInt(2);
                if (randomSlika == 0) {

                    filip.setBackgroundResource(R.drawable.filip1);
                } else {

                    filip.setBackgroundResource(R.drawable.filip2);
                }

                filip.animate().setDuration(randomDuration).scaleX(1.2f).scaleY(1.2f).start();
            } else if (randomBroj == 3) {
                int randomSlika = randomGenerator.nextInt(2);
                if (randomSlika == 0) {

                    filip.setBackgroundResource(R.drawable.filip1);
                } else {

                    filip.setBackgroundResource(R.drawable.filip2);
                }
                filip.animate().translationX(translationX).translationY(translationY).setDuration(randomDuration).start();

            } else if (randomBroj == 4) {
                int randomSlika = randomGenerator.nextInt(2);
                if (randomSlika == 0) {

                    filip.setBackgroundResource(R.drawable.filip1);
                } else {

                    filip.setBackgroundResource(R.drawable.filip2);
                }
                filip.animate().translationX(translationX).translationY(translationY).setDuration(randomDuration).rotation(randomRotation).start();

            } else if (randomBroj == 5) {
                int randomSlika = randomGenerator.nextInt(2);
                if (randomSlika == 0) {

                    filip.setBackgroundResource(R.drawable.filip1);
                } else {

                    filip.setBackgroundResource(R.drawable.filip2);
                }
                filip.animate().translationY(translationY).setDuration(randomDuration).rotation(randomRotation).start();

            }
        }


    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        upisiIme = (TextView) findViewById(R.id.upisiIme);
        gumbDaj = (Button) findViewById(R.id.button);
        gumbReset = (Button) findViewById(R.id.button2);
        result = (TextView) findViewById(R.id.result);
        cityName = (EditText) findViewById(R.id.editText);

        filip = (ImageView) findViewById(R.id.filip);
        anja = (ImageView) findViewById(R.id.anja);
    }

    private void startThread(String url) {
        task = new DownloadTask();
        try {
            task.execute(url);
            gumbReset.setVisibility(View.VISIBLE);
            gumbDaj.setVisibility(View.INVISIBLE);
            upisiIme.setVisibility(View.INVISIBLE);
        } catch (Exception e) {


            Context context = getApplicationContext();
            CharSequence text = "Zbugggano tralalalalaa!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            e.printStackTrace();
        }
    }


}
