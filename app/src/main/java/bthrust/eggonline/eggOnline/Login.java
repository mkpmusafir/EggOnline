package bthrust.eggonline.eggOnline;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import bthrust.eggonline.Fragment.LoginFragment;
import bthrust.eggonline.Fragment.SignUpFragment;
import bthrust.eggonline.R;


public class Login extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this , HomeActivity.class);
                startActivity(intent);
                Login.this.finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.w_viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.w_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void setupViewPager(ViewPager w_viewpager) {

        adapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        adapter.addFrag(new LoginFragment(), " Login");
        adapter.addFrag(new SignUpFragment(), " Register ");



        w_viewpager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> wFragmentList = new ArrayList<>();
        private final List<String> wFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {

            return wFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return wFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            wFragmentList.add(fragment);
            wFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return wFragmentTitleList.get(position);
        }
    }

}
