package com.example.ftl_calc;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.TimeZone;
//import com.ftl_calc_with_FDP.BuildConfig;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private AppBarConfiguration appBarConfiguration;
    //private ActivityMainBinding binding;
    TimePicker datePicker1;
    Boolean Pressed,Sector1_Pressed,Sector2_Pressed,Sector3_Pressed,Sector4_Pressed,DiscretionOn,ExtendedOn,Press_For_Flight=false;
    String[] First_Array,Array_0600_1329,Array_1330_1359,Array_1400_1429,Array_1430_1459,Array1600_1629,Array_1630_1659,Array_1700_0459,Array_0500_0514,Array_0515_0529,Array_0530_0544,Array_0545_0559;
    LocalTime date1, date2, date3, date4,date5, MaxDuty1, MinTurn, Duty_Start, Taxi1, Pre_Flight_Time1,SUTTO1;
    LocalTime FDP1,FDP2,FDP3,FDP4,FDP5,FDP6,FDP7,FDP8,FDP9,FDP10,FDP11,FDP12,FDP13,FDP14,FDP15,FDP16,FDP17,FDP18,FDP19,FDP20,FDP21,FDP22,FDP23,FDP24,FDP25,FDP26,FDP27,FDP28,FDP29,DiscretionAmount,ExtendedDutyTime,MaxDutyCalc,MaxDutyCalc1;
    Button Set_Time_Button,Reset;
    RadioButton Sector_1, Sector_2, Sector_3, Sector_4;
    double first, second;
    LocalTime sum,sum1,sum2,sum3,sum4;
    int i = 0, Destination=1,Destination_Array,Flight_Duty_Table_Lookup; //where to send time from picker
    int minuteFirstFromString;
    int hourFirstFromString, hourFirstFromString1, minuteFirstFromString1;
    EditText First_Sector_Time, Second_Sector_Time, Third_Sector_Time, Fourth_Sector_Time, MAX_FDP, Min_Turn_Time, Duty_Start_Time;
    TextView firstHour, firstMinute, Planned_Duty_Time, Blocks_on_Time, Last_Takeoff_Time,Last_Take_Off1,Last_Take_Off2,Last_Take_Off3,Last_Take_Off4,Max_Discretion_textview,Title;
    RadioGroup Sectors_Group;
    private CheckBox Extended_checkBox2,MaxDiscretion_checkBox;
    String Y3;
    int CheckBoxChecker;
    int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);


        addListenerMaxDiscretion(); //max disc checkbox
        addListenerExtDuty();


        //  TextView resultView = findViewById(R.id.resultView);
        TextView Number1 = findViewById(R.id.FirstHour);
        //  Number1.setText("" + value1);
        //  TextView Number2 = findViewById(R.id.hours2);
        // Number2.setText("" + value2);


       // MaxDiscretion_checkBox=findViewById(R.id.MaxDiscretion_checkBox);
       // Extended_checkBox2=findViewById(R.id.Extended_checkBox2);



        String taxi = "00:05"; //taxi in to shutdown NB all localtime variables must have 4 digits.
        //Taxi1 = timeFormat.parse(taxi);
        String Pre_Flight_Time="01:00";
        CheckBoxChecker=0;
        DiscretionOn=false; //is discretion selected?
        ExtendedOn=false;  //is Extended selected?



        Last_Take_Off1=findViewById(R.id.Last_Take_Off1);
        Last_Take_Off2=findViewById(R.id.Last_Take_Off2);
        Last_Take_Off3=findViewById(R.id.Last_Take_Off3);
        Last_Take_Off4=findViewById(R.id.Last_Take_Off4);
        Title=findViewById(R.id.enter_landing_speed);
        Title.setText("  FTL and Discretion Calculator v "+versionName); //sets current app version

        //Reset = findViewById(R.id.Reset);
        Set_Time_Button = findViewById(R.id.Set_Time_Button);
        First_Sector_Time = findViewById(R.id.First_Sector_Time);
        Second_Sector_Time = findViewById(R.id.Second_Sector_Time);
        Third_Sector_Time = findViewById(R.id.Third_Sector_Time);
        Fourth_Sector_Time = findViewById(R.id.Fourth_Sector_Time);
        MAX_FDP = findViewById(R.id.MAX_FDP);
        Min_Turn_Time = findViewById(R.id.Min_Turn_Time);
        Duty_Start_Time = findViewById(R.id.Duty_Start_Time);
        datePicker1 = findViewById(R.id.datePicker1);
        Sectors_Group = findViewById(R.id.Sectors_Group);
        datePicker1.setIs24HourView(true);

        Sector_1 = findViewById(R.id.Sector_1);
        Sector_2 = findViewById(R.id.Sector_2);
        Sector_3 = findViewById(R.id.Sector_3);
        Sector_4 = findViewById(R.id.Sector_4);

        //Reset.setOnClickListener(this);
        First_Sector_Time.setOnClickListener(this);
        Second_Sector_Time.setOnClickListener(this);
        Third_Sector_Time.setOnClickListener(this);
        Fourth_Sector_Time.setOnClickListener(this);
        MAX_FDP.setOnClickListener(this);
        Min_Turn_Time.setOnClickListener(this);
        Duty_Start_Time.setOnClickListener(this);
        Sectors_Group.setOnClickListener(this);

        Set_Time_Button.setEnabled(false); //prevents pressing wrong button at start
        Set_Time_Button.setTextColor(Color.GRAY);
        Extended_checkBox2.setEnabled(false);
        MaxDiscretion_checkBox.setEnabled(false);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        DiscretionAmount=LocalTime.parse("00:00");
        ExtendedDutyTime=LocalTime.parse("00:00");
       //Extended_checkBox2.setOnCheckedChangeListener(this);

               // MaxDiscretion_checkBox.setOnCheckedChangeListener(this);

        Duty_Start_Time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){Pressed=false;
                Duty_Start_Time.setText("");
            }
                else {
                  Pressed=true;
                }}
        });

        datePicker1.setHour(0);
        datePicker1.setMinute(0);

        Pressed=false;

        firstHour = findViewById(R.id.FirstHour);
        firstMinute = findViewById(R.id.Last_Takeoff_Time);
        Planned_Duty_Time = findViewById(R.id.Planned_Duty_Time);
        Blocks_on_Time = findViewById(R.id.Blocks_on_Time);

        First_Sector_Time.setShowSoftInputOnFocus(false);
        Second_Sector_Time.setShowSoftInputOnFocus(false);
        Third_Sector_Time.setShowSoftInputOnFocus(false);
        Fourth_Sector_Time.setShowSoftInputOnFocus(false);
        Duty_Start_Time.setShowSoftInputOnFocus(false);
        addListenerRadioGroup(); //must add this to activate method below

    }

    public void addListenerMaxDiscretion() {

        MaxDiscretion_checkBox=findViewById(R.id.MaxDiscretion_checkBox);
        Extended_checkBox2=findViewById(R.id.Extended_checkBox2); //new
        MaxDiscretion_checkBox.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                //is chkIos checked?
                int hour, minute;
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                //LocalTime time = LocalTime.of(hour,minute);

                try {
                    if (((CheckBox) v).isChecked()) {
                        Blocks_on_Time.setTextColor(Color.RED);

                        String MaxDutyDay = Duty_Start_Time.getText().toString(); //get current start of fdp.
                        String MaxDutyDay1 = MAX_FDP.getText().toString(); //get current max fdp

                        //MaxDutyCalc=timeFormat.parse((MaxDutyDay));
                       // LocalTime MaxDutyCalc=LocalTime.parse(MaxDutyDay);
                       // MaxDutyCalc1=timeFormat.parse((MaxDutyDay1));
                      //  LocalTime MaxDutyCalc1=LocalTime.parse(MaxDutyDay1);
                        DiscretionAmount=LocalTime.parse("02:00");
                        if(ExtendedOn==true){    //sees if extended duty is ticked NB must use "==" not"="!!!!!
                            DiscretionAmount=LocalTime.parse(("01:00"));


                        }
                        DiscretionOn=true;

                        // DiscretionAmount = LocalTime.parse("02:00");
                      //  sum=MaxDutyCalc.plusHours(DiscretionAmount.getHour()).plusHours(MaxDutyCalc1.getHour()).plusMinutes(MaxDutyCalc1.getMinute());
                        //date5=timeFormat.format((new Date(sum)));
                      //  Blocks_on_Time.setText(sum.toString()); //add 2 hours discretion.
                        CheckBoxChecker=CheckBoxChecker+1;


                    } else {
                        Blocks_on_Time.setTextColor(Color.BLACK);
                        String MaxDutyDay = Duty_Start_Time.getText().toString(); //get current start of fdp.
                        String MaxDutyDay1 = MAX_FDP.getText().toString(); //get current max fdp
                        DiscretionAmount = LocalTime.parse("00:00");
                        /*MaxDutyCalc=timeFormat.parse((MaxDutyDay));
                        MaxDutyCalc1=timeFormat.parse((MaxDutyDay1));
                        sum=MaxDutyCalc.getTime()+MaxDutyCalc1.getTime();
                        date5=timeFormat.format((new Date(sum)));
                        Blocks_on_Time.setText(date5); //add 2 hours discretion. //take off discretion*/
                        CheckBoxChecker=CheckBoxChecker-1;
                        DiscretionOn=false;
                        if (ExtendedOn==true){
                            Blocks_on_Time.setTextColor(Color.RED);

                        }
                    }
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
                }

                Set_Time_Click(v);//must be within this VIEW
            }

    });



    }



    public void addListenerExtDuty() {

        Extended_checkBox2=findViewById(R.id.Extended_checkBox2);
        Extended_checkBox2.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    ExtendedOn=true;

                    Blocks_on_Time.setTextColor(Color.RED);
                    ExtendedDutyTime=LocalTime.parse("01:00");
                    if(DiscretionOn==true){
                        DiscretionAmount=LocalTime.parse("01:00");
                    }CheckBoxChecker=CheckBoxChecker+1;
                }
                else  {
                    Blocks_on_Time.setTextColor(Color.BLACK);
                    CheckBoxChecker=CheckBoxChecker-1;
                    ExtendedDutyTime=LocalTime.parse("00:00");
                    ExtendedOn=false;
                    if (DiscretionOn==true){ //is discretion is selected, keep highlight red.
                        Blocks_on_Time.setTextColor(Color.RED);
                        DiscretionAmount=LocalTime.parse("02:00");
                    }
                }
                // sendMessage(v);// run calc again
                Set_Time_Click(v);//must be within this VIEW
            }
        });

    }

    public void OnDutyStartTimeClick(View v){

            Pressed=false;
            Duty_Start_Time.setText("");




    }

    public void Reset(View view){

        //Pressed=false;
        Duty_Start_Time.setText("");
        MAX_FDP.setText("");
        Blocks_on_Time.setText("");
        Last_Take_Off1.setText("");
        Last_Take_Off2.setText("");
        First_Sector_Time.setText("");
        Second_Sector_Time.setText("");
        Planned_Duty_Time.setText("");
        Sector_1.setChecked(true); //resets sector radio button to 1 and sets other buttons as off
        Sector_2.setChecked(false);
        Sector_3.setChecked(false);
        Sector_4.setChecked(false);
        Set_Time_Button.setTextColor(Color.GRAY);
        Set_Time_Button.setEnabled(false);
        Extended_checkBox2.setEnabled(false);
        MaxDiscretion_checkBox.setEnabled(false);

        Destination=1;
        Pressed=false;




    }

    public void Extended (View view){
        //change Blocks_on_Time tec to red

        boolean checked= ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.Extended_checkBox2:
                if (checked)
                    Blocks_on_Time.setTextColor(Color.parseColor("#E30B56"));
                // Put some meat on the sandwich
            else
                    Blocks_on_Time.setTextColor(Color.parseColor("#0B0B0B"));
                // Remove the meat
                break;


        }

    }



    public void onClick(View v) {
        //InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);


        switch (v.getId()) {


            case R.id.First_Sector_Time:
                //imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                First_Sector_Time.getText().clear();


                //First_Sector_Time.setShowSoftInputOnFocus(false);
                Destination = 1; //tells timepicker where to send data
                break;

            case R.id.Second_Sector_Time:
                Second_Sector_Time.getText().clear();

               // imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                //Second_Sector_Time.setShowSoftInputOnFocus(false);
                Destination = 2;
                break;

            case R.id.Third_Sector_Time:
                Third_Sector_Time.getText().clear();

               // imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                Destination = 3;
                break;

            case R.id.Fourth_Sector_Time:
                Fourth_Sector_Time.getText().clear();

               // imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                Destination = 4;
                break;

            case R.id.Duty_Start_Time:
                Duty_Start_Time.getText().clear();
               // imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                Destination = 1;


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void onSubmitClick(View view) { //accessed by "Calc" button and radio button change


        /* //   TextView Answer = findViewById(R.id.resultView);
        //EditText Attempt = findViewById(R.id.numberEntry);
        //   TextView Result= findViewById(R.id.TotalMinutesView);
        String tester = First_Sector_Time.getText().toString();

        // firstHour.setText("Hello");
        //firstMinute.setText(tester);

        //   TextView firstHour2 = findViewById(R.id.hours2);
        //  TextView firstMinute2 = findViewById(R.id.minutes2);
        //  TextView finalHours = findViewById(R.id.TotalHoursView);
        //  TextView finalMinutes = findViewById(R.id.TotalMinutesView);
        if (tester.matches("")) {
            return; //checks that there is data in the field
        }

        double userAnswer = 5;
        String time1 = First_Sector_Time.getText().toString();
        String time2 = Second_Sector_Time.getText().toString();
        String time3 = Third_Sector_Time.getText().toString();
        String time4 = Fourth_Sector_Time.getText().toString();
        String time5 = Min_Turn_Time.getText().toString();
        String MaxDutyString = MAX_FDP.getText().toString(); //max allowed FDP
        String DutyStart = Duty_Start_Time.getText().toString();

        // String MaxDutyString="12:00";


        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));


        try {
            date1 = timeFormat.parse(time1);
            date2 = timeFormat.parse(time2);
            date3 = timeFormat.parse(time3);
            date4 = timeFormat.parse(time4);
            MinTurn = timeFormat.parse(time5);
            MaxDuty1 = timeFormat.parse(MaxDutyString); //max allowed FDP
            Duty_Start = timeFormat.parse(DutyStart);
            String taxi = "0:05"; //taxi in to shutdown
            Taxi1 = timeFormat.parse(taxi);
            String Pre_Flight_Time="1:00";

            Pre_Flight_Time1 = timeFormat.parse(Pre_Flight_Time);

        } catch (ParseException e) {
            e.printStackTrace();
        }

         sum = date1.getTime() + date2.getTime() + date3.getTime() + date4.getTime() + MinTurn.getTime() + Taxi1.getTime()+Pre_Flight_Time1.getTime();
        //all sectors plus turns plus taxi to shutdown
         sum1 = MaxDuty1.getTime() - sum;
         sum2=MaxDuty1.getTime()+Duty_Start.getTime();
         sum3 = Duty_Start.getTime() + sum1;

        String date3 = timeFormat.format(new Date(sum)); //max length of duties
        String date4 = timeFormat.format(new Date(sum2));//available time before last takeoff
        String date5 = timeFormat.format((new Date(sum3)));//last takeoff

        Planned_Duty_Time.setText(date3);
        Blocks_on_Time.setText(date4);
        //System.out.println("The sum is "+date3);

        Pressed=true;
        Button btn; //do FDP change
        btn=(Button)findViewById(R.id.FDP_Calc_btn); //FDP calc button runs FDP_Lookup

        btn.performClick();*/



    }

    public void FDP_Lookup(View view) { //fdp calc button

        int hour, minute;

        String am_pm;

        if (Pressed==false) {
                // hello
            hour = datePicker1.getHour();
            minute = datePicker1.getMinute();
            if (minute < 10) {
                Duty_Start_Time.setText(hour + ":" + "0" + minute);
            } else Duty_Start_Time.setText(hour + ":" + minute);

        }
        Set_Time_Button.setEnabled(true); //re-enable set flight time button
        Set_Time_Button.setTextColor(Color.BLACK);
        Extended_checkBox2.setEnabled(true);
        MaxDiscretion_checkBox.setEnabled(true);

        String FDP_Start = Duty_Start_Time.getText().toString();
        int i = FDP_Start.length(); //NB time MUST have "00:00" not "0:00" or will fail
        if(i<5) {
            FDP_Start=("0"+FDP_Start);
        }
        LocalTime timeFormat = LocalTime.parse(FDP_Start);
        //SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
       // timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));




        try {

            FDP1 = LocalTime.parse(FDP_Start);
            FDP2 = LocalTime.parse("05:59");
            FDP3 = LocalTime.parse("13:30");
            FDP4 = LocalTime.parse("13:29");
            FDP5 = LocalTime.parse("14:00");
            FDP6 = LocalTime.parse("13:59");
            FDP7 = LocalTime.parse("14:30");
            FDP8 = LocalTime.parse("14:29");
            FDP9 = LocalTime.parse("15:00");
            FDP10 = LocalTime.parse("14:59");
            FDP11 = LocalTime.parse("15:30");
            FDP12 = LocalTime.parse("15:29");
            FDP13 = LocalTime.parse("16:00");
            FDP14 = LocalTime.parse("15:59");
            FDP15 = LocalTime.parse("16:30");
            FDP16 = LocalTime.parse("16:29");
            FDP17 = LocalTime.parse("17:00");
            FDP18 = LocalTime.parse("16:59");
            FDP19 = LocalTime.parse("05:00");
            FDP20 = LocalTime.parse("04:59");
            FDP21 = LocalTime.parse("05:15");
            FDP22 = LocalTime.parse("05:14");
            FDP23 = LocalTime.parse("05:30");
            FDP24 = LocalTime.parse("05:29");
            FDP25 = LocalTime.parse("05:45");
            FDP26 = LocalTime.parse("05:44");
            FDP27 = LocalTime.parse("06:00");
            FDP28 = LocalTime.parse("23:59");
            FDP29 = LocalTime.parse("00:00");
           // DiscretionAmount=LocalTime.parse("02:00");
            Resources r = getResources(); //allows array to be loaded

            if (Destination<3){ //if less than 3 sectors, must be made to equal 1to choose first figure in array
                Destination_Array=1; //convert Destination (ie how many sectors) into the array access
            }
            if (Destination==3){ //if less than 3 sectors, must be made to equal 1to choose first figure in array
                Destination_Array=2;
            }
            if (Destination==4){ //if less than 3 sectors, must be made to equal 1to choose first figure in array
                Destination_Array=3; //must reduce to access 0 based array
            }

            if (FDP1.MIN.isAfter(FDP2) && FDP1.isBefore(FDP3)){
                Flight_Duty_Table_Lookup=1;
                First_Array = r.getStringArray(R.array.Array_0600_1329);
                Y3=First_Array[Destination_Array-1];
                date1 = LocalTime.parse(Y3);
                sum=date1;
                //date5=timeFormat.format((new Date(sum)));

                MAX_FDP.setText(date1.toString());
                date2=LocalTime.parse(FDP_Start);

                sum=sum.plusHours(date2.getHour()).plusMinutes(date2.getMinute()); //calc new on blocks time from max fdp lookup
                //date5=timeFormat.format((new Date(sum)));
                date5=sum;
                Blocks_on_Time.setText(date5.toString());

            }
            if (FDP1.isAfter(FDP4) && FDP1.isBefore(FDP5)){
                Flight_Duty_Table_Lookup=2;
                First_Array = r.getStringArray(R.array.Array_1330_1359);
                Y3=First_Array[Destination_Array-1];
                date1 = LocalTime.parse(Y3);
                sum=date1;
               // date5=timeFormat.format((new Date(sum)));
                MAX_FDP.setText(date1.toString());
                date2=LocalTime.parse(FDP_Start);
               // sum=sum+date2.getTime(); //calc new on blocks time from max fdp lookup
                sum=sum.plusHours(date2.getHour()).plusMinutes(date2.getMinute());
                date5=sum;
                Blocks_on_Time.setText(date5.toString());
            }
            if (FDP1.isAfter(FDP6) && FDP1.isBefore(FDP7)){
                Flight_Duty_Table_Lookup=3;
                First_Array = r.getStringArray(R.array.Array_1400_1429);
                Y3=First_Array[Destination_Array-1];
                date1 = LocalTime.parse(Y3);
                sum=date1;
                // date5=timeFormat.format((new Date(sum)));
                MAX_FDP.setText(date1.toString());
                date2=LocalTime.parse(FDP_Start);
                // sum=sum+date2.getTime(); //calc new on blocks time from max fdp lookup
                sum=sum.plusHours(date2.getHour()).plusMinutes(date2.getMinute());
                date5=sum;
                Blocks_on_Time.setText(date5.toString());
            }
            if (FDP1.isAfter(FDP8) && FDP1.isBefore(FDP9)){
                Flight_Duty_Table_Lookup=4;
                First_Array = r.getStringArray(R.array.Array_1430_1459);
                Y3=First_Array[Destination_Array-1];
                date1 = LocalTime.parse(Y3);
                sum=date1;
                // date5=timeFormat.format((new Date(sum)));
                MAX_FDP.setText(date1.toString());
                date2=LocalTime.parse(FDP_Start);
                // sum=sum+date2.getTime(); //calc new on blocks time from max fdp lookup
                sum=sum.plusHours(date2.getHour()).plusMinutes(date2.getMinute());
                date5=sum;
                Blocks_on_Time.setText(date5.toString());
            }
            if (FDP1.isAfter(FDP10) && FDP1.isBefore(FDP11)){
                Flight_Duty_Table_Lookup=5;
                First_Array = r.getStringArray(R.array.Array_1500_1529);
                Y3=First_Array[Destination_Array-1];
                date1 = LocalTime.parse(Y3);
                sum=date1;
                // date5=timeFormat.format((new Date(sum)));
                MAX_FDP.setText(date1.toString());
                date2=LocalTime.parse(FDP_Start);
                // sum=sum+date2.getTime(); //calc new on blocks time from max fdp lookup
                sum=sum.plusHours(date2.getHour()).plusMinutes(date2.getMinute());
                date5=sum;
                Blocks_on_Time.setText(date5.toString());
            }
            if (FDP1.isAfter(FDP12) && FDP1.isBefore(FDP13)){
                Flight_Duty_Table_Lookup=6;
                First_Array = r.getStringArray(R.array.Array_1530_1559);
                Y3=First_Array[Destination_Array-1];
                date1 = LocalTime.parse(Y3);
                sum=date1;
                // date5=timeFormat.format((new Date(sum)));
                MAX_FDP.setText(date1.toString());
                date2=LocalTime.parse(FDP_Start);
                // sum=sum+date2.getTime(); //calc new on blocks time from max fdp lookup
                sum=sum.plusHours(date2.getHour()).plusMinutes(date2.getMinute());
                date5=sum;
                Blocks_on_Time.setText(date5.toString());
            }
            if (FDP1.isAfter(FDP14) && FDP1.isBefore(FDP15)){
                Flight_Duty_Table_Lookup=7;
                First_Array = r.getStringArray(R.array.Array_1600_1629);
                Y3=First_Array[Destination_Array-1];
                date1 = LocalTime.parse(Y3);
                sum=date1;
                // date5=timeFormat.format((new Date(sum)));
                MAX_FDP.setText(date1.toString());
                date2=LocalTime.parse(FDP_Start);
                // sum=sum+date2.getTime(); //calc new on blocks time from max fdp lookup
                sum=sum.plusHours(date2.getHour()).plusMinutes(date2.getMinute());
                date5=sum;
                Blocks_on_Time.setText(date5.toString());

            }
            if (FDP1.isAfter(FDP16) && FDP1.isBefore(FDP17)){
                Flight_Duty_Table_Lookup=8;
                First_Array = r.getStringArray(R.array.Array_1630_1659);
                Y3=First_Array[Destination_Array-1];
                date1 = LocalTime.parse(Y3);
                sum=date1;
                // date5=timeFormat.format((new Date(sum)));
                MAX_FDP.setText(date1.toString());
                date2=LocalTime.parse(FDP_Start);
                // sum=sum+date2.getTime(); //calc new on blocks time from max fdp lookup
                sum=sum.plusHours(date2.getHour()).plusMinutes(date2.getMinute());
                date5=sum;
                Blocks_on_Time.setText(date5.toString());
            }
            if (FDP1.isAfter(FDP18) && FDP1.isBefore(FDP28)) {
                Flight_Duty_Table_Lookup = 9;
                First_Array = r.getStringArray(R.array.Array_1700_0459);
                Y3 = First_Array[Destination_Array - 1];
                date1 = LocalTime.parse(Y3);
                sum=date1;
                // date5=timeFormat.format((new Date(sum)));
                MAX_FDP.setText(date1.toString());
                date2=LocalTime.parse(FDP_Start);
                // sum=sum+date2.getTime(); //calc new on blocks time from max fdp lookup
                sum=sum.plusHours(date2.getHour()).plusMinutes(date2.getMinute());
                date5=sum;
                Blocks_on_Time.setText(date5.toString());
            }
                if (FDP1.isAfter(FDP29) && FDP1.isBefore(FDP19)){
                    Flight_Duty_Table_Lookup=9;
                    First_Array = r.getStringArray(R.array.Array_1700_0459);
                    Y3=First_Array[Destination_Array-1];
                    date1 = LocalTime.parse(Y3);
                    sum=date1;
                    // date5=timeFormat.format((new Date(sum)));
                    MAX_FDP.setText(date1.toString());
                    date2=LocalTime.parse(FDP_Start);
                    // sum=sum+date2.getTime(); //calc new on blocks time from max fdp lookup
                    sum=sum.plusHours(date2.getHour()).plusMinutes(date2.getMinute());
                    date5=sum;
                    Blocks_on_Time.setText(date5.toString());
            }
            if (FDP1.isAfter(FDP18) && FDP1.isBefore(FDP29)){ //after 1700 until 2359
                Flight_Duty_Table_Lookup=9;
                First_Array = r.getStringArray(R.array.Array_1700_0459);
                Y3=First_Array[Destination_Array-1];
                date1 = LocalTime.parse(Y3);
                sum=date1;
                // date5=timeFormat.format((new Date(sum)));
                MAX_FDP.setText(date1.toString());
                date2=LocalTime.parse(FDP_Start);
                // sum=sum+date2.getTime(); //calc new on blocks time from max fdp lookup
                sum=sum.plusHours(date2.getHour()).plusMinutes(date2.getMinute());
                date5=sum;
                Blocks_on_Time.setText(date5.toString());
            }

            if (FDP1.equals(FDP29)){ //does not work!!
                Flight_Duty_Table_Lookup=9;
                First_Array = r.getStringArray(R.array.Array_1700_0459);
                Y3=First_Array[Destination_Array-1];
                date1 = LocalTime.parse(Y3);
                sum=date1;
                // date5=timeFormat.format((new Date(sum)));
                MAX_FDP.setText(date1.toString());
                date2=LocalTime.parse(FDP_Start);
                // sum=sum+date2.getTime(); //calc new on blocks time from max fdp lookup
                sum=sum.plusHours(date2.getHour()).plusMinutes(date2.getMinute());
                date5=sum;
                Blocks_on_Time.setText(date5.toString());
            }




            if (FDP1.isAfter(FDP20) && FDP1.isBefore(FDP21)){
                Flight_Duty_Table_Lookup=10;
                First_Array = r.getStringArray(R.array.Array_0500_0514);
                Y3=First_Array[Destination_Array-1];
                date1 = LocalTime.parse(Y3);
                sum=date1;
                // date5=timeFormat.format((new Date(sum)));
                MAX_FDP.setText(date1.toString());
                date2=LocalTime.parse(FDP_Start);
                // sum=sum+date2.getTime(); //calc new on blocks time from max fdp lookup
                sum=sum.plusHours(date2.getHour()).plusMinutes(date2.getMinute());
                date5=sum;
                Blocks_on_Time.setText(date5.toString());

            }
            if (FDP1.isAfter(FDP22) && FDP1.isBefore(FDP23)){
                Flight_Duty_Table_Lookup=11;
                First_Array = r.getStringArray(R.array.Array_0515_0529);
                Y3=First_Array[Destination_Array-1];
                date1 = LocalTime.parse(Y3);
                sum=date1;
                // date5=timeFormat.format((new Date(sum)));
                MAX_FDP.setText(date1.toString());
                date2=LocalTime.parse(FDP_Start);
                // sum=sum+date2.getTime(); //calc new on blocks time from max fdp lookup
                sum=sum.plusHours(date2.getHour()).plusMinutes(date2.getMinute());
                date5=sum;
                Blocks_on_Time.setText(date5.toString());
            }
            if (FDP1.isAfter(FDP24) && FDP1.isBefore(FDP25)){
                Flight_Duty_Table_Lookup=12;
                First_Array = r.getStringArray(R.array.Array_0530_0544);
                Y3=First_Array[Destination_Array-1];
                date1 = LocalTime.parse(Y3);
                sum=date1;
                // date5=timeFormat.format((new Date(sum)));
                MAX_FDP.setText(date1.toString());
                date2=LocalTime.parse(FDP_Start);
                // sum=sum+date2.getTime(); //calc new on blocks time from max fdp lookup
                sum=sum.plusHours(date2.getHour()).plusMinutes(date2.getMinute());
                date5=sum;
                Blocks_on_Time.setText(date5.toString());
            }
            if (FDP1.isAfter(FDP26) && FDP1.isBefore(FDP27)){
                Flight_Duty_Table_Lookup=13;
                First_Array = r.getStringArray(R.array.Array_0545_0559);
                Y3=First_Array[Destination_Array-1];
                date1 = LocalTime.parse(Y3);
                sum=date1;
                // date5=timeFormat.format((new Date(sum)));
                MAX_FDP.setText(date1.toString());
                date2=LocalTime.parse(FDP_Start);
                // sum=sum+date2.getTime(); //calc new on blocks time from max fdp lookup
                sum=sum.plusHours(date2.getHour()).plusMinutes(date2.getMinute());
                date5=sum;
                Blocks_on_Time.setText(date5.toString());
            }

           //if (MaxDiscretion_checkBox.isChecked()) {
               //DiscTime=timeFormat.parse(DiscretionAmount);
               sum=sum.plusHours(DiscretionAmount.getHour()).plusMinutes(DiscretionAmount.getMinute()).plusHours(ExtendedDutyTime.getHour()).plusMinutes(ExtendedDutyTime.getMinute());
               //sum=sum+DiscretionAmount;
               date5=sum;
               Blocks_on_Time.setText(date5.toString()); //add 2 hours discretion.

           //}
           //else
          // if(Extended_checkBox2.isChecked()&& (MaxDiscretion_checkBox.isChecked())){
          //     sum=sum.plusHours(DiscretionAmount.getHour()).plusMinutes(DiscretionAmount.getMinute());
               //sum=sum+DiscretionAmount;
           //    date5=sum;
            //   Blocks_on_Time.setText(date5.toString()); //add 2 hours discretion.
           //}

        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }

        Pressed=true; //stops start of fdp period changing
        //  if (FDP1.after(01:30);
    }


    public void Press_For_Flight_Length(View view){

        Press_For_Flight=true;
        Set_Time_Click(view);


    }


    public void Set_Time_Click(View view) { //set time button and radiogroup change

        //MaxDiscretion_checkBox=findViewById(R.id.MaxDiscretion_checkBox);

        FDP_Lookup(view);

        if (Press_For_Flight==false){
            String MaxDutyString = MAX_FDP.getText().toString();
            i = MaxDutyString.length();
            if(i<5) {
                MaxDutyString=("0"+MaxDutyString);
            }
            String DutyStart = Duty_Start_Time.getText().toString();
            i = DutyStart.length();
            if(i<5) {
                DutyStart=("0"+DutyStart);
            }
            MaxDuty1 = LocalTime.parse(MaxDutyString);
            Duty_Start = LocalTime.parse(DutyStart);
            MaxDuty1=MaxDuty1.plusHours(DiscretionAmount.getHour()).plusHours(ExtendedDutyTime.getHour());
            sum=Duty_Start.plusHours(MaxDuty1.getHour()).plusMinutes(MaxDuty1.getMinute());
            Blocks_on_Time.setText(sum.toString());

            return;
        }





        int hour, minute;

        String am_pm;

        hour = datePicker1.getHour();
        minute = datePicker1.getMinute();


        if (hour > 12) {
            am_pm = "PM";
            //hour = hour - 12;
        } else {
            am_pm = "AM";
        }
        if (Destination == 1) {
            if (minute < 10) {
                First_Sector_Time.setText(hour + ":" + "0" + minute);
            } else First_Sector_Time.setText(hour + ":" + minute);

        } else if (Destination == 2) {
            if (minute < 10) {
                Second_Sector_Time.setText(hour + ":" + "0" + minute);
            } else
                Second_Sector_Time.setText(hour + ":" + minute);


        } else if (Destination == 3) {
            if (minute < 10) {
                Third_Sector_Time.setText(hour + ":" + "0" + minute);
            } else
                Third_Sector_Time.setText(hour + ":" + minute);
        } else if (Destination == 4) {
            if (minute < 10) {
                Fourth_Sector_Time.setText(hour + ":" + "0" + minute);
            } else
                Fourth_Sector_Time.setText(hour + ":" + minute);

        } else if (Destination == 5) { //what is this for?
            if (minute < 10) {
                Duty_Start_Time.setText(hour + ":" + "0" + minute);
            } else
                Duty_Start_Time.setText(hour + ":" + minute);
        }


        //   TextView Answer = findViewById(R.id.resultView);
        //EditText Attempt = findViewById(R.id.numberEntry);
        //   TextView Result= findViewById(R.id.TotalMinutesView);
        String tester = First_Sector_Time.getText().toString();

        // firstHour.setText("Hello");
        //firstMinute.setText(tester);

        //   TextView firstHour2 = findViewById(R.id.hours2);
        //  TextView firstMinute2 = findViewById(R.id.minutes2);
        //  TextView finalHours = findViewById(R.id.TotalHoursView);
        //  TextView finalMinutes = findViewById(R.id.TotalMinutesView);
        if (tester.matches("")) {
            return; //checks that there is data in the field
        }

        double userAnswer = 5;

        String time1 = First_Sector_Time.getText().toString();//entered flight time
        int i = time1.length(); //NB time MUST have "00:00" not "0:00" or will fail
        if(i<5) {
            time1=("0"+time1);
        }

        String time2 = Second_Sector_Time.getText().toString();
         i = time2.length();
        if(i<5) {
            time2=("0"+time2);
        }
        String time3 = Third_Sector_Time.getText().toString();
         i = time3.length();
        if(i<5) {
            time3=("0"+time3);
        }
        String time4 = Fourth_Sector_Time.getText().toString();
         i = time4.length();
        if(i<5) {
            time4=("0"+time4);
        }
        String time5 = Min_Turn_Time.getText().toString();
         i = time5.length();
        if(i<5) {
            time5=("0"+time5);
        }
        String MaxDutyString = MAX_FDP.getText().toString();
         i = MaxDutyString.length();
        if(i<5) {
            MaxDutyString=("0"+MaxDutyString);
        }
        String DutyStart = Duty_Start_Time.getText().toString();
         i = DutyStart.length();
        if(i<5) {
            DutyStart=("0"+DutyStart);
        }


        // String MaxDutyString="12:00";



        LocalTime timeFormat = LocalTime.parse(DutyStart);
        //SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
       // timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));


        //try {
        //time1=("01:30");
            date1 = LocalTime.parse(time1);
            date2 = LocalTime.parse(time2);
            date3 = LocalTime.parse(time3);
            date4 = LocalTime.parse(time4);
            MinTurn = LocalTime.parse(time5);
            MaxDuty1 = LocalTime.parse(MaxDutyString);
            MaxDuty1=MaxDuty1.plusHours(DiscretionAmount.getHour()).plusHours(ExtendedDutyTime.getHour());
            Duty_Start = LocalTime.parse(DutyStart);
            String taxi = "00:05"; //taxi in to shutdown
            Taxi1 = LocalTime.parse(taxi);
            String SUTTO = "00:10"; //taxi in to shutdown
            SUTTO1 = LocalTime.parse(SUTTO);//SUTTO
            String Pre_Flight_Time="01:00"; //planning time

            Pre_Flight_Time1 = LocalTime.parse(Pre_Flight_Time);

       // } catch (DateTimeParseException e) {
           // e.printStackTrace();
        //}
        if (Destination==1){

            sum=date1.plusMinutes(Taxi1.getMinute()).plusHours(Pre_Flight_Time1.getHour()).plusMinutes(Pre_Flight_Time1.getMinute());
            //sum1 = MaxDuty1.getTime() - sum;
            sum1 = MaxDuty1.minusHours(sum.getHour()).minusMinutes(sum.getMinute());
            sum2=date1.plusHours(Taxi1.getHour()).plusMinutes(Taxi1.getMinute()).plusHours(SUTTO1.getHour()).plusMinutes(SUTTO1.getMinute());
            sum3 = Duty_Start.plusHours(MaxDuty1.getHour()).plusMinutes(MaxDuty1.getMinute()).minusHours(sum2.getHour()).minusMinutes(sum2.getMinute()); //changes here
            //String date5 = timeFormat.format((new Date(sum3)));//last takeoff
            Last_Take_Off1.setText(sum3.toString());
            sum4=sum.plusHours(Pre_Flight_Time1.getHour()).plusMinutes(Pre_Flight_Time1.getMinute());
            //date5=timeFormat.format((new Date(sum)));

            Planned_Duty_Time.setText(sum.toString());


        }
        if (Destination==2){
            sum=date2.plusHours(Taxi1.getHour()).plusMinutes(Taxi1.getMinute()).plusHours(SUTTO1.getHour()).plusMinutes(SUTTO1.getMinute());
            sum1 = MaxDuty1.minusHours(sum.getHour()).minusMinutes(sum.getMinute());
            sum3 = Duty_Start.plusHours(sum1.getHour()).plusMinutes(sum1.getMinute());
             //date5 = timeFormat.format((new Date(sum3)));//last takeoff
            Last_Take_Off2.setText(sum3.toString());
            //sum=date1.getTime()+ date2.getTime()+MinTurn.getTime()+ Taxi1.getTime()+SUTTO1.getTime()+SUTTO1.getTime()+Taxi1.getTime();
            sum=date1.plusHours(date2.getHour()).plusMinutes(date2.getMinute()).plusMinutes(MinTurn.getMinute()).plusMinutes(Taxi1.getMinute()).plusMinutes(SUTTO1.getMinute()).plusMinutes(SUTTO1.getMinute()).plusMinutes(Taxi1.getMinute());
            sum1 = MaxDuty1.minusHours(sum.getHour()).minusMinutes(sum.getMinute());
            sum3 = Duty_Start.plusHours(sum1.getHour()).plusMinutes(sum1.getMinute());
            // date5 = timeFormat.format((new Date(sum3)));//last takeoff
            Last_Take_Off1.setText(sum3.toString());
            sum4=sum.plusHours(Pre_Flight_Time1.getHour()).plusMinutes(Pre_Flight_Time1.getMinute());
            //date5=timeFormat.format((new Date(sum4)));
            Planned_Duty_Time.setText(sum4.toString());


        }
        if (Destination==3) {
            sum=date3.plusHours(Taxi1.getHour()).plusMinutes(Taxi1.getMinute()).plusHours(SUTTO1.getHour()).plusMinutes(SUTTO1.getMinute());
            sum1 = MaxDuty1.minusHours(sum.getHour()).minusMinutes(sum.getMinute());
            sum3 = Duty_Start.plusHours(sum1.getHour()).plusMinutes(sum1.getMinute());
            //date5 = timeFormat.format((new Date(sum3)));//last takeoff
            Last_Take_Off3.setText(sum3.toString());
            //sum=date2.getTime()+ date3.getTime()+MinTurn.getTime()+ Taxi1.getTime()+ Taxi1.getTime()+SUTTO1.getTime()+SUTTO1.getTime();
            sum=date2.plusHours(date3.getHour()).plusMinutes(date3.getMinute()).plusMinutes(MinTurn.getMinute()).plusMinutes(Taxi1.getMinute()).plusMinutes(Taxi1.getMinute()).plusMinutes(SUTTO1.getMinute()).plusMinutes(SUTTO1.getMinute());
            sum1 = MaxDuty1.minusHours(sum.getHour()).minusMinutes(sum.getMinute());
            sum3 = Duty_Start.plusHours(sum1.getHour()).plusMinutes(sum1.getMinute());
            //date5 = timeFormat.format((new Date(sum3)));//last takeoff
            Last_Take_Off2.setText(sum3.toString());
            //sum = date1.getTime() + date2.getTime() + date3.getTime()+MinTurn.getTime()+MinTurn.getTime()+ Taxi1.getTime()+Taxi1.getTime() + Taxi1.getTime()+SUTTO1.getTime()+SUTTO1.getTime()+SUTTO1.getTime(); //3 x turn & taxi times
            sum=date1.plusHours(date2.getHour()).plusMinutes(date2.getMinute()).plusHours(date3.getHour()).plusMinutes(date3.getMinute()).plusMinutes(MinTurn.getMinute()).plusMinutes(MinTurn.getMinute()).plusHours(Taxi1.getHour()).plusMinutes(Taxi1.getMinute()).plusMinutes(Taxi1.getMinute()).plusHours(SUTTO1.getHour()).plusMinutes(SUTTO1.getMinute()).plusHours(SUTTO1.getHour());
            //sum1 = MaxDuty1.getTime() - sum;
            sum1 = MaxDuty1.minusHours(sum.getHour()).minusMinutes(sum.getMinute());
            //sum3 = Duty_Start.getTime() + sum1;
            sum3 = Duty_Start.plusHours(sum1.getHour()).plusMinutes(sum1.getMinute());
            //date5 = timeFormat.format((new Date(sum3)));//last takeoff
            Last_Take_Off1.setText(sum3.toString());
            //sum4=sum+Pre_Flight_Time1.getTime();
            sum4=sum.plusHours(Pre_Flight_Time1.getHour()).plusMinutes(Pre_Flight_Time1.getMinute());
            //date5=timeFormat.format((new Date(sum4)));
            Planned_Duty_Time.setText(sum4.toString());

        }
        if (Destination==4) {
            //sum=date4.getTime()+ Taxi1.getTime()+SUTTO1.getTime();
            sum=date4.plusHours(Taxi1.getHour()).plusMinutes(Taxi1.getMinute()).plusHours(SUTTO1.getHour());
           // sum1 = MaxDuty1.getTime() - sum;
            sum1 = MaxDuty1.minusHours(sum.getHour()).minusMinutes(sum.getMinute());
            //sum3 = Duty_Start.getTime() + sum1;
            sum3 = Duty_Start.plusHours(sum1.getHour()).plusMinutes(sum1.getMinute());
           // date5 = timeFormat.format((new Date(sum3)));//last takeoff
            Last_Take_Off4.setText(sum3.toString());
            //sum=date3.getTime()+ date4.getTime()+MinTurn.getTime()+ Taxi1.getTime()+ Taxi1.getTime()+SUTTO1.getTime()+SUTTO1.getTime();
            sum=date3.plusHours(date4.getHour()).plusMinutes(date4.getMinute()).plusMinutes(MinTurn.getMinute()).plusMinutes(Taxi1.getMinute()).plusMinutes(Taxi1.getMinute()).plusMinutes(SUTTO1.getMinute()).plusMinutes(SUTTO1.getMinute());
            //sum1 = MaxDuty1.getTime() - sum;
            sum1 = MaxDuty1.minusHours(sum.getHour()).minusMinutes(sum.getMinute());
            //sum3 = Duty_Start.getTime() + sum1;
            sum3 = Duty_Start.plusHours(sum1.getHour()).plusMinutes(sum1.getMinute());
            //date5 = timeFormat.format((new Date(sum3)));//last takeoff
            Last_Take_Off3.setText(sum3.toString());
            sum=date2.plusHours(date3.getHour()).plusMinutes(date3.getMinute()).plusHours(date4.getHour()).plusMinutes(date4.getMinute()).plusMinutes(MinTurn.getMinute()).plusMinutes(Taxi1.getMinute()).plusMinutes(MinTurn.getMinute()).plusMinutes(Taxi1.getMinute()).plusMinutes(Taxi1.getMinute()).plusMinutes(SUTTO1.getMinute()).plusMinutes(SUTTO1.getMinute()).plusMinutes(SUTTO1.getMinute());
           // sum = date2.getTime() + date3.getTime()+ date4.getTime()+MinTurn.getTime()+ Taxi1.getTime()+MinTurn.getTime()+ Taxi1.getTime() + Taxi1.getTime()+SUTTO1.getTime()+SUTTO1.getTime()+SUTTO1.getTime();
            //sum1 = MaxDuty1.getTime() - sum;
            sum1 = MaxDuty1.minusHours(sum.getHour()).minusMinutes(sum.getMinute());
            //sum3 = Duty_Start.getTime() + sum1;
            sum3 = Duty_Start.plusHours(sum1.getHour()).plusMinutes(sum1.getMinute());
            //date5 = timeFormat.format((new Date(sum3)));//last takeoff
            Last_Take_Off2.setText(sum3.toString());
            sum=date1.plusHours(date2.getHour()).plusMinutes(date2.getMinute()).plusHours(date3.getHour()).plusMinutes(date3.getMinute()).plusHours(date4.getHour()).plusMinutes(date4.getMinute()).plusMinutes(MinTurn.getMinute()).plusMinutes(Taxi1.getMinute()).plusMinutes(MinTurn.getMinute()).plusMinutes(MinTurn.getMinute()).plusMinutes(Taxi1.getMinute()).plusMinutes(Taxi1.getMinute()).plusMinutes(Taxi1.getMinute()).plusMinutes(SUTTO1.getMinute()).plusMinutes(SUTTO1.getMinute()).plusMinutes(SUTTO1.getMinute()).plusMinutes(SUTTO1.getMinute());
            //sum = date1.getTime()+MinTurn.getTime()+date2.getTime()+MinTurn.getTime()+ Taxi1.getTime() + date3.getTime()+ +MinTurn.getTime() + Taxi1.getTime()+date4.getTime() + Taxi1.getTime()+SUTTO1.getTime()+SUTTO1.getTime()+SUTTO1.getTime()+SUTTO1.getTime();
            //sum1 = MaxDuty1.getTime() - sum;
            sum1 = MaxDuty1.minusHours(sum.getHour()).minusMinutes(sum.getMinute());
            //sum3 = Duty_Start.getTime() + sum1;
            sum3 = Duty_Start.plusHours(sum1.getHour()).plusMinutes(sum1.getMinute());
            //date5 = timeFormat.format((new Date(sum3)));//last takeoff
            Last_Take_Off1.setText(sum3.toString());
            //sum4=sum+Pre_Flight_Time1.getTime();
            sum4=sum.plusHours(Pre_Flight_Time1.getHour()).plusMinutes(Pre_Flight_Time1.getMinute());
            //date5=timeFormat.format((new Date(sum4)));
            Planned_Duty_Time.setText(sum4.toString());

        }
         //sum = date1.getTime() + date2.getTime() + date3.getTime() + date4.getTime() + MinTurn.getTime() + Taxi1.getTime();
        //all sectors plus turns plus taxi to shutdown
        // sum1 = MaxDuty1.getTime() - sum;
         //sum3 = Duty_Start.getTime() + sum1;
         sum4 = Duty_Start.plusHours(MaxDuty1.getHour()).plusMinutes(MaxDuty1.getMinute());

       // String date3 = timeFormat.format(new Date(sum)); //max length of duties
       // String date4 = timeFormat.format(new Date(sum1));//available time before last takeoff
       // String date5 = timeFormat.format((new Date(sum3)));//last takeoff
       // String date6 = timeFormat.format((new Date(sum4)));//end of FDP

       // Planned_Duty_Time.setText(date3);
        Blocks_on_Time.setText(sum4.toString());
        //System.out.println("The sum is "+date3);
        Pressed = true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();

    }

    public double calculateHours(String userAnswer1) {

        double first1 = Double.parseDouble(userAnswer1);
        String xHour1 = userAnswer1;
        String[] separated = xHour1.split("\\.");

        hourFirstFromString1 = Integer.parseInt(separated[0]);
        minuteFirstFromString1 = Integer.parseInt(separated[1]);
        if (minuteFirstFromString1 > 59) {
            //numberEntry.setText("CHECK MINUTES!"); //capture error

            First_Sector_Time.setError("Check Minutes!");

        }
        int infrontOfDecimal1 = ((int) first1); // remove numbers after decimal
        double FirstHours1 = infrontOfDecimal1; //convert to double

        double FirstMinutes1 = ((first1 - FirstHours1) * 100); //convert decimal minutes to minutes

        int IntFirstHour1 = (int) FirstHours1; //convert double to int for display
        int IntFirstMinute1 = (int) FirstMinutes1; //convert double to int for display
        firstHour.setText(String.valueOf(IntFirstHour1));
        First_Sector_Time.requestFocus();

        firstHour.setText(String.valueOf(hourFirstFromString1) + " :");

        if (minuteFirstFromString1 < 10) {
            firstMinute.setText(String.valueOf("0" + minuteFirstFromString1));// show first hours

        } else {
            firstMinute.setText(String.valueOf(minuteFirstFromString1));// show first hours
        }


        return FirstHours1;
    }


    public void addListenerRadioGroup() {   //must add this to oncreate to activate it

        //RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);

        Sectors_Group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            // checkedId is the RadioButton selected
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
             /*   Button btn;
                btn=(Button)findViewById(R.id.button);//this simulates pressing "calculate"
                btn.performClick();  //                 button and runs code
            }*/

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                switch (checkedId) {
                    case R.id.Sector_1:
                        First_Sector_Time.setVisibility(View.VISIBLE);
                        Second_Sector_Time.setVisibility(View.INVISIBLE);
                        Third_Sector_Time.setVisibility(View.INVISIBLE);
                        Fourth_Sector_Time.setVisibility(View.INVISIBLE);
                        Second_Sector_Time.setText("00:00");
                        Third_Sector_Time.setText("00:00");
                        Fourth_Sector_Time.setText("00:00");
                        Last_Take_Off2.setText("");
                        Last_Take_Off3.setText("");
                        Last_Take_Off4.setText("");
                        Last_Take_Off1.setVisibility(View.VISIBLE);
                        Last_Take_Off2.setVisibility(View.INVISIBLE);
                        Last_Take_Off3.setVisibility(View.INVISIBLE);
                        Last_Take_Off4.setVisibility(View.INVISIBLE);
                        Destination=1;//first sector
                       /* sum=date1.getTime()+ Taxi1.getTime();
                        sum1 = MaxDuty1.getTime() - sum;
                        sum3 = Duty_Start.getTime() + sum1;
                        date5 = timeFormat.format((new Date(sum3)));//last takeoff
                        Last_Take_Off1.setText(date5);
                        Destination=1;

                        sum=date1.getTime()+ Taxi1.getTime()+Pre_Flight_Time1.getTime();
                        sum1 = MaxDuty1.getTime() - sum;
                        sum2=date1.getTime()+ Taxi1.getTime()+SUTTO1.getTime();
                        sum3 = Duty_Start.getTime() + MaxDuty1.getTime()-sum2; //changes here
                        String date5 = timeFormat.format((new Date(sum3)));//last takeoff
                        Last_Take_Off1.setText(date5);
                        sum4=sum+Pre_Flight_Time1.getTime();
                        date5=timeFormat.format((new Date(sum)));
                        Planned_Duty_Time.setText(date5);
                        Sector2_Pressed=false; //sets next selection ready to accept time*/




                        break;
                    case R.id.Sector_2:
                        Second_Sector_Time.setVisibility(View.VISIBLE);
                        Third_Sector_Time.setVisibility(View.INVISIBLE);
                        Fourth_Sector_Time.setVisibility(View.INVISIBLE);
                        Third_Sector_Time.setText("00:00");
                        Fourth_Sector_Time.setText("00:00");
                        if (Sector2_Pressed=false) {
                            Last_Take_Off2.setText("");
                        }
                        Last_Take_Off3.setText("");
                        Last_Take_Off4.setText("");
                        Last_Take_Off2.setVisibility(View.VISIBLE);
                        Last_Take_Off3.setVisibility(View.INVISIBLE);
                        Last_Take_Off4.setVisibility(View.INVISIBLE);
                        Destination=2;
                       // if (Pressed==false){ //stop requesting a time which may not yet exist
                       /* sum=date2.getTime()+ Taxi1.getTime();
                        sum1 = MaxDuty1.getTime() - sum;
                        sum3 = Duty_Start.getTime() + sum1;
                        date5 = timeFormat.format((new Date(sum3)));//last takeoff
                        Last_Take_Off2.setText(date5);
                        sum=date1.getTime()+ date2.getTime()+MinTurn.getTime()+ Taxi1.getTime()+Taxi1.getTime()+SUTTO1.getTime()+SUTTO1.getTime();
                        sum1 = MaxDuty1.getTime() - sum;
                        sum3 = Duty_Start.getTime() + sum1;
                        date5 = timeFormat.format((new Date(sum3)));//last takeoff
                        Last_Take_Off1.setText(date5);//}
                        Pressed=true;

                        sum=date2.getTime()+ Taxi1.getTime()+SUTTO1.getTime();
                        sum1 = MaxDuty1.getTime() - sum;
                        sum3 = Duty_Start.getTime() + sum1;
                        date5 = timeFormat.format((new Date(sum3)));//last takeoff
                        Last_Take_Off2.setText(date5);
                        sum=date1.getTime()+ date2.getTime()+MinTurn.getTime()+ Taxi1.getTime()+SUTTO1.getTime()+SUTTO1.getTime()+Taxi1.getTime();
                        sum1 = MaxDuty1.getTime() - sum;
                        sum3 = Duty_Start.getTime() + sum1;
                        date5 = timeFormat.format((new Date(sum3)));//last takeoff
                        Last_Take_Off1.setText(date5);
                        sum4=sum+Pre_Flight_Time1.getTime();
                        date5=timeFormat.format((new Date(sum4)));
                        Planned_Duty_Time.setText(date5);*/
                        Sector2_Pressed=true; //will not allow time to be deleted until sector 1 pressed again


                        break;
                    case R.id.Sector_3:
                        Second_Sector_Time.setVisibility(View.VISIBLE);
                        Third_Sector_Time.setVisibility(View.VISIBLE);
                        Fourth_Sector_Time.setVisibility(View.INVISIBLE);
                        Fourth_Sector_Time.setText("00:00");
                        Last_Take_Off4.setText("");
                        Last_Take_Off3.setVisibility(View.VISIBLE);
                        Last_Take_Off4.setVisibility(View.INVISIBLE);
                        Third_Sector_Time.setText("");
                        Destination=3;
                        /*sum=date2.getTime()+ Taxi1.getTime()+SUTTO1.getTime();
                        sum1 = MaxDuty1.getTime() - sum;
                        sum3 = Duty_Start.getTime() + sum1;
                        date5 = timeFormat.format((new Date(sum3)));//last takeoff
                        Last_Take_Off2.setText(date5);
                        sum=date1.getTime()+ date2.getTime()+MinTurn.getTime()+ Taxi1.getTime()+SUTTO1.getTime()+SUTTO1.getTime()+Taxi1.getTime();
                        sum1 = MaxDuty1.getTime() - sum;
                        sum3 = Duty_Start.getTime() + sum1;
                        date5 = timeFormat.format((new Date(sum3)));//last takeoff
                        Last_Take_Off1.setText(date5);
                        sum4=sum+Pre_Flight_Time1.getTime();
                        date5=timeFormat.format((new Date(sum4)));
                        Planned_Duty_Time.setText(date5);*/

                        break;
                    case R.id.Sector_4:
                        Second_Sector_Time.setVisibility(View.VISIBLE);
                        Third_Sector_Time.setVisibility(View.VISIBLE);
                        Fourth_Sector_Time.setVisibility(View.VISIBLE);
                        Last_Take_Off4.setVisibility(View.VISIBLE);
                        Fourth_Sector_Time.setText("");
                        Destination=4;

                }
                Pressed=true;
                Button btn;
                btn=(Button)findViewById(R.id.Calculate); //onsubmitclick

                btn.performClick();
            }

            // Sectors_Group=(RadioGroup) findViewById(R.id.Sectors_Group);
            //Sectors_Group.(new OnCheckedChangeListener() {

            // @Override


            //public void onCheckedChanged (RadioGroup group,int checkedId){
         /*   switch (checkedId) {
                case R.id.Sector_2:
                    Second_Sector_Time.setVisibility(View.VISIBLE);

                    break;
                case R.id.Sector_3:
                    Third_Sector_Time.setVisibility(View.VISIBLE);
                    break;
                case R.id.Sector_4:
                    Fourth_Sector_Time.setVisibility(View.VISIBLE);

            } */
        });
    }

}





    


