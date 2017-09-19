package com.example.ritik.btpcamera;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {
    ProgressDialog pd;
    byte[] byteArray=new byte[100000];
    static final int REQUEST_IMAGE_CAPTURE = 1;
    //
    //  ImageView ImageView;
    //  private TextView txtSpeechInput;
    EditText edit_text;
    static  Bitmap photo;
    private ImageSurfaceView mImageSurfaceView;
    private Camera camera;
    private SensorManager mSensorManager;

    private ShakeEventListener mSensorListener;
    private FrameLayout cameraPreviewLayout;
    private ImageView capturedImageHolder;
    Socket s ;
    private final int REQ_CODE_SPEECH_INPUT = 100;


    URL url;
    boolean yo;
    TextToSpeech t1;

    public boolean isURLReachable(Context context) {


        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                //  URL url = new URL("http://facerecognition.us-west-2.elasticbeanstalk.com/");   // Change to "http://google.com" for www  test.
                //  URL url = new URL("http://192.168.0.44:8080/MyServletProject/DoubleMeServlet");

                //    URL url = new URL("http://192.168.43.218:8080/MyServletProject/DoubleMeServlet");
                System.setProperty("http.keepAlive", "false");

                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setUseCaches(false);
                urlc.setRequestProperty("Connection", "Keep-Alive");


                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.setMessage("Trying to connect to Server...");
                        pd.setTitle("     Checking Connection ");
                        pd.show();
                        //    pd.setCancelable(true);

                        //  pd.hide();


                    }
                });
                Log.d("Exception", "its here 1 !");
                urlc.setConnectTimeout(5 * 1000);
                urlc.getReadTimeout();
                Log.d("Exception", "its here 2 !");
                urlc.connect();

                Log.d("Exception", "its here 3 !");
                urlc.getReadTimeout();
                Log.d("Exception", "its here 3 !" + urlc.getResponseCode());
                if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
                    Log.d("Exception", "Success !");
                    urlc.disconnect();
                    return true;
                } else {
                    urlc.disconnect();
                    Log.d("Exception", "fail1 !");
                    return false;
                }
            } catch (MalformedURLException e1) {
                Log.d("Exception", "fail 2!");
                return false;
            } catch (IOException e) {
                Log.d("Exception", "fail 3!");
                return false;

            } catch (Exception e) {
                Log.d("Exception", "fail 4!");
                return false;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        //   txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        //  Button Button1 = (Button) findViewById(R.id.button1);
        //  ImageView = (ImageView) findViewById(R.id.imageview);
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cameraPreviewLayout = (FrameLayout)findViewById(R.id.camera_preview);


        camera = checkDeviceCamera();
        mImageSurfaceView = new ImageSurfaceView(MainActivity.this, camera);
        cameraPreviewLayout.addView(mImageSurfaceView);



        //  String p="Say One if you want to read text .Say Two if you want to know about your surroundings";
        //   t1.speak(p, TextToSpeech.QUEUE_FLUSH, null);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                promptSpeechInput();
                //   camera.takePicture(null, null, pictureCallback);
                Log.d("haha","got");
                Toast.makeText(MainActivity.this, "Shake!", Toast.LENGTH_SHORT).show();
            }
        });
        //   promptSpeechInput();
        //disable button if user has no camera
        if (!hasCamera()) {

            //     Button1.setEnabled(false);

        }
    }
    public void myMethod(View view)
    {
        promptSpeechInput();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
    private Camera checkDeviceCamera(){
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCamera;
    }
    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            camera.startPreview();
            photo = BitmapFactory.decodeByteArray(data, 0, data.length);
            if(photo==null){
                Toast.makeText(MainActivity.this, "Captured image is empty", Toast.LENGTH_LONG).show();
                return;
            }
            //  ImageView.setImageBitmap(scaleDownBitmapImage(photo, 300, 400 ));
            if (inp == 1) {
                Frame imageFrame = new Frame.Builder()
                        .setBitmap(photo)
                        .build();
                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < textBlocks.size(); i++) {

                    TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));

                    String text = textBlock.getValue();
                    Log.d("Exception", "" + text);
                    sb.append(text);
                    sb.append("     .     \n");


                    //  Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

                }
                if(textBlocks.size()>0)
                    t1.speak(sb.toString(), TextToSpeech.QUEUE_FLUSH, null);
                else
                {
                    String spp="No   text   found .";
                    t1.speak(spp, TextToSpeech.QUEUE_FLUSH, null);
                }
             /*  final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        promptSpeechInput();

                    }
                }, 5000);*/

            }
            else        if (inp == 2) {
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                //   final int lnth=photo.getByteCount();
                //  final int lnth=byteSizeOf(photo);
                ///  Log.d("Exception", "hoho " + lnth);
                //  ByteBuffer dst= ByteBuffer.allocate(lnth);
                /// photo.copyPixelsToBuffer(dst);
                //   final byte[] barray=dst.array();
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(photo, 400, 500, true);
                if(resizedBitmap!=null) {
                    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    //
                    byteArray = byteArrayOutputStream.toByteArray();
                    Log.d("Exception", "hoho " + byteArray.length);
                    // final String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    // photo=darkenBitMap(photo);
                    //    photo=RotateBitmap(photo, 0);
                    //   pics=RotateBitmap(pics,90);
                    //If you want to show the image

                    //    final String st=bps.toString();
                    Log.d("Exception", "hoho un");

                    sendpic();
                }

            }


        }
    };
    public void sendpic()
    {
        Log.d("Exception", "hello");

        try {   //tv.setText("ritik12");
            Log.d("Exception", "2ohoh222");

            s = new Socket("192.168.43.218", 7000);
            Log.d("Exception", "2222ohoh");
            //   s.setSoTimeout(5000);
            Log.d("Exception", "2222");
                /*        ////    String name = "desire.png";

                          //  FileOutputStream fos = openFileOutput(name,Context.MODE_PRIVATE);
                            fos.write(barray);
fos.flush();
                            fos.close();
*/
                        /*    OutputStrea                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              m os = s.getOutputStream();
                            PrintWriter pw = new PrintWriter(os);
Log.d("Exception","size= "+st.length());
                            pw.println(st);*/
                           /* File ff=new File(name);
                            FileInputStream fis = new FileInputStream("/data/data/com.example.ritik.btpcamera/files/desire.png");

                            int size = fis.available();

                            byte[] data = new byte[size];
                            Log.d("Exception", "size= " + size);
                            fis.read(data);
                           // dos.writeInt(size);
                           // dos.write(data);*/
            //        Log.d("Exception",""+st);

            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            s.setReuseAddress(true);
            Log.d("Exception", "222");
            //  dos.writeInt(5);
            dos.writeInt(byteArray.length);
            dos.write(byteArray);
            //  dos.writeInt(lnth);

            //     dos.flush();

            //     dos.close();
            Log.d("Exception", "22");
            BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            Log.d("Exception", "21");
            String line = null;
            while ((line = reader.readLine()) != null) {

                t1.speak(line, TextToSpeech.QUEUE_FLUSH, null);
            }
              /*      final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            promptSpeechInput();

                        }
                    }, 5000);
*/
            //   reader.close();


            s.close();
            //     Log.d("Exception", "2");


        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                public void run() {
                    //  pd.hide();
                    // tv.setText(tot.toString());
                    //  tv.setText("ritikexp " + e.toString());
                }
            });
            Log.d("Exception", e.toString() + " uulalalsadasa");
            Log.d("Exception", e.toString() + " uulalala");

        } finally {
            Log.d("Exception", " fuck");
                  /* final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            promptSpeechInput();

                        }
                    }, 5000);*/
        }





    }
    private Bitmap scaleDownBitmapImage(Bitmap bitmap, int newWidth, int newHeight){
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        return resizedBitmap;
    }
    private void promptSpeechInput() {



        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    //check for camera
    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }



    public void launchCamera1(View view) {

        camera.takePicture(null, null, pictureCallback);
        if (inp == 2) {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //   final int lnth=photo.getByteCount();
            //  final int lnth=byteSizeOf(photo);
            ///  Log.d("Exception", "hoho " + lnth);
            //  ByteBuffer dst= ByteBuffer.allocate(lnth);
            /// photo.copyPixelsToBuffer(dst);
            //   final byte[] barray=dst.array();
            if(photo!=null) {
                photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                //
                byteArray = byteArrayOutputStream.toByteArray();
                Log.d("Exception", "hoho " + byteArray.length);
                // final String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                // photo=darkenBitMap(photo);
                //    photo=RotateBitmap(photo, 0);
                //   pics=RotateBitmap(pics,90);
                //If you want to show the image

                //    final String st=bps.toString();
                Log.d("Exception", "hoho un");

                sendpic();
            }

        }




        //   Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //take pitcute and pass it to onActivityResult
        //   startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

    }


    int inp=0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> resultss = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    String h=resultss.get(0).toString();
                    Log.d("Exception",""+h);
                    if(h.contains("text")||h.contains("book")||h.contains("logo")||h.contains("sign")||h.contains("read")||h.contains("label")||h.contains("letter")||h.contains("poster"))
                        inp=1;
                    else if(h.contains("around")||h.contains("surrounding")||h.contains("environment")||h.contains("object")||h.contains("who")||h.contains("people")||h.contains("thing")||h.contains("person")||h.contains("where")) inp=2;
                    Log.d("Exception", "" + inp);
                    //   txtSpeechInput.setText(resultss.get(0));
                    camera.takePicture(null, null, pictureCallback);



                }

                //    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //take pitcute and pass it to onActivityResult
                //  startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
            break;
        }

    }



}



