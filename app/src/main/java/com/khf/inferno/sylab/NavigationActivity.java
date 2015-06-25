package com.khf.inferno.sylab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationActivity extends FragmentActivity {

    private DrawerLayout mDrawerLayout;

    private Fragment fragment = null;

    private static final String ARG_PARAM1 = "fileName";
    private static final String ARG_PARAM2 = "url";

    private Typeface bold;
    private Typeface normal;

    ExpandableListView expListView;
    ExpandableListAdapter listAdapter;

    private String[] subjectNames = {
            "TE1", "M1", "EP", "EC", "EG", "CT", "PL", "CL", "EPL", "CP-Lab",
            "TE2", "M2", "PCE", "CP", "PC++", "DPSD", "PRG-Lab", "DIGITAL-Lab", "PAD", "PAD",
            "ANT", "EDC", "DS", "DBMS", "EVS", "CA", "DBMS-Lab", "DS-Lab", "PAD", "PAD",
            "EECS", "DAA", "OS", "JIP", "PQT", "SE", "OS-Lab", "JIP-Lab", "PAD", "PAD",
            "OOAD", "TOC", "SSI", "MP", "DCCN", "TW", "CN-Lab", "CT-Lab", "MP-Lab", "PAD",
            "AI", "DSP", "CG", "CD", "PP", "CG-Lab", "PAD", "PAD", "PAD", "PAD",
            "MPC", "SIC", "POM", "PARALLEL", "SD-Lab", "MAD-Lab", "PAD", "PAD", "PAD", "PAD"
    };
    private String[] missing = { "TE1", "EP", "EC", "EG", "PL", "CL", "EPL", "TE2"};
    private ArrayList<String> missingSubjects;
    private String baseUrl = "http://cs.annauniv.edu/academic/ug2012/";

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        missingSubjects = new ArrayList<String>();
        missingSubjects.add(0, missing[0]);
        missingSubjects.add(1, missing[1]);
        missingSubjects.add(2, missing[2]);
        missingSubjects.add(3, missing[3]);
        missingSubjects.add(4, missing[4]);
        missingSubjects.add(5, missing[5]);
        missingSubjects.add(6, missing[6]);
        missingSubjects.add(7, missing[7]);

        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(homeOnclickListener);

        bold = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoCondensed-Bold.ttf");
        normal = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoCondensed-Regular.ttf");

        TextView appName = (TextView) findViewById(R.id.appname);
        appName.setTypeface(bold);

        Button openButton = (Button) findViewById(R.id.open_btn);
        openButton.setTypeface(normal);

        setupDrawer();
    }

    //Get the names and icons references to build the drawer menu...
    private void setupDrawer() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
        mDrawerLayout.setDrawerListener(mDrawerListener);

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

        mDrawerLayout.closeDrawer(expListView);

        expListView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                int index = groupPosition * 10 + childPosition;
                if(groupPosition == 7 && childPosition == 0) fragment = new About();
                else if (missingSubjects.contains(subjectNames[index])) fragment = new DisplayFragment();
                else {
                    fragment = new DisplayFragment();
                    Bundle args = new Bundle();
                    args.putString(ARG_PARAM1, subjectNames[index]);
                    args.putString(ARG_PARAM2, baseUrl + subjectNames[index] + ".pdf");
                    fragment.setArguments(args);
                }
                getSupportFragmentManager().beginTransaction().addToBackStack(fragment.toString()).replace(R.id.content_frame, fragment).commit();
                mDrawerLayout.closeDrawer(expListView);
                return false;
            }
        });
    }

    View.OnClickListener homeOnclickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mDrawerLayout.isDrawerOpen(expListView)) mDrawerLayout.closeDrawer(expListView);
            else mDrawerLayout.openDrawer(expListView);
        }
    };

    // Catch the events related to the drawer to arrange views according to this action if necessary...
    private DrawerListener mDrawerListener = new DrawerListener() {

        @Override
        public void onDrawerStateChanged(int status) {

        }

        @Override
        public void onDrawerSlide(View view, float slideArg) {

        }

        @Override
        public void onDrawerOpened(View view) {
        }

        @Override
        public void onDrawerClosed(View view) {
        }
    };

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data - All semester titles represent a group button
        listDataHeader.add("Semester 1");
        listDataHeader.add("Semester 2");
        listDataHeader.add("Semester 3");
        listDataHeader.add("Semester 4");
        listDataHeader.add("Semester 5");
        listDataHeader.add("Semester 6");
        listDataHeader.add("Semester 7");
        listDataHeader.add("More...");

        // Adding child data
        List<String> s1 = new ArrayList<>();
        s1.add("Technical English 1");
        s1.add("Mathematics 1");
        s1.add("Engineering Physics");
        s1.add("Engineering Chemistry");
        s1.add("Engineering Graphics");
        s1.add("Computing Techniques");
        s1.add("Physics Lab");
        s1.add("Chemistry Lab");
        s1.add("Engineering Practices Lab");
        s1.add("Computing Techniques Lab");

        List<String> s2 = new ArrayList<>();
        s2.add("Technical English 2");
        s2.add("Mathematics 2");
        s2.add("Principles of Computer Engineering");
        s2.add("Physics for Information Science");
        s2.add("Programming using C++");
        s2.add("Digital Principles and System Design");
        s2.add("Programming Laboratory");
        s2.add("Digital Laboratory");

        List<String> s3 = new ArrayList<>();
        s3.add("Algebra and Number Theory");
        s3.add("EDC");
        s3.add("Data Structures");
        s3.add("DBMS");
        s3.add("EVS");
        s3.add("Computer Architecture");
        s3.add("DBMS Lab");
        s3.add("Data Structures Lab");

        List<String> s4 = new ArrayList<>();
        s4.add("Electrical Engineering and Control Systems");
        s4.add("Design and Analysis of Algorithms");
        s4.add("Operating Systems");
        s4.add("Java and Internet Programming");
        s4.add("Probability and Queuing Theory");
        s4.add("Software Engineering");
        s4.add("Operating Systems Lab");
        s4.add("Java and Internet Programming Lab");

        List<String> s5 = new ArrayList<>();
        s5.add("Object oriented Analysis and Design");
        s5.add("Theory Of Computation");
        s5.add("Software System Internals");
        s5.add("Microprocessors and Micro Controllers");
        s5.add("Data Communication and Computer Networks");
        s5.add("Employability Skills");
        s5.add("Communications and Networks Laboratory");
        s5.add("Case Tools Lab");
        s5.add("Microprocessor Lab");

        List<String> s6 = new ArrayList<>();
        s6.add("Artificial Intelligence");
        s6.add("DSP");
        s6.add("Computer Graphics and Multimedia");
        s6.add("Compiler Design");
        s6.add("Programming Paradigms");
        s6.add("Creative and Innovative Project");
        s6.add("Computer Graphics and Multimedia Laboratory");

        List<String> s7 = new ArrayList<>();
        s7.add("Mobile and Persuasive Computing");
        s7.add("Security in Computing");
        s7.add("Principles Of Management");
        s7.add("Parallel Programming");
        s7.add("Software Development Laboratory");
        s7.add("Mobile Application Development Laboratory");

        List<String> s8 = new ArrayList<>();
        s8.add("About");

        listDataChild.put(listDataHeader.get(0), s1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), s2);
        listDataChild.put(listDataHeader.get(2), s3);
        listDataChild.put(listDataHeader.get(3), s4);
        listDataChild.put(listDataHeader.get(4), s5);
        listDataChild.put(listDataHeader.get(5), s6);
        listDataChild.put(listDataHeader.get(6), s7);
        listDataChild.put(listDataHeader.get(7), s8);
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, List<String>> _listDataChild;

        public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            final String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item, null);
            }

            TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
            txtListChild.setTypeface(normal);
            txtListChild.setText(childText);
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_group, null);
            }

            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(bold);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
