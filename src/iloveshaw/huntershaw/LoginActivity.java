package iloveshaw.huntershaw;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class LoginActivity extends Activity{
	private EditText username;
	private Button mBtn;
	private SharedPreferences data;
	private ResponeObject responeObject;
	
	private ProgressBar mProgress;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        username = (EditText)findViewById(R.id.username);
        mBtn = (Button)findViewById(R.id.okbtn);
        data = getSharedPreferences("mypref", 0);
        mProgress = (ProgressBar) findViewById(R.id.progress_small);
        mProgress.setVisibility(View.GONE);
        
        if (getUserID()!=""){
        	mProgress.setVisibility(View.VISIBLE);
        	mBtn.setVisibility(Button.GONE);
        	//Toast.makeText(LoginActivity.this, getUserID(), Toast.LENGTH_SHORT).show();
        	okOnclick(null);
        }
        
	}
	
	public void okOnclick(View view){
		mProgress.setVisibility(View.VISIBLE);
		if (username.length()>0 || view == null){
			//Toast.makeText(LoginActivity.this, username.getText(), Toast.LENGTH_LONG).show();
			RequestQueue queue = Volley.newRequestQueue(this);
			
			String url;
			try {
		        if (getUserID()!=""){
		        	url = "http://ec2-50-16-152-216.compute-1.amazonaws.com:5000/huntershaw/init?userid="+URLEncoder.encode(getUserID(), "UTF-8");
		        } else
		        	url = "http://ec2-50-16-152-216.compute-1.amazonaws.com:5000/huntershaw/init?username="+URLEncoder.encode(username.getText().toString(), "UTF-8");
			
				JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.i("log_tag", response.toString());
						try {
							String userid = response.getString("userid");
							String message = response.getString("message");  
							JSONArray buttons = response.getJSONArray("buttons");
							responeObject = new ResponeObject(userid,message,buttons);
							saveUserID(userid);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						goToGame();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// do something
						Log.i("log_tag", error.toString());
					}
				});
				queue.add(jsObjRequest);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
		}else{
			Toast.makeText(LoginActivity.this, "Please enter your name", Toast.LENGTH_LONG).show();
		}
	}
	
	private void goToGame(){
		mProgress.setVisibility(View.GONE);
		Intent intent = new Intent();
	    intent.setClass(LoginActivity.this,GameActivity.class);
		Log.v("log_tag","put extra 1 ");
	    intent.putExtra("downloadedJSON", responeObject);
		Log.v("log_tag","put extrar 2 ");
		Log.v("log_tag",responeObject.getButtons().toString());
	    intent.putParcelableArrayListExtra("downloadedJSON_buttons", responeObject.getButtons());
	    startActivity(intent);
	    finish();
	}
	
	private void saveUserID(String userid){
		SharedPreferences.Editor e = data.edit();
		e.putString("userid", userid);
		e.commit();
	}
	
	private String getUserID(){
		String result = data.getString("userid", "");
		return result;
	}
}
