package accessweb.com.br.radiocontrole.fragment;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.dialog.EscolherDialogFragment;
import accessweb.com.br.radiocontrole.model.Hosts;
import accessweb.com.br.radiocontrole.model.Program;
import accessweb.com.br.radiocontrole.model.Programa;
import accessweb.com.br.radiocontrole.model.Programs;
import accessweb.com.br.radiocontrole.util.CacheData;
import accessweb.com.br.radiocontrole.util.CognitoClientManager;
import accessweb.com.br.radiocontrole.util.RadiocontroleClient;

import static com.ampiri.sdk.c.b.a.S;

/**
 * Created by Des. Android on 26/06/2017.
 */

public class ProgramacaoFragment extends Fragment {

	private static String TAG = EscolherDialogFragment.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Programs programs;
    Hosts hosts;
    public ProgramacaoFragment() {
        // Required empty public constructor
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
        View rootView = inflater.inflate(R.layout.fragment_programacao, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setTabTextColors(Color.parseColor("#8d939b"), Color.parseColor(cacheData.getString("color")));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(cacheData.getString("color")));
        tabLayout.setupWithViewPager(viewPager);




        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setupViewPager(final ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ApiClientFactory factory = new ApiClientFactory();
                factory.credentialsProvider(CognitoClientManager.getCredentials());
                factory.apiKey("QgpKgwmkrA3ilAhtFbtW4abS5l9AHNP89Pe0WlrK");
                final RadiocontroleClient client = factory.build(RadiocontroleClient.class);
                CacheData cacheData = new CacheData(getContext());
                programs = client.radioIdProgramsGet(cacheData.getString("idRadio"));
                hosts = client.radioIdHostsGet(cacheData.getString("idRadio"));
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                //Log.v("AAAAAAAAAAAAA", "Nome:" + programs.get(0).getName());
                // SEGUNDA
                ProgramacaoDiaFragment segunda = new ProgramacaoDiaFragment();
                Bundle argsSegunda = new Bundle();
                argsSegunda.putString("dia", "segunda");
                argsSegunda.putSerializable("programas", programs);
                argsSegunda.putSerializable("hosts", hosts);
                segunda.setArguments(argsSegunda);
                adapter.addFragment(segunda, "SEG");

                // TERÇA
                ProgramacaoDiaFragment terca = new ProgramacaoDiaFragment();
                Bundle argsTerca = new Bundle();
                argsTerca.putString("dia", "terca");
                argsTerca.putSerializable("programas", programs);
                argsTerca.putSerializable("hosts", hosts);
                terca.setArguments(argsTerca);
                adapter.addFragment(terca, "TER");

                // QUARTA
                ProgramacaoDiaFragment quarta = new ProgramacaoDiaFragment();
                Bundle argsQuarta = new Bundle();
                argsQuarta.putString("dia", "quarta");
                argsQuarta.putSerializable("programas", programs);
                argsQuarta.putSerializable("hosts", hosts);
                quarta.setArguments(argsQuarta);
                adapter.addFragment(quarta, "QUA");

                // QUINTA
                ProgramacaoDiaFragment quinta = new ProgramacaoDiaFragment();
                Bundle argsQuinta = new Bundle();
                argsQuinta.putString("dia", "quinta");
                argsQuinta.putSerializable("programas", programs);
                argsQuinta.putSerializable("hosts", hosts);
                quinta.setArguments(argsQuinta);
                adapter.addFragment(quinta, "QUI");

                // SEXTA
                ProgramacaoDiaFragment sexta = new ProgramacaoDiaFragment();
                Bundle argsSexta = new Bundle();
                argsSexta.putString("dia", "sexta");
                argsSexta.putSerializable("programas", programs);
                argsSexta.putSerializable("hosts", hosts);
                sexta.setArguments(argsSexta);
                adapter.addFragment(sexta, "SEX");

                // SABADO
                ProgramacaoDiaFragment sabado = new ProgramacaoDiaFragment();
                Bundle argsSabado = new Bundle();
                argsSabado.putString("dia", "sabado");
                argsSabado.putSerializable("programas", programs);
                argsSabado.putSerializable("hosts", hosts);
                sabado.setArguments(argsSabado);
                adapter.addFragment(sabado, "SAB");

                // DOMINGO
                ProgramacaoDiaFragment domingo = new ProgramacaoDiaFragment();
                Bundle argsDomingo = new Bundle();
                argsDomingo.putString("dia", "domingo");
                argsDomingo.putSerializable("programas", programs);
                argsDomingo.putSerializable("hosts", hosts);
                domingo.setArguments(argsDomingo);
                adapter.addFragment(domingo, "DOM");

                viewPager.setAdapter(adapter);

                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2;

                tabLayout.setScrollPosition(dayOfWeek,0f,true);
                viewPager.setCurrentItem(dayOfWeek);
            }
        }.execute();

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
