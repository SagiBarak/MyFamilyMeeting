package sagib.edu.myfamilymeeting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int RC_WRITE = 1122;
    @BindView(R.id.content)
    ViewPager content;
    BottomNavigationView navigation;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (mViewPager.getCurrentItem() == 1 || mViewPager.getCurrentItem() == 0)
                        return true;
                    mSectionsPagerAdapter.getItem(1);
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_menu:
                    if (mViewPager.getCurrentItem() == 3)
                        return true;
                    mSectionsPagerAdapter.getItem(3);
                    mViewPager.setCurrentItem(3);
                    return true;
                case R.id.navigation_quiz:
                    if (mViewPager.getCurrentItem() == 2)
                        return true;
                    mSectionsPagerAdapter.getItem(2);
                    mViewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }

    };

    private FirebaseAuth mAuth;
    SharedPreferences prefs;
    SharedPreferences dataPrefs;
    ArrayList<String> images = new ArrayList<>();
    ArrayList<String> dates = new ArrayList<>();
    String latestDate = "";
    SubstringComparatorForDates comparator = new SubstringComparatorForDates();
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Fresco.initialize(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            mAuth.signInAnonymously();
        }
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
        prefs = getSharedPreferences("Values", MODE_PRIVATE);
        dataPrefs = getSharedPreferences("Data", MODE_PRIVATE);
        latestDate = dataPrefs.getString("LatestDate", "");
        FirebaseDatabase.getInstance().getReference("Data").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String date = dataSnapshot.getKey();
                dates.add(date);
                Collections.sort(dates, comparator);
                dataPrefs.edit().remove("currentParsedDates");
                dataPrefs.edit().putString("currentParsedDates", gson.toJson(dates)).commit();
                Collections.reverse(dates);
                latestDate = dates.get(0);
                dataPrefs.edit().putString("LatestDate", latestDate).commit();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("Data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getDataByDate(latestDate);
                Log.d("SagiB onData", latestDate);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        gson = new Gson();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        navigation.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.content);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.mytext)).setText("ברוכים הבאים");
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                        getSupportActionBar().setCustomView(R.layout.abs_layout);
                        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.mytext)).setText("ברוכים הבאים");
                        break;
                    case 1:
                        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                        getSupportActionBar().setCustomView(R.layout.abs_layout);
                        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.mytext)).setText("דברי פתיחה");
                        navigation.setSelectedItemId(R.id.navigation_home);
                        break;
                    case 2:
                        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                        getSupportActionBar().setCustomView(R.layout.abs_layout);
                        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.mytext)).setText("חידות");
                        navigation.setSelectedItemId(R.id.navigation_quiz);
                        break;
                    case 3:
                        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                        getSupportActionBar().setCustomView(R.layout.abs_layout);
                        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.mytext)).setText("תפריט");
                        navigation.setSelectedItemId(R.id.navigation_menu);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mSectionsPagerAdapter.getItem(1);
                mViewPager.setCurrentItem(1);
            }
        };
        IntentFilter intentFilter = new IntentFilter("Begin");
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_admin:
                AdminFragment adminFragment = new AdminFragment();
                adminFragment.show(getSupportFragmentManager(), "Admin");
                return true;
            case R.id.menu_setData:
                ChooseContentFragment chooseContentFragment = new ChooseContentFragment();
                chooseContentFragment.show(getSupportFragmentManager(), "Chooser");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getDataByDate(final String date) {
        if (date == null) {
            return;
        }
        FirebaseDatabase.getInstance().getReference("Data").child(date).child("text").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String adjusted = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    switch (snapshot.getKey()) {
                        case "body":
                            String body = snapshot.getValue(String.class);
                            if (body != null)
                                adjusted = body.replaceAll("(?m)^[ \t]*\r?\n", "").trim();
                            prefs.edit().putString("body", adjusted).commit();
                            break;
                        case "endingText":
                            String endingText = snapshot.getValue(String.class);
                            if (endingText != null)
                                adjusted = endingText.replaceAll("(?m)^[ \t]*\r?\n", "").trim();
                            prefs.edit().putString("endingText", adjusted).commit();
                            break;
                        case "firstFood":
                            String firstFood = snapshot.getValue(String.class);
                            if (firstFood != null)
                                adjusted = firstFood.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("firstFood", adjusted).commit();
                            break;
                        case "header":
                            String header = snapshot.getValue(String.class);
                            if (header != null)
                                adjusted = header.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("header", adjusted).commit();
                            break;
                        case "lastFood":
                            String lastFood = snapshot.getValue(String.class);
                            if (lastFood != null)
                                adjusted = lastFood.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("lastFood", adjusted).commit();
                            break;
                        case "openMenu":
                            String openMenu = snapshot.getValue(String.class);
                            if (openMenu != null)
                                adjusted = openMenu.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("openMenu", adjusted).commit();
                            break;
                        case "opening":
                            String opening = snapshot.getValue(String.class);
                            if (opening != null)
                                adjusted = opening.replaceAll("(?m)^[ \t]*\r?\n", "").trim();
                            prefs.edit().putString("opening", adjusted).commit();
                            break;
                        case "primaryFood":
                            String primaryFood = snapshot.getValue(String.class);
                            if (primaryFood != null)
                                adjusted = primaryFood.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("primaryFood", adjusted).commit();
                            break;
                        case "quiz":
                            String quiz = snapshot.getValue(String.class);
                            if (quiz != null)
                                adjusted = quiz.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("quiz", adjusted).commit();
                            break;
                        case "secondFood":
                            String secondFood = snapshot.getValue(String.class);
                            if (secondFood != null)
                                adjusted = secondFood.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("secondFood", adjusted).commit();
                            break;
                        case "textBeforeMenu":
                            String textBeforeMenu = snapshot.getValue(String.class);
                            if (textBeforeMenu != null)
                                adjusted = textBeforeMenu.replaceAll("(?m)^[ \t]*\r?\n", "").trim();
                            prefs.edit().putString("textBeforeMenu", adjusted).commit();
                            break;
                        case "textDate":
                            String textDate = snapshot.getValue(String.class);
                            if (textDate != null)
                                adjusted = textDate.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("textDate", adjusted).commit();
                            break;
                        case "topingsFood":
                            String topingsFood = snapshot.getValue(String.class);
                            if (topingsFood != null)
                                adjusted = topingsFood.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("topingsFood", adjusted).commit();
                            break;
                        case "allRecipes":
                            String allRecipes = snapshot.getValue(String.class);
                            prefs.edit().putString("allRecipes", allRecipes).commit();
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference("Data").child(date).child("imageCount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    switch (snapshot.getKey()) {
                        case "bodyImages":
                            String bodyImages = snapshot.getValue(String.class);
                            prefs.edit().putString("bodyImages", bodyImages).commit();
                            break;
                        case "foodImages":
                            String foodImages = snapshot.getValue(String.class);
                            prefs.edit().putString("foodImages", foodImages).commit();
                            break;
                        case "openImages":
                            String openImages = snapshot.getValue(String.class);
                            prefs.edit().putString("openImages", openImages).commit();
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference("Data").child(date).child("images").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    images.add(snapshot.getValue(String.class));
                    prefs.edit().remove("allImages").commit();
                    prefs.edit().putString("allImages", gson.toJson(images)).commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class SubstringComparatorForDates implements Comparator {
        public int compare(Object o1, Object o2) {
            String o1Year = o1.toString().substring(o1.toString().length() - 1 - 4);
            String o1Month = o1.toString().substring(o1.toString().length() - 1 - 7, o1.toString().length() - 1 - 4);
            String o1Day = o1.toString().substring(0, o1.toString().length() - 1 - 7);
            String o2Year = o2.toString().substring(o2.toString().length() - 1 - 4);
            String o2Month = o2.toString().substring(o2.toString().length() - 1 - 7, o2.toString().length() - 1 - 4);
            String o2Day = o2.toString().substring(0, o2.toString().length() - 1 - 7);
            if (o1Year.compareTo(o2Year) == 0) {
                if (o1Month.compareTo(o2Month) == 0) {
                    return o1Day.compareTo(o2Day);
                } else {
                    return o1Month.compareTo(o2Month);
                }
            } else {
                return o1Year.compareTo(o2Year);
            }
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Log.d("SagiB case 1", "here");
                    return new WelcomeFragment();
                case 1:
                    Log.d("SagiB case 2", "here");
                    return new OpenFragment();
                case 2:
                    Log.d("SagiB case 3", "here");
                    return new QuizFragment();
                case 3:
                    Log.d("SagiB case 4", "here");
                    return new FoodFragment();
            }
            return new Fragment();
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    Log.d("SagiB case 1", "here");
                    return "ברוכים הבאים";
                case 1:
                    Log.d("SagiB case 2", "here");
                    return "דברי פתיחה";
                case 2:
                    Log.d("SagiB case 3", "here");
                    return "חידות";
                case 3:
                    Log.d("SagiB case 4", "here");
                    return "תפריט";
            }
            return "MyFamilyMeeting";
        }
    }
}