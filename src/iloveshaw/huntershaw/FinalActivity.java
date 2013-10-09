package iloveshaw.huntershaw;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinalActivity extends Activity{
	private TextView mtxt;
	private Button mBtn;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finalcontent);
        
        mtxt = (TextView)findViewById(R.id.txt);
        mBtn = (Button)findViewById(R.id.goBack);
        
        Log.i("log_tag", "finalwords"+getIntent().getExtras().getString("finalwords"));
        
        mtxt.setText(getIntent().getExtras().getString("finalwords"));
        
        mBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	goLogin();
            }
        });
	}
	
	private void goLogin(){
		// clear userid
		SharedPreferences settings = getSharedPreferences("mypref", 0);
		settings.edit().clear().commit();
		
		// go login activity
		Intent i = new Intent(this, LoginActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
	}
}
