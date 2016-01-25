package my.edu.chiawaikeith.canteenpos.Fragments;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import my.edu.chiawaikeith.canteenpos.R;


public class ChartFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private int a=35,b=25,c=35,d=5;

    private OnFragmentInteractionListener mListener;

    public static ChartFragment newInstance(String param1, String param2) {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ChartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_chart, container, false);

        PieChart mPieChart = (PieChart)view. findViewById(R.id.piechart);

        mPieChart.addPieSlice(new PieModel("Fast Food", a, Color.parseColor("#FE6DA8")));
        mPieChart.addPieSlice(new PieModel("Malay Food", b, Color.parseColor("#56B7F1")));
        mPieChart.addPieSlice(new PieModel("Chinese Food", c, Color.parseColor("#CDA67F")));
        mPieChart.addPieSlice(new PieModel("Mamak", d, Color.parseColor("#FED70E")));

        mPieChart.startAnimation();
        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
