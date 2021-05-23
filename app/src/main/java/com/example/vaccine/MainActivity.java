package com.example.vaccine;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.DatePickerDialog;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements itemClickinterface {
    FloatingActionButton fab;
    ProgressDialog pd;
    Button selectdate;
    String url;
    private int year, month, day;

    RecyclerView recyclerView;

    Adapter adapter;
    List<CenterRvModal> modals=new ArrayList<>();
    EditText pincode;
    Button search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab=findViewById(R.id.add_fab);
        search=findViewById(R.id.idBtnSearch);
        pincode=findViewById(R.id.idEdtPinCode);
        search=findViewById(R.id.idBtnSearch);
        recyclerView=findViewById(R.id.centersRV);
        selectdate=findViewById(R.id.date);
        day=0;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Now You will redirect My Fb",Toast.LENGTH_SHORT).show();
                String face = "https://www.facebook.com/mohammad.ehsan.7186896";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(face));
                startActivity(i);
            }
        });
        selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int cyear, int cmonth, int cdayOfMonth) {
                        year=cyear;
                        month=cmonth;
                        day=cdayOfMonth;
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(pincode.length()==6&&day!=0)
                {
                    month++;
                    url="https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode="+pincode.getText().toString()+"&date="+day+"-"+month+"-"+year;
                    pd=new ProgressDialog(MainActivity.this);
                    pd.setMessage("Please wait");
                    pd.show();
                    getData();
                      }
                else
                {
                    modals.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),"Please Enter Correct Pin Code",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
    private void getData() {
        modals.clear();
        Toast.makeText(MainActivity.this,"Thank for Using",Toast.LENGTH_SHORT).show();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pd.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("centers");
                    if(array.length()==0)
                    {
                        Toast.makeText(getApplicationContext(),"No center Avalaible",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        for(int i=0;i<array.length();i++)
                        {
                            JSONObject centerObj=array.getJSONObject(i);
                            JSONObject sessionObj = centerObj.getJSONArray("sessions").getJSONObject(0);
                            CenterRvModal rv=new CenterRvModal(
                                    centerObj.getString("name"),
                                    centerObj.getString("address"),
                                    centerObj.getString("from"),
                                    centerObj.getString("to"),
                                    centerObj.getString("fee_type"),
                                    sessionObj.getInt("min_age_limit"),
                                    sessionObj.getString("vaccine"),
                                    sessionObj.getInt("available_capacity"),
                                    sessionObj.getString("date")
                            );
                            if(rv!=null)
                            modals.add(rv);

                        }
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        adapter=new Adapter(MainActivity.this,modals,MainActivity.this::onItemClick);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,
                "Hospital Name and Address "+modals.get(position).getCenterName()+" "+modals.get(position).getCenterAddress()+"Date "
                +modals.get(position).getDate()  +"\nAvailable "+modals.get(position).getAvailableCapacity()+"\nPrice "+modals.get(position).getFee_type()
        );
        intent.setType("text/plain");
        MainActivity.this.startActivity(Intent.createChooser(intent, "Send To"));
    }
}

//