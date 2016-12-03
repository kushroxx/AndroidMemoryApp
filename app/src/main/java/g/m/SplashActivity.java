package g.m;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import g.m.R;
import g.m.utils.PreferenceManager;
import g.m.utils.Utils;

public class SplashActivity extends AppCompatActivity {

    String now_playing, earned;
    ContentHelper server;
    ProgressBar progress;

    private static SplashActivity instance;

    public static SplashActivity getInstance() {
        return instance;
    }

    // skipping package and imports
    public class ShowDialog implements AlertDialog.OnClickListener {

        void createAndShowDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);//Context parameter
            builder.setNeutralButton("Try Again",ShowDialog.this );
            builder.setTitle("No Internet Connection");
            builder.setMessage("Connect to Internet and try again");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if( Utils.isNetworkAvailable(SplashActivity.this)) {

                progress.setVisibility(View.VISIBLE);
                server = ContentHelper.getInstance();
                server.loadJsonFromServer(getApplicationContext());

                new PrefetchData().execute();
                //  PreferenceManager.get().putInt(PreferenceManager.PREF_ALREADY_CACHED, 1);

            }else {
                createAndShowDialog();
            }
            }
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        instance = this;

        PreferenceManager.get().init(this);
        progress = (ProgressBar)findViewById(R.id.progress_spinner);
      //   int  already_cached=PreferenceManager.get().getInt(PreferenceManager.PREF_ALREADY_CACHED, 0);

       //  Log.e("Memory_app","Cached Value "+already_cached+"network info "+ Utils.isNetworkAvailable(this));

         if( Utils.isNetworkAvailable(this)) {
             server = ContentHelper.getInstance();
             server.loadJsonFromServer(getApplicationContext());

             new PrefetchData().execute();
       //  PreferenceManager.get().putInt(PreferenceManager.PREF_ALREADY_CACHED, 1);

         }else {
             ImageView img_view = (ImageView)findViewById(R.id.imgLogo);
             img_view.setImageAlpha(120);

            new ShowDialog().createAndShowDialog();
         }


    }

    public class NetworkStateReceiver extends BroadcastReceiver {
        private static final String TAG = "NetworkStateReceiver";

        @Override
        public void onReceive(final Context context, final Intent intent) {

            Log.d(TAG, "Network connectivity change");

            if (intent.getExtras() != null) {
                final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

                if (ni != null && ni.isConnectedOrConnecting()) {
                    Log.i(TAG, "Network " + ni.getTypeName() + " connected");
                    server = ContentHelper.getInstance();
                    server.loadJsonFromServer(getApplicationContext());

                    new PrefetchData().execute();
                } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                    Log.d(TAG, "There's no network connectivity");
                }
            }
        }
    }

    public void onDataLoaded (Context context){

        ContentHelper.getInstance().loadLevelData();
        progress.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);

        // close this activity
        finish();

    }

    /**
     * Async Task to make http call
     */
    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls         

        }

        @Override

        protected Void doInBackground(Void... arg0) {


            server.loadAllImagesFromServer(getApplicationContext());
            return null;


        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // After completing http call
            // will close this activity and lauch main activity

        }



    }
}