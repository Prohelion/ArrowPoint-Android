package au.com.teamarrow.arrowpoint;

import java.util.Locale;

import com.example.arrowpoint.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import au.com.teamarrow.arrowpoint.fragments.DashboardFragment;
import au.com.teamarrow.arrowpoint.fragments.SystemsDetailFragment;
import au.com.teamarrow.arrowpoint.fragments.PowerDetailFragment;
import au.com.teamarrow.arrowpoint.fragments.GraphFragment;
import au.com.teamarrow.arrowpoint.fragments.PlaceholderFragment;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class
        // below).

        switch (position) {

            case 0: return new DashboardFragment();
            case 1: return new PowerDetailFragment();
            case 2: return new SystemsDetailFragment();
            case 3: return new GraphFragment();

        }

        return PlaceholderFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return context.getResources().getString(R.string.title_section2).toUpperCase(l);
            case 2:
                return context.getResources().getString(R.string.title_section3).toUpperCase(l);
            case 3:
                return context.getResources().getString(R.string.title_section4).toUpperCase(l);
        }
        return null;
    }
}
