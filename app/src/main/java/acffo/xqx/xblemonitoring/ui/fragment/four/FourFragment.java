package acffo.xqx.xblemonitoring.ui.fragment.four;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import acffo.xqx.xblemonitoring.BluetoothData;
import acffo.xqx.xblemonitoring.CloseBle;
import acffo.xqx.xblemonitoring.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class FourFragment extends Fragment implements View.OnClickListener {


    @Bind(R.id.btnOne)
    RelativeLayout btnOne;
    @Bind(R.id.btnTwo)
    RelativeLayout btnTwo;
    @Bind(R.id.btnThree)
    RelativeLayout btnThree;

    public FourFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_four, container, false);
        ButterKnife.bind(this, view);

        initEvent();
        return view;
    }

    private void initEvent() {
        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnOne:
            case R.id.btnTwo:
            case R.id.btnThree:

                PackageManager packageManager = getActivity().getPackageManager();
                Intent intent = new Intent();
                intent = packageManager.getLaunchIntentForPackage("com.magikare.SensorOffline");

                if (intent == null) {
                    Toast.makeText(getActivity(), "打开康复运动应用失败！\n请检查相应应用是否正确安装！", Toast.LENGTH_SHORT).show();
                    return;
                }
                EventBus.getDefault().post(new CloseBle());// 关闭蓝牙
                intent.putExtra("mac", BluetoothData.SENSOR_DOWN_ADRESS);
                try {
                    startActivity(intent);
                } catch (Exception ee) {
                    Toast.makeText(getActivity(), "打开康复运动应用失败！\n请检查相应应用是否正确安装！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
