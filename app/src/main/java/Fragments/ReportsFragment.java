package Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrapps.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Utility.NestedListView;

import static Utility.Util.isOnline;


@SuppressLint("SimpleDateFormat")
public class ReportsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    NestedListView nestedListView;
    String _viewStatus, _viewDate = "";

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    private SwipeRefreshLayout refreshLayout;
    public static Boolean refreshInProgress = false;
    RelativeLayout r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11;

    @SuppressLint("SimpleDateFormat")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        final View v = LayoutInflater.from(getActivity()).inflate(R.layout.views, null);
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);

        nestedListView = (NestedListView) v.findViewById(R.id.list);

        r1 = (RelativeLayout) v.findViewById(R.id.layout1);
        r2 = (RelativeLayout) v.findViewById(R.id.layout2);
        r3 = (RelativeLayout) v.findViewById(R.id.layout3);
        r4 = (RelativeLayout) v.findViewById(R.id.layout4);
        r5 = (RelativeLayout) v.findViewById(R.id.layout5);
        r6 = (RelativeLayout) v.findViewById(R.id.layout6);
        r7 = (RelativeLayout) v.findViewById(R.id.layout7);
        r8 = (RelativeLayout) v.findViewById(R.id.layout8);
        r9 = (RelativeLayout) v.findViewById(R.id.layout9);
        r10 = (RelativeLayout) v.findViewById(R.id.layout10);
        r11 = (RelativeLayout) v.findViewById(R.id.layout11);

        r1.setOnClickListener(onClickListener);
        r2.setOnClickListener(onClickListener);
        r3.setOnClickListener(onClickListener);
        r4.setOnClickListener(onClickListener);
        r5.setOnClickListener(onClickListener);
        r6.setOnClickListener(onClickListener);
        r7.setOnClickListener(onClickListener);
        r8.setOnClickListener(onClickListener);
        r9.setOnClickListener(onClickListener);
        r10.setOnClickListener(onClickListener);
        r11.setOnClickListener(onClickListener);

        TextView tv1 = (TextView) v.findViewById(R.id.label3);
        TextView tv2 = (TextView) v.findViewById(R.id.label6);
        TextView tv3 = (TextView) v.findViewById(R.id.label9);
        TextView tv4 = (TextView) v.findViewById(R.id.label12);
        TextView tv5 = (TextView) v.findViewById(R.id.label5);
        TextView tv6 = (TextView) v.findViewById(R.id.label18);
        TextView tv7 = (TextView) v.findViewById(R.id.label21);
        TextView tv8 = (TextView) v.findViewById(R.id.label24);
        TextView tv9 = (TextView) v.findViewById(R.id.label27);
        TextView tv10 = (TextView) v.findViewById(R.id.label30);
        TextView tv11 = (TextView) v.findViewById(R.id.label33);

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(calendar.getTime());

        tv1.setText(formattedDate);
        tv2.setText(formattedDate);
        tv3.setText(formattedDate);
        tv4.setText(formattedDate);
        tv5.setText(formattedDate);
        tv6.setText(formattedDate);
        tv7.setText(formattedDate);
        tv8.setText(formattedDate);
        tv9.setText(formattedDate);
        tv10.setText(formattedDate);
        tv11.setText(formattedDate);
        return v;

    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.layout1) {
//                Intent intent = new Intent(getActivity(), BarChartActivity.class);
//                startActivity(intent);
            }
        }
    };

    @Override
    public void onRefresh() {

        if (isOnline(getActivity())) {
            refreshLayout.setRefreshing(true);
            refreshInProgress = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(false);
                }
            }, 5000);
        } else {
            refreshLayout.setRefreshing(false);
        }
        if (refreshInProgress)
            return;
        refreshInProgress = false;

    }


    @Override
    public void onDetach() {
        super.onDetach();
        if (isRemoving()) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isRemoving()) {
        }
    }

    public void setStatus(String value) {
        _viewStatus = value;
    }

    public void setDate(String value) {
        _viewDate = value;
    }


}
