package com.example.mad_camp_week2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
  TextView facebook_id;
  ProgressDialog mDialog;
  ImageView facebook_profile;
  Button ok_button,name_button;

  String Name="";

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

    facebook_id = (TextView)findViewById(R.id.facebook_id);
    facebook_profile = (ImageView)findViewById(R.id.facebook_profile);
    ok_button = (Button)findViewById(R.id.ok_button);
    name_button = (Button)findViewById(R.id.name_button);

    LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
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
        parameters.putString("fields","id,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();


      }

      @Override
      public void onCancel() {

      }

      @Override
      public void onError(FacebookException error) {

      }
    });

    //Init Service
    Retrofit retrofitClient = RetrofitClient.getInstance();
    iMyService = retrofitClient.create(IMyService.class);

    if(AccessToken.getCurrentAccessToken() != null)
    {
      name_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          loginUser(AccessToken.getCurrentAccessToken().getUserId());
          facebook_id.setText(Name);
        }
      });

      URL profile_picture = null;
      try {
        profile_picture = new URL("http://graph.facebook.com/"+ facebook_id+"/picture?width=250&height=250");
        Picasso.get().load(profile_picture.toString()).into(facebook_profile);
        //facebook_id.setText(AccessToken.getCurrentAccessToken().getUserId());
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }

    }

    //Transition to Tab Activity
    ok_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Log.v("check 3 : ", "hi");
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
      }
    });
  }

  private void getData(JSONObject object) {
    try{
      registerUser(object.getString("id"),object.getString("first_name"));
      URL profile_picture = new URL("http://graph.facebook.com/"+object.getString("id")+"/picture?width=250&height=250");
      Picasso.get().load(profile_picture.toString()).into(facebook_profile);

      facebook_id.setText(object.getString("first_name"));
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  //Function for DB Server Connection
  private void registerUser(String id, String name) {
    compositeDisposable.add(iMyService.registerUser(id,name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<String>() {
              @Override
              public void accept(String response) throws Exception {
                Toast.makeText(SplashActivity.this, ""+response, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SplashActivity.this, ""+response, Toast.LENGTH_SHORT).show();
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