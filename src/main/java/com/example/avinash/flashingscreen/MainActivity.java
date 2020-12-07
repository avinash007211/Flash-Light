package com.example.avinash.flashingscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
/* Here the main layout is named as mMainLayout and the button layout is named as button area
and also the button ID's is named as Red_color, Yellow_color, Green_color, White_color, SOS_button
* */
    LinearLayout mMainLayout;
    LinearLayout buttonArea;
     Button Red_color;
     Button Yellow_color;
     Button Green_color;
     Button Black_color;
     Button White_color;
     Button SOS_button;
     TextView text;


    ProgressBar the_progress_bar;
    sosActivity SOS_signal;

    int hide_the_text = 0;
    int presentBackgroundColor = Color.rgb(255, 255, 255);
    int indexing, check_the_RadioButton;
/* Here the boolean variables are declared to instantiate the different activities in the program.
* */
    boolean complete_screen = false;
    boolean initialButtonVisibility = true;
    boolean initialButtonVisibilityDialog = false;
    boolean initialColorDialog = false;
    boolean pressed = false;
/* Here the static final string is declared to be use into the later part of the program code
* */
    static final String text_Visibility_string = "text_Visibility_string";
    static final String button_area_Visibility_string = "button area Visibility";
    static final String back_colour_string = "back Colour state";

    static final String storage_foil_string ="startup Storage";
    static final String initial_ButtonLayout_Visibility = "INIBTVI";
    static final String initial_Background_Color = "INITBGC";

/* Here the buttons are assigned there designated tasks of change of color with the help of on
click listener and also assigned  there respective ID's for that operation. The saved instance state
is also implemented here in the program.
* */
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

/*   It listens to the battery level with null receiver ***********************/
        Intent BATTERYintent = this.registerReceiver(null, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
        int level = BATTERYintent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        Log.v(null, "LEVEL" + level);
/******************************************************************************/

        mMainLayout = (LinearLayout) findViewById(R.id.mMainLayout);
        buttonArea = (LinearLayout) findViewById(R.id.buttonArea);
        text = (TextView) findViewById(R.id.textbox);
        Red_color = (Button) findViewById(R.id.red_button);
        Yellow_color = (Button) findViewById(R.id.yellow_button);
        Black_color = (Button) findViewById(R.id.black_button);
        White_color = (Button) findViewById(R.id.white_button);
        Green_color = (Button) findViewById(R.id.green_button);
        SOS_button = (Button) findViewById(R.id.SOS_button);
        the_progress_bar =(ProgressBar) findViewById(R.id.the_progress_bar);

        the_progress_bar.setVisibility(View.GONE);
        initialButtonVisibility = getsavedOnStartupVisibilty();
        setInitialButtonVisibility(initialButtonVisibility);
        presentBackgroundColor = getsavedOnSartupColor();
        mMainLayout.setBackgroundColor(presentBackgroundColor);
        setcheckCurrentColorinRadioGroup(presentBackgroundColor);

        mMainLayout.setOnClickListener(this);
        text.setOnClickListener(this);
        Red_color.setOnClickListener(this);
        Yellow_color.setOnClickListener(this);
        Black_color.setOnClickListener(this);
        White_color.setOnClickListener(this);
        Green_color.setOnClickListener(this);
        SOS_button.setOnClickListener(this);
        restoreparameterState(savedInstanceState);
    }


/* Here the check radio group and buttons program is defined which is used to select the startup
 colour in the application.
* */
    private void setcheckCurrentColorinRadioGroup(int color){
        if (color==Color.rgb(255, 0, 0))
            check_the_RadioButton =(R.id.Radio_red);
        else if (color==Color.rgb(255, 255, 0))
        check_the_RadioButton =(R.id.Radio_yellow);
        else if (color==Color.rgb(0, 255, 0))
                check_the_RadioButton =(R.id.Radio_green);
        else if (color==Color.rgb(0, 0, 0))
                check_the_RadioButton =(R.id.Radio_black);
        else if(color==Color.rgb(255, 255, 255))
                check_the_RadioButton =(R.id.Radio_white);
    }

    private void setInitialButtonVisibility(boolean initialButtonVisibility) {
        if(initialButtonVisibility ==true){
            buttonArea.setVisibility(View.VISIBLE);
        }
        else buttonArea.setVisibility(View.GONE);
    }

    private void stopSOS(){
        if(SOS_signal !=null){
            SOS_signal.cancel(true);
        }
    }
    /* Here the enum class is implemented for assigning the colours*/
    public enum MyColors {
        COLOUR_BLACK,
        COLOUR_WHITE,
        COLOUR_RED,
        COLOUR_YELLOW,
        COLOUR_GREEN;
        static int enumToColor(MyColors mycolour)
        {
            switch (mycolour)
            {
                case COLOUR_BLACK:
                    return Color.BLACK;
                case COLOUR_WHITE:
                    return Color.WHITE;
                case COLOUR_RED:
                    return Color.RED;
                case COLOUR_YELLOW:
                    return Color.YELLOW;
                case COLOUR_GREEN:
                    return Color.GREEN;
                default:
                    return Color.BLACK;
            }
        }
        static MyColors intToEnum(int mycolor) {
            return MyColors.values()[mycolor];
        }
    };





