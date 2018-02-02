package bthrust.eggonline.eggOnline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import bthrust.eggonline.Config.ConfigClass;
import bthrust.eggonline.R;

public class UserProfile_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView emailtext = (TextView) findViewById(R.id.emailtext);
        TextView nametext = (TextView) findViewById(R.id.nametext);

        SharedPreferences shrePref = getSharedPreferences(ConfigClass.SHARE_PREF , MODE_PRIVATE);
        String email = shrePref.getString("userEmail" , "");
        String name = shrePref.getString("userName" , "");

        emailtext.setText(email);
        nametext.setText(name);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

      if (id == R.id.action_logout) {

          SharedPreferences.Editor logineditor = getSharedPreferences(ConfigClass.SHARE_PREF, MODE_PRIVATE).edit();
          logineditor.remove("userEmail");
          logineditor.remove("userName");
          logineditor.apply();

          SharedPreferences sp = getSharedPreferences(ConfigClass.SHARE_PREF , MODE_PRIVATE);
          String email1 = sp.getString("userEmail" , "");
          String name1 = sp.getString("userName" , "");

          if(email1.equals("")) {
              Intent intent = new Intent(UserProfile_Activity.this, Login.class);
              startActivity(intent);
              UserProfile_Activity.this.finish();
              this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
          }else{
              Toast.makeText(getApplicationContext() , "User is not Logout..!" , Toast.LENGTH_LONG).show();
          }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
