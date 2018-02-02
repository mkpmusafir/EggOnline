package bthrust.eggonline.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


public class SignUpFragment extends Fragment {

    private EditText email_edit , pass_edit;
    private TextView signUpBTN;
    private String email , password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        email_edit = (EditText) view.findViewById(R.id.email_edit_S);
        pass_edit = (EditText) view.findViewById(R.id.pass_edit_S);
        signUpBTN = (TextView) view.findViewById(R.id.signUpBTN);


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



        signUpBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = email_edit.getText().toString();
                password = pass_edit.getText().toString();

                if(email.isEmpty()){

                }else if(password.isEmpty()){

                }else{
                    signUpService();
                }
            }
        });


        return view;
    }

    private void signUpService() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...!");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfigClass.SIGNUP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                Log.e("response==> " , response);
                boolean status = false;

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getBoolean("status");
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(ConfigClass.SHARE_PREF , Context.MODE_PRIVATE).edit();
                    editor.putBoolean("userStatus" , status);
                    editor.commit();

                    if(status == true){

                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String userID = jsonObject1.getString("id");
                        SharedPreferences.Editor editor1 = getActivity().getSharedPreferences(ConfigClass.SHARE_PREF , Context.MODE_PRIVATE).edit();
                        editor1.putString("userID" , jsonObject1.getString("id"));
                        editor1.putString("userEmail" , jsonObject1.getString("email"));
                        editor1.putString("userName" , jsonObject1.getString("username"));
                        editor1.commit();

                        Intent intent = new Intent(getActivity() , HomeActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

           protected Map<String , String> getParams() throws AuthFailureError{

               Map<String , String> params = new HashMap<>();
               params.put("email" , email);
               params.put("password" ,password);
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
