package parse.com.android.thanhtran.newstoryview;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.app.ActionBar.LayoutParams;


public class MainActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar actionBar = getActionBar();
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		View customNav = LayoutInflater.from(this).inflate(R.layout.top_menu_layout, null); // layout which contains your button.
		actionBar.setCustomView(customNav, lp);
		actionBar.setDisplayShowCustomEnabled(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_bar, menu);
		this.setTitle("Add new Story...");
		this.getActionBar().setIcon(R.drawable.mail);
	    return super.onCreateOptionsMenu(menu);
	}

	
//	public void openSecondActivity(View view) {
//		Intent intent = new Intent(this, SecondActivity.class);
//		startActivity(intent);
//	}

	public void postStoryClick(View view){
		Toast.makeText(this, "Post story clicked", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
			case R.id.my_action:
				Toast.makeText(this, "Gear clicked", Toast.LENGTH_SHORT).show();
				return true;
	        case R.id.action_record:
	        	Toast.makeText(this, "Record clicked", Toast.LENGTH_SHORT).show();
	            return true;
	        case R.id.action_save:
	        	Toast.makeText(this, "Save clicked", Toast.LENGTH_SHORT).show();
	            return true;
	        case R.id.action_label:
	        	Toast.makeText(this, "Label clicked", Toast.LENGTH_SHORT).show();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}


}
