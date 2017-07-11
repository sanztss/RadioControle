package accessweb.com.br.radiocontrole.dialog;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.fragment.CadastroFragment;
import accessweb.com.br.radiocontrole.fragment.EntrarFragment;
import accessweb.com.br.radiocontrole.util.CacheData;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.*;
import android.support.design.widget.TabLayout;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Des. Android on 27/06/2017.
 */

public class SuaContaDialogFragment extends DialogFragment {

    private static String TAG = SuaContaDialogFragment.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public SuaContaDialogFragment(){

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CacheData cacheData = new CacheData(getContext());
        View rootView = inflater.inflate(R.layout.fragment_sua_conta, null, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.suaContaToolbar);
        toolbar.setTitle("Sua Conta");
        toolbar.setBackgroundColor(Color.parseColor(cacheData.getString("color")));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.parseColor("#8d939b"), Color.parseColor(cacheData.getString("color")));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(cacheData.getString("color")));

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return dialog;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        /*EntrarFragment entrarFragment = new EntrarFragment();
        entrarFragment.set*/
        adapter.addFragment(new EntrarFragment(), "ENTRAR");
        adapter.addFragment(new CadastroFragment(), "CADASTRAR");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}