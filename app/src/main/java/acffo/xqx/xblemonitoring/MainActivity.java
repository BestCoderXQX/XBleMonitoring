package acffo.xqx.xblemonitoring;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import acffo.xqx.xblemonitoring.ui.fragment.four.FourFragment;
import acffo.xqx.xblemonitoring.ui.fragment.one.OneFragment;
import acffo.xqx.xblemonitoring.ui.fragment.three.ThreeFragment;
import acffo.xqx.xblemonitoring.ui.fragment.two.TwoFragment;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    @Bind(R.id.btnOne)
    LinearLayout btnOne;
    @Bind(R.id.btnTwo)
    LinearLayout btnTwo;
    @Bind(R.id.btnThree)
    LinearLayout btnThree;
    @Bind(R.id.btnFour)
    LinearLayout btnFour;
    @Bind(R.id.framelayout)
    FrameLayout framelayout;
    @Bind(R.id.imgOne)
    ImageView imgOne;
    @Bind(R.id.txtOne)
    TextView txtOne;
    @Bind(R.id.imgTwo)
    ImageView imgTwo;
    @Bind(R.id.txtTwo)
    TextView txtTwo;
    @Bind(R.id.imgThree)
    ImageView imgThree;
    @Bind(R.id.txtThree)
    TextView txtThree;
    @Bind(R.id.imgFour)
    ImageView imgFour;
    @Bind(R.id.txtFour)
    TextView txtFour;
    @Bind(R.id.txtChongjili)
    TextView txtChongjili;


    private Fragment oneFragment;
    private Fragment twoFragment;
    private Fragment threeFragment;
    private Fragment fourFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        initEvent();
        setSelect(0);
        choosedevice1();
        EventBus.getDefault().register(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        EventBus.getDefault().post(new UpdataChongjili());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.i("xqxaaaa", "e:" + e.toString());
                    }
                }
            }
        }).start();
    }

    private void initEvent() {
        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnFour.setOnClickListener(this);
    }

    private void initView() {

    }

    @Override
    public void onClick(View v) {
        resetImage();
        switch (v.getId()) {
            case R.id.btnOne:
                setSelect(0);
                break;
            case R.id.btnTwo:
                setSelect(1);
                break;
            case R.id.btnThree:
                setSelect(2);
                break;
            case R.id.btnFour:
                setSelect(3);
                break;
        }
    }


    private void setSelect(int i) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        //隐藏所有
        hidFragment(transaction);

        //把图片设置为亮的

        //设置内容区域
        switch (i) {
            case 0:
                if (oneFragment == null) {
                    oneFragment = new OneFragment();
                    transaction.add(R.id.framelayout, oneFragment);
                } else {
                    transaction.show(oneFragment);

                }
                imgOne.setImageResource(R.mipmap.icon_one_selected);
                txtOne.setTextColor(getResources().getColor(R.color.white));
                btnOne.setBackgroundColor(getResources().getColor(R.color.menu_selected_bg));
                break;
            case 1:
                if (twoFragment == null) {
                    twoFragment = new TwoFragment();
                    transaction.add(R.id.framelayout, twoFragment);
                } else {
                    transaction.show(twoFragment);

                }
                imgTwo.setImageResource(R.mipmap.icon_two_selected);
                txtTwo.setTextColor(getResources().getColor(R.color.white));
                btnTwo.setBackgroundColor(getResources().getColor(R.color.menu_selected_bg));
                break;
            case 2:
                if (threeFragment == null) {
                    threeFragment = new ThreeFragment();
                    transaction.add(R.id.framelayout, threeFragment);
                } else {
                    transaction.show(threeFragment);

                }
                imgThree.setImageResource(R.mipmap.icon_three_selected);
                txtThree.setTextColor(getResources().getColor(R.color.white));
                btnThree.setBackgroundColor(getResources().getColor(R.color.menu_selected_bg));
                break;
            case 3:
                if (fourFragment == null) {
                    fourFragment = new FourFragment();
                    transaction.add(R.id.framelayout, fourFragment);
                } else {
                    transaction.show(fourFragment);

                }
                imgFour.setImageResource(R.mipmap.icon_four_seleted);
                txtFour.setTextColor(getResources().getColor(R.color.white));
                btnFour.setBackgroundColor(getResources().getColor(R.color.menu_selected_bg));
                break;

            default:
                break;

        }
        transaction.commit();
    }

    private void hidFragment(FragmentTransaction transaction) {
        // TODO Auto-generated method stub
        if (oneFragment != null) {
            transaction.hide(oneFragment);
        }
        if (twoFragment != null) {
            transaction.hide(twoFragment);
        }
        if (threeFragment != null) {
            transaction.hide(threeFragment);
        }
        if (fourFragment != null) {
            transaction.hide(fourFragment);
        }

    }

    //将所有功能图标的界面设为暗色
    private void resetImage() {
        // TODO Auto-generated method stub
        imgOne.setImageResource(R.mipmap.icon_one_unselect);
        imgTwo.setImageResource(R.mipmap.icon_two_unselect);
        imgThree.setImageResource(R.mipmap.icon_three_unselect);
        imgFour.setImageResource(R.mipmap.icon_four_unselect);
        txtOne.setTextColor(getResources().getColor(R.color.menu_unselect_txt));
        txtTwo.setTextColor(getResources().getColor(R.color.menu_unselect_txt));
        txtThree.setTextColor(getResources().getColor(R.color.menu_unselect_txt));
        txtFour.setTextColor(getResources().getColor(R.color.menu_unselect_txt));
        btnOne.setBackgroundColor(getResources().getColor(R.color.menu_unselect_bg));
        btnTwo.setBackgroundColor(getResources().getColor(R.color.menu_unselect_bg));
        btnThree.setBackgroundColor(getResources().getColor(R.color.menu_unselect_bg));
        btnFour.setBackgroundColor(getResources().getColor(R.color.menu_unselect_bg));
    }


    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    public static final int MAGIKARE_SENSOR_UP = 2;
    public static final int MAGIKARE_SENSOR_DOWN = 1;
    public static final int MAGIKARE_SENSOR_CENTER = 3;
    private String mConnectedDeviceName = null;
    private BluetoothService mBluetoothService = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private int current_pos = 1;

    public static double[] m_receive_data_down;


    private final Handler mHandler = new Handler() {
        // 匿名内部类写法，实现接口Handler的一些方法
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
//                    连接状态
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
//                            mTitle.setText(R.string.title_connected_to);
//                            mTitle.append(mConnectedDeviceName);
                            Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();

                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Toast.makeText(MainActivity.this, "连接中", Toast.LENGTH_SHORT).show();
//                            mTitle.setText(R.string.title_connecting);
                            // sensor_ready=false;
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Toast.makeText(MainActivity.this, "连接丢失", Toast.LENGTH_SHORT).show();

//                            mTitle.setText(R.string.title_not_connected);
//                            sensor_ready=false;
                            break;
                    }
                    break;
                case MESSAGE_READ:
                    try {
                        String str = msg.getData().getString("index");
                        int index = Integer.valueOf(str);

                        if (index == 1) {
                            m_receive_data_down = msg.getData().getDoubleArray("Data");
                        }
//                        switch (index) {
//                            //第一个传感器
//                            case 2:
//                                Log.i("xqxinfo","数据大小1"+m_receive_data_down.toString());
//                                break;
//                            case 1:
//                                Log.i("xqxinfo","数据大小2"+m_receive_data_down.toString());
//                                m_receive_data_down = msg.getData().getDoubleArray("Data");
//                                break;
//                            case 3:
//                                Log.i("xqxinfo","数据大小3"+m_receive_data_down.toString());
//
//                                break;
//
//                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        Log.i("xqxinfo", "e:" + e.toString());

                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString("device_name");
                    Log.d("成功连接到", mConnectedDeviceName);
                    Toast.makeText(getApplicationContext(), "成功连接到设备" + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    Log.d("多少个设备连接1", String.valueOf(current_pos));


                    break;
                case MESSAGE_TOAST:
                    int index = msg.getData().getInt("device_id");
                    Toast.makeText(getApplicationContext(), msg.getData().getString("toast"), Toast.LENGTH_SHORT).show();
                    //当失去设备或者不能连接设备时，重新连接
                    Log.d("Magikare", "当失去设备或者不能连接设备时，重新连接");
//                    if(check_two_sensor_ready>0)
//                        check_two_sensor_ready--;
//                    sensor_ready=false;
                    if (mBluetoothService != null) {
                        switch (index) {
                            case MAGIKARE_SENSOR_DOWN:
                                BluetoothDevice sensor_down = mBluetoothAdapter.getRemoteDevice(BluetoothData.SENSOR_DOWN_ADRESS);
                                if (sensor_down != null)
                                    mBluetoothService.connect(sensor_down, MAGIKARE_SENSOR_DOWN);
                                break;
                            case MAGIKARE_SENSOR_UP:
                                BluetoothDevice sensor_up = mBluetoothAdapter.getRemoteDevice(BluetoothData.SENSOR_UP_ADRESS);
                                if (sensor_up != null)
                                    mBluetoothService.connect(sensor_up, MAGIKARE_SENSOR_UP);
                                break;
                            case MAGIKARE_SENSOR_CENTER:
                                BluetoothDevice center = mBluetoothAdapter.getRemoteDevice(BluetoothData.SENSOR_CENTER_ADRESS);
                                if (center != null)
                                    mBluetoothService.connect(center, MAGIKARE_SENSOR_CENTER);
                                break;
                        }
                    }

                    break;
            }
        }
    };


    private void initBlueTooth() {

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "蓝牙不可用", Toast.LENGTH_LONG).show();
            //finish();
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) mBluetoothAdapter.enable();
        if (mBluetoothService == null)
            mBluetoothService = new BluetoothService(this, mHandler); // 用来管理蓝牙的连接

        if (mBluetoothService != null) {
            //初始时连接一个蓝牙
            BluetoothDevice sensor_down = mBluetoothAdapter.getRemoteDevice(BluetoothData.SENSOR_DOWN_ADRESS); // 2

            if (sensor_down != null
                    ) {
                mBluetoothService.connect(sensor_down, MAGIKARE_SENSOR_DOWN);  //新的 2
            } else {
            }
        }

    }


    //连接蓝牙设备
    private void choosedevice1() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            //设备不支持蓝牙功能
            Toast.makeText(this, "当前设备不支持蓝牙功能", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            boolean enable = mBluetoothAdapter.enable(); //返回值表示 是否成功打开了蓝牙功能
            if (enable) {
                Toast.makeText(this, "打开蓝牙功能成功！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "打开蓝牙功能失败，请到'系统设置'中手动开启蓝牙功能！", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        //如果有配对的设备
        ArrayList<String> name = new ArrayList<>();
        final ArrayList<String> mac = new ArrayList<>();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                //通过array adapter在列表中添加设备名称和地址
                name.add(device.getName().toString());
                mac.add(device.getAddress());
            }

        } else {
            Toast.makeText(this, "暂无已配对设备", Toast.LENGTH_SHORT).show();
        }
        String device_name[] = new String[mac.size()];
        for (int i = 0; i < name.size(); i++) {
            device_name[i] = name.get(i);
//                Log.i("saveinfo",device_name[i]);
        }
        if (device_name.length > 0) {
            Toast.makeText(this, "有已配对设备", Toast.LENGTH_SHORT).show();
//            final String[] items = new String[]{"语文","数学","英语","物理","化学"};   //列表项
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(MainActivity.this);
            alertdialog.setTitle("选择已配对的设备一").setItems(device_name, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    BluetoothData.SENSOR_DOWN_ADRESS = mac.get(which);

                    initBlueTooth();
                }
            });
            alertdialog.setCancelable(false);
            alertdialog.create();
            alertdialog.show();
        } else {
            Toast.makeText(this, "当前设备未有已配对的硬件设备，请前去'设置'界面设置", Toast.LENGTH_SHORT).show();
        }
    }

    public void onEventMainThread(CloseBle event) {
        mBluetoothService.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(UpdataChongjili event) {
        double v = MainActivity.m_receive_data_down[21]+
                MainActivity.m_receive_data_down[22]+
                MainActivity.m_receive_data_down[23]+
                MainActivity.m_receive_data_down[24]+
                MainActivity.m_receive_data_down[25]+
                MainActivity.m_receive_data_down[26]+
                MainActivity.m_receive_data_down[27]+
                MainActivity.m_receive_data_down[28];
        Log.i("xqxinfochongjili",v+",");
        txtChongjili.setText( (int)(v*100)-20+"");
    }
}
