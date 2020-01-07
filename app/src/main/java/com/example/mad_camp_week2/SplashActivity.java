package com.example.mad_camp_week2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mad_camp_week2.Retrofit.IMyService;
import com.example.mad_camp_week2.Retrofit.RetrofitClient;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SplashActivity extends AppCompatActivity {
  //Ask permission
  String[] PERMISSIONS = {
          android.Manifest.permission.READ_CONTACTS,
          android.Manifest.permission.READ_EXTERNAL_STORAGE
  };

  //Facebook Login System
  CallbackManager callbackManager;
  ProgressDialog mDialog;
  private ImageView facebook_profile;
  private Button ok_button;
  private String Name="NAME";
  private Boolean load_profile=false;
  private Animation fade_in_move_up;
  private FrameLayout screen;
  private LinearLayout app_logo, login_splash;
  private ImageView app_name;
  private Boolean new_user = true;
  private String fb_id = "";
  private String number = "";
  private String friends_list = "0000000000000001,0000000000000002,0000000000000003,0000000000000004,0000000000000005,0000000000000006";

  //Connect to DB
  CompositeDisposable compositeDisposable = new CompositeDisposable();
  IMyService iMyService;

  @Override
  protected void onStop() {
    compositeDisposable.clear();
    super.onStop();
  }

  @Override
  protected void onActivityResult (int requestCode, int resultCode, Intent data){
    super.onActivityResult(requestCode,resultCode,data);
    callbackManager.onActivityResult(requestCode,resultCode,data);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    //Ask permission in first time
    askPermission();
    while(!hasPermissions(this,PERMISSIONS)){}
    Log.v("Test already", "Pass");
    callbackManager = CallbackManager.Factory.create();
    fade_in_move_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_move_up);
    facebook_profile = (ImageView)findViewById(R.id.facebook_profile);
    ok_button = (Button)findViewById(R.id.ok_button);
    screen = (FrameLayout)findViewById(R.id.screen);
    login_splash = (LinearLayout)findViewById(R.id.login_list);
    app_logo = (LinearLayout)findViewById(R.id.app_logo);
    app_name = (ImageView)findViewById(R.id.app_name);

    final LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
    loginButton.setReadPermissions(Arrays.asList("public_profile","email"));

    loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
      @Override
      public void onSuccess(LoginResult loginResult) {
        mDialog = new ProgressDialog(SplashActivity.this);
        mDialog.setMessage("Retrieving data...");
        mDialog.show();

        String accesstoken = loginResult.getAccessToken().getToken();

        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
          @Override
          public void onCompleted(JSONObject object, GraphResponse response) {
            mDialog.dismiss();

            getData(object);
          }
        });

        //Request Graph API
        Bundle parameters = new Bundle();
        parameters.putString("fields","id,name,birthday,gender,friends");
        request.setParameters(parameters);
        request.executeAsync();
      }

      @Override
      public void onCancel() {}

      @Override
      public void onError(FacebookException error) {}
    });

    //Connecting server Init Service
    Retrofit retrofitClient = RetrofitClient.getInstance();
    iMyService = retrofitClient.create(IMyService.class);

    if(AccessToken.getCurrentAccessToken() != null)
    {
      fb_id = AccessToken.getCurrentAccessToken().getUserId();
      if(!load_profile){
        load_profile = true;
        loginUser(fb_id);
        //Toast.makeText(SplashActivity.this, "Login", Toast.LENGTH_SHORT).show();
        Picasso.get().load("http://graph.facebook.com/"+ fb_id+"/picture?width=250&height=250").into(facebook_profile);
      }
    }

    //Transition to Tab Activity
    ok_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra("fbid",fb_id);
        startActivity(intent);

      }
    });
    screen.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        screen.setEnabled(false);
        ObjectAnimator animation = ObjectAnimator.ofFloat(app_logo, "translationY", -190f);
        ObjectAnimator animation2= ObjectAnimator.ofFloat(app_name,"alpha",1f,0.0f);
        animation.setDuration(1000);
        animation2.setDuration(500);
        animation.start();
        animation2.start();
        login_splash.setVisibility(View.VISIBLE);
        login_splash.startAnimation(fade_in_move_up);
      }
    });
  }

  private void getData(JSONObject object) {
    try{
      fb_id = object.getString("id");
      registerUser(object.getString("id"),
              object.getString("name"),
              object.getString("birthday"),
              object.getString("gender"),
              friends_list,
              "http://graph.facebook.com/"+object.getString("id")+"/picture?width=250&height=250",
              number);
      URL profile_picture = new URL("http://graph.facebook.com/"+object.getString("id")+"/picture?width=250&height=250");
      Toast.makeText(SplashActivity.this, "페이스북 로그인 중...", Toast.LENGTH_SHORT).show();
      Picasso.get().load(profile_picture.toString()).into(facebook_profile);

      ok_button.setText('"'+object.getString("name")+'"'+"으로 로그인 하시겠습니까?");
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  //Function for DB Server Connection
  private void registerUser(String id, String name, String birthday, String gender, String friends_list, String profile_url,String number) {
    compositeDisposable.add(iMyService.registerUser(id,name,birthday,gender,friends_list,profile_url,":",number)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<String>() {
              @Override
              public void accept(String response) throws Exception {
                if(response.contains("success")){  //서버에 등록된 계정이 아니면 전화번호를 받아야함
                  final View register_layout = LayoutInflater.from(SplashActivity.this)
                          .inflate(R.layout.register_number, null);

                  new MaterialStyledDialog.Builder(SplashActivity.this)
                          .setTitle("REGISTRATION")
                          .setCustomView(register_layout)
                          .setNegativeText("CANCEL")
                          .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                              dialog.dismiss();
                            }
                          })
                          .setPositiveText("REGISTER")
                          .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                              MaterialEditText edt_register_number = (MaterialEditText)register_layout.findViewById(R.id.edt_number);

                              if(TextUtils.isEmpty(edt_register_number.getText().toString())){
                                Toast.makeText(SplashActivity.this, "Number cannot be null or empty", Toast.LENGTH_SHORT).show();
                                return;
                              }
                              Toast.makeText(SplashActivity.this, ""+edt_register_number.getText().toString(), Toast.LENGTH_SHORT).show();
                              registerNumber(fb_id,edt_register_number.getText().toString());
                            }
                          }).show();
                }
              }
            }));
  }
  private void loginUser(String id) {
    compositeDisposable.add(iMyService.loginUser(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<String>() {
              @Override
              public void accept(String response) throws Exception {
                Name = response;
                //Toast.makeText(SplashActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
                ok_button.setText(Name+"으로 로그인 하시겠습니까?");
              }
            }));
  }
  private void registerNumber(String id, String number) {
    compositeDisposable.add(iMyService.registerNumber(id, number)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<String>() {
              @Override
              public void accept(String response) throws Exception {
                Toast.makeText(SplashActivity.this, "등록 완료!", Toast.LENGTH_SHORT).show();

                new_user = false;
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
              }
            }));
  }

  //Functions for ask permission
  private void askPermission() {
    int PERMISSION_ALL = 1;

    if (!hasPermissions(this, PERMISSIONS)) {
      ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
    }
  }
  public static boolean hasPermissions(Context context, String... permissions) {
    if (context != null && permissions != null) {
      for (String permission : permissions) {
        if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
          return false;
        }
      }
    }
    return true;
  }
}