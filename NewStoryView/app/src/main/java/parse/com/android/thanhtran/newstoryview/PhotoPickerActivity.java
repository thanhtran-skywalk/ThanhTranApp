package parse.com.android.thanhtran.newstoryview;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class PhotoPickerActivity extends AppCompatActivity {

  private PhotoPickerFragment pickerFragment;

  public final static String KEY_SELECTED_PHOTOS = "SELECTED_PHOTOS";
  private MenuItem menuDoneItem;


  /** to prevent multiple calls to inflate menu */
  private boolean menuIsInflated = false;

  private boolean showGif = false;


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setShowGif(showGif);

    setContentView(R.layout.activity_photo_picker);

    Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(mToolbar);
    setTitle(R.string.picker_images);

    ActionBar actionBar = getSupportActionBar();

    assert actionBar != null;
    actionBar.setDisplayHomeAsUpEnabled(true);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      actionBar.setElevation(25);
    }


    pickerFragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentById(R.id.photoPickerFragment);

    pickerFragment.getPhotoGridAdapter().setShowCamera(false);

    pickerFragment.getPhotoGridAdapter().setOnItemCheckListener(new OnItemCheckListener() {
      @Override public boolean OnItemCheck(int position, Photo photo, final boolean isCheck, int selectedItemCount) {
        int total = selectedItemCount + (isCheck ? -1 : 1);
        menuDoneItem.setEnabled(total > 0);
        menuDoneItem.setTitle(getString(R.string.picker_done_with_count, total));
        return true;
      }
    });

  }


  /**
   * Overriding this method allows us to run our exit animation first, then exiting
   * the activity when it complete.
   */
  @Override public void onBackPressed() {
    super.onBackPressed();
  }


//  public void addImagePagerFragment(ImagePagerFragment imagePagerFragment) {
//    this.imagePagerFragment = imagePagerFragment;
//    getSupportFragmentManager()
//        .beginTransaction()
//        .replace(R.id.container, this.imagePagerFragment)
//        .addToBackStack(null)
//        .commit();
//  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    if (!menuIsInflated) {
      getMenuInflater().inflate(R.menu.menu_picker, menu);
      menuDoneItem = menu.findItem(R.id.done);
      menuDoneItem.setEnabled(false);
      menuIsInflated = true;
      return true;
    }
    return false;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      super.onBackPressed();
      return true;
    }

    if (item.getItemId() == R.id.done) {
      Intent intent = new Intent();
      ArrayList<String> selectedPhotos = pickerFragment.getPhotoGridAdapter().getSelectedPhotoPaths();
      intent.putStringArrayListExtra(KEY_SELECTED_PHOTOS, selectedPhotos);
      setResult(RESULT_OK, intent);
      finish();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  public PhotoPickerActivity getActivity() {
    return this;
  }

  public boolean isShowGif() {
    return showGif;
  }

  public void setShowGif(boolean showGif) {
    this.showGif = showGif;
  }
}
