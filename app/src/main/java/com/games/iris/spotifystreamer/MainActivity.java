package com.games.iris.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements
    MainActivityFragment.OnMainFragmentInteractionListener,
    TopTracksFragment.TopTracksFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // If we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
            return;
        }

        getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment, new MainActivityFragment()).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onArtistSelected(String spotifyId, String artistName) {
        TopTracksFragment topsFragment = TopTracksFragment.newInstance(spotifyId, artistName);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (findViewById(R.id.fragment_top_tracks) != null) {
            transaction.replace(R.id.fragment_top_tracks, topsFragment);
        }
        else {
            transaction.replace(R.id.fragment, topsFragment);
            transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        transaction.commit();

    }

    @Override
    public void onBackPressedFragment() {
        getSupportFragmentManager().popBackStack();
    }
}
