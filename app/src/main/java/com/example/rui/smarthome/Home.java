package com.example.rui.smarthome;

import android.app.ActionBar;
import android.app.Activity;

import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.Bundle;
/*import android.app.Fragment;
import android.app.FragmentManager;*/
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.DialogInterface;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

public class Home extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*if(getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().
                heightPixels)
        {
            Toast.makeText(this,"Screen switched to Landscape mode",Toast.LENGTH_SHORT).show();
            //setContentView(R.layout.fragment_home_landscape);
        }
        else
        {
            Toast.makeText(this,"Screen switched to Portrait mode",Toast.LENGTH_SHORT).show();
        }*/

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        Fragment fragment = new HomeView();
        //int position = mNavigationDrawerFragment.getListView().getSelectedItemPosition();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        switch(position) {
            case 0: {
                Fragment fragment = new HomeView();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;
            }
            case 1: {
                Fragment fragment = new Room();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;
            }
            case 2:
            {
                Fragment fragment = new Bedroom();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;
            }
            case 3:
            {
                Fragment fragment = new Kitchen();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;
            }
            case 4: {
                Fragment fragment = new Bath();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;
            }
            case 5: {
                Fragment fragment = new Office();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;
            }
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.home);
                break;
            case 1:
                mTitle = getString(R.string.dinnerroom);
                break;
            case 2:
                mTitle = getString(R.string.bedroom);
                break;
            case 3:
                mTitle = getString(R.string.kitchen);
            break;
            case 4:
                mTitle = getString(R.string.bath);
                break;
            case 5:
                mTitle = getString(R.string.office);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        List l = fragmentManager.getFragments();
        if (l == null) {
            finish();
        } else {
            String[] parts = l.get(l.size() - 1).toString().split("\\{");
            if (parts[0].equals("Home")) {
                finish();
            } else {
                Fragment fragment = new HomeView();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
            }
        }
    }

    public static class HomeView extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_home, container, false);
            //mNavigationDrawerFragment.getListView().setItemChecked(0,true);
            Button quarto = (Button) view.findViewById((R.id.quarto));
            Button sala = (Button) view.findViewById((R.id.sala));
            Button cozinha = (Button) view.findViewById((R.id.cozinha));
            Button escritorio = (Button) view.findViewById((R.id.escritorio));
            Button wc = (Button) view.findViewById((R.id.wc));

            getActivity().getActionBar().setTitle("Home");

            quarto.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getActivity().getActionBar().setTitle("Bedroom");
                    Fragment fragment = new Bedroom();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });

            sala.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getActivity().getActionBar().setTitle("Room");
                    Fragment fragment = new Room();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });

            cozinha.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getActivity().getActionBar().setTitle("Kitchen");
                    Fragment fragment = new Kitchen();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });

            escritorio.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getActivity().getActionBar().setTitle("Office");
                    Fragment fragment = new Office();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });

            wc.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getActivity().getActionBar().setTitle("Bath");
                    Fragment fragment = new Bath();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });

            return view;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Home) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
