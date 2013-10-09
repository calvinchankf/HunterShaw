package iloveshaw.huntershaw;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class ResponeObject implements Parcelable {
	private String _userid="";
	private String _message="";
	private ArrayList<ResponseObjectButtons> _buttons  = new ArrayList<ResponseObjectButtons>();
	
	
	public ResponeObject(String userid, String message, JSONArray buttons){
		setMessage(message);
		setUserID(userid);
		for (int i = 0 ;i<buttons.length();i++){
			try {
				JSONObject btn;
				btn = (JSONObject)buttons.get(i);
				int completed = btn.getInt("completed");
				String text = btn.getString("text");
				_buttons.add(new ResponseObjectButtons(text,completed));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	
	public String getUserID()
	{ 
	    return _userid; 
	}
	public String getMessage()
	{ 
	    return _message;
	}
	public ArrayList<ResponseObjectButtons> getButtons()
	{ 
	    return _buttons;
	}
	public void setUserID(String userid)
	{ 
		_userid = userid; 
	}
	public void setMessage(String message)
	{ 
		_message = message; 
	}
	public void setButtons(ArrayList<ResponseObjectButtons> buttons)
	{ 
		_buttons = buttons;
	}
	
    public static final Parcelable.Creator<ResponeObject> CREATOR = new Parcelable.Creator<ResponeObject>() {
    	 
        @Override
        public ResponeObject createFromParcel(Parcel source) {
            return new ResponeObject(source);
        }
 
        @Override
        public ResponeObject[] newArray(int size) {
            return new ResponeObject[size];
        }
    };

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(_userid);
		dest.writeString(_message);
	}
    private ResponeObject(Parcel in){
    	this.setUserID(in.readString());
    	this.setMessage(in.readString());
    }
}
