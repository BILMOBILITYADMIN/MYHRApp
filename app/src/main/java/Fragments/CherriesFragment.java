package Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hrapps.CSC_Britannia.CSC_Dashboard;
import com.hrapps.LMS_Britannia.LMS_Dashboard;
import com.hrapps.Leave_Britannia.Leave_Dashboard;
import com.hrapps.OnBoarding_Britannia.OnBoarding_Dashboard;
import com.hrapps.PMS_Britannia.PMS_Dashboard;
import com.hrapps.R;
import com.hrapps.Recruitment_Britannia.Recruitment_Dashboard;
import com.hrapps.dev_con_britannia.DevConDashboardActivity;
import com.hrapps.eBAT.EBATActivity;
import com.hrapps.eBAT.EBATActivityNew;

import java.util.ArrayList;

import DB.DbUtil;
import Model.ConfigModel;
import Model.RolesModel;
import Utility.Constants;
import adapters.CherriesAdapter;

/**
 * Created by Arun on 10-09-2015.
 */
public class CherriesFragment extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recycler_view;
    private CherriesAdapter portsAdapter;
    TabLayout tabLayout;
    boolean mini = false;
    DbUtil db = new DbUtil();
    ArrayList<ConfigModel> types = new ArrayList<>();
    ArrayList<RolesModel> rolesListPMS = new ArrayList<>();
    ArrayList<RolesModel> rolesListCSC = new ArrayList<>();
    ArrayList<RolesModel> rolesListREC = new ArrayList<>();
    ArrayList<RolesModel> rolesListLMS = new ArrayList<>();
    ArrayList<RolesModel> rolesListOB = new ArrayList<>();
    ArrayList<RolesModel> rolesListeBAT = new ArrayList<>();
    ArrayList<RolesModel> rolesListLeave = new ArrayList<>();
    ArrayList<RolesModel> rolesListDC = new ArrayList<>();
    private BroadcastReceiver broadcastReceiver;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ports_fragment, container, false);

        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);

        GridLayoutManager grid = new GridLayoutManager(getActivity(), 4);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(grid);

        setupCherries(mini);

        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setupCherries(mini);
            }
        };


    }

    private void refresh() {
        setupCherries(mini);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 5000);
    }


    public void gridItemClicked(int position) {

        if (types.get(position).id.equalsIgnoreCase("Champion Score Card")) {
            Intent csc = new Intent(getActivity(), CSC_Dashboard.class);
            csc.putExtra("type", "dashboard");
            csc.putExtra("roles", rolesListCSC);
            csc.putExtra("cherrySelected", "csc");
            startActivity(csc);
        }

        if (types.get(position).id.equalsIgnoreCase("Recruitment")) {
            Intent recruitment = new Intent(getActivity(), Recruitment_Dashboard.class);
            recruitment.putExtra("roles", rolesListREC);
            recruitment.putExtra("cherrySelected", "recruitment");
            startActivity(recruitment);
        }

        if (types.get(position).id.equalsIgnoreCase("Performance Management System")) {
            Intent pms = new Intent(getActivity(), PMS_Dashboard.class);
            pms.putExtra("roles", rolesListPMS);
            pms.putExtra("cherrySelected", "pms");
            startActivity(pms);
        }

        if (types.get(position).id.equalsIgnoreCase("Learning Management System")) {
            Intent lms = new Intent(getActivity(), LMS_Dashboard.class);
            lms.putExtra("roles", rolesListLMS);
            lms.putExtra("cherrySelected", "lms");
            startActivity(lms);
        }
        if (types.get(position).id.equalsIgnoreCase("On Boarding")) {
            Intent ob = new Intent(getActivity(), OnBoarding_Dashboard.class);
            ob.putExtra("roles", rolesListOB);
            ob.putExtra("cherrySelected", "onboarding");
            startActivity(ob);
        }
        if (types.get(position).id.equalsIgnoreCase("e-bat")) {
            Intent ob = new Intent(getActivity(), EBATActivityNew.class);
            ob.putExtra("roles", rolesListeBAT);
            ob.putExtra("cherrySelected", "e-bat");
            startActivity(ob);
        }
        if (types.get(position).id.equalsIgnoreCase("Leave Application")) {
            Intent leave = new Intent(getActivity(), Leave_Dashboard.class);
            leave.putExtra("roles", rolesListLeave);
            leave.putExtra("cherrySelected", "leave");
            startActivity(leave);
        }
        if (types.get(position).id.equalsIgnoreCase("Development Conversations")) {
            Intent ob = new Intent(getActivity(), DevConDashboardActivity.class);
            ob.putExtra("roles", rolesListDC);
            ob.putExtra("cherrySelected", "e-bat");
            startActivity(ob);
        }
    }

    private void setupCherries(boolean mini) {
        types = db.getTypes(getActivity());
        boolean isLeaveAvailable = false, isDCAvailable = false, isEbatAvailable = false, isOnBoarding = false;

        for (int t = 0; t < types.size(); t++) {
            if (types.get(t).id.equalsIgnoreCase("Leave Application")) {
                isLeaveAvailable = true;
            } else if (types.get(t).id.equalsIgnoreCase(getString(R.string.development_conversation))) {
                isDCAvailable = true;
            } else if (types.get(t).id.equalsIgnoreCase(getString(R.string.eBat))) {
                isEbatAvailable = true;
            } else if (types.get(t).id.equalsIgnoreCase("Recruitment")) {
                isOnBoarding = true;
            }

            if (isLeaveAvailable && isDCAvailable && isEbatAvailable && isOnBoarding) {
                break;
            }
        }
//
        if (isLeaveAvailable) {
            types.clear();
            ConfigModel configModel = new ConfigModel();
            configModel.id = "Leave Application";
            types.add(configModel);
        }

        if (isDCAvailable) {
            if (!isLeaveAvailable) types.clear();
            ConfigModel configModel = new ConfigModel();
            configModel.id = getString(R.string.development_conversation);
            types.add(configModel);
        }
        if (isEbatAvailable) {
            if (!isEbatAvailable) types.clear();
            ConfigModel configModel = new ConfigModel();
            configModel.id = getString(R.string.eBat);
            types.add(configModel);
        }
        if (isOnBoarding) {
            if (!isOnBoarding) types.clear();
            ConfigModel configModel = new ConfigModel();
            configModel.id = "Recruitment";
            types.add(configModel);
        }

//        ConfigModel configModel = new ConfigModel();
//        configModel.id = "Recruitment";
//        types.add(configModel);

        setupRoles();

        if (mini) {
            db = new DbUtil();
            portsAdapter = new CherriesAdapter(getActivity(), types, true);
            recycler_view.setAdapter(portsAdapter);
        } else {
            db = new DbUtil();
            portsAdapter = new CherriesAdapter(getActivity(), types, false);
            recycler_view.setAdapter(portsAdapter);
        }

        portsAdapter.setOnItemClickListener(new CherriesAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                gridItemClicked(position);
            }
        });
    }

    private void setupRoles() {
        rolesListPMS = db.getMasterRolesList("Performance Management System");
        rolesListCSC = db.getMasterRolesList("Champion Score Card");
        rolesListREC = db.getMasterRolesList("Recruitment");
        rolesListLMS = db.getMasterRolesList("Learning Management System");
        rolesListOB = db.getMasterRolesList("On Boarding");
        rolesListeBAT = db.getMasterRolesList("e-BAT");
        rolesListLeave = db.getMasterRolesList("Leave Application");
        rolesListDC = db.getMasterRolesList(getString(R.string.development_conversation));
    }

    @Override
    public void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((broadcastReceiver),
                new IntentFilter(Constants.CONFIG_UPDATED)
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
    }
}
