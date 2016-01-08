package com.example.skywalker.imagepicker_test;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoPickerActivity extends AppCompatActivity {

    public static final String TAG = PhotoPickerActivity.class.getName();

    private Context mCxt;

    public static final String EXTRA_SELECT_MODE = "select_count_mode";

    public static final int MODE_SINGLE = 0;
    public static final int MODE_MULTI = 1;
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    public static final int DEFAULT_MAX_TOTAL= 9;
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_result";
    public static final String EXTRA_IMAGE_CONFIG = "image_config";
    public static final String EXTRA_RESULT = "select_result";

    private ArrayList<String> resultList = new ArrayList<>();
    private ArrayList<Folder> mResultFolder = new ArrayList<>();

    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;

    private MenuItem menuDoneItem;
    private GridView mGridView;
    private View mPopupAnchorView;
    private Button btnAlbum;
    private ImageCaptureManager captureManager;
    private int mDesireImageCount;
    private ImageConfig imageConfig;

    private ImageGridAdapter mImageAdapter;
    private FolderAdapter mFolderAdapter;
    private ListPopupWindow mFolderPopupWindow;

    private boolean hasFolderGened = false;
    private boolean mIsShowCamera = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photopicker);

        initViews();
        imageConfig = getIntent().getParcelableExtra(EXTRA_IMAGE_CONFIG);
        getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
        mDesireImageCount = getIntent().getIntExtra(EXTRA_SELECT_COUNT, DEFAULT_MAX_TOTAL);
        final int mode = getIntent().getExtras().getInt(EXTRA_SELECT_MODE, MODE_SINGLE);
        if(mode == MODE_MULTI) {
            ArrayList<String> tmp = getIntent().getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
            if(tmp != null && tmp.size() > 0) {
                resultList.addAll(tmp);
            }
        }

        mIsShowCamera = getIntent().getBooleanExtra(EXTRA_SHOW_CAMERA, false);
        mImageAdapter = new ImageGridAdapter(mCxt, mIsShowCamera, getItemImageWidth());
        mImageAdapter.showSelectIndicator(mode == MODE_MULTI);
        mGridView.setAdapter(mImageAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mImageAdapter.isShowCamera()) {
                    if (i == 0) {
                        if(mode == MODE_MULTI){
                            if(mDesireImageCount == resultList.size()){
                                Toast.makeText(mCxt, R.string.msg_amount_limit, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        showCameraAction();
                    } else {
                        Image image = (Image) adapterView.getAdapter().getItem(i);
                        selectImageFromGrid(image, mode);
                    }
                } else {
                    Image image = (Image) adapterView.getAdapter().getItem(i);
                    selectImageFromGrid(image, mode);
                }
            }
        });

        mFolderAdapter = new FolderAdapter(mCxt);
        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFolderPopupWindow == null) {
                    createPopupFolderList();
                }

                if (mFolderPopupWindow.isShowing()) {
                    mFolderPopupWindow.dismiss();
                } else {
                    mFolderPopupWindow.show();
                    int index = mFolderAdapter.getSelectIndex();
                    index = index == 0 ? index : index - 1;
                    mFolderPopupWindow.getListView().setSelection(index);
                }
            }
        });
    }

    private void initViews(){
        mCxt = this;
        captureManager = new ImageCaptureManager(mCxt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.pickerToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.image));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mGridView = (GridView) findViewById(R.id.grid);
        mGridView.setNumColumns(getNumColnums());

        mPopupAnchorView = findViewById(R.id.photo_picker_footer);
        btnAlbum = (Button) findViewById(R.id.btnAlbum);
    }

    private void createPopupFolderList(){
        mFolderPopupWindow = new ListPopupWindow(mCxt);
        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setContentWidth(ListPopupWindow.MATCH_PARENT);
        mFolderPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);

        int folderItemViewHeight = getResources().getDimensionPixelOffset(R.dimen.folder_cover_size)
                + getResources().getDimensionPixelOffset(R.dimen.folder_padding)
                + getResources().getDimensionPixelOffset(R.dimen.folder_padding);
        int folderViewHeight = mFolderAdapter.getCount() * folderItemViewHeight;

        int screenHeigh = getResources().getDisplayMetrics().heightPixels;
        if(folderViewHeight >= screenHeigh){
            mFolderPopupWindow.setHeight(Math.round(screenHeigh * 0.6f));
        }else{
            mFolderPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        }

        mFolderPopupWindow.setAnchorView(mPopupAnchorView);
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mFolderAdapter.setSelectIndex(position);

                final int index = position;
                final AdapterView v = parent;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFolderPopupWindow.dismiss();
                        if (index == 0) {
                            getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                            btnAlbum.setText(R.string.all_image);
                            mImageAdapter.setShowCamera(mIsShowCamera);
                        } else {
                            Folder folder = (Folder) v.getAdapter().getItem(index);
                            if (null != folder) {
                                mImageAdapter.setData(folder.images);
                                btnAlbum.setText(folder.name);
                                if (resultList != null && resultList.size() > 0) {
                                    mImageAdapter.setDefaultSelected(resultList);
                                }
                            }
                            mImageAdapter.setShowCamera(false);
                        }
                        mGridView.smoothScrollToPosition(0);
                    }
                }, 100);
            }
        });
    }

    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        resultList.add(path);
        data.putStringArrayListExtra(EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    public void onImageSelected(String path) {
        if(!resultList.contains(path)) {
            resultList.add(path);
        }
        refreshActionStatus();
    }

    public void onImageUnselected(String path) {
        if(resultList.contains(path)){
            resultList.remove(path);
        }
        refreshActionStatus();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    if(captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();
                        resultList.add(captureManager.getCurrentPhotoPath());
                    }
                    complete();
                    break;
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "on change");
        mGridView.setNumColumns(getNumColnums());
        mImageAdapter.setItemSize(getItemImageWidth());

        if(mFolderPopupWindow != null){
            if(mFolderPopupWindow.isShowing()){
                mFolderPopupWindow.dismiss();
            }
            int screenHeigh = getResources().getDisplayMetrics().heightPixels;
            mFolderPopupWindow.setHeight(Math.round(screenHeigh * 0.6f));
        }

        super.onConfigurationChanged(newConfig);
    }
    private void showCameraAction() {
        try {
            Intent intent = captureManager.dispatchTakePictureIntent();
            startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            Toast.makeText(mCxt, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void selectImageFromGrid(Image image, int mode) {
        if(image != null) {
            if(mode == MODE_MULTI) {
                if (resultList.contains(image.path)) {
                    resultList.remove(image.path);
                    onImageUnselected(image.path);
                } else {
                    if(mDesireImageCount == resultList.size()){
                        Toast.makeText(mCxt, R.string.msg_amount_limit, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    resultList.add(image.path);
                    onImageSelected(image.path);
                }
                mImageAdapter.select(image);
            }else if(mode == MODE_SINGLE){
                onSingleImageSelected(image.path);
            }
        }
    }

    private void refreshActionStatus(){
        String text = getString(R.string.done_with_count, resultList.size(), mDesireImageCount);
        menuDoneItem.setTitle(text);
        boolean hasSelected = resultList.size() > 0;
        menuDoneItem.setVisible(hasSelected);
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            StringBuilder selectionArgs = new StringBuilder();
            if(imageConfig != null){
                if(imageConfig.minWidth != 0){
                    selectionArgs.append(MediaStore.Images.Media.WIDTH + " >= " + imageConfig.minWidth);
                }

                if(imageConfig.minHeight != 0){
                    selectionArgs.append("".equals(selectionArgs.toString()) ? "" : " and ");
                    selectionArgs.append(MediaStore.Images.Media.HEIGHT + " >= " + imageConfig.minHeight);
                }

                if(imageConfig.minSize != 0f){
                    selectionArgs.append("".equals(selectionArgs.toString()) ? "" : " and ");
                    selectionArgs.append(MediaStore.Images.Media.SIZE + " >= " + imageConfig.minSize);
                }

                if(imageConfig.mimeType != null){
                    selectionArgs.append(" and (");
                    for(int i = 0, len = imageConfig.mimeType.length; i < len; i++){
                        if(i != 0){
                            selectionArgs.append(" or ");
                        }
                        selectionArgs.append(MediaStore.Images.Media.MIME_TYPE + " = '" + imageConfig.mimeType[i] + "'");
                    }
                    selectionArgs.append(")");
                }
            }

            if(id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(mCxt,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        selectionArgs.toString(), null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }else if(id == LOADER_CATEGORY){
                String selectionStr = selectionArgs.toString();
                if(!"".equals(selectionStr)){
                    selectionStr += " and" + selectionStr;
                }
                CursorLoader cursorLoader = new CursorLoader(mCxt,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'" + selectionStr, null,
                        IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                List<Image> images = new ArrayList<>();
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do{
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));

                        Image image = new Image(path, name, dateTime);
                        images.add(image);
                        if( !hasFolderGened ) {
                            File imageFile = new File(path);
                            File folderFile = imageFile.getParentFile();
                            Folder folder = new Folder();
                            folder.name = folderFile.getName();
                            folder.path = folderFile.getAbsolutePath();
                            folder.cover = image;
                            if (!mResultFolder.contains(folder)) {
                                List<Image> imageList = new ArrayList<>();
                                imageList.add(image);
                                folder.images = imageList;
                                mResultFolder.add(folder);
                            } else {
                                Folder f = mResultFolder.get(mResultFolder.indexOf(folder));
                                f.images.add(image);
                            }
                        }

                    }while(data.moveToNext());

                    mImageAdapter.setData(images);
                    if(resultList != null && resultList.size()>0){
                        mImageAdapter.setDefaultSelected(resultList);
                    }

                    mFolderAdapter.setData(mResultFolder);
                    hasFolderGened = true;

                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private int getItemImageWidth(){
        int cols = getNumColnums();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
        return (screenWidth - columnSpace * (cols-1)) / cols;
    }

    private int getNumColnums(){
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        return cols < 3 ? 3 : cols;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picker, menu);
        menuDoneItem = menu.findItem(R.id.action_picker_done);
        menuDoneItem.setVisible(false);
        refreshActionStatus();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }

        if(item.getItemId() == R.id.action_picker_done){
            complete();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void complete(){
        Intent data = new Intent();
        data.putStringArrayListExtra(EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        captureManager.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        captureManager.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }
}
