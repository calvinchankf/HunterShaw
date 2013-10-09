package iloveshaw.huntershaw;

import android.os.Parcel;
import android.os.Parcelable;

public class ResponseObjectButtons implements Parcelable{
	private String _text = "";
	private int _completed;
	
	public ResponseObjectButtons(String text,int completed){
		setText(text);
		setCompleted(completed);
	}
	
	public String getText()
	{ 
	    return _text; 
	}
	public int getCompleted()
	{ 
	    return _completed; 
	}
	public void setText(String text)
	{ 
		_text = text; 
	}
	public void setCompleted(int completed)
	{ 
		_completed = completed; 
	}
    public static final Parcelable.Creator<ResponseObjectButtons> CREATOR = new Parcelable.Creator<ResponseObjectButtons>() {
   	 
        @Override
        public ResponseObjectButtons createFromParcel(Parcel source) {
            return new ResponseObjectButtons(source);
        }
 
        @Override
        public ResponseObjectButtons[] newArray(int size) {
            return new ResponseObjectButtons[size];
        }
    };
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
    private ResponseObjectButtons(Parcel in){
		this._completed = in.readInt();
		this._text = in.readString();
    }
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(_completed);
		dest.writeString(_text);
	}
}
