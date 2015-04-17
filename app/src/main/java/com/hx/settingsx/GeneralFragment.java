package com.hx.settingsx;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class GeneralFragment extends Fragment {

    int vibratorLevel = -1;
    String vibratorPath = "";
    int dt2w_val = 0;
    String dt2wPath = "";

    public GeneralFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_general, container, false);

        final SeekBar vibratorBar = (SeekBar) rootView.findViewById(R.id.vibrator_bar);
        final TextView vibratorValue = (TextView) rootView.findViewById(R.id.vibrator_value);
        final Switch dt2w = (Switch) rootView.findViewById(R.id.dt2w_ed);

        if (new File(Constants.VIBRATOR_LEVEL).exists()) {
            vibratorLevel = Integer.valueOf(Utils.readOneLine(Constants.VIBRATOR_LEVEL));
            vibratorPath = Constants.VIBRATOR_LEVEL;
        } else if (new File(Constants.VIBRATOR_LEVEL_VTG).exists()) {
            vibratorLevel = Integer.valueOf(Utils.readOneLine(Constants.VIBRATOR_LEVEL_VTG));
            vibratorPath = Constants.VIBRATOR_LEVEL_VTG;
        }

        if (new File(Constants.DT2W).exists()) {
            dt2wPath = Constants.DT2W;
            dt2w_val = Integer.valueOf(Utils.readOneLine(dt2wPath));
            if(dt2w_val == 1) {
                dt2w.setChecked(true);
            }
        } else {
            dt2w_val = -1;
        }

        if (vibratorLevel == -1) {
            vibratorBar.setEnabled(false);
            vibratorValue.setText("-");
            vibratorValue.setTextColor(getActivity().getResources().getColor(android.R.color.background_light));
        } else {
            vibratorBar.setProgress(vibratorLevel);
            vibratorValue.setText(String.valueOf(vibratorLevel));
        }

        if (dt2w_val == -1) {
            dt2w.setClickable(false);
        }

        vibratorBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                vibratorValue.setText(String.valueOf(progress));
                if (vibratorLevel != -1) {
                    if (vibratorPath.equals(Constants.VIBRATOR_LEVEL)) {
                        Utils.writeValue(vibratorPath, String.valueOf(seekBar.getProgress()));
                    } else if (vibratorPath.equals(Constants.VIBRATOR_LEVEL_VTG)) {
                        Utils.writeValue(vibratorPath, String.valueOf(getVtgLevel(seekBar.getProgress())));
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(500);
            }
        });

        dt2w.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(dt2w.isChecked()==true)
                {
                    Utils.writeValue(dt2wPath, "1");
                } else {
                    Utils.writeValue(dt2wPath, "0");
                }
            }
        });

        return rootView;
    }

    private int getVtgLevel(int percentage) {
        // TODO: This function is temporary until we learn more about vtg_level
        return ((19 * percentage) / 100) + 12;
    }
}
