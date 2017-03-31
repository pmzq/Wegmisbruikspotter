package nl.wegmisbruikspotter.maps;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import static android.os.SystemClock.sleep;
import static com.facebook.AccessToken.getCurrentAccessToken;

public class MainLogin extends Activity {

    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private static final String TAG = "Test";
    public AccessToken test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        //callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main_login);
        info = (TextView)findViewById(R.id.info);
        //loginButton = (LoginButton)findViewById(R.id.login_button);


        //LoginManager.getInstance().logOut();
        //sleep(1000);

        /*FacebookSdk.sdkInitialize(getApplicationContext(), new FacebookSdk.InitializeCallback() {
            @Override
            public void onInitialized() {
                if(AccessToken.getCurrentAccessToken() == null){
                    System.out.println("not logged in yet");
                } else {
                    System.out.println("Logged in");
                }
            }
        });*/

        test = AccessToken.getCurrentAccessToken();

        if (test != null) {
            Log.v(TAG, "YES");
        }   else {
            Log.v(TAG, "NO!");
        }

        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            Log.v(TAG, "Logged, user name=" + profile.getFirstName() + " " + profile.getLastName());
            String facebookID = getCurrentAccessToken().getUserId();
            ((Globals) getApplication()).setfacebookName(profile.getFirstName());
            ((Globals) getApplication()).setfacebookID(facebookID);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Log.v(TAG, "Logged, user name=");
        }

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            private ProfileTracker mProfileTracker;
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            // profile2 is the new profile
                            Log.v("facebook - profile", profile2.getFirstName());
                            Intent intent = new Intent(MainLogin.this, MainActivity.class);
                            String facebookID = getCurrentAccessToken().getUserId();
                            ((Globals) getApplication()).setfacebookID(facebookID);
                            Log.v(TAG, "Logged, user name=" + profile2.getFirstName());
                            ((Globals) getApplication()).setfacebookID(profile2.getFirstName());
                            startActivity(intent);
                            mProfileTracker.stopTracking();
                        }
                    };
                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.
                }
                else {
                    Profile profile = Profile.getCurrentProfile();
                    Log.v("facebook - profile", profile.getFirstName());
                    Log.v(TAG, "Logged, user name=" + profile.getFirstName());
                    String facebookID = getCurrentAccessToken().getUserId();
                    ((Globals) getApplication()).setfacebookID(facebookID);
                    ((Globals) getApplication()).setfacebookName(profile.getFirstName());
                    Intent intent = new Intent(MainLogin.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancel() {
                Log.v("facebook - onCancel", "cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.v("facebook - onError", e.getMessage());
            }

        });

        /*loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            // profile2 is the new profile
                            Log.v("facebook - profile", profile2.getFirstName());
                            Intent intent = new Intent(MainLogin.this, MainActivity.class);
                            String facebookID = getCurrentAccessToken().getUserId();
                            ((Globals) getApplication()).setfacebookID(facebookID);
                            Log.v(TAG, "Logged, user name=" + profile2.getFirstName());
                            ((Globals) getApplication()).setfacebookID(profile2.getFirstName());
                            startActivity(intent);
                            mProfileTracker.stopTracking();
                        }
                    };
                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.
                }
                else {
                    Profile profile = Profile.getCurrentProfile();
                    Log.v("facebook - profile", profile.getFirstName());
                    Log.v(TAG, "Logged, user name=" + profile.getFirstName());
                    String facebookID = getCurrentAccessToken().getUserId();
                    ((Globals) getApplication()).setfacebookID(facebookID);
                    ((Globals) getApplication()).setfacebookName(profile.getFirstName());
                    Intent intent = new Intent(MainLogin.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancel() {
                Log.v("facebook - onCancel", "cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.v("facebook - onError", e.getMessage());
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
