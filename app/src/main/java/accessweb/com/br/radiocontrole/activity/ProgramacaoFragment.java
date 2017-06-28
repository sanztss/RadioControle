package accessweb.com.br.radiocontrole.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

import accessweb.com.br.radiocontrole.R;

/**
 * Created by Des. Android on 26/06/2017.
 */

public class ProgramacaoFragment extends Fragment {

	private static String TAG = EscolherDialogFragment.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_programacao, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        // SEGUNDA
        ProgramacaoDiaFragment segunda = new ProgramacaoDiaFragment();
        Bundle argsSegunda = new Bundle();
        argsSegunda.putString("dia", "segunda");
        segunda.setArguments(argsSegunda);
        adapter.addFragment(segunda, "SEG");

        // TERÇA
        ProgramacaoDiaFragment terca = new ProgramacaoDiaFragment();
        Bundle argsTerca = new Bundle();
        argsTerca.putString("dia", "terca");
        terca.setArguments(argsTerca);
        adapter.addFragment(terca, "TER");

        // QUARTA
        ProgramacaoDiaFragment quarta = new ProgramacaoDiaFragment();
        Bundle argsQuarta = new Bundle();
        argsQuarta.putString("dia", "quarta");
        quarta.setArguments(argsQuarta);
        adapter.addFragment(quarta, "QUA");

        // QUINTA
        ProgramacaoDiaFragment quinta = new ProgramacaoDiaFragment();
        Bundle argsQuinta = new Bundle();
        argsQuinta.putString("dia", "quinta");
        quinta.setArguments(argsQuinta);
        adapter.addFragment(quinta, "QUI");

        // SEXTA
        ProgramacaoDiaFragment sexta = new ProgramacaoDiaFragment();
        Bundle argsSexta = new Bundle();
        argsSexta.putString("dia", "sexta");
        sexta.setArguments(argsSexta);
        adapter.addFragment(sexta, "SEX");

        // SABADO
        ProgramacaoDiaFragment sabado = new ProgramacaoDiaFragment();
        Bundle argsSabado = new Bundle();
        argsSabado.putString("dia", "sabado");
        sabado.setArguments(argsSabado);
        adapter.addFragment(sabado, "SAB");

        // DOMINGO
        ProgramacaoDiaFragment domingo = new ProgramacaoDiaFragment();
        Bundle argsDomingo = new Bundle();
        argsDomingo.putString("dia", "domingo");
        domingo.setArguments(argsDomingo);
        adapter.addFragment(domingo, "DOM");

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
