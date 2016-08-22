package com.example.davidyuan7536.workoutlog;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LogExerciseFragment extends Fragment {

    Button addSet;
    Button saveExercise;
    Button cancelExercise;
    Button deleteExerciseBtn;

    EditText exerciseName;
    LinearLayout exerciseSets;
    LinearLayout saveDeleteHolder;

    List <EditText> setList;
    List <LinearLayout> setListLL;
    List <TextView> setListSet;
    List <ImageButton> setListDelete;


    String workoutID;
    String eName;
    ArrayList<String> eSets;
    String exerciseID;


    int currentSet;

    OnSaveExerciseListener mCallback;

    public interface OnSaveExerciseListener {
        void onExerciseSaved(String exerciseName, List<String> exerciseSets, String workoutID);
    }



    public LogExerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnSaveExerciseListener) (Activity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_exercise, container, false);

        addSet = (Button) view.findViewById(R.id.logExerciseAddSet);
        saveExercise = (Button) view.findViewById(R.id.logExerciseSaveBtn);
        cancelExercise = (Button) view.findViewById(R.id.logExerciseCancelBtn);
        deleteExerciseBtn = (Button) view.findViewById(R.id.logExerciseDeleteBtn);

        exerciseName = (EditText) view.findViewById(R.id.logExerciseExerciseName);
        exerciseSets = (LinearLayout) view.findViewById(R.id.logExerciseSets);
        saveDeleteHolder = (LinearLayout) view.findViewById(R.id.logExerciseSaveDeleteHolder);

        setList = new ArrayList<EditText>();
        setListLL = new ArrayList<LinearLayout>();
        setListSet = new ArrayList<TextView>();
        setListDelete = new ArrayList<ImageButton>();

        workoutID = getArguments().getString("workoutID");
        eName = getArguments().getString("exerciseName");
        eSets = getArguments().getStringArrayList("exerciseSets");
        exerciseID = getArguments().getString("exerciseID");

        if(exerciseID != null && eName != null && eSets != null){

            currentSet = 1;
            exerciseName.setText(eName);

            deleteExerciseBtn.setOnClickListener(deleteExercise);

//            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
//

            float density = view.getResources().getDisplayMetrics().density;
            int paddingPixel10 = (int)(10 * density);

//            Button deleteExerciseBtn = new Button(view.getContext(), null,);
//            deleteExerciseBtn.setLayoutParams(linearLayoutParams);
//            deleteExerciseBtn.setText("Delete");
//            deleteExerciseBtn.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.maroonPrimary));
//            deleteExerciseBtn.setTextColor(ContextCompat.getColor(view.getContext(), R.color.textColorPrimary));
//            deleteExerciseBtn.setOnClickListener(deleteExercise);
//            saveDeleteHolder.addView(deleteExerciseBtn);



            for(String set: eSets){

                ScrollView.LayoutParams scrollViewParams = new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams linearLayoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.9f);
                LinearLayout.LayoutParams linearLayoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f);
                LinearLayout.LayoutParams linearLayoutParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);



                LinearLayout ll = new LinearLayout(view.getContext());
                ll.setLayoutParams(scrollViewParams);
                ll.setId(0+currentSet);
                exerciseSets.addView(ll);
                setListLL.add(ll);

                TextView setTxt = new TextView(view.getContext());
                setTxt.setLayoutParams(linearLayoutParams1);
                setTxt.setText("Set "+ currentSet+":");
                setTxt.setPadding(paddingPixel10, paddingPixel10, paddingPixel10, paddingPixel10);
                setTxt.setSingleLine(true);
                setTxt.setId(0+currentSet);
                setTxt.setEllipsize(TextUtils.TruncateAt.END);
                ll.addView(setTxt);
                setListSet.add(setTxt);

                EditText setDescription = (EditText) View.inflate(view.getContext(), R.layout.material_factory_edit_text, null);
//                EditText setDescription = new EditText(view.getContext());
                setDescription.setLayoutParams(linearLayoutParams2);
                setDescription.setPadding(paddingPixel10, paddingPixel10, paddingPixel10, paddingPixel10);
                setDescription.setId(0+currentSet);
                setDescription.setText(set);
                ll.addView(setDescription);
                setList.add(setDescription);
                setDescription.requestFocus();


                ImageButton setDelete = new ImageButton(view.getContext(), null, R.style.AppTheme);
                setDelete.setLayoutParams(linearLayoutParams3);
                setDelete.setPadding(paddingPixel10, paddingPixel10, paddingPixel10, paddingPixel10);
                setDelete.setId(0+currentSet);
                setDelete.setOnClickListener(deleteSet);
                setDelete.setImageResource(R.drawable.ic_clear_black_24dp);
                setListDelete.add(setDelete);
                ll.addView(setDelete);

                currentSet++;

            }

        }
        else{

            currentSet = 1;
            saveDeleteHolder.removeView(deleteExerciseBtn);

        }

        addSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(currentSet != 1 && setList.get(currentSet-2).getText().toString().matches("")){

                    Toast.makeText(view.getContext(), "Please fill in information for set " + (currentSet-1) + " first", Toast.LENGTH_SHORT).show();
                    return;
                }
                ScrollView.LayoutParams scrollViewParams = new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams linearLayoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.9f);
                LinearLayout.LayoutParams linearLayoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f);
                LinearLayout.LayoutParams linearLayoutParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);


                float density = view.getResources().getDisplayMetrics().density;
                int paddingPixel10 = (int)(10 * density);

                LinearLayout ll = new LinearLayout(view.getContext());
                ll.setLayoutParams(scrollViewParams);
                ll.setId(currentSet);
                exerciseSets.addView(ll);
                setListLL.add(ll);

                TextView setTxt = new TextView(view.getContext());
                setTxt.setLayoutParams(linearLayoutParams1);
                setTxt.setText("Set "+ currentSet+":");
                setTxt.setPadding(paddingPixel10, paddingPixel10, paddingPixel10, paddingPixel10);
                setTxt.setSingleLine(true);
                setTxt.setId(currentSet);
                setTxt.setEllipsize(TextUtils.TruncateAt.END);
                ll.addView(setTxt);
                setListSet.add(setTxt);

                EditText setDescription = (EditText) View.inflate(view.getContext(), R.layout.material_factory_edit_text, null);
