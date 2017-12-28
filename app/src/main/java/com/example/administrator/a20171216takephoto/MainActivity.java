package com.example.administrator.a20171216takephoto;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//测试修改上传
public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Button btn_takePhoto;
    private Button btn_take;
   // private ImageView iv_picture;
    private ImageView photo;
//    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
//    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;

    private String capturePath = null;
    private static final String SAVED_IMAGE_DIR_PATH1= "/storage/emulated/0/DCIM/Camera/";


    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileU;
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_take= (Button) findViewById(R.id.btn_tphoto );
        btn_takePhoto= (Button) findViewById(R.id.btn_takephoto );
        photo= (ImageView) findViewById(R.id.iv_picture );



        btn_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                capturePath = SAVED_IMAGE_DIR_PATH1 + System.currentTimeMillis() + ".jpg";
//                Date date = new Date(System.currentTimeMillis());
//                Toast.makeText(MainActivity.this," "+date,Toast.LENGTH_SHORT).show();
                fileU=new File(capturePath);
                Toast.makeText(MainActivity.this,""+System.currentTimeMillis(),Toast.LENGTH_SHORT).show();
           //   fileU=createImageFile();
                autoObtainCameraPermission();

            }
        });

        btn_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                autoObtainStoragePermission();


            }
        });




//        iv_picture = (ImageView) findViewById(R.id.iv_picture);
//
//
//        btn_takePhoto = (Button) findViewById(R.id.btn_takephoto);
//        btn_takePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
////                if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
////                Uri imageUri = Uri.fromFile(file);
////                Intent intent = new Intent();
////                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
////                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
////                startActivityForResult(intent, 1006);
//
//
//                File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
//
//                if (!file.getParentFile().exists()) {
//                    file.getParentFile().mkdirs();
//                }
//
//                String authorities = "com.example.administrator.a20171216takephoto";
////通过FileProvider创建一个content类型的Uri
//                Uri imageUri =
//                        FileProvider.getUriForFile(MainActivity.this, authorities, file);
//
//                Intent intent = new Intent();
////添加这一句表示对目标应用临时授权该Uri所代表的文件
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
//                startActivityForResult(intent, 1006);
//                Toast.makeText(MainActivity.this, "" + imageUri, Toast.LENGTH_SHORT).show();
//                iv_picture.setImageURI(imageUri);
//            }
//        });


//        btn_take = (Button) findViewById(R.id.btn_tphoto);
//        btn_take.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//              //  dispatchTakePictureIntent();
//               autoObtainCameraPermission();
//            }
//        });

    }




    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ToastUtils.showShort(this, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                //// TODO: 2017/12/17
                //imageUri = Uri.fromFile(fileUri);
//                capturePath = SAVED_IMAGE_DIR_PATH1 + System.currentTimeMillis() + ".jpg";
//                fileU=new File(capturePath);
                imageUri = Uri.fromFile(fileU);
                //通过FileProvider创建一个content类型的Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // TODO: 2017/12/17
                    //imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.administrator.a20171216takephoto", fileUri);
                    imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.administrator.a20171216takephoto", fileU);
                }
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                ToastUtils.showShort(this, "设备没有SD卡！");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case CAMERA_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        //// TODO: 2017/12/17
                        //imageUri = Uri.fromFile(fileUri);
                        imageUri = Uri.fromFile(fileU);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            //// TODO: 2017/12/17
                            //imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.administrator.a20171216takephoto", fileUri);//通过FileProvider创建一个content类型的Uri
                            imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.administrator.a20171216takephoto", fileU);
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                } else {

                    ToastUtils.showShort(this, "请允许打开相机！！");
                }
                break;


            }
            //调用系统相册申请Sdcard权限回调
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {

                    ToastUtils.showShort(this, "请允许打操作SDCard！！");
                }
                break;
            default:
        }
    }

    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //拍照完成回调
                case CODE_CAMERA_REQUEST:
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    break;
                //访问相册完成回调
                case CODE_GALLERY_REQUEST:
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            newUri = FileProvider.getUriForFile(this, "com.example.administrator.a20171216takephoto", new File(newUri.getPath()));
                        }
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        showImages(bitmap);
                    }
                    break;
                default:
            }
        }
    }


    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }

    private void showImages(Bitmap bitmap) {
        photo.setImageBitmap(bitmap);
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }










//    public static boolean hasSdcard() {
//        String state = Environment.getExternalStorageState();
//        return state.equals(Environment.MEDIA_MOUNTED);
//    }
//
//
//
//    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
//    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
//    private Uri imageUri;
//    private Uri cropImageUri;

//    /**
//     * 自动获取相机权限
//     */
//    private void autoObtainCameraPermission() {
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
//               // ToastUtils.showShort(this, "您已经拒绝过一次");
//                Toast.makeText(MainActivity.this,"您已经拒绝过一次",Toast.LENGTH_SHORT).show();
//            }
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
//        } else {//有权限直接调用系统相机拍照
//            if (hasSdcard()) {
//                imageUri = Uri.fromFile(fileUri);
//
//             //   imageUri=Uri.fromFile(createImageFile().getAbsoluteFile());
//                //通过FileProvider创建一个content类型的Uri
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.administrator.a20171216takephoto", fileUri);
//                }
//               // PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
//                Intent intentCamera = new Intent();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    //添加这一句表示对目标应用临时授权该Uri所代表的文件
//                    intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                }
//                intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                //将拍照结果保存至photo_file的Uri中，不保留在相册中
//                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                //startActivityForResult(intentCamera, CODE_CAMERA_REQUEST);
//                startActivity(intentCamera);
//
//            } else {
//               // ToastUtils.showShort(this, "设备没有SD卡！");
//                Toast.makeText(MainActivity.this,"设备没有SD卡",Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//
//
//        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            if (hasSdcard()) {
//                imageUri = Uri.fromFile(fileUri);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//                    imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.administrator.a20171216takephoto", fileUri);//通过FileProvider创建一个content类型的Uri
//
//                // PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
//                Intent intentCamera = new Intent();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    //添加这一句表示对目标应用临时授权该Uri所代表的文件
//                    intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                }
//                intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                //将拍照结果保存至photo_file的Uri中，不保留在相册中
//                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                // activity.startActivityForResult(intentCamera, requestCode);
//                startActivity(intentCamera);
//
//            } else {
//                // ToastUtils.showShort(this, "设备没有SD卡！");
//                Toast.makeText(MainActivity.this,"设备没有SD卡",Toast.LENGTH_SHORT).show();
//            }
//        } else {
//
//            // ToastUtils.showShort(this, "请允许打开相机！！");
//            Toast.makeText(MainActivity.this,"请允许打开相机",Toast.LENGTH_SHORT).show();
//        }
//
//    }





//    String mCurrentPhotoPath;

//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(mCurrentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }

//    private void setPic() {
//        // Get the dimensions of the View
//        int targetW = iv_picture.getWidth();
//        int targetH = iv_picture.getHeight();
//
//        // Get the dimensions of the bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        iv_picture.setImageBitmap(bitmap);
//    }

//    static final int REQUEST_TAKE_PHOTO = 1;
//
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//                // ...
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.administrator.a20171216takephoto",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
//    }






    //    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            iv_picture.setImageBitmap(imageBitmap);
//        }
//    }
}
