package com.example.ekta.noir.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.cocosw.bottomsheet.BottomSheet;
import com.example.ekta.noir.R;
import com.example.ekta.noir.adapters.UnsplashDetailAdapter;
import com.example.ekta.noir.model.PaletteData;
import com.example.ekta.noir.model.PhotosProvider;
import com.example.ekta.noir.model.UnsplashData;
import com.example.ekta.noir.model.UnsplashProviderMethods;
import com.example.ekta.noir.utils.VolleySingleton;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.vipul.hp_hp.library.Layout_to_Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.ekta.noir.activities.SettingsActivity.SP_NAME;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_COLOR;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_CREATED_AT;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_HEIGHT;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_ID;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_URLS_FULL;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_URLS_REGULAR;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_USER_NAME;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_WIDTH;

public class UnsplashDetailFragment extends Fragment implements View.OnClickListener{

    private String unsplashId, unsplashColor, unsplashUrlRegular, unsplashUrlFull, unsplashUser, unsplashCreatedAt;
    private int unsplashWidth, unsplashHeight;
    private UnsplashData unsplashData;

    private ImageView imageLikeBorder, imageLike, imageWallpaper, imageBack, imageShare, imageDownload ;
    private SimpleDraweeView imagePhoto;
    private TextView textDownloading;
    private ArrayList<PaletteData> paletteList;

    private RecyclerView mRecyclerView;
    private UnsplashDetailAdapter mUnsplashDetailAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue = null;
    private ImageLoader imageLoader;

    private Animation animZoomOut, animZoomIn, animBlink;

    SharedPreferences settingsDatabase;

//    final Bitmap[] map = new Bitmap[1];

    private Animation slideUpAppBar, slideUpRecycler;
    private AppBarLayout appBarLayout;
    private Activity activity;
    private ProgressBar progressBar;

    private Bitmap bmp;
//    private Uri imageUri;
//    private ImageExporter exporter;

    private Button buttonDownloadLayout;
    private CoordinatorLayout coordinatorLayoutMain;

    private Layout_to_Image layout_to_image;
    private boolean isPhotoInDb;
    private int wallQuality, wallpaperValue, paletteValue;

    public UnsplashDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paletteList = new ArrayList();

        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();
        imageLoader = volleySingleton.getImageLoader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.fragment_unsplash_detail, container, false);

        appBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar_layout);
        imagePhoto = (SimpleDraweeView) view.findViewById(R.id.ivPhoto);
        imageLike = (ImageView) view.findViewById(R.id.ivLike);
        imageLikeBorder = (ImageView) view.findViewById(R.id.ivLikeBorder);
        imageWallpaper = (ImageView) view.findViewById(R.id.ivWallpaper);
        imageBack = (ImageView) view.findViewById(R.id.ivBack);
        imageShare = (ImageView) view.findViewById(R.id.ivShare);
        imageDownload = (ImageView) view.findViewById(R.id.ivDownload);
        textDownloading = (TextView) view.findViewById(R.id.tvDownloading);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvPalette);
        progressBar = (ProgressBar) view.findViewById(R.id.pbWallpaper);
        coordinatorLayoutMain = (CoordinatorLayout) view.findViewById(R.id.clMain);

     //   buttonDownloadLayout = (Button) view.findViewById(R.id.bDownloadLayout);

        mLinearLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        slideUpAppBar = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        slideUpRecycler = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        animZoomIn = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        animZoomOut = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_out);
        animBlink = AnimationUtils.loadAnimation(getContext(), R.anim.blink);

        slideUpAppBar.setDuration(400);
        slideUpRecycler.setDuration(500);

        appBarLayout.setAnimation(slideUpAppBar);
        mRecyclerView.setAnimation(slideUpRecycler);

        progressBar.setVisibility(View.GONE);
        textDownloading.setVisibility(View.GONE);

        settingsDatabase = activity.getSharedPreferences(SP_NAME, 0);
        wallpaperValue = settingsDatabase.getInt("wallpaperStatus", 0);
        paletteValue = settingsDatabase.getInt("paletteStatus", 0);
        wallQuality = settingsDatabase.getInt("wallpaperQualityStatus", 1);

        if(wallpaperValue == 1){
            imageWallpaper.setVisibility(View.VISIBLE);
        }else{
            imageWallpaper.setVisibility(View.GONE);
        }

        //GETTING VALUES FROM FRAGMENTS
        unsplashData = (UnsplashData) getArguments().getSerializable("unsplashData");
        unsplashId = unsplashData.getId();
        unsplashColor = unsplashData.getColor();
        unsplashWidth = unsplashData.getWidth();
        unsplashHeight = unsplashData.getHeight();
        unsplashUrlRegular = unsplashData.getUrlRegular();
        unsplashUrlFull = unsplashData.getUrlFull();
        unsplashUser = unsplashData.getUsername();
        unsplashCreatedAt = unsplashData.getCreatedAt();

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        boolean isPhotoInDb;
        isPhotoInDb = UnsplashProviderMethods.isPhotoInDatabase(activity, unsplashId);

        if (isPhotoInDb) {
            imageLike.setVisibility(View.VISIBLE);
            imageLikeBorder.setVisibility(View.GONE);
        } else {
            imageLike.setVisibility(View.GONE);
            imageLikeBorder.setVisibility(View.VISIBLE);
        }


