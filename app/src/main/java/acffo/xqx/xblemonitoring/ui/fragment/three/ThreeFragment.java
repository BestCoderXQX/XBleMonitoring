package acffo.xqx.xblemonitoring.ui.fragment.three;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import acffo.xqx.xblemonitoring.MainActivity;
import acffo.xqx.xblemonitoring.R;
import acffo.xqx.xblemonitoring.UpdataJiaodu;
import acffo.xqx.xblemonitoring.UpdataRed2;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThreeFragment extends Fragment {


    @Bind(R.id.img)
    ImageView img;
    @Bind(R.id.img2)
    ImageView img2;

    public ThreeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        EventBus.getDefault().post(new UpdataJiaodu());
                        EventBus.getDefault().post(new UpdataRed2());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.i("xqxaaaa", "e:" + e.toString());
                    }
                }
            }
        }).start();
        return view;
    }

    public void onEventMainThread(UpdataRed2 event) {
        int alpha = (((int)MainActivity.m_receive_data_down[20])-2000)/5;
        Log.i("xqxinfo333","肌电:"+(int)MainActivity.m_receive_data_down[20]+",透明底"+alpha);
        img2.setAlpha(alpha);
    }

    public void onEventMainThread(UpdataJiaodu event) {
        int v = (int) MainActivity.m_receive_data_down[17] / 2 + 90;
        Log.i("xqxaaaa", "v:" + v);
        if (v < 60) {
            img.setImageResource(R.mipmap.a60);
        } else if (60 <= v && v < 62) {
            img.setImageResource(R.mipmap.a60);
        } else if (62 <= v && v < 64) {
            img.setImageResource(R.mipmap.a62);
        } else if (64 <= v && v < 66) {
            img.setImageResource(R.mipmap.a64);
        } else if (66 <= v && v < 68) {
            img.setImageResource(R.mipmap.a66);
        } else if (68 <= v && v < 70) {
            img.setImageResource(R.mipmap.a68);
        } else if (70 <= v && v < 72) {
            img.setImageResource(R.mipmap.a70);
        } else if (72 <= v && v < 74) {
            img.setImageResource(R.mipmap.a72);
        } else if (74 <= v && v < 76) {
            img.setImageResource(R.mipmap.a74);
        } else if (76 <= v && v < 78) {
            img.setImageResource(R.mipmap.a76);
        } else if (78 <= v && v < 80) {
            img.setImageResource(R.mipmap.a78);
        } else if (80 <= v && v < 82) {
            img.setImageResource(R.mipmap.a80);
        } else if (82 <= v && v < 84) {
            img.setImageResource(R.mipmap.a82);
        } else if (84 <= v && v < 86) {
            img.setImageResource(R.mipmap.a84);
        } else if (86 <= v && v < 88) {
            img.setImageResource(R.mipmap.a86);
        } else if (88 <= v && v < 90) {
            img.setImageResource(R.mipmap.a88);
        } else if (90 <= v && v < 92) {
            img.setImageResource(R.mipmap.a90);
        } else if (92 <= v && v < 94) {
            img.setImageResource(R.mipmap.a92);
        } else if (94 <= v && v < 96) {
            img.setImageResource(R.mipmap.a94);
        } else if (96 <= v && v < 98) {
            img.setImageResource(R.mipmap.a96);
        } else if (98 <= v && v < 100) {
            img.setImageResource(R.mipmap.a98);
        } else if (100 <= v && v < 102) {
            img.setImageResource(R.mipmap.a100);
        } else if (102 <= v && v < 104) {
            img.setImageResource(R.mipmap.a102);
        } else if (104 <= v && v < 106) {
            img.setImageResource(R.mipmap.a104);
        } else if (106 <= v && v < 108) {
            img.setImageResource(R.mipmap.a106);
        } else if (108 <= v && v < 110) {
            img.setImageResource(R.mipmap.a108);
        } else if (110 <= v && v < 112) {
            img.setImageResource(R.mipmap.a110);
        } else if (112 <= v && v < 114) {
            img.setImageResource(R.mipmap.a112);
        } else if (114 <= v && v < 116) {
            img.setImageResource(R.mipmap.a114);
        } else if (116 <= v && v < 118) {
            img.setImageResource(R.mipmap.a116);
        } else if (118 <= v && v < 120) {
            img.setImageResource(R.mipmap.a118);
        } else if (120 <= v && v < 122) {
            img.setImageResource(R.mipmap.a120);
        } else if (122 <= v && v < 124) {
            img.setImageResource(R.mipmap.a122);
        } else if (124 <= v && v < 126) {
            img.setImageResource(R.mipmap.a124);
        } else if (126 <= v && v < 128) {
            img.setImageResource(R.mipmap.a126);
        } else if (128 <= v && v < 130) {
            img.setImageResource(R.mipmap.a128);
        } else if (130 <= v && v < 132) {
            img.setImageResource(R.mipmap.a130);
        } else if (132 <= v && v < 134) {
            img.setImageResource(R.mipmap.a132);
        } else if (134 <= v && v < 136) {
            img.setImageResource(R.mipmap.a134);
        } else if (136 <= v && v < 138) {
            img.setImageResource(R.mipmap.a136);
        } else if (138 <= v && v < 140) {
            img.setImageResource(R.mipmap.a138);
        } else if (140 <= v && v < 142) {
            img.setImageResource(R.mipmap.a140);
        } else if (142 <= v && v < 144) {
            img.setImageResource(R.mipmap.a142);
        } else if (144 <= v && v < 146) {
            img.setImageResource(R.mipmap.a144);
        } else if (146 <= v && v < 148) {
            img.setImageResource(R.mipmap.a146);
        } else if (148 <= v && v < 150) {
            img.setImageResource(R.mipmap.a148);
        } else if (150 <= v && v < 152) {
            img.setImageResource(R.mipmap.a150);
        } else if (152 <= v && v < 154) {
            img.setImageResource(R.mipmap.a152);
        } else if (154 <= v && v < 156) {
            img.setImageResource(R.mipmap.a154);
        } else if (156 <= v && v < 158) {
            img.setImageResource(R.mipmap.a156);
        } else if (158 <= v && v < 160) {
            img.setImageResource(R.mipmap.a158);
        } else {
            img.setImageResource(R.mipmap.a160);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

        EventBus.getDefault().unregister(this);


    }
}