/* Here the On click activity program is defined with STOP SOS functionality.
* */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.mMainLayout: {
                if (buttonArea.getVisibility() == View.VISIBLE) {
                    buttonArea.setVisibility(View.GONE);
                    hide_the_text++;
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    complete_screen =true;
                    stopSOS();
                } else if (buttonArea.getVisibility() == View.GONE) {
                    buttonArea.setVisibility(View.VISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    complete_screen =false;
                    stopSOS();
                }
                if (hide_the_text > 0) {
                    text.setVisibility(View.GONE);
                }
            }
            break;
            case R.id.red_button:
                MyColors objColors;
                objColors=MyColors.COLOUR_BLACK;
                MyColors red;
                red=MyColors.COLOUR_RED;
                objColors.enumToColor(red);

                mMainLayout.setBackgroundColor(MyColors.enumToColor(MyColors.COLOUR_RED));
                buttonArea.setVisibility(View.GONE);
                presentBackgroundColor = (MyColors.enumToColor(MyColors.COLOUR_RED));
                stopSOS();
                break;
            case R.id.yellow_button:
                mMainLayout.setBackgroundColor(MyColors.enumToColor(MyColors.COLOUR_YELLOW));
                buttonArea.setVisibility(View.GONE);
                presentBackgroundColor = (MyColors.enumToColor(MyColors.COLOUR_YELLOW));
                stopSOS();
                break;
            case R.id.green_button:
                mMainLayout.setBackgroundColor(MyColors.enumToColor(MyColors.COLOUR_GREEN));
                buttonArea.setVisibility(View.GONE);
                presentBackgroundColor = (MyColors.enumToColor(MyColors.COLOUR_GREEN));
                stopSOS();
                break;
            case R.id.black_button:
                mMainLayout.setBackgroundColor(MyColors.enumToColor(MyColors.COLOUR_BLACK));
                buttonArea.setVisibility(View.GONE);
                presentBackgroundColor = (MyColors.enumToColor(MyColors.COLOUR_BLACK));
                stopSOS();
                break;
            case R.id.white_button:
                mMainLayout.setBackgroundColor(MyColors.enumToColor(MyColors.COLOUR_WHITE));
                buttonArea.setVisibility(View.GONE);
                presentBackgroundColor = (MyColors.enumToColor(MyColors.COLOUR_WHITE));
                stopSOS();
                break;
            case R.id.SOS_button:
                SOS_signal =new sosActivity();
                SOS_signal.execute("...---...");
                //buttonArea.setVisibility(View.GONE);
        }

    }
/* Here the on saved instance functionality is defined here into the code.
* */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(text_Visibility_string, text.getVisibility());
        outState.putInt(button_area_Visibility_string, buttonArea.getVisibility());
        outState.putInt(back_colour_string, presentBackgroundColor);
        outState.putBoolean("button area Visibility choice", initialButtonVisibilityDialog);
        outState.putBoolean("colour Decision Dialog got open", initialColorDialog);
        outState.putBoolean("color got selected from the dialog", pressed);
        outState.putInt("ID of the Selected Color from the dialog", check_the_RadioButton);
        outState.putBoolean("complete screen status", complete_screen);
    }
/* Here the flag fullscreen functionality  is defined here to remove the title during touching the
screen operation by the user in  the application.
* */
    private void restoreparameterState(Bundle state) {
        if (state != null) {
            text.setVisibility(state.getInt(text_Visibility_string));
            buttonArea.setVisibility(state.getInt(button_area_Visibility_string));
            presentBackgroundColor = state.getInt(back_colour_string);
            mMainLayout.setBackgroundColor(presentBackgroundColor);
            complete_screen = state.getBoolean("fullScreenStatus");
            if(complete_screen ==true){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }

            initialButtonVisibilityDialog = state.getBoolean("buttonLayoutVisibilityDecision");
            if (initialButtonVisibilityDialog ==true)
            {
                openButtonLayoutVisibilityDialog();
            }
            initialColorDialog = state.getBoolean("colorDecisionDialogOpen");
            pressed = state.getBoolean("colorSelectedfromDialog");
            check_the_RadioButton = state.getInt("IDofSelectedColorfromDialog");
                        if(initialColorDialog ==true)
            {
                openColorDialog();
            }

        }
    }
/* Here the menu options are defined in the program which can be used to declare the settings
for next startup of the application.
* */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.button_visibility_option:
                initialButtonVisibilityDialog = true;
                openButtonLayoutVisibilityDialog();
                return true;
            case R.id.color_setting_option:
                initialColorDialog =true;
                openColorDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
