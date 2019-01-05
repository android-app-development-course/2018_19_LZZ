package com.example.jie95.fakeoffice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jie95.fakeoffice.Login.LoginUI;

public class MyFragment3 extends Fragment implements View.OnClickListener{

    private TextView m_tologin;
    private TextView m_vip;
    private TextView m_switch;
    private Button m_exit;
    private TextView m_cloud;
    public String m_people=null;
    public String m_password=null;

    public MyFragment3() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_content, container, false);
        m_tologin=(TextView) view.findViewById(R.id.tologin);
        m_vip=(TextView) view.findViewById(R.id.vip);
        m_cloud=(TextView) view.findViewById(R.id.cloud);
        m_switch=(TextView) view.findViewById(R.id.switch_on);
        m_exit=(Button)view.findViewById(R.id.set);
        m_tologin.setOnClickListener(this);
        m_switch.setOnClickListener(this);
        m_exit.setOnClickListener(this);
        if((m_people==null)&&(m_password==null))
        {
            m_vip.setVisibility(View.GONE);
        }
        Log.e("HEHE", "3日狗");
        return view;
    }
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.tologin:
                Intent intent=new Intent(getActivity(),LoginUI.class);
                startActivityForResult(intent,1);
                break;
            case R.id.switch_on:

                break;
            case R.id.set:
                System.exit(0);
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            m_people = data.getStringExtra("name");
            m_password = data.getStringExtra("password");
            if((m_people!=null)&&(m_password!=null))
            {
                m_tologin.setVisibility(View.GONE);
                m_vip.setVisibility(View.VISIBLE);
            }
        }
    }
}