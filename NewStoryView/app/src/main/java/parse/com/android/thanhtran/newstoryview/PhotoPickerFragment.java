package parse.com.android.thanhtran.newstoryview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;



import static android.app.Activity.RESULT_OK;


/**
 * Created by donglua on 15/5/31.
 */
public class PhotoPickerFragment extends Fragment {
  public final static String EXTRA_SHOW_GIF      = "SHOW_GIF";
  public final static int INDEX_ALL_PHOTOS = 0;

  private ImageCaptureManager captureManager;
  private PhotoGridAdapter photoGridAdapter;

 // private PopupDirectoryListAdapter listAdapter;
  private List<PhotoDirectory> directories;

  private int SCROLL_THRESHOLD = 30;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    directories = new ArrayList<>();

    captureManager = new ImageCaptureManager(getActivity());

    Bundle mediaStoreArgs = new Bundle();
    if (getActivity() instanceof PhotoPickerActivity) {
      mediaStoreArgs.putBoolean(EXTRA_SHOW_GIF, ((PhotoPickerActivity) getActivity()).isShowGif());
    }

    MediaStoreHelper.getPhotoDirs(getActivity(), mediaStoreArgs,
        new MediaStoreHelper.PhotosResultCallback() {
          @Override public void onResultCallback(List<PhotoDirectory> dirs) {
            directories.clear();
            directories.addAll(dirs);
            photoGridAdapter.notifyDataSetChanged();
        //    listAdapter.notifyDataSetChanged();
          }
        });
  }


  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    setRetainInstance(true);

    final View rootView = inflater.inflate(R.layout.fragment_photo_picker, container, false);

    photoGridAdapter = new PhotoGridAdapter(getActivity(), directories);
  //  listAdapter  = new PopupDirectoryListAdapter(getActivity(), directories);


    RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_photos);
    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
    layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(photoGridAdapter);
    recyclerView.setItemAnimator(new DefaultItemAnimator());

 //   final Button btSwitchDirectory = (Button) rootView.findViewById(R.id.button);

//
//    final ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
//    listPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
//    listPopupWindow.setAnchorView(btSwitchDirectory);
//    listPopupWindow.setAdapter(listAdapter);
//    listPopupWindow.setModal(true);
//    listPopupWindow.setDropDownGravity(Gravity.BOTTOM);
//    listPopupWindow.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);
//
//    listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        listPopupWindow.dismiss();
//
//        PhotoDirectory directory = directories.get(position);
//
//        btSwitchDirectory.setText(directory.getName());
//
//        photoGridAdapter.setCurrentDirectoryIndex(position);
//        photoGridAdapter.notifyDataSetChanged();
//      }
//    });

//    photoGridAdapter.setOnPhotoClickListener(new OnPhotoClickListener() {
//      @Override public void onClick(View v, int position, boolean showCamera) {
//        final int index = showCamera ? position - 1 : position;
//
//        List<String> photos = photoGridAdapter.getCurrentPhotoPaths();
//
//        int[] screenLocation = new int[2];
//        v.getLocationOnScreen(screenLocation);
//        ImagePagerFragment imagePagerFragment =
//            ImagePagerFragment.newInstance(photos, index, screenLocation, v.getWidth(),
//                v.getHeight());
//
//        ((PhotoPickerActivity) getActivity()).addImagePagerFragment(imagePagerFragment);
//      }
//    });



    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        // Log.d(">>> Picker >>>", "dy = " + dy);
        if (Math.abs(dy) > SCROLL_THRESHOLD) {
          Glide.with(getActivity()).pauseRequests();
        } else {
          Glide.with(getActivity()).resumeRequests();
        }
      }
      @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
          Glide.with(getActivity()).resumeRequests();
        }
      }
    });


    return rootView;
  }


  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == ImageCaptureManager.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
      captureManager.galleryAddPic();
      if (directories.size() > 0) {
        String path = captureManager.getCurrentPhotoPath();
        PhotoDirectory directory = directories.get(INDEX_ALL_PHOTOS);
        directory.getPhotos().add(INDEX_ALL_PHOTOS, new Photo(path.hashCode(), path));
        directory.setCoverPath(path);
        photoGridAdapter.notifyDataSetChanged();
      }
    }
  }


  public PhotoGridAdapter getPhotoGridAdapter() {
    return photoGridAdapter;
  }


  @Override public void onSaveInstanceState(Bundle outState) {
    captureManager.onSaveInstanceState(outState);
    super.onSaveInstanceState(outState);
  }


  @Override public void onViewStateRestored(Bundle savedInstanceState) {
    captureManager.onRestoreInstanceState(savedInstanceState);
    super.onViewStateRestored(savedInstanceState);
  }

  public ArrayList<String> getSelectedPhotoPaths() {
    return photoGridAdapter.getSelectedPhotoPaths();
  }

}
