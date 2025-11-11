package com.example.javalessons;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LecturesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LecturesFragment extends Fragment {
    private DatabaseHelper dbHelper;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LecturesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LecturesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LecturesFragment newInstance(String param1, String param2) {
        LecturesFragment fragment = new LecturesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_lectures, container, false);

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        dbHelper.createDatabaseIfNotExists();

        List<String> lectures = dbHelper.getLectureTitles();

        LinearLayout lecture_buttons = view.findViewById(R.id.lecture_buttons);

        for (String title : lectures) {
            Button btn = createLectureBtn(title, lectures.indexOf(title));

            lecture_buttons.addView(btn);
        }

        // Inflate the layout for this fragment
        return view;
    }

    public Button createLectureBtn(String lecture_title, int lecture_id) {
        Button button = new Button(getContext());
        button.setText(lecture_title);
        button.setBackgroundColor(Color.argb(255, 242, 242, 242));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 30);

        button.setLayoutParams(params);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Lecture.class);
                intent.putExtra("lecture_id", lecture_id);
                startActivity(intent);
            }
        });

        return button;
    }
}