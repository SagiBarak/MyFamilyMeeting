package sagib.edu.myfamilymeeting;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class QuizFragment extends Fragment {


//    @BindView(R.id.tvQuizTitle)
//    TextView tvQuizTitle;
    @BindView(R.id.tvQuiz)
    TextView tvQuiz;
    Unbinder unbinder;
    SharedPreferences prefs;
    String quiz = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quiz, container, false);
        unbinder = ButterKnife.bind(this, v);
        prefs = getContext().getSharedPreferences("Values", Context.MODE_PRIVATE);
        quiz = prefs.getString("quiz", "");
        tvQuiz.setText(quiz);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
