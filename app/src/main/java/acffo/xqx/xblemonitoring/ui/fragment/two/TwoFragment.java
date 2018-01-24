package acffo.xqx.xblemonitoring.ui.fragment.two;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import acffo.xqx.xblemonitoring.MainActivity;
import acffo.xqx.xblemonitoring.R;
import acffo.xqx.xblemonitoring.UpdataRed;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwoFragment extends Fragment {


    @Bind(R.id.imageView2)
    ImageView imageView2;
    @Bind(R.id.img2)
    ImageView img2;
    @Bind(R.id.img3)
    ImageView img3;
    @Bind(R.id.img4)
    ImageView img4;
    @Bind(R.id.img5)
    ImageView img5;
    @Bind(R.id.img6)
    ImageView img6;
    @Bind(R.id.img7)
    ImageView img7;
    @Bind(R.id.img8)
    ImageView img8;

    public TwoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        EventBus.getDefault().post(new UpdataRed());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(UpdataRed event) {

        int v = (int) (MainActivity.m_receive_data_down[23] * 350 * 5);
        Log.i("xqxinfored", MainActivity.m_receive_data_down[23] + ",透明度:" + v);
        imageView2.setAlpha((int) (MainActivity.m_receive_data_down[21] * 350 * 5));
        img2.setAlpha((int) (MainActivity.m_receive_data_down[22] * 350 * 5));
        img3.setAlpha((int) (MainActivity.m_receive_data_down[23] * 350 * 5));
        img4.setAlpha((int) (MainActivity.m_receive_data_down[24] * 350 * 5));
        img5.setAlpha((int) (MainActivity.m_receive_data_down[25] * 350 * 5));
        img6.setAlpha((int) (MainActivity.m_receive_data_down[26] * 350 * 5));
        img7.setAlpha((int) (MainActivity.m_receive_data_down[27] * 350 * 5));
        img8.setAlpha((int) (MainActivity.m_receive_data_down[28] * 350 * 5));
    }
}
