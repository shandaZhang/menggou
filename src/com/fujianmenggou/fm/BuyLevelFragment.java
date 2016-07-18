package com.fujianmenggou.fm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fujianmenggou.R;
import com.fujianmenggou.atv.WebActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

import dujc.dtools.FinalHttp;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;

public final class BuyLevelFragment extends Fragment implements OnClickListener {
	private static final String KEY_CONTENT = "BuyLevelFragment:Content";
    private String content = "";
    String levelRoad = "";
    String rbdiscount = "";
    String ybdiscount = "";
    String xmunionpaydiscount = "";
    protected SharedPreferences userInfoPreferences;
    String group_id;
    
    private TextView text_rbdiscount, text_ybdiscount, text_xmunionpaydiscount, text_levelRoad;
    private Button btn_levelUp;
    
    public static BuyLevelFragment newInstance(String content, String levelRoad, String rbdiscount, String ybdiscount, String xmunionpaydiscount) {
        BuyLevelFragment fragment = new BuyLevelFragment();
        fragment.content = content;
        fragment.levelRoad = levelRoad;
        fragment.rbdiscount = rbdiscount;
        fragment.ybdiscount = ybdiscount;
        fragment.xmunionpaydiscount = xmunionpaydiscount;
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
        	content = savedInstanceState.getString("content");
        	levelRoad = savedInstanceState.getString("levelRoad");
        	rbdiscount = savedInstanceState.getString("rbdiscount");
        	ybdiscount = savedInstanceState.getString("ybdiscount");
        	xmunionpaydiscount = savedInstanceState.getString("xmunionpaydiscount");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_buylevel, null);
    	text_rbdiscount = (TextView) view.findViewById(R.id.text_rbdiscount);
    	text_ybdiscount = (TextView) view.findViewById(R.id.text_ybdiscount);
    	text_xmunionpaydiscount = (TextView) view.findViewById(R.id.text_xmunionpaydiscount);
    	text_levelRoad = (TextView) view.findViewById(R.id.text_levelRoad);
    	btn_levelUp = (Button) view.findViewById(R.id.btn_levelUp);
    	btn_levelUp.setOnClickListener(this);
    	initData();
    	
        return view;
    }
    
    private void initData() {
    	text_levelRoad.setText(levelRoad);
    	text_rbdiscount.setText(rbdiscount+"%");
    	text_ybdiscount.setText(ybdiscount+"%");
    	text_xmunionpaydiscount.setText(xmunionpaydiscount+"%");
    	userInfoPreferences = getActivity().getSharedPreferences("user_info",getActivity().MODE_PRIVATE);
    	group_id = userInfoPreferences.getString("group_id","");
    	if (group_id.equals("2")) {
			btn_levelUp.setBackgroundResource(R.color.gray);
		}
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(content, "content");
        outState.putString(levelRoad, "levelRoad");
        outState.putString(rbdiscount, "rbdiscount");
        outState.putString(ybdiscount, "ybdiscount");
        outState.putString(xmunionpaydiscount, "xmunionpaydiscount");
    }


	@Override
	public void onClick(View v) {
		
		if (!group_id.equals("2")) {
			doUp();
		}else {
			Tools.showTextToast(getActivity(), "您已是特级代理，无需升级！");
		}
	}
	
	private void doUp() {
		String userId = userInfoPreferences.getString("id","");
//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		intent.setData(Uri.parse(GlobalVars.upUrl+userId));
//		getActivity().startActivity(intent);
		getActivity().startActivity(new Intent(getActivity(), WebActivity.class).putExtra("url", GlobalVars.upUrl+userId) );
	}
	
}
