package bthrust.eggonline.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bthrust.eggonline.Config.ConfigClass;
import bthrust.eggonline.R;
import bthrust.eggonline.eggOnline.HomeActivity;


public class LoginFragment extends Fragment {

    private EditText email_edit , pass_edit;
    private TextView loginBTN , forgotPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);


        email_edit = (EditText) view.findViewById(R.id.email_edit);
        pass_edit = (EditText) view.findViewById(R.id.pass_edit);
        loginBTN = (TextView) view.findViewById(R.id.loginBTN);
        forgotPassword = (TextView) view.findViewById(R.id.forgotPassword);


        email_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                email_edit.setBackgroundResource(R.drawable.border_shape);
                pass_edit.setBackgroundResource(R.drawable.edit_shape);

            }
        });

        pass_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                email_edit.setBackgroundResource(R.drawable.edit_shape);
                pass_edit.setBackgroundResource(R.drawable.border_shape);
            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://btservicenow.com/Online-egg/my-account/lost-password/");
                Intent intent = new Intent(Intent.ACTION_VIEW , uri);
                startActivity(intent);
            }
        });

        loginSectionController();

        return view;
    }

    private void loginSectionController() {



        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadServise(email_edit.getText().toString() , pass_edit.getText().toString());
            }
        });


    }

    private void loadServise(final String email_edit_str, final String pass_edit_str) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfigClass.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Login Response===>  " , response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String token = jsonObject.getString("token");
                    if(jsonObject.has("token")){

                        SharedPreferences.Editor editor = getActivity().getSharedPreferences(ConfigClass.SHARE_PREF , Context.MODE_PRIVATE).edit();
                        editor.putString("userEmail" , jsonObject.getString("user_email"));
                        editor.putString("userName" , jsonObject.getString("user_nicename"));
                        editor.commit();

                        Intent intent = new Intent(getActivity() , HomeActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                    }else{

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            protected Map<String  , String> getParams() throws AuthFailureError
            {
                Map<String  , String> params = new HashMap<>();

                params.put("username" , email_edit_str);
                params.put("password" , pass_edit_str);
                Log.e("params=====>> " , String.valueOf(params));

                return params;
            }
        };

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                Toast.makeText(getContext(),"Slow internet connection..!",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        requestQueue.add(stringRequest);
    }


}
