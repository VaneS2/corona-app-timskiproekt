package com.example.demo.Controllers;

import com.example.demo.models.CoronaEvents;
import com.example.demo.models.Country;
import com.example.demo.models.CountryInfo;
import org.aspectj.bridge.AbortException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;


@Controller
public class Cases {
    HttpURLConnection connection;
    private AbortException LoggerUtil;
    ArrayList<CountryInfo> list=new ArrayList<>();
    ArrayList<Country> allCountries;
    ArrayList<Country> allCountries2;



    @GetMapping("/cases")
    public String showDefault(Model model,HttpServletRequest request) throws IOException, JSONException {

        allCountries =new ArrayList<>();
        allCountries2 =new ArrayList<>();
        String line;
        StringBuffer responseContext = new StringBuffer();
        request.getSession().setAttribute("zemja","MACEDONIA");
        BufferedReader reader;
        try{
            URL url =new URL("https://api.covid19api.com/country/Macedonia" );
            connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int status=connection.getResponseCode();
            if(status>299){
                reader=new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while((line=reader.readLine())!=null){
                    responseContext.append(line);
                }
                reader.close();
            }
            else{
                reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line=reader.readLine())!=null){
                    responseContext.append(line);
                    break;
                }
                reader.close();

            }

            JSONArray data= new JSONArray(responseContext.toString());
            JSONObject previousDay =null;
            for(int i=0;i<data.length();i++) {

                JSONObject day = data.getJSONObject(i);
                String [] de= day.getString("Date").split("T");
                allCountries2.add(new Country(de[0],day.getInt("Confirmed"),day.getInt("Deaths")));
                if(i>0)
                {
                    previousDay =data.getJSONObject(i-1);
                    String [] s= day.getString("Date").split("T");
                    allCountries.add(new Country(s[0], day.getInt("Confirmed")- previousDay.getInt("Confirmed"), day.getInt("Deaths")- previousDay.getInt("Deaths")));
                }else{

                //System.out.println(day.getString("Country") + " " + day.getInt("Confirmed") + " " +day.getInt("Deaths"));
                    String [] s= day.getString("Date").split("T");
                allCountries.add(new Country(s[0], day.getInt("Confirmed"), day.getInt("Deaths")));}
            }


            model.addAttribute("datumm",java.time.LocalDate.now().toString());



        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }


        ////////////////////////////////////////////////////////////////////////////////////////

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
                if(zemja1.equals("MACEDONIA"))
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
                    model.addAttribute("countryInfo",countryInfo);
                    model.addAttribute("pita",day.getInt("cases")+"."+day.getInt("recovered")+"."+day.getInt("active")+"."+day.getInt("deaths"));
                    model.addAttribute("populacijaCases",day.getInt("cases")+"."+day.getInt("population"));


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
        // model.addAttribute("nastani", list);


        model.addAttribute("nastani", allCountries);
        model.addAttribute("vkupno",allCountries2);

        return "cases.html";
    }


    @GetMapping("/cases/country")
    public String showUsingSearch(Model model, HttpServletRequest request) throws IOException, JSONException {
        model.addAttribute("datumm",java.time.LocalDate.now().toString());
        String zemja = request.getParameter("search");
        request.getSession().setAttribute("zemja",zemja);

        allCountries =new ArrayList<>();
        allCountries2 =new ArrayList<>();
        String line;
        StringBuffer responseContext = new StringBuffer();
        BufferedReader reader;

        try{
            URL url =new URL("https://api.covid19api.com/country/"+zemja );
            connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int status=connection.getResponseCode();
            if(status>299){
                reader=new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while((line=reader.readLine())!=null){
                    responseContext.append(line);
                }
                reader.close();
            }
            else{
                reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line=reader.readLine())!=null){
                    responseContext.append(line);
                    break;
                }
                reader.close();

            }

            JSONArray data= new JSONArray(responseContext.toString());
            JSONObject previousDay =null;
            for(int i=0;i<data.length();i++) {


                JSONObject day = data.getJSONObject(i);


                String [] de= day.getString("Date").split("T");
                allCountries2.add(new Country(de[0],day.getInt("Confirmed"),day.getInt("Deaths")));
                if(i>0)
                {
                    previousDay =data.getJSONObject(i-1);
                    String [] s= day.getString("Date").split("T");
                    Country d=new Country(s[0], day.getInt("Confirmed")- previousDay.getInt("Confirmed"), day.getInt("Deaths")- previousDay.getInt("Deaths"));
                    if(previousDay.getInt("Confirmed")<= day.getInt("Confirmed") && previousDay.getInt("Deaths")<= day.getInt("Deaths") ) {
                        allCountries.add(d);
                    }
                }else{

                    //System.out.println(day.getString("Country") + " " + day.getInt("Confirmed") + " " +day.getInt("Deaths"));
                    String [] s= day.getString("Date").split("T");
                    allCountries.add(new Country(s[0], day.getInt("Confirmed"), day.getInt("Deaths")));}
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
        model.addAttribute("nastani", allCountries);
        model.addAttribute("vkupno",allCountries2);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



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
                String zemja2=zemja.toUpperCase(Locale.ROOT);
                if(zemja1.equals(zemja2))
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
                   model.addAttribute("countryInfo",countryInfo);
                   model.addAttribute("pita",day.getInt("cases")+"."+day.getInt("recovered")+"."+day.getInt("active")+"."+day.getInt("deaths"));
                   model.addAttribute("populacijaCases",day.getInt("cases")+"."+day.getInt("population"));


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
       // model.addAttribute("nastani", list);



        return "cases.html";
    }

    @GetMapping("/global")
    public String visual(){
        return "global.html";
    }

}
