package iloveshaw.huntershaw;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class GameActivity extends Activity{
	private SharedPreferences data;
	private ResponeObject responeObject;
	private String finalwordsText="";
	
	public void onCreate(Bundle savedInstanceState) {
        Log.i("log_tag","game activity on create");
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.game);
        
        data = getSharedPreferences("mypref", 0);
        
        //ResponeObject p = getIntent().getParcelableExtra("downloadedJSON");
        ArrayList<ResponseObjectButtons> bs = getIntent().getParcelableArrayListExtra("downloadedJSON_buttons");
        
        initButtons(bs);
	}
	
	private void initButtons(ArrayList<ResponseObjectButtons> bs){
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.gamelinearlayout);
		linearLayout.removeAllViews(); // clear once
        
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                0, 1.0f);
        param.setMargins(30, 10, 30, 10);
        
        int count = 0;
        
        for (int i=0;i<bs.size();i++){
        	Button btn = new Button(this);
        	btn.setLayoutParams(param);
        	btn.setText(bs.get(i).getText());
        	if (bs.get(i).getCompleted()==0){
        		// yellow
        		btn.setBackgroundResource(R.drawable.button_sytle_light);
        		if (i==count){
        			btn.setTextColor(Color.parseColor("#000000"));
        			btn.setOnClickListener(new MyBtnListener(i));
        		}else
        			btn.setTextColor(Color.parseColor("#AAAAAA"));
        	}else{
        		// black
        		btn.setBackgroundResource(R.drawable.button_sytle_fill);
        		btn.setTextColor(Color.parseColor("#FFCC00"));
        		count++;
        	}
        	
        	linearLayout.addView(btn);
        }
        
        if (finalwordsText!=""){
        	Log.i("log_tag","add finalwords"+finalwordsText);
        	Button btn = new Button(this);
        	btn.setLayoutParams(param);
        	btn.setText("Final");
        	btn.setBackgroundResource(R.drawable.button_sytle_light);
        	btn.setTextColor(Color.parseColor("#FF0000"));
        	btn.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                	Intent intent = new Intent();
            	    intent.setClass(GameActivity.this,FinalActivity.class);
            	    intent.putExtra("finalwords", finalwordsText);
            	    startActivity(intent);
                }
            });
        	linearLayout.addView(btn);
        } 
	}
	
	private class MyBtnListener implements Button.OnClickListener {
	    int pos;
	    public MyBtnListener (int position) {
	        pos = position;
	    }
	    @Override
	    public void onClick(View v) {
	        Log.i("log_tag", "onclick="+pos);
	        Intent intent = new Intent(GameActivity.this, CameraTestActivity.class );
	        intent.putExtra("button_no", pos);
	        startActivityForResult( intent, 1);
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if(data != null) {
	        // doing something
	    	
	    	String callbackString = data.getExtras().getString("callback");
	    	Log.i("log_tag", "callback data="+callbackString);
	    	
	    	if (callbackString.length()>0){
	    		RequestQueue queue = Volley.newRequestQueue(this);
	    		String url = "http://ec2-50-16-152-216.compute-1.amazonaws.com:5000/huntershaw/reportLocation?userid="+getUserID()+callbackString;
	    		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.i("log_tag", response.toString());
						try {
							String userid = response.getString("userid");
							String message = response.getString("message");  
							JSONArray buttons = response.getJSONArray("buttons");
							responeObject = new ResponeObject(userid,message,buttons);
							// corresponding action
							if (response.has("finalwords")) finalwordsText = response.getString("finalwords");
							initButtons(responeObject.getButtons());
							Toast.makeText(GameActivity.this, message, Toast.LENGTH_LONG).show();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// do something
						Log.i("log_tag", error.toString());
					}
				});
				queue.add(jsObjRequest);
	    	}
	    }     
	}
	
	private String getUserID(){
		String result = data.getString("userid", "");
		return result;
	}
}
