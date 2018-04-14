package com.bestow.hackmhs.bestow;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private FrameLayout fragmentFrame;
    private PlusFragment plusFragment;
    private ProfileFragment profileFragment;
    private BrowseFragment browseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentFrame = findViewById(R.id.MainActivity_Fragment_frame);

        plusFragment = new PlusFragment();
        browseFragment = new BrowseFragment();
        profileFragment = new ProfileFragment();

        loadFragment(browseFragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.MainActivity_NavigationView_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frag = null;

                switch (item.getItemId()){
                    case R.id.NavigationView_browse:
                        frag = browseFragment;
                        break;
                    case R.id.NavigationView_plus:
                        frag = plusFragment;
                        break;
                    case R.id.NavigationView_profile:
                        frag = profileFragment;
                        break;
                }
                return loadFragment(frag);
            }
        });
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment!=null){
            getFragmentManager().beginTransaction().replace(R.id.MainActivity_Fragment_frame, fragment).commit();
            return true;
        }
        return false;
    }
}