imageDownload.setOnClickListener(this);
        imageLike.setOnClickListener(this);
        imageLikeBorder.setOnClickListener(this);
        imagePhoto.setOnClickListener(this);
        imageShare.setOnClickListener(this);
        imageWallpaper.setOnClickListener(this);
            final Uri imageUri = Uri.parse(unsplashUrlRegular);

            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            com.facebook.imagepipeline.request.ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(imageUri)
                    .setRequestPriority(Priority.HIGH)
                    .setLowestPermittedRequestLevel(com.facebook.imagepipeline.request.ImageRequest.RequestLevel.FULL_FETCH)
                    .build();
            final DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, activity);
            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                @Override
                protected void onNewResultImpl(Bitmap bitmap) {
                    if(dataSource.isFinished() && bitmap != null){
                        bmp = Bitmap.createBitmap(bitmap);
                        imagePhoto.setImageURI(imageUri);

                        //////////////////////////////////////////////////////
                        if(bmp != null) {
                            Palette palette = Palette.from(bmp).generate();
                            List<Palette.Swatch> swatchList = palette.getSwatches();

                            for (int i = 0; i < swatchList.size(); i++) {

                                Palette.Swatch swatch = swatchList.get(i);
                                int rgbColor1 = swatch.getRgb();

                                String hexColor = Integer.toHexString(rgbColor1);
                                int pixelCount = swatch.getPopulation();

                                PaletteData paletteData = new PaletteData();
                                paletteData.setColorName("Color " + i);
                                paletteData.setColorHexCode(hexColor);
                                paletteData.setColorPopulation(pixelCount);
                                paletteList.add(paletteData);
                            }

                            Collections.sort(paletteList);
                 //           if(paletteValue == 0 && (R.integer.screen == 1)){
                                mRecyclerView.setBackgroundColor(Color.parseColor("#" + paletteList.get(paletteList.size() - 1).getColorHexCode()));
                /*            }else if(paletteValue == 1 || paletteValue == 0 || (R.integer.screen == 2) || (R.integer.screen == 3)){
                                if(paletteList.size() >= 6) {
                                    mRecyclerView.setBackgroundColor(Color.parseColor("#" + paletteList.get(5).getColorHexCode()));
                                }else if(paletteList.size() < 6){
                                    mRecyclerView.setBackgroundColor(Color.parseColor("#" + paletteList.get(paletteList.size() - 1).getColorHexCode()));
                                }
                            }
                            */
                            if(activity != null && bmp != null) {
                                mUnsplashDetailAdapter = new UnsplashDetailAdapter(activity, paletteList, unsplashData);
                                mRecyclerView.setAdapter(mUnsplashDetailAdapter);
                                mUnsplashDetailAdapter.notifyDataSetChanged();
                            }
                        }
                        dataSource.close();
                    }
                }

                @Override
                protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                    if(dataSource != null){
                        dataSource.close();
                    }
                }
            }, CallerThreadExecutor.getInstance());




        return view;
    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        FileOutputStream out = null;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String filename = "pippo.png";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, filename);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Toast.makeText(getContext(), path, Toast.LENGTH_LONG).show();
        return Uri.parse(path);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {

        isPhotoInDb = UnsplashProviderMethods.isPhotoInDatabase(activity, unsplashId);
        boolean result = Utility.checkPermission(getActivity());

        switch (v.getId()){

            case R.id.ivLike:
                Log.d("like","clciked");
                isPhotoInDb = UnsplashProviderMethods.isPhotoInDatabase(activity, unsplashId);

                if (isPhotoInDb) {
                    Uri contentUri = PhotosProvider.BASE_CONTENT_URI;
                    activity.getContentResolver().delete(contentUri, "id=?", new String[]{String.valueOf(unsplashId)});

                    imageLike.startAnimation(animZoomIn);
                    imageLikeBorder.startAnimation(animZoomOut);

                    imageLike.setVisibility(View.GONE);
                    imageLikeBorder.setVisibility(View.VISIBLE);

                    Snackbar.make(activity.findViewById(android.R.id.content), "Removed", Snackbar.LENGTH_LONG).show();
                } else {
//                    ContentValues values = new ContentValues();
//                    values.put(IMAGE_ID, unsplashId);
//                    values.put(IMAGE_WIDTH, unsplashWidth);
//                    values.put(IMAGE_HEIGHT, unsplashHeight);
//                    values.put(IMAGE_CREATED_AT, unsplashCreatedAt);
//                    values.put(IMAGE_COLOR, unsplashColor);
//                    values.put(IMAGE_URLS_FULL, unsplashUrlFull);
//                    values.put(IMAGE_URLS_REGULAR, unsplashUrlRegular);
//                    values.put(IMAGE_USER_NAME, unsplashUser);
//new GetContacts().execute((Object[]) null);
                    ContentValues values = new ContentValues();
                   new AsyncClass().execute();
                    imageLike.startAnimation(animZoomOut);
                    imageLikeBorder.startAnimation(animZoomIn);

                    imageLike.setVisibility(View.VISIBLE);
                    imageLikeBorder.setVisibility(View.GONE);
                    Snackbar.make(activity.findViewById(android.R.id.content), "Liked " + getResources().getString(R.string.heart), Snackbar.LENGTH_LONG).show();
                }
                break;

            case R.id.ivLikeBorder:
                isPhotoInDb = UnsplashProviderMethods.isPhotoInDatabase(activity, unsplashId);

                if (isPhotoInDb) {
                    Uri contentUri = PhotosProvider.BASE_CONTENT_URI;
                    activity.getContentResolver().delete(contentUri, "id=?", new String[]{String.valueOf(unsplashId)});

                    imageLike.startAnimation(animZoomIn);
                    imageLikeBorder.startAnimation(animZoomOut);

                    imageLike.setVisibility(View.GONE);
                    imageLikeBorder.setVisibility(View.VISIBLE);

                    Snackbar.make(activity.findViewById(android.R.id.content), "Removed", Snackbar.LENGTH_LONG).show();
                } else {
                    ContentValues values = new ContentValues();
                    values.put(IMAGE_ID, unsplashId);
                    values.put(IMAGE_WIDTH, unsplashWidth);
                    values.put(IMAGE_HEIGHT, unsplashHeight);
                    values.put(IMAGE_CREATED_AT, unsplashCreatedAt);
                    values.put(IMAGE_COLOR, unsplashColor);
                    values.put(IMAGE_URLS_FULL, unsplashUrlFull);
                    values.put(IMAGE_URLS_REGULAR, unsplashUrlRegular);
                    values.put(IMAGE_USER_NAME, unsplashUser);

                    imageLike.startAnimation(animZoomOut);
                    imageLikeBorder.startAnimation(animZoomIn);

                    imageLike.setVisibility(View.VISIBLE);
                    imageLikeBorder.setVisibility(View.GONE);

                    activity.getContentResolver().insert(PhotosProvider.BASE_CONTENT_URI, values);
                    Snackbar.make(activity.findViewById(android.R.id.content), "Liked " + getResources().getString(R.string.heart), Snackbar.LENGTH_LONG).show();
                }
                break;

            case R.id.ivWallpaper:
                if (result) {
                    if (wallQuality == 0) {
                        try {
                            if (bmp != null) {
                                Uri uri = getImageUri(getContext(), bmp);
                                Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setDataAndType(uri, "image/*");
                                intent.putExtra("mimeType", "image/*");
                                startActivity(Intent.createChooser(intent, "Use As"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(activity, R.string.wallpaper_set_failed, Toast.LENGTH_SHORT).show();
                        }
                    } else if (wallQuality == 1) {
                        imageDownload.setVisibility(View.GONE);
                        imageWallpaper.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        textDownloading.setVisibility(View.VISIBLE);
                        textDownloading.startAnimation(animBlink);

                        Toast.makeText(activity, R.string.high_quality_image_downloading, Toast.LENGTH_LONG).show();

                        if (unsplashUrlFull != null) {
                            imageLoader.get(unsplashUrlFull, new ImageLoader.ImageListener() {
                                @Override
                                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                                    Bitmap bitmap = response.getBitmap();
                                    if (bitmap != null) {

                                        imageDownload.setVisibility(View.VISIBLE);
                                        imageWallpaper.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        textDownloading.setVisibility(View.GONE);
                                        textDownloading.clearAnimation();

                                        Uri uri = getImageUri(getContext(), bitmap);
                                        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                                        intent.setDataAndType(uri, "image/*");
                                        intent.putExtra("mimeType", "image/*");
                                        startActivity(Intent.createChooser(intent, "Use As"));
//                                    startActivityForResult(Intent.createChooser(intent, "set as"), 1);
                                    }
                                }

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            });
                        }
                    }

                }
                break;

            case R.id.ivShare:
                if(result) {
                    if (bmp != null) {
                        imageShare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Uri uri = getImageUri(getContext(), bmp);
                                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                shareIntent.setType("image/jpeg");
                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                try {
                                    startActivity(shareIntent);
                                } catch (android.content.ActivityNotFoundException ex) {

                                }
                            }
                        });
                    }
                }
                break;

            case R.id.ivDownload:
                if(result) {
                    if (bmp != null) {
                        imageDownload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new BottomSheet.Builder(getActivity()).title("Download").sheet(R.menu.menu_download).listener(new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case R.id.regular:
                                                MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bmp, "", "");
                                                Toast.makeText(getActivity(), R.string.image_saved, Toast.LENGTH_SHORT).show();
                                                break;

                                            case R.id.full:
                                                imageWallpaper.setVisibility(View.GONE);
                                                imageDownload.setVisibility(View.GONE);
                                                progressBar.setVisibility(View.VISIBLE);
                                                textDownloading.setVisibility(View.VISIBLE);
                                                textDownloading.startAnimation(animBlink);

                                                imageLoader.get(unsplashUrlFull, new ImageLoader.ImageListener() {
                                                    @Override
                                                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                                                        final Bitmap bitmap = response.getBitmap();
                                                        if (bitmap != null) {
                                                            if(wallpaperValue == 1){
                                                                imageWallpaper.setVisibility(View.VISIBLE);
                                                            }else if(wallpaperValue == 0) {
                                                                imageWallpaper.setVisibility(View.GONE);
                                                            }

                                                            imageDownload.setVisibility(View.VISIBLE);
                                                            progressBar.setVisibility(View.GONE);
                                                            textDownloading.setVisibility(View.GONE);
                                                            textDownloading.clearAnimation();
//                                                    try {
//                                                        exporter.exportFromView(getActivity(), bitmap);
//                                                        Toast.makeText(getActivity(), "Image saved", Toast.LENGTH_SHORT).show();
//                                                    } catch (IOException e) {
//                                                        Toast.makeText(getActivity(), "Download failed", Toast.LENGTH_SHORT).show();
//                                                    }
                                                            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "", "");
                                                            Toast.makeText(getActivity(), R.string.image_saved, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                    }
                                                });
                                                break;
                                        }
                                    }
                                }).show();
                            }
                        });
                    }
                }
                break;

         //   case R.id.bDownloadLayout:
//                Bitmap map = getBitmapFromView1(coordinatorLayoutMain);
//                Log.e("Bitmap", map.toString());
//                MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), map, "", "");

//                layout_to_image = new Layout_to_Image(activity, coordinatorLayoutMain);
//                Bitmap bitmap = layout_to_image.convert_layout();
//                Log.e("Bitmap", bitmap.toString());
//                MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "", "");
             //   break;
        }
    }

    static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context) {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }

    class AsyncClass extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {
            ContentValues values = new ContentValues();
            values.put(IMAGE_ID, unsplashId);
            values.put(IMAGE_WIDTH, unsplashWidth);
            values.put(IMAGE_HEIGHT, unsplashHeight);
            values.put(IMAGE_CREATED_AT, unsplashCreatedAt);
            values.put(IMAGE_COLOR, unsplashColor);
            values.put(IMAGE_URLS_FULL, unsplashUrlFull);
            values.put(IMAGE_URLS_REGULAR, unsplashUrlRegular);
            values.put(IMAGE_USER_NAME, unsplashUser);
            activity.getContentResolver().insert(PhotosProvider.BASE_CONTENT_URI, values);

            return null;

        }
    }
}

