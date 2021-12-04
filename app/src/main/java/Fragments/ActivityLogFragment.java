//package Fragments;
//
//import android.annotation.TargetApi;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cherrywork.R;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.Map;
//
//import Model.DayDataModel;
//import Model.DayModel;
//import Model.MonthModel;
//import Model.YearModel;
//import Utility.ActivityLog;
//import Utility.AsyncResponse;
//import Utility.CWIncturePreferences;
//import Utility.CWUrls;
//import Utility.CustomExpListview;
//import Utility.TimeUtils;
//import Utility.Util;
//import adapters.YearExpListviewAdapter;
//
///**
// * Created by harshu on 5/9/2016.
// */
//public class ActivityLogFragment extends Fragment implements AsyncResponse, ActivityLog {
//
//    CustomExpListview activity_log_list;
//
//    YearExpListviewAdapter adapter;
//    ArrayList<DayDataModel> daydataList = new ArrayList<>();
//    ArrayList<YearModel> yearList = new ArrayList<>();
//
//    TextView empty_text;
//    ArrayList<String> monthsAdded = new ArrayList<>();
//
//    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View v = LayoutInflater.from(getActivity()).inflate(R.layout.activitylog_fragment, null);
//
//        activity_log_list = (CustomExpListview) v.findViewById(R.id.activity_log_list);
//
//        empty_text = (TextView)v.findViewById(R.id.empty_text);
//        activity_log_list.setEmptyView(empty_text);
//
//        adapter = new YearExpListviewAdapter(getActivity(), yearList, activity_log_list, ActivityLogFragment.this);
//        activity_log_list.setAdapter(adapter);
//        activity_log_list.setNestedScrollingEnabled(true);
//        Util.setListViewHeightBasedOnChildren(activity_log_list);
//
//        activity_log_list.expandGroup(0);
//
//        Calendar calendar1 = Calendar.getInstance();
//
//        getActivityLog(calendar1.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US), calendar1.get(Calendar.YEAR));
//
//        return v;
//    }
//
//
//    public void getActivityLog(String month, int year) {
//
//        Calendar calendar1 = Calendar.getInstance();
//
//        for (int i = 0; i < months.length; i++) {
//
//            if (month.equalsIgnoreCase(months[i])) {
//                calendar1.set(Calendar.MONTH, (i));
//            }
//        }
//
//        calendar1.set(Calendar.YEAR, year);
//        calendar1.set(Calendar.DATE, 1);
//        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
//        String startDate = sdf.format(calendar1.getTime());
//
//        calendar1.set(Calendar.DATE, calendar1.getActualMaximum(Calendar.DAY_OF_MONTH));
//        String endDate = sdf.format(calendar1.getTime());
//
//        {
//            int j;
//            for (j = 0; j < monthsAdded.size(); j++) {
//                if (month.equalsIgnoreCase(monthsAdded.get(j))) {
//                    break;
//                }
//            }
//
//            if (j == monthsAdded.size()) {
//
//                if (Util.isOnline(getActivity())) {
//                    Map<String, String> headers = new HashMap<>();
//                    headers.put("x-email-id", CWIncturePreferences.getEmail());
//                    headers.put("x-access-token",
//                            CWIncturePreferences.getAccessToken());
//
////                    NetworkConnector connect = new NetworkConnector(getActivity(), NetworkConnector.TYPE_GET, CWUrls.GET_ACTIVITY_LOG + "startDate=" + startDate + "&" + "endDate=" + endDate, headers, null, this);
////                    if (connect.isAllowed()) {
////                        connect.execute();
////                    } else {
////                        Toast.makeText(getActivity(), "This action is not allowed", Toast.LENGTH_SHORT).show();
////                    }
//
//                } else {
//                    Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
//                    getActivity().onBackPressed();
//                }
//
//            }
//
//        }
//
//    }
//
//    @Override
//    public void processFinish(String output, int status_code, String url, int type) {
//
//        if (url.contains(CWUrls.GET_ACTIVITY_LOG)) {
//            String actualString = output;
//            if (output.startsWith("null")) {
//                actualString = output.substring(4);
//            }
//            parse(actualString);
//
//        }
//
//    }//end of process finish
//
//    private void parse(String output) {
//
//        daydataList.clear();
//        try {
//            JSONObject mainObject = new JSONObject(output);
//
//            if (mainObject.has("status")) {
//
//                if (mainObject.optString("status").equalsIgnoreCase("success")) {
//
//                    JSONObject dataObject = mainObject.optJSONObject("data");
//
//
//                    JSONArray activityLog = (dataObject.optJSONArray("activityLogs"));
//
//                    for (int a = 0; a < activityLog.length(); a++) {
//                        JSONObject objects = activityLog.optJSONObject(a);
//                        DayDataModel dayDataModel = new DayDataModel();
//                        dayDataModel.activityLog_id = objects.optString("_id");
//                        dayDataModel.time = objects.optString("time");
//                        dayDataModel.type = objects.optString("type");
//                        dayDataModel.action = objects.optString("action");
//                        dayDataModel.deleted = objects.optString("deleted");
//
//                        if (objects.has("content")) {
//
//                            JSONArray contentArray = objects.optJSONArray("content");
//                            if (dayDataModel.type.equalsIgnoreCase("timesheet")) {
//                                for (int c = 0; c < contentArray.length(); c++) {
//                                    JSONObject contentObject = contentArray.optJSONObject(c);
//                                    dayDataModel.workitem_id = contentObject.optString("_id");
//                                    dayDataModel.start_date = contentObject.optString("startDate");
//                                    dayDataModel.end_date = contentObject.optString("endDate");
//
//                                }
//
//                            }//type=timesheet
//
//                            else if (dayDataModel.type.equalsIgnoreCase("workitem")) {
//                                for (int c = 0; c < contentArray.length(); c++) {
//                                    JSONObject contentObj = contentArray.optJSONObject(c);
//                                    dayDataModel.workitem_id = contentObj.optString("_id");
//                                    dayDataModel.content_type = contentObj.optString("type");
//
//                                    if (dayDataModel.content_type.equalsIgnoreCase("manualTask")) {
//                                        dayDataModel.title = contentObj.optString("title");
//                                        dayDataModel.description = contentObj.optString("description");
//
//                                        JSONObject createdBy = contentObj.optJSONObject("createdBy");
//                                        dayDataModel.c_id = createdBy.optString("_id");
//                                        dayDataModel.c_displayName = createdBy.optString("displayName");
//                                        dayDataModel.c_designation = createdBy.optString("designation");
//                                        dayDataModel.c_avatar = createdBy.optString("avatar");
//                                        dayDataModel.c_email = createdBy.optString("email");
//
//                                    }//contenttype = manualtask
//
//                                    else if (dayDataModel.content_type.equalsIgnoreCase("timesheet")) {
//                                        dayDataModel.start_date = contentObj.optString("startDate");
//                                        dayDataModel.end_date = contentObj.optString("endDate");
//
//                                        JSONObject submittedBy = contentObj.optJSONObject("submittedBy");
//                                        dayDataModel.s_id = submittedBy.optString("_id");
//                                        dayDataModel.s_displayName = submittedBy.optString("displayName");
//                                        dayDataModel.s_avatar = submittedBy.optString("avatar");
//
//
//                                    }//contenttype = timesheet
//
//                                }//end of contentarray
//
//                            }//type= workitem
//                        }
//                        dayDataModel.activity = objects.optString("activity");
//
//                        daydataList.add(dayDataModel);
//
//                    }//end of activityLogs array length
//
//
//                    if (yearList.isEmpty()) {
//                        createYearModel(dataObject.optString("joinDate"));
//                    }
//
//                    for (int i = 0; i < daydataList.size(); i++)
//
//                    {
//                        String year = TimeUtils.getYear(daydataList.get(i).time);
//                        YearModel y1 = checkYear(year, yearList);
//
//                        if (y1 == null) {
//                            YearModel yModel = new YearModel();
//                        } else {
//                            if (y1.getMonthModelArrayList().isEmpty()) {
//
//                                DayModel dayModel = new DayModel();
//                                dayModel.setDay(TimeUtils.getDate(daydataList.get(i).time));
//                                dayModel.getData().add(daydataList.get(i));
//
//                                MonthModel monthModel = new MonthModel();
//                                monthModel.setMonth(TimeUtils.getMonth(daydataList.get(i).time));
//                                monthModel.getDayModelArrayList().add(dayModel);
//
//                                y1.getMonthModelArrayList().add(monthModel);
//
//                            } else {
//
//                                String month = TimeUtils.getMonth(daydataList.get(i).time);
//
//                                MonthModel m1 = checkMonth(month, y1.getMonthModelArrayList());
//
//                                if (m1 == null) {
//
//                                    DayModel dayModel = new DayModel();
//                                    dayModel.setDay(TimeUtils.getDate(daydataList.get(i).time));
//                                    dayModel.getData().add(daydataList.get(i));
//
//                                    MonthModel monthModel = new MonthModel();
//                                    monthModel.setMonth(TimeUtils.getMonth(daydataList.get(i).time));
//                                    monthModel.getDayModelArrayList().add(dayModel);
//
//                                    y1.getMonthModelArrayList().add(monthModel);
//                                }//end of if checkmonth=null
//
//                                else {
//
//                                    if (m1.getDayModelArrayList().isEmpty()) {
//                                        DayModel dayModel = new DayModel();
//                                        dayModel.setDay(TimeUtils.getDate(daydataList.get(i).time));
//                                        dayModel.getData().add(daydataList.get(i));
//
//                                        m1.getDayModelArrayList().add(dayModel);
//                                        monthsAdded.add(m1.getMonth());
//                                    } else {
//                                        DayModel d1 = checkDay(TimeUtils.getDate(daydataList.get(i).time), m1.getDayModelArrayList());
//
//                                        if (d1 == null) {
//                                            DayModel dayModel = new DayModel();
//                                            dayModel.setDay(TimeUtils.getDate(daydataList.get(i).time));
//                                            dayModel.getData().add(daydataList.get(i));
//
//                                            m1.getDayModelArrayList().add(dayModel);
//                                        } else {
//                                            d1.getData().add(daydataList.get(i));
//                                        }
//                                    }
//
//                                }
//                            }
//
//                        }//if y1 is not null
//                    }//end of daydatalist loop
//
//                    adapter.dataChanged(yearList);
//                    adapter.notifyDataSetChanged();
//
//                }
//            } else {
//                Toast.makeText(getActivity(), mainObject.optString("message"), Toast.LENGTH_SHORT).show();
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private YearModel checkYear(String year, ArrayList<YearModel> yearList) {
//        for (int y = 0; y < yearList.size(); y++) {
//            if (year.equalsIgnoreCase(String.valueOf(yearList.get(y).year))) {
//                return yearList.get(y);
//            }
//        }
//        return null;
//
//    }
//
//    private void createYearModel(String joiningDate) {
//
//        Calendar c = Calendar.getInstance();
//        int currentYear = c.get(Calendar.YEAR);
//        int joiningYear = Integer.parseInt(TimeUtils.getYear(joiningDate));
//
//        int joiningMonth = Integer.parseInt(TimeUtils.getMonthNumber(joiningDate));
//        c.set(Calendar.MONTH, (joiningMonth - 1));
//        c.set(Calendar.YEAR, joiningYear);
//        c.set(Calendar.DATE, 1);
//
//        do {
//            YearModel yearModel = new YearModel();
//            yearModel.setYear(Integer.parseInt(String.valueOf(currentYear)));
//            createMonthModel(yearModel, c.getTime());
//            currentYear--;
//
//        } while (currentYear >= joiningYear);
//
//
//    }
//
//    private void createMonthModel(YearModel yearModel, Date joinDate) {
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DATE, 1);
//        int joinyear = joinDate.getYear();
//        int joiningMonth = joinDate.getMonth();
//
//        {
//
//            for (int i = 0; i < months.length; i++) {
//
//                Calendar cal = Calendar.getInstance();
//                cal.set(Calendar.MONTH, i);
//                cal.set(Calendar.YEAR, Integer.parseInt(String.valueOf(yearModel.year)));
//                cal.set(Calendar.DATE, 1);
//
////                if ((cal.getTime().after(joinDate) || (cal.get(Calendar.MONTH) == joiningMonth && cal.get(Calendar.YEAR) == joinyear)) && (cal.getTime().before(calendar.getTime()) || (cal.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) && cal.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)))) {
////                    MonthModel monthModel = new MonthModel();
////                    monthModel.setMonth(months[i]);
////                    yearModel.getMonthModelArrayList().add(monthModel);
////                }
//            }
//        }
//        Collections.reverse(yearModel.getMonthModelArrayList());
//        yearList.add(yearModel);
//
//    }
//
//    private MonthModel checkMonth(String month, ArrayList<MonthModel> monthList) {
//        String mon;
//        ArrayList<MonthModel> list;
//        mon = month;
//        list = monthList;
//        for (int i = 0; i < list.size(); i++) {
//
//            if (mon.equalsIgnoreCase(list.get(i).getMonth())) {
//                return list.get(i);
//            }
//        }
//        return null;
//    }
//
//    private DayModel checkDay(String day, ArrayList<DayModel> dayList) {
//        String day1;
//        ArrayList<DayModel> list;
//        day1 = day;
//        list = dayList;
//        for (int d = 0; d < list.size(); d++) {
//            if (day1.equalsIgnoreCase(list.get(d).getDay())) {
//                return list.get(d);
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public void configurationUpdated(boolean configUpdated) {
//
//    }

//}
