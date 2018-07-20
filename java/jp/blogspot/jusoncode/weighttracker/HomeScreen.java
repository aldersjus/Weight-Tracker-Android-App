    package jp.blogspot.jusoncode.weighttracker;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import static jp.blogspot.jusoncode.weighttracker.GlobalVariables.BARS;
import static jp.blogspot.jusoncode.weighttracker.GlobalVariables.TOP;
import static jp.blogspot.jusoncode.weighttracker.R.id.enterDetails;
import static jp.blogspot.jusoncode.weighttracker.R.id.frameLayoutOne;



public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,EnterWeight.Communication,EnterDetails.CommunicationHere {

    /**
     * implemented method for EnterDetails.CommunicationHere
     * after details entered saves shared preferences
     */
    @Override
    public void detailsEntered() {
        WeightTracker weightTracker = new WeightTracker();
        fm.beginTransaction().replace(frameLayoutOne, weightTracker, "weightTracker").commit();
        Graph graph = new Graph();
        fm.beginTransaction().replace(R.id.frameLayoutTwo, graph, "graph").commit();
        saveSharedPreferences();
    }

    /**
     * implemented method for communication of fragments EnterWeight
     *
     * @param weight //string passed to weight tracker
     */
    @Override
    public void passWeight(String weight) {
        WeightTracker weightTracker = (WeightTracker) getFragmentManager().findFragmentByTag("weightTracker");
        if (weightTracker != null) {
            weightTracker.updateWeight(weight);
        }
    }

    static boolean dark = false;
    static boolean top = false;
    static boolean alarmSet = false;
    static boolean alarmCancel = false;
    static boolean intentSent = false;
    static boolean tablet;
    static boolean jobScheduled;
    static boolean firstLoad = true;
    static boolean kilograms = true;

    static String measurement;
    static String userName;
    static String userAge;
    static String userHeight;
    static String userWeight;
    static String userTargetWeight;
    static String sex;
    static String userSet;

    static int displayBarNumber;
    private FragmentManager fm;
    private final static int TWENTY_FOUR_HOURS = 86400000;
    private static PendingIntent pendingIntent;
    static DatabaseAccess databaseAccess;
    private static AlarmManager alarmManager;

    private final String MEASUREMENT = "measurement";
    private final String FIRST_LOAD = "firstLoad";
    private final String USER_SET = "set";
    private final String USER_NAME = "userName";
    private final String AGE = "age";
    private final String USER_HEIGHT = "userHeight";
    private final String USER_WEIGHT = "userWeight";
    private final String USER_WEIGHT_TARGET = "userTargetWeight";
    private final String SEX = "sex";
    private final String BACKGROUND = "background";
    private final String ALARM_SET = "alarm";
    private final String JOB_SCHEDULED = "job_scheduled";
    private FrameLayout frameLayoutTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        frameLayoutTwo = (FrameLayout) findViewById(R.id.frameLayoutTwo);

        fm = getFragmentManager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.popBackStack();
                if (!top) {
                    WeightTracker weightTracker = new WeightTracker();
                    fm.beginTransaction().replace(frameLayoutOne, weightTracker, "weightTracker").commit();
                    EnterWeight enterWeight = new EnterWeight();
                    fm.beginTransaction().replace(R.id.frameLayoutTwo, enterWeight).commit();
                } else {
                    WeightTracker weightTracker = new WeightTracker();
                    fm.beginTransaction().replace(R.id.frameLayoutTwo, weightTracker, "weightTracker").commit();
                    EnterWeight enterWeight = new EnterWeight();
                    fm.beginTransaction().replace(frameLayoutOne, enterWeight).commit();
                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            loadDatabase();
            clearFrameLayout();
            loadSharedPreferences();
        }


        if (firstLoad) {
            firstLoad = false;
            FrameLayout frameLayoutTwo = (FrameLayout) findViewById(R.id.frameLayoutTwo);
            WelcomeScreen welcomeScreen = new WelcomeScreen();
            fm.beginTransaction().replace(frameLayoutOne, welcomeScreen).commit();
            frameLayoutTwo.removeAllViews();
            Tutorial tutorial = new Tutorial();
            tutorial.show(this.getSupportFragmentManager(), "tutorial");
        }


        //Adjusts screen slightly for 10 inch...
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {

            int screenOrientation = this.getResources().getConfiguration().orientation;
            int size = getBaseContext().getResources().getDisplayMetrics().widthPixels;

            if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (size > 2400) {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.content_home_screen);
                    linearLayout.setPadding(300, 50, 0, 50);
                }
            }
        }


    }

    public void checkTargetReached() {
        if (!userName.equalsIgnoreCase("_") & Double.valueOf(userWeight) <= Double.valueOf(userTargetWeight)) {

            Congratulations congratulations = new Congratulations();
            congratulations.show(this.getSupportFragmentManager(), "congrats");
        }
    }


    ////CAN SET THE WIDGET TO BE UPDATED EVERY 24 HOURS IN THE XML FILE, BUT IF USING JOB SCHEDULER IT
    ///CAN BE SET  TO RUN ONLY WHEN DEVICE IS CONNECTED FOR CHARGING MIGHT WANT TO ADJUST TIMING MAYBE 12 HOURS ETC....
    static void setJobScheduler(Context context) {


        //final JobScheduler mJobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        final JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(context.getPackageName(), WeightTrackerJobScheduler.class.getName()));

        //////BELOW RUNS EVERY 3 SECONDS
        /////
        //builder.setPeriodic(3000);
        builder.setPeriodic(TWENTY_FOUR_HOURS);///24 hours
        /////SEE IF BELOW RUNS ON CHARGE, NEEDS API 21 OR OVER..  ELSE CHANGE TO USE FIREBASE INSTEAD OF JOBSCHEDULER....
        // builder.setRequiresCharging(true);
        ////WORKS AFTER A REBOOT
        builder.setPersisted(true);

    }



    ///BELOW SETS ALARM TO LAUNCH INTENT TO SEND NOTIFICATION AT 7AM EVERY MORNING, BUT NEED API 24...
    ///CURRENTLY TESTING BY JUST REPEATING THE ALARM EVERY MINUTE.....
    static void setAlarm(Context context) {
        ////Was going to use a service, but seems unnecessary. Can be done by AlarmManger........

       /*NEED API LEVEL 24
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,7);
        */

        Intent intent = new Intent(context, WeightTrackerAlarmBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);//plus ten second
        ////TEST THIS CODE BELOW USE WITH API 24, WILL WAKE DEVICE EVEN IF IN DOZE MODE.... REPLACES THE NEXT LINE OF CODE...
        // alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP ,AlarmManager.INTERVAL_DAY,pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (TWENTY_FOUR_HOURS), pendingIntent);//24 hours
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP ,System.currentTimeMillis(),(60000),pendingIntent);//one minute
        /////For above calendar.... NEED API LEVEL 24
        //alarmManager.setInexactRepeating(AlarmManager.RTC,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

        alarmSet = true;

        Toast.makeText(context, context.getString(R.string.alarmSetting), Toast.LENGTH_SHORT).show();
    }

    static void cancelAlarm() {
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            alarmCancel = true;
        }
    }

    public void loadDatabase() {

        AsyncTask<Void, Void, Void> loadDatabase = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                databaseAccess = new DatabaseAccess(getApplicationContext());
                databaseAccess.getWeights();
                return null;
            }
        };

        loadDatabase.execute();

    }


    public void setPositionOfFragments() {

        fm.popBackStack();

        if (top) {
            fm = getFragmentManager();
            WeightTracker weightTracker = new WeightTracker();
            fm.beginTransaction().replace(R.id.frameLayoutTwo, weightTracker, "weightTracker").commit();
            Graph graph = new Graph();
            fm.beginTransaction().replace(frameLayoutOne, graph, "graph").commit();

        } else {
            fm = getFragmentManager();
            WeightTracker weightTracker = new WeightTracker();
            fm.beginTransaction().replace(frameLayoutOne, weightTracker, "weightTracker").commit();
            Graph graph = new Graph();
            fm.beginTransaction().replace(R.id.frameLayoutTwo, graph, "graph").commit();
        }

    }

    public void loadSharedPreferences() {
        final boolean DEFAULT_VALUE = false;
        final String DEFAULT_VALUE_STRING = "_";
        final String DEFAULT_VALUE_WEIGHT = "0.0";
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        dark = sharedPreferences.getBoolean(BACKGROUND, DEFAULT_VALUE);
        top = sharedPreferences.getBoolean(GlobalVariables.TOP, DEFAULT_VALUE);
        alarmSet = sharedPreferences.getBoolean(ALARM_SET, DEFAULT_VALUE);
        alarmCancel = sharedPreferences.getBoolean(GlobalVariables.ALARM_CANCEL, DEFAULT_VALUE);
        jobScheduled = sharedPreferences.getBoolean(JOB_SCHEDULED, DEFAULT_VALUE);
        userName = sharedPreferences.getString(USER_NAME, DEFAULT_VALUE_STRING);
        userAge = sharedPreferences.getString(AGE, DEFAULT_VALUE_STRING);
        userHeight = sharedPreferences.getString(USER_HEIGHT, DEFAULT_VALUE_STRING);
        userWeight = sharedPreferences.getString(USER_WEIGHT, DEFAULT_VALUE_WEIGHT);
        userTargetWeight = sharedPreferences.getString(USER_WEIGHT_TARGET, DEFAULT_VALUE_WEIGHT);
        sex = sharedPreferences.getString(SEX, DEFAULT_VALUE_STRING);
        userSet = sharedPreferences.getString(USER_SET, "no");
        firstLoad = sharedPreferences.getBoolean(FIRST_LOAD, true);
        displayBarNumber = sharedPreferences.getInt(BARS, 7);
        kilograms = sharedPreferences.getBoolean(GlobalVariables.KILOS_OR_POUNDS, true);
        measurement = sharedPreferences.getString(MEASUREMENT, "kg");

        if (dark) {
            this.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

        } else {
            this.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        }

        if (!alarmSet & !alarmCancel) {
            setAlarm(this);
            alarmSet = true;
        }

        if (!jobScheduled) {
            setJobScheduler(this);
            jobScheduled = true;
        }

        if (alarmCancel) {
            cancelAlarm();
        }

        setPositionOfFragments();
    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().findFragmentById(R.id.frameLayoutOne) != getFragmentManager().findFragmentByTag("weightTracker") &
                getFragmentManager().findFragmentById(R.id.frameLayoutTwo) != getFragmentManager().findFragmentByTag("weightTracker")) {

            setPositionOfFragments();

        } else if (getFragmentManager().findFragmentById(R.id.frameLayoutOne) != getFragmentManager().findFragmentByTag("graph") &
                getFragmentManager().findFragmentById(R.id.frameLayoutTwo) != getFragmentManager().findFragmentByTag("graph")) {

            setPositionOfFragments();
        } else {

            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, ActivitySettings.class);
            intent.putExtra(GlobalVariables.DARK, dark);
            intent.putExtra(GlobalVariables.ALARM_CANCEL, alarmCancel);
            intent.putExtra(TOP, top);
            intent.putExtra(GlobalVariables.BARS, displayBarNumber);
            startActivity(intent);
        } else if (id == R.id.itemShare) {
            shareWeightLoss();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.homeScreen) {

            fm.popBackStack();
            if (!top) {
                WeightTracker weightTracker = new WeightTracker();
                fm.beginTransaction().replace(frameLayoutOne, weightTracker, "weightTracker").commit();
                Graph graph = new Graph();
                fm.beginTransaction().replace(R.id.frameLayoutTwo, graph, "graph").commit();
            } else {

                WeightTracker weightTracker = new WeightTracker();
                fm.beginTransaction().replace(R.id.frameLayoutTwo, weightTracker, "weightTracker").commit();
                Graph graph = new Graph();
                fm.beginTransaction().replace(frameLayoutOne, graph, "graph").commit();

            }
        } else if (id == R.id.enterNew) {
            fm.popBackStack();
            if (!top) {
                WeightTracker weightTracker = new WeightTracker();
                fm.beginTransaction().replace(frameLayoutOne, weightTracker, "weightTracker").commit();
                EnterWeight enterWeight = new EnterWeight();
                fm.beginTransaction().replace(R.id.frameLayoutTwo, enterWeight).commit();
            } else {
                WeightTracker weightTracker = new WeightTracker();
                fm.beginTransaction().replace(R.id.frameLayoutTwo, weightTracker, "weightTracker").commit();
                EnterWeight enterWeight = new EnterWeight();
                fm.beginTransaction().replace(frameLayoutOne, enterWeight).commit();
            }

        } else if (id == enterDetails) {
            fm.popBackStack();
            EnterDetails enterDetails = new EnterDetails();
            fm.beginTransaction().replace(frameLayoutOne, enterDetails, "enterDetails").commit();
            frameLayoutTwo.removeAllViews();
        } else if (id == R.id.displayWeights) {
            fm.popBackStack();
            DisplayWeights displayWeights = new DisplayWeights();
            fm.beginTransaction().replace(frameLayoutOne, displayWeights, "seeAll").commit();
            frameLayoutTwo.removeAllViews();
        } else if (id == R.id.settingsMenu) {
            fm.popBackStack();
            Intent intent = new Intent(this, ActivitySettings.class);
            intent.putExtra(GlobalVariables.DARK, dark);
            intent.putExtra(GlobalVariables.ALARM_CANCEL, alarmCancel);
            intent.putExtra(TOP, top);
            intent.putExtra(GlobalVariables.BARS, displayBarNumber);
            startActivity(intent);
        } else if (id == R.id.menu_item_share) {

            shareWeightLoss();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareWeightLoss() {

        Weight firstWeight = databaseAccess.databaseList.get(databaseAccess.databaseList.size() - 1);
        double lost = Double.valueOf(firstWeight.weight) - Double.valueOf(userWeight);

        Intent yo = ShareCompat.IntentBuilder.from(this).setType("text/plain").setText(String.format(getString(
                R.string.shareString), String.valueOf(lost), measurement)).getIntent();
        try {
            startActivity(Intent.createChooser(yo, "Share with"));
        } catch (Exception e) {
            Toast.makeText(this, "Failed to share, report to developer", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveSharedPreferences() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(BACKGROUND, dark);
        editor.putBoolean(TOP, top);
        editor.putBoolean(ALARM_SET, alarmSet);
        editor.putBoolean(GlobalVariables.ALARM_CANCEL, alarmCancel);
        editor.putBoolean(JOB_SCHEDULED, jobScheduled);
        editor.putString(USER_NAME, userName);
        editor.putString(AGE, userAge);
        editor.putString(USER_HEIGHT, userHeight);
        editor.putString(USER_WEIGHT, userWeight);
        editor.putString(USER_WEIGHT_TARGET, userTargetWeight);
        editor.putString(SEX, sex);
        editor.putString(USER_SET, userSet);
        editor.putBoolean(FIRST_LOAD, firstLoad);
        editor.putInt(GlobalVariables.BARS, displayBarNumber);
        editor.putBoolean(GlobalVariables.KILOS_OR_POUNDS, kilograms);
        editor.putString(MEASUREMENT, measurement);
        editor.apply();

    }

    public void onPause() {
        super.onPause();
        saveSharedPreferences();
    }

    private void clearFrameLayout() {
        if (getFragmentManager().findFragmentById(R.id.frameLayoutOne) == getFragmentManager().findFragmentByTag("enterDetails")) {
            frameLayoutTwo.removeAllViews();
        }
        if (getFragmentManager().findFragmentById(R.id.frameLayoutOne) == getFragmentManager().findFragmentByTag("seeAll")) {
            frameLayoutTwo.removeAllViews();
        }

        if (getFragmentManager().findFragmentById(R.id.frameLayoutOne) == getFragmentManager().findFragmentByTag("about")) {
            frameLayoutTwo.removeAllViews();
        }
    }

    public void onResume() {
        super.onResume();

        clearFrameLayout();

        Intent intent = getIntent();
        intentSent = intent.getBooleanExtra("intentSent", false);
        if (intentSent) {
            dark = intent.getBooleanExtra(GlobalVariables.DARK, false);
            top = intent.getBooleanExtra("position", false);
            alarmCancel = intent.getBooleanExtra(GlobalVariables.ALARM_CANCEL, false);
            displayBarNumber = intent.getIntExtra(GlobalVariables.BARS, 7);

            if (dark) {

                this.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

            } else {
                this.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));//23 ?????
            }

            setPositionOfFragments();
        } else {
            if (dark) {

                this.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

            } else {
                this.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));//23 ?????
            }

        }

        checkTargetReached();

    }


}
