package com.example.semesterschedulingapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.semesterschedulingapp.Pattern.MySingleton;
import com.example.semesterschedulingapp.R;
import com.example.semesterschedulingapp.Utils.Config;
import com.example.semesterschedulingapp.Utils.SharedPrefManager;
import com.example.semesterschedulingapp.model.Courses;
import com.example.semesterschedulingapp.model.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {

    private List<Courses> coursesList;
    private Context mContext;
    private OnItemClickListener mListener;

    public CoursesAdapter(List<Courses> coursesList, Context mContext, OnItemClickListener listener) {
        this.coursesList = coursesList;
        this.mContext = mContext;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public CoursesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_cardview, parent, false);
        return new ViewHolder(view);
    }

    public interface OnItemClickListener {
        void onClick(Courses courses);
    }

    @Override
    public void onBindViewHolder(@NonNull final CoursesAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.tv_course_name.setText(coursesList.get(position).getCourse_name());
        viewHolder.tv_teacher_name.setText(coursesList.get(position).getCourse_teacher());
        viewHolder.tv_session_name.setText(coursesList.get(position).getCourse_session());
    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_course_name;
        TextView tv_teacher_name;
        TextView tv_session_name;
        Button btn_join_course;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_course_name = itemView.findViewById(R.id.tv_course_name);
            tv_teacher_name = itemView.findViewById(R.id.tv_teacher_name);
            tv_session_name = itemView.findViewById(R.id.tv_session_name);
            btn_join_course = itemView.findViewById(R.id.btn_join_course);

            btn_join_course.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(coursesList.get(getLayoutPosition()));
                }
            });
        }
    }

    private void verifyCourse(final String course_id, final String course_code) {

        StringRequest courseRequest = new StringRequest(Request.Method.GET, Config.VERIFYCODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject resObj = new JSONObject(response);

                    if (resObj.optString("success").equals("1")) {

                        String success_messge = resObj.getString("message");
                        Toast.makeText(mContext, success_messge, Toast.LENGTH_SHORT).show();
                        Log.i("SSARegRes", success_messge);

                    } else if (resObj.optString("success").equals("0")) {

                        String success_messge = resObj.getString("message");
                        Toast.makeText(mContext, success_messge, Toast.LENGTH_SHORT).show();
                        Log.i("SSARegResErr", success_messge);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(mContext, "Error in Data Response", Toast.LENGTH_SHORT).show();
                Log.e("SSA SignUp ERR", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Users user = SharedPrefManager.getInstance(mContext).getUser();
                String token_type = user.getTokenType();
                String access_token = user.getUserToken();

                Log.i("TokenFromModel", token_type + " " + access_token);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token_type + " " + access_token);
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Log.i("course_id", course_id);

                Map<String, String> params = new HashMap<String, String>();
                params.put("course_id", course_id);
                params.put("course_code", course_code);
                return params;
            }
        };
        MySingleton.getInstance(mContext).addToRequestQueue(courseRequest);

    }
}