/*  Here the positive and negative button dialog is added as per the Preferences.
* */
    private void openButtonLayoutVisibilityDialog(){
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Startup Button Visibility")
                .setNegativeButton("Visible", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        saveOnStartupVisibility(true);
                        initialButtonVisibilityDialog = false;
                    }
                })
                .setPositiveButton("Hidden", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        saveOnStartupVisibility(false);
                        initialButtonVisibilityDialog = false;
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        initialButtonVisibilityDialog = false;
                    }
                }).setCancelable(false);
        builder.create().show();
    }

    private void openColorDialog(){
        final View toDisplayInDialog= getLayoutInflater().inflate(R.layout.radiogroup, null);
        builder = new AlertDialog.Builder(this);
        builder.setView(toDisplayInDialog);
        final RadioGroup myRadioGroup= (RadioGroup) toDisplayInDialog.findViewById(R.id.R_color_select);
        myRadioGroup.check(check_the_RadioButton);
        final RadioButton myCheckedButton= (RadioButton) toDisplayInDialog.findViewById(check_the_RadioButton);
        myCheckedButton.setChecked(true);


        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId){
                int radioGroupId = myRadioGroup.getCheckedRadioButtonId();
                check_the_RadioButton = radioGroupId;
                final RadioButton myCheckedButton= (RadioButton) toDisplayInDialog.findViewById(radioGroupId);

                indexing = myRadioGroup.indexOfChild(myCheckedButton);
                pressed =true;
            }
        });
        setDialogColorSelect();
    }

/* Here startup color dialog selection is defined in to the program.
* */
    private void setDialogColorSelect() {
        builder.setTitle("Choose Startup Color")
                .setPositiveButton("Set Startup Color", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initialColorDialog = false;
                        pressed = false;
                        switch (indexing){
                            case 0:
                                saveOnStartupColor(Color.rgb(255, 255, 255));
                                break;
                            case 1:
                                saveOnStartupColor(Color.rgb(0, 0, 0));
                                break;
                            case 2:
                                saveOnStartupColor(Color.rgb(255, 0, 0));
                                break;
                            case 3:
                                saveOnStartupColor(Color.rgb(255, 255, 0));
                                break;
                            case 4:
                                saveOnStartupColor(Color.rgb(0, 255, 0));
                                break;
                            default:
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initialColorDialog = false;
                        pressed = false;
                    }
                }).setCancelable(false);
        builder.create().show();
    }

    /* Here b is the boolean value that tells the visibility of the button layout on its startup.
    */
    private void saveOnStartupVisibility(boolean b) {
        /*storage_foil_string is the name of the common foil that saves all sort of values*/

        SharedPreferences pref= getSharedPreferences(storage_foil_string, MODE_PRIVATE);
        SharedPreferences.Editor write= pref.edit();
        write.putBoolean(initial_ButtonLayout_Visibility, b);
        write.commit();//necessary code for saving
    }
/* to save the startup visibility.
* */
    private boolean getsavedOnStartupVisibilty(){
        SharedPreferences pref= getSharedPreferences(storage_foil_string, MODE_PRIVATE);
        boolean rb= pref.getBoolean(initial_ButtonLayout_Visibility, true);
        return rb;
    }
/* to save the startup colour
* */
    private void saveOnStartupColor(int rgb){
        SharedPreferences pref= getSharedPreferences(storage_foil_string, MODE_PRIVATE);
        SharedPreferences.Editor write = pref.edit();
        write.putInt(initial_Background_Color, rgb);
        write.commit();
    }

    private int getsavedOnSartupColor(){
        SharedPreferences pref= getSharedPreferences(storage_foil_string, MODE_PRIVATE);
        int color= pref.getInt(initial_Background_Color, Color.rgb(255, 0, 0));
        return color;
    }
/* Async start is implemented here that send the signal in the form of Morse code.
* */

    public class sosActivity extends AsyncTask<String, Integer, Void>{

        int time =0;
        int count = 0;
        int progress=0;
        @Override
        protected void onPreExecute(){
            mMainLayout.setBackgroundColor(presentBackgroundColor);
            the_progress_bar.setVisibility(View.VISIBLE);
            the_progress_bar.setProgress(0);
        }
        @Override
        protected Void doInBackground(String... params) {
            progress= params[0].length();
            while (!isCancelled()){
                for(int i=0; i< params[0].length(); i++){
                    if(isCancelled()){
                        break;
                    }
                    if(params[0].charAt(i)=='.'){
                        time = 500;
                    }
                    else if (params[0].charAt(i)=='-'){
                        time=1000;
                    }
                    try{
                        Thread.sleep(time);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                    publishProgress(Color.rgb(0,0,255));

                    try{
                        Thread.sleep(time);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                    publishProgress(presentBackgroundColor);
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            mMainLayout.setBackgroundColor(values[0]);
            count=++count%(2*progress);
            the_progress_bar.setProgress((1+count)*50/progress);
        }

        @Override
        protected void onPostExecute(Void Result){
            the_progress_bar.setVisibility(View.GONE);
            mMainLayout.setBackgroundColor(presentBackgroundColor);
        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
            mMainLayout.setBackgroundColor(presentBackgroundColor);
        }


    }
    @Override
    protected void onStop(){
        super.onPause();
        if(SOS_signal !=null)
        {
            SOS_signal.cancel(true);
        }
    }
}