//                EditText setDescription = new EditText(view.getContext());
                setDescription.setLayoutParams(linearLayoutParams2);
                setDescription.setPadding(paddingPixel10, paddingPixel10, paddingPixel10, paddingPixel10);
                setDescription.setId(currentSet);
                ll.addView(setDescription);
                setList.add(setDescription);
                setDescription.requestFocus();


                ImageButton setDelete = new ImageButton(view.getContext(), null, R.style.AppTheme);
                setDelete.setLayoutParams(linearLayoutParams3);
                setDelete.setPadding(paddingPixel10, paddingPixel10, paddingPixel10, paddingPixel10);
                setDelete.setId(currentSet);
                setDelete.setOnClickListener(deleteSet);
                setDelete.setImageResource(R.drawable.ic_clear_black_24dp);
                setListDelete.add(setDelete);
                ll.addView(setDelete);

                currentSet++;

            }
        });





        saveExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentSet == 1){
                    Toast.makeText(view.getContext(), "Please add at least one set", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(exerciseName.getText().toString().matches("")){
                    Toast.makeText(view.getContext(), "Please give this exercise a name", Toast.LENGTH_SHORT).show();
                    exerciseName.requestFocus();
                    return;
                }

                for(int x = 0; x != currentSet-1; x++){
                    if(setList.get(x).getText().toString().matches("")){
                        Toast.makeText(view.getContext(), "Please fill in information for set " + (x+1) + " first", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                String eName = exerciseName.getText().toString();
                List<String> eSets = new ArrayList<String>();
                for(int x = 0; x != currentSet-1; x++){
                    eSets.add(setList.get(x).getText().toString());
                }


                if(exerciseID != null){

                    ((HomeActivity)getActivity()).updateExercise(eName, eSets, workoutID, exerciseID);
                    Toast.makeText(view.getContext(), "Exercise updated", Toast.LENGTH_SHORT).show();

                    ((HomeActivity)getActivity()).onExerciseUpdated(eName, eSets, workoutID, exerciseID);

                    getFragmentManager().popBackStackImmediate();

                }
                else{

                    String uniqueID = ((HomeActivity)getActivity()).saveExercise(eName, eSets, workoutID);
                    Toast.makeText(view.getContext(), "Exercise saved", Toast.LENGTH_SHORT).show();

                    ((HomeActivity)getActivity()).onExerciseSaved(eName, eSets, workoutID, uniqueID);

                    getFragmentManager().popBackStackImmediate();

                    return;

                }

//                if(((HomeActivity)getActivity()).saveExercise(eName, eSets,uniqueID, workoutID, exerciseNum)){
//                    Toast.makeText(view.getContext(), "Exercise saved", Toast.LENGTH_LONG).show();
//                    getFragmentManager().popBackStackImmediate();
//                    return;
//                }
//                else{
//                    Toast.makeText(view.getContext(), "Exercise could not be saved", Toast.LENGTH_LONG).show();
//                    return;
//                }






            }
        });

        cancelExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        return view;
    }

    View.OnClickListener deleteSet = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            ((ViewGroup)setListLL.get(view.getId()-1).getParent()).removeView(setListLL.get(view.getId()-1));

            for(int x = view.getId(); x < setListLL.size(); x++){
                setListSet.get(x).setText("Set " + (x));

                setListDelete.get(x).setId(x);
                setListLL.get(x).setId(x);
                setList.get(x).setId(x);
                setListSet.get(x).setId(x);
            }
            setListLL.remove(view.getId()-1);
            setListSet.remove(view.getId()-1);
            setList.remove(view.getId()-1);
            setListDelete.remove(view.getId()-1);


            currentSet--;

        }
    };

    View.OnClickListener deleteExercise = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            ((HomeActivity)getActivity()).deleteExercise(workoutID, exerciseID);
            Toast.makeText(view.getContext(), "Exercise deleted", Toast.LENGTH_SHORT).show();
            ((HomeActivity)getActivity()).onExerciseDeleted(workoutID, exerciseID);
            getFragmentManager().popBackStackImmediate();


        }
    };
}
