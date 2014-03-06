package com.parentlink.jsonparsertest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	static String jsonURL = "https://prod.api.pvp.net/api/lol/na/v1.1/champion?api_key=acf9bb44-85f2-44d4-bde1-26d9b2b4c3db";
	
	static String  champion_name_info= "";
	static String champion_attack_rank = "";
	static String champion_defense_rank = "";
	static String champion_magic_rank = "";
	static String champion_difficulty_rank = "";
	static String champion_free_to_play = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		new MyAsyncTask().execute();
	}
	
	private class MyAsyncTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... arg0) {
			
			DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
			
			HttpGet httppost = new HttpGet(jsonURL);
			
			httppost.setHeader("Content-type","application/json");
			
			InputStream inputStream = null;
			
			String result = null;
			
			try{
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				inputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
				StringBuilder theStringBuilder = new StringBuilder();
				
				String line = null;
				
				while((line = reader.readLine()) != null){
					
					theStringBuilder.append(line +"\n");
					
				}
				result = theStringBuilder.toString();
			}
			
			catch(Exception e){
				
				e.printStackTrace();
			}
			
			finally{
				
				try{
					
					if(inputStream != null)inputStream.close();
				}
				
				catch(Exception e){
					
					e.printStackTrace();
				}
				
				JSONObject jsonArray;
				List<String> allChamps = new ArrayList<String>();
				Log.v("RESULT:  ", result);
				try{
					
					//Log.v("JSONParser RESULT", result);
					
					jsonArray = new JSONObject(result);
					
					JSONArray champions = jsonArray.getJSONArray("champions");
					
					for(int i=0; i < champions.length(); i++)
					{
						JSONObject champ = champions.getJSONObject(i);
						String champion = champ.getString("name");
						allChamps.add(champion);
					}
					champion_name_info= allChamps.get(24); 
					
				}
				catch(JSONException e){
					
					e.printStackTrace();
				}
				
			}
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result){
			
			TextView line1 = (TextView) findViewById(R.id.line1);
			
			line1.setText("Champion: " + champion_name_info);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)  {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
