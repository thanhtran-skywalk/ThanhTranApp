package parse.com.android.thanhtran.newstoryview;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
	private Menu menu;
	public final static int REQUEST_CODE = 1;

	RecyclerView recyclerView;
	PhotoAdapter photoAdapter;

	ArrayList<String> selectedPhotos = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar actionBar = getActionBar();
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		View customNav = LayoutInflater.from(this).inflate(R.layout.top_menu_layout, null); // layout which contains your button.
		actionBar.setCustomView(customNav, lp);
		actionBar.setDisplayShowCustomEnabled(true);


		recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		photoAdapter = new PhotoAdapter(this, selectedPhotos);
		recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
		recyclerView.setAdapter(photoAdapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_bar, menu);
		this.setTitle("Add new Story...");
		this.getActionBar().setIcon(R.mipmap.pencil_selected);
		this.menu = menu;
	    return super.onCreateOptionsMenu(menu);
	}


	public void postStoryClick(View view){
		Toast.makeText(this, "Post story clicked", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_bar_smiley:
	        	Toast.makeText(this, "Record clicked", Toast.LENGTH_SHORT).show();
	            return true;
	        case R.id.action_bar_image:
				PhotoPickerIntent intent = new PhotoPickerIntent(MainActivity.this);
				intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
				startActivityForResult(intent, REQUEST_CODE);
	            return true;
	        case R.id.action_bar_random:
	        	Toast.makeText(this, "Label clicked", Toast.LENGTH_SHORT).show();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}


	public void previewPhoto(Intent intent) {
		startActivityForResult(intent, REQUEST_CODE);
	}


	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		List<String> photos = null;
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			if (data != null) {
				photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
			}
			selectedPhotos.clear();

			if (photos != null) {

				selectedPhotos.addAll(photos);
			}
			photoAdapter.notifyDataSetChanged();
		}
	}


}
