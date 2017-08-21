package sagib.edu.myfamilymeeting;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class WelcomeFragment extends Fragment {


    @BindView(R.id.tvDesc)
    TextView tvDesc;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.btnBegin)
    BootstrapButton btnBegin;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_welcome, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnBegin)
    public void onBtnBeginClicked() {
        Intent intent = new Intent("Begin");
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }
}
