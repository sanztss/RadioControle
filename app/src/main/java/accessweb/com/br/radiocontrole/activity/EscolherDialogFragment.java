package accessweb.com.br.radiocontrole.activity;



import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import accessweb.com.br.radiocontrole.R;
import accessweb.com.br.radiocontrole.adapter.ModalListAdapter;
import accessweb.com.br.radiocontrole.model.NavDrawerItem;

public class EscolherDialogFragment extends DialogFragment {

    private static String TAG = EscolherDialogFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ModalListAdapter adapter;
    private View containerView;
    private static ArrayList<String> titles = new ArrayList<String>();
    private static ArrayList<Integer> icons = new ArrayList<Integer>();
    private ModalListFragmentListener drawerListener;
    private String modalTitle;
    private String modalTipo;
    //private NavDrawerItem[] itens;

    public EscolherDialogFragment(String title, String modal) {
        modalTitle =  title;
        modalTipo = modal;
    }

    public void setDrawerListener(ModalListFragmentListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();


        // preparing navigation drawer items
        for (int i = 0; i < titles.size(); i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles.get(i));
            navItem.setIcon(icons.get(i));
            data.add(navItem);
        }


        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels

        titles.clear();
        icons.clear();

        if (modalTipo.equals("canais")){
            titles.add("Ao vivo");
            titles.add("Sertanejo");
            titles.add("MPB");
            icons.add(R.drawable.ic_library_music_gray);
            icons.add(R.drawable.ic_library_music_gray);
            icons.add(R.drawable.ic_library_music_gray);
        } else if (modalTipo.equals("dormir")) {
            titles.add("00:30");
            titles.add("01:00");
            titles.add("01:30");
            titles.add("02:00");
            titles.add("02:30");
            titles.add("03:00");
            titles.add("03:30");
            titles.add("04:00");

            icons.add(R.drawable.ic_clock_gray);
            icons.add(R.drawable.ic_clock_gray);
            icons.add(R.drawable.ic_clock_gray);
            icons.add(R.drawable.ic_clock_gray);
            icons.add(R.drawable.ic_clock_gray);
            icons.add(R.drawable.ic_clock_gray);
            icons.add(R.drawable.ic_clock_gray);
            icons.add(R.drawable.ic_clock_gray);
        } else if (modalTipo.equals("mural")) {
            titles.add("Texto");
            titles.add("Imagem");
            titles.add("Áudio");

            icons.add(R.drawable.ic_format_text_gray);
            icons.add(R.drawable.ic_file_image_gray);
            icons.add(R.drawable.ic_music_gray);
        }


        // Array String [] e TypedArray
        //titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
        //icons = getActivity().getResources().obtainTypedArray(R.array.nav_drawer_icons);
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.custom_dialog, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.my_recycler_view);

        adapter = new ModalListAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.v("aaaa","ahushuahus");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        this.getDialog().setTitle(modalTitle);
        return layout;
    }*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String titleText = modalTitle;
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(1.0f);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(titleText);
        spannableStringBuilder.setSpan(
                relativeSizeSpan,
                0,
                1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout LLayout = new LinearLayout(this.getContext());
        LLayout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(15,15,15,15);
        LLayout.setLayoutParams(params);
        LLayout.setPadding(15,15,15,15);
        TextView tv_title = new TextView(this.getContext());
        tv_title.setLayoutParams(params);
        tv_title.setTextColor(Color.parseColor("#0074c8"));
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
        tv_title.setGravity(Gravity.CENTER_HORIZONTAL);
        tv_title.setText(spannableStringBuilder);
        LLayout.addView(tv_title);

        AlertDialog.Builder builder =  new  AlertDialog.Builder(getActivity())
            .setCustomTitle(LLayout)
            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            }
        );

        LayoutInflater i = getActivity().getLayoutInflater();

        View layout = i.inflate(R.layout.custom_dialog,null);
        recyclerView = (RecyclerView) layout.findViewById(R.id.my_recycler_view);

        adapter = new ModalListAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.v("aaaa","" + view);
                int itemPosition = recyclerView.getChildLayoutPosition(view);
                String abrirTelaTitulo = titles.get(itemPosition);
                getDialog().dismiss();
                if (modalTipo.equals("mural")){
                    Log.v("aaaaaaa", abrirTelaTitulo);
                    if (abrirTelaTitulo.equals("Texto")) {
                        MuralDialogFragment dialog = new MuralDialogFragment("Postagem de texto", "texto");
                        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                        dialog.show(getActivity().getFragmentManager(), "dialog");
                    } else if (abrirTelaTitulo.equals("Imagem")) {
                        MuralDialogFragment dialog = new MuralDialogFragment("Postagem de imagem", "imagem");
                        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                        dialog.show(getActivity().getFragmentManager(), "dialog");
                    } else if (abrirTelaTitulo.equals("Áudio")) {
                        MuralDialogFragment dialog = new MuralDialogFragment("Postagem de áudio", "audio");
                        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                        dialog.show(getActivity().getFragmentManager(), "dialog");
                    }
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        builder.setView(layout);

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }

    public interface ModalListFragmentListener {
        public void onDrawerItemSelected(View view, int position);
    }
}
