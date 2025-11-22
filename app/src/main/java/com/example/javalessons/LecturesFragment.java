package com.example.javalessons;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LecturesFragment extends Fragment {
    private DatabaseHelper dbHelper;
    SharedPreferences sharedPreferences;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public LecturesFragment() {
        // Required empty public constructor
    }

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
        dbHelper.forceCopy();

        sharedPreferences = getContext().getSharedPreferences("userLocalData", Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sharedPreferences.edit();

        List<String> lectures = dbHelper.getLectureTitles();

        LinearLayout lecture_buttons = view.findViewById(R.id.lecture_buttons);

        for (String title : lectures) {
            Button btn = createLectureBtn(title, lectures.indexOf(title), spEditor);

            lecture_buttons.addView(btn);
        }

        return view;
    }

    public Button createLectureBtn(String lecture_title, int lecture_id, SharedPreferences.Editor spEditor) {
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
                spEditor.putString("lastReadLecture", lecture_title);
                spEditor.commit();

                Intent intent = new Intent(getActivity(), Lecture.class);
                intent.putExtra("lecture_id", lecture_id);
                startActivity(intent);
            }
        });

        return button;
    }
}