package parse.com.android.thanhtran.piecechatparse;

import android.app.Application;

import com.parse.Parse;

/**
 * The Class ChattApp is the Main Application class of this app. The onCreate
 * method of this class initializes the Parse.
 */
public class ChattApp extends Application
{

	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate()
	{
		super.onCreate();

		Parse.initialize(this, "LxbXmq7lYZYbGMyN6mVASXxdNqr9HmVitS54bSFV",
				"1ex9zIFUOb1LIFVBpj826drvryimyPvA8s2Nlgqo");

	}
}
