package com.example.demo.Controllers;

import com.example.demo.models.CountryInfo;
import com.example.demo.models.Vaccines;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

@RestController
public class RestApi {


    HttpURLConnection connection;
    CountryInfo izlez=null;
    HttpURLConnection connection2;
    Vaccines izlez2=null;

    @GetMapping("/restapi")
    public CountryInfo findAll(@RequestParam String zemja)
    {

        String line2;
        StringBuffer responseContext2 = new StringBuffer();
        BufferedReader reader2;




        try{
            URL url =new URL("https://corona.lmao.ninja/v2/countries");
            connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int status=connection.getResponseCode();
            if(status>299){
                reader2=new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while((line2=reader2.readLine())!=null){
                    responseContext2.append(line2);
                }
                reader2.close();
            }
            else{
                reader2=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line2=reader2.readLine())!=null){
                    responseContext2.append(line2);
                    break;
                }
                reader2.close();

            }

            JSONArray data= new JSONArray(responseContext2.toString());
            JSONObject previousDay =null;
            for(int i=0;i<data.length();i++) {

                JSONObject day = data.getJSONObject(i);
                String zemja1=day.getString("country").toUpperCase(Locale.ROOT);
                if(zemja1.equals(zemja))
                {
                    String img=day.getJSONObject("countryInfo").getString("flag");

                    CountryInfo countryInfo= new CountryInfo(
                            day.getString("country"),
                            img,
                            day.getInt("cases"),
                            day.getInt("todayCases"),
                            day.getInt("deaths"),
                            day.getInt("todayDeaths"),
                            day.getInt("recovered"),
                            day.getInt("todayRecovered"),
                            day.getInt("active"),
                            day.getInt("population"),
                            day.getInt("tests")



                    );

                    izlez=countryInfo;

                }

            }




        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return izlez;
    }

    @GetMapping("/vaccines")
    public Vaccines vacciness()
    {
//ss
        String line2;
        StringBuffer responseContext2 = new StringBuffer();
        BufferedReader reader2;




        try{
            URL url =new URL("https://disease.sh/v3/covid-19/all");
            connection2=(HttpURLConnection)url.openConnection();
            connection2.setRequestMethod("GET");
            connection2.setConnectTimeout(5000);
            connection2.setReadTimeout(5000);
            int status=connection2.getResponseCode();
            if(status>299){
                reader2=new BufferedReader(new InputStreamReader(connection2.getErrorStream()));
                while((line2=reader2.readLine())!=null){
                    responseContext2.append(line2);
                }
                reader2.close();
            }
            else{
                reader2=new BufferedReader(new InputStreamReader(connection2.getInputStream()));
                while((line2=reader2.readLine())!=null){
                    responseContext2.append(line2);
                    break;
                }
                reader2.close();

            }

            JSONObject global = new JSONObject(responseContext2.toString());


            izlez2= new Vaccines(global.getInt("updated"),global.getInt("cases"),global.getInt("todayCases"),global.getInt("deaths"),global.getInt("todayDeaths")
            ,global.getInt("recovered"),global.getInt("todayRecovered"),global.getInt("active"),global.getInt("critical"),global.getInt("casesPerOneMillion"),
                    global.getInt("deathsPerOneMillion"),global.getInt("tests"));




        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            connection2.disconnect();
        }
        return izlez2;
    }
}
