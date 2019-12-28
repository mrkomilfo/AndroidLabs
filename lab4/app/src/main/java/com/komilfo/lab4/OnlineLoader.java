package com.komilfo.lab4;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class OnlineLoader extends AsyncTask<Void, Void, Object> {
    private String urlString;
    @SuppressLint("StaticFieldLeak")
    private NetworkActivity context;

    OnlineLoader(NetworkActivity context, String urlString){
        this.context = context;
        this.urlString = urlString;
    }

    @Override
    protected Object doInBackground(Void... ignored) {
        try{
            return download(urlString);
        }
        catch (IOException e){
            return e;
        }
    }

    @Override
    protected void onPostExecute(Object data) {
        super.onPostExecute(data);
        if(data instanceof IOException)
            context.onError((IOException) data);
        else {
            new Parser(context, (InputStream) data).execute();
        }
    }

    private static InputStream download(String urlString)
            throws IOException{
        HttpURLConnection connection = connect(urlString);
        int responseCode = connection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK){
            return new BufferedInputStream(connection.getInputStream());
        }
        throw new ConnectException(connection.getResponseMessage());
    }

    private static HttpURLConnection connect(String urlString)
            throws IOException {
        URL url=new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setDoInput(true);

        return connection;
    }
}
