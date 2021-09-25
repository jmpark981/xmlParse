package com.example.xmlparse;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    String str="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.text);
        new ProcessInBackGround().execute();
    }
    public InputStream getInputStream(URL url){
        try{
            return url.openConnection().getInputStream();
        }catch (IOException e){
            return null;
        }
    }
    public class ProcessInBackGround extends AsyncTask<Integer,Void,Exception> {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        Exception exception = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Wait..");
            progressDialog.show();
        }

        @Override
        protected Exception doInBackground(Integer... integers) {
            try{
                //URL url = new URL(urlString);
                //URL url = new URL("https://run.mocky.io/v3/332b92df-4887-4e1a-9b52-e972570d981a");
                URL url = new URL("http://apis.data.go.kr/B553748/CertImgListService/getCertImgListService?serviceKey=NHJqDcrDUsR060JebFVb%2BWBkkjatoYeBcNogtgYK8KrG6jb5p8WHCyT9lCCFruMS2WenwUFO%2FqdDu0uGp%2BvmMw%3D%3D&prdlstNm=%EB%8F%88%EA%B9%8C%EC%8A%A4&returnType=xml&pageNo=1&numOfRows=10");
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(getInputStream(url), "UTF_8");
                boolean insideItem=false;
                int eventType=xpp.getEventType();
                while(eventType!=XmlPullParser.END_DOCUMENT){
                    if(eventType==XmlPullParser.START_TAG){
                        if(xpp.getName().equalsIgnoreCase("item")){
                            insideItem=true;
                        }
                        else if(xpp.getName().equalsIgnoreCase("allergy")){
                            if(insideItem){
                                str+=(xpp.nextText());
                            }
                        }
                        /*
                        else if(xpp.getName().equalsIgnoreCase("description")){
                            if(insideItem){
                                descriptions.add(xpp.nextText());
                            }
                        }*/
                    }
                    else if (eventType==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
                        insideItem=false;
                    }
                    eventType=xpp.next();
                }
            }catch(MalformedURLException e){
                exception=e;
            }catch(XmlPullParserException e){
                exception=e;
            }catch (IOException e) {
                return null;
            }

            return exception;
        }

        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);
            textView.setText(str);
            /*
            for(int i=0;i<titles.size();i++){
                news=new News(titles.get(i).toString(), descriptions.get(i).toString());
                newsList.add(news);
            }
            NewsAdapter newsAdapter = new NewsAdapter(MainActivity.this,R.layout.adapter_list, newsList);
            listView.setAdapter(newsAdapter);*/
            progressDialog.dismiss();
        }
    }
}