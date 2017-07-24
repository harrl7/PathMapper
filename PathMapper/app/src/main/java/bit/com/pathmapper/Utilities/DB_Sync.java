package bit.com.pathmapper.Utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import bit.com.pathmapper.Models.Collection;
import bit.com.pathmapper.Models.PointOfInterest;

/**
 * Created by jacksct1 on 18/10/2016.
 */

public class DB_Sync
{
    protected Context context;

    public DB_Sync(Context context)
    {
        this.context = context;

        WebService APIThread = new WebService(context);
        try {
            String st = APIThread.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    class WebService extends AsyncTask<Void,Void,String>
    {
        private Context mContext;

        public WebService(Context context)
        {
            mContext = context;

        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void...params)
        {
            String JSONString = null;

            try
            {
                //URL-------------------------------------------------------------
                //String urlString = "http://jacksct1.pythonanywhere.com/points";
                String urlString = "http://pyth0nandr3w.pythonanywhere.com/points";

                URL URLObject = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) URLObject.openConnection();
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode != 200)
                {
                    Toast.makeText(mContext, "Failed to connect to server", Toast.LENGTH_LONG).show();
                }
                else
                {
                    InputStream inputStream = connection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    String responseString;
                    StringBuilder stringBuilder = new StringBuilder();
                    while((responseString = bufferedReader.readLine()) != null)
                    {
                        stringBuilder = stringBuilder.append(responseString);
                    }
                    JSONString = stringBuilder.toString();
                }

            } catch (MalformedURLException e) {
                Log.e("URL exception:  ", e.getMessage());
            } catch (IOException e) {
                Log.e("IO exception:  ", e.getMessage());
            }

            return JSONString;
        }

        @Override
        protected void onPostExecute(String fetchedString)
        {

            try
            {
                JSONObject pointsOfInterest = new JSONObject(fetchedString);

                JSONArray collections = pointsOfInterest.getJSONArray("Collections");
                int nCollections = collections.length();
                for (int i = 0; i<nCollections; i++)
                {
                    JSONObject collection = collections.getJSONObject(i);
                    String id = collection.getString("id");

                    if( !new DB_Handler(context).checkExistingCollection(Integer.parseInt(id)))
                    {
                        String name = collection.getString("name");
                        DB_Handler db = new DB_Handler(context);
                        db.addCollection(new Collection(Integer.parseInt(id), name, null));
                    }

                }

                JSONArray interestPoints = pointsOfInterest.getJSONArray("InterestPoints");
                int nPoints = interestPoints.length();
                for (int i = 0; i<nPoints; i++)
                {
                    JSONObject iPoints = interestPoints.getJSONObject(i);
                    String id = iPoints.getString("id");

                    if( !new DB_Handler(context).checkExistingPOI(Integer.parseInt(id)))
                    {
                        String name = iPoints.getString("name");
                        String scientific_name = iPoints.getString("scientific_name");
                        String lat = iPoints.getString("lat");
                        String lng = iPoints.getString("lng");
                        String description = iPoints.getString("description");
                        String collectionID = iPoints.getString("collection");

                        DB_Handler db = new DB_Handler(context);

                        // Modified/Added the below code 15/05/17 as previous code breaking here
                        // due to existing data in DB having null values for collection id.
                        /////////////////////////////////////////////////////

                        int _id = Integer.parseInt(id);
                        double _lat = Double.parseDouble(lat);
                        double _long = Double.parseDouble(lng);
                        int _collectId;

                        if(collectionID.equals("null"))
                        {
                            _collectId = 999; // Need to handle this differently
                        }
                        else
                        {
                            _collectId = Integer.parseInt(collectionID);
                        }

                        PointOfInterest p = new PointOfInterest(_id, name, scientific_name, _lat , _long, description, _collectId);
                        /////////////////////////////////////////////////////

                        db.addPOI(p);
                    }
                }
            }
            catch (JSONException e)
            {
                Log.e("JSON exception:  ", e.getMessage());
            }
        }
    }
}
