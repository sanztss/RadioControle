package accessweb.com.br.radiocontrole.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import accessweb.com.br.radiocontrole.R;

/**
 * Created by Des. Android on 26/06/2017.
 */

public class SabadoFragment extends Fragment {

    public SabadoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dia_semana, container, false);

        return rootView;
    }

}
