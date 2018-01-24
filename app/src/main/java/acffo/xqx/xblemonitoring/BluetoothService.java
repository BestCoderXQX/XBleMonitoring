package acffo.xqx.xblemonitoring;

//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;


public class BluetoothService {

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String NAME = "BluetoothData";
    public static final int  SENSEOR_NUM=3;
    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private AcceptThread mAcceptThread;// 请求连接的监听进程
    private ConnectThread mConnectThread;// 连接一个设备的进程
    public ConnectedThread[] mConnectedThread=new ConnectedThread[SENSEOR_NUM];// 已经连接之后的管理进程
    private int mState;// 当前状态

    //xqx

    // 指明连接状态的常量
    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;



    public BluetoothService(Context context, Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mHandler = handler;
    }
    private synchronized void setState(int state) {
        mState = state;
        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(MainActivity.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    public synchronized int getState() {
        return mState;
    }

    public synchronized void start() {

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Start the thread to listen on a BluetoothServerSocket
//        if (mAcceptThread == null)
        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }

        setState(STATE_LISTEN);
    }

    public synchronized void connect(BluetoothDevice device, int index) {

        // Cancel any thread currently running a connection
        //连接一个蓝牙时
//        index=1;
        Log.i("xqxinfo","建立连接1"+BluetoothData.SENSOR_DOWN_ADRESS);

        if (mConnectedThread[index-1] != null) {
            mConnectedThread[index-1].cancel();
            mConnectedThread[index-1]=null;
        }
        Log.i("xqxinfo","建立连接2"+BluetoothData.SENSOR_DOWN_ADRESS);

        mConnectThread=new ConnectThread(device,index);
        mConnectThread.start();
        Log.d("MAGIKARE","创建线程"+index);
        setState(STATE_CONNECTING);
    }

    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device, int index) {
        Log.d("MAGIKARE","连接到线程"+index);
        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        // Cancel the accept thread because we only want to connect to one device
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread[index-1] = new ConnectedThread(socket,index);

        mConnectedThread[index-1].start();

        // Send the name of the connected device back to the UI Activity
        Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString("device_name", device.getName()+" "+index);

        msg.setData(bundle);
        mHandler.sendMessage(msg);

        setState(STATE_CONNECTED);
    }

    public synchronized void stop() {

        if (mConnectedThread != null) {
            for(int i=0;i<mConnectedThread.length;i++)
            {
                mConnectedThread[i].cancel();
            }
            mConnectedThread = null;
        }
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }

        setState(STATE_NONE);
    }

    private void connectionFailed(int index) {
        setState(STATE_LISTEN);

        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
//        bundle.putString("toast", "未能连接设备"+index);
        bundle.putInt("device_id",index);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    private void connectionLost(int index) {
        setState(STATE_LISTEN);
        Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
//        bundle.putString("toast", "设备丢失"+index);
        bundle.putInt("device_id",index);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;
        //private int index;
        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            // this.index=index;
            // Create a new listening server socket
            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            }
            catch (IOException e) {}
            mmServerSocket = tmp;
        }

        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();

        }

        public void cancel() {

            try {
                mmServerSocket.close();
            }
            catch (IOException e) {}
        }
    }

    /**
     * This thread runs while attempting to make an outgoing connection with a
     * device. It runs straight through; the connection either succeeds or
     * fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private int index;
        public ConnectThread(BluetoothDevice device, int index) {
            mmDevice = device;
            this.index=index;
            Log.d("脉吉 Connecting",device.getAddress());
            BluetoothSocket tmp = null;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);// Get a BluetoothSocket for a connection with the given BluetoothDevice
            }
            catch (IOException e) {}
            mmSocket = tmp;
        }

        public void run() {

            setName("ConnectThread");
            mAdapter.cancelDiscovery();// Always cancel discovery because it will slow down a connection

            // Make a connection to the BluetoothSocket
            try {
                mmSocket.connect();// This is a blocking call and will only return on a successful connection or an exception
            }
            catch (IOException e) {
                connectionFailed(this.index);
                try {
                    mmSocket.close();
                } catch (IOException e2) {}

                BluetoothService.this.start();// 引用来说明要调用的是外部类的方法 run
                return;
            }

            synchronized (BluetoothService.this) {// Reset the ConnectThread because we're done
                mConnectThread = null;
            }
            connected(mmSocket, mmDevice,index);// Start the connected thread
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {

            }
        }
    }

    /**
     * This thread runs during a connection with a remote device. It handles all
     * incoming and outgoing transmissions.
     */
    class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private int index;
        private Queue<Byte> queueBuffer = new LinkedList<Byte>();
        private byte[] packBuffer = new byte[110];
        public ConnectedThread(BluetoothSocket socket, int index) {

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            this.index=index;
            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {}

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        private String strDate,strTime;

        private double [] fData=new double[30];
        public void run() {
            byte[] tempInputBuffer = new byte[1024];
            int acceptedLen = 0;

            long lLastTime = System.currentTimeMillis(); // 获取开始时间

            while (true) {

                try {
                    // 每次对inputBuffer做覆盖处理
                    acceptedLen = mmInStream.read(tempInputBuffer);

                    for (int i = 0; i < acceptedLen; i++)
                        queueBuffer.add(tempInputBuffer[i]);// 从缓冲区读取到的数据，都存到队列里

                    while (queueBuffer.size() >= 110) {
//                        Log.i("xqxinfo","acceptedLen"+acceptedLen+"数据"+queueBuffer.toString());
                        if ((queueBuffer.poll()) != -120 ) continue;// peek()返回对首但不删除 poll 移除并返回
                        if ((queueBuffer.poll()) != 116 ) continue;// peek()返回对首但不删除 poll 移除并返回

//                        queueBuffer.poll();
//                        queueBuffer.poll();
                        queueBuffer.poll(); // 去掉帧长
                        for (int j = 0; j < 106; j++) packBuffer[j] = queueBuffer.poll();
                        //加速度
                        fData[1] = ((short)(packBuffer[0]<<8)| (short) (packBuffer[1] & 0xff))/32768.0f*8;  // x
                        fData[2] = ((short)(packBuffer[2]<<8)| (short) (packBuffer[3] & 0xff))/32768.0f*8;  // y
                        fData[3] = ((short)(packBuffer[4]<<8)| (short) (packBuffer[5] & 0xff))/32768.0f*8;  // z

                        // 角速度
                        fData[4] = ((short)(packBuffer[6]<<8)| (short) (packBuffer[7] & 0xff))/32768.0f*2000;  // x
                        fData[5] = ((short)(packBuffer[8]<<8)| (short) (packBuffer[9] & 0xff))/32768.0f*2000;  // y
                        fData[6] = ((short)(packBuffer[10]<<8)| (short) (packBuffer[11] & 0xff))/32768.0f*2000;  // z

                        fData[17] = ((((short) packBuffer[12]) << 8) | ((short) packBuffer[13] & 0xff)) / 100.0f;  // 角度

                        // 四元素
                        fData[7] = ((short)(packBuffer[12]<<8)| (short) (packBuffer[13] & 0xff)) /32768.0f;
                        fData[8] = ((short)(packBuffer[14]<<8)| (short) (packBuffer[15] & 0xff)) /32768.0f;
                        fData[9] = ((short)(packBuffer[16]<<8)| (short) (packBuffer[17] & 0xff)) /32768.0f;
                        fData[10] = ((short)(packBuffer[18]<<8)| (short) (packBuffer[19] & 0xff)) /32768.0f;

                        fData[20] = ((short)(packBuffer[104]<<8)| (short) (packBuffer[105] & 0xff));  // 肌电

                        double sum1 = 0;
                        double sum2 = 0;
                        double sum3 = 0;
                        fData[21] =(((short)(packBuffer[40]<<8)| (short) (packBuffer[41] & 0xff))/4096.0f+
                                ((short)(packBuffer[42]<<8)| (short) (packBuffer[43] & 0xff))/4096.0f+
                                ((short)(packBuffer[56]<<8)| (short) (packBuffer[57] & 0xff))/4096.0f+
                                ((short)(packBuffer[58]<<8)| (short) (packBuffer[59] & 0xff))/4096.0f)/4;

                        fData[22] =(((short)(packBuffer[72]<<8)| (short) (packBuffer[73] & 0xff))/4096.0f+
                                ((short)(packBuffer[74]<<8)| (short) (packBuffer[75] & 0xff))/4096.0f+
                                ((short)(packBuffer[88]<<8)| (short) (packBuffer[89] & 0xff))/4096.0f+
                                ((short)(packBuffer[90]<<8)| (short) (packBuffer[91] & 0xff))/4096.0f)/4;

                        fData[23] =(((short)(packBuffer[44]<<8)| (short) (packBuffer[45] & 0xff))/4096.0f+
                                ((short)(packBuffer[46]<<8)| (short) (packBuffer[47] & 0xff))/4096.0f+
                                ((short)(packBuffer[60]<<8)| (short) (packBuffer[61] & 0xff))/4096.0f+
                                ((short)(packBuffer[62]<<8)| (short) (packBuffer[63] & 0xff))/4096.0f)/4;

                        fData[24] =(((short)(packBuffer[76]<<8)| (short) (packBuffer[77] & 0xff))/4096.0f+
                                ((short)(packBuffer[78]<<8)| (short) (packBuffer[79] & 0xff))/4096.0f+
                                ((short)(packBuffer[92]<<8)| (short) (packBuffer[93] & 0xff))/4096.0f+
                                ((short)(packBuffer[94]<<8)| (short) (packBuffer[95] & 0xff))/4096.0f)/4;

                        fData[25] =(((short)(packBuffer[48]<<8)| (short) (packBuffer[49] & 0xff))/4096.0f+
                                ((short)(packBuffer[50]<<8)| (short) (packBuffer[51] & 0xff))/4096.0f+
                                ((short)(packBuffer[64]<<8)| (short) (packBuffer[65] & 0xff))/4096.0f+
                                ((short)(packBuffer[66]<<8)| (short) (packBuffer[67] & 0xff))/4096.0f)/4;

                        fData[26] =(((short)(packBuffer[80]<<8)| (short) (packBuffer[81] & 0xff))/4096.0f+
                                ((short)(packBuffer[82]<<8)| (short) (packBuffer[83] & 0xff))/4096.0f+
                                ((short)(packBuffer[96]<<8)| (short) (packBuffer[97] & 0xff))/4096.0f+
                                ((short)(packBuffer[98]<<8)| (short) (packBuffer[99] & 0xff))/4096.0f)/4;

                        fData[27] =(((short)(packBuffer[52]<<8)| (short) (packBuffer[53] & 0xff))/4096.0f+
                                ((short)(packBuffer[54]<<8)| (short) (packBuffer[55] & 0xff))/4096.0f+
                                ((short)(packBuffer[68]<<8)| (short) (packBuffer[69] & 0xff))/4096.0f+
                                ((short)(packBuffer[70]<<8)| (short) (packBuffer[71] & 0xff))/4096.0f)/4;

                        fData[28] =(((short)(packBuffer[84]<<8)| (short) (packBuffer[85] & 0xff))/4096.0f+
                                ((short)(packBuffer[86]<<8)| (short) (packBuffer[87] & 0xff))/4096.0f+
                                ((short)(packBuffer[100]<<8)| (short) (packBuffer[101] & 0xff))/4096.0f+
                                ((short)(packBuffer[102]<<8)| (short) (packBuffer[103] & 0xff))/4096.0f)/4;



//                        Log.i("xqxinfofdata",((short)(packBuffer[40]<<8)| (short) (packBuffer[41] & 0xff))/4096.0f+"");
                    }//while (queueBuffer.size() >= 11)

                    long lTimeNow = System.currentTimeMillis(); // 获取开始时间


                    if (lTimeNow - lLastTime > 80) {
                        lLastTime = lTimeNow;
                        Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_READ);
                        Bundle bundle = new Bundle();
                        bundle.putString("index",String.valueOf(this.index));
                        bundle.putDoubleArray("Data", fData);
                        bundle.putString("Date", strDate);
                        bundle.putString("Time", strTime);
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                    }

                } catch (IOException e) {
                    connectionLost(this.index);
                    break;
                }
            }
        }

        private  String byte2hex(byte [] buffer){
            String h = "";

            for(int i = 0; i < buffer.length; i++){
                String temp = Integer.toHexString(buffer[i] & 0xFF);
                if(temp.length() == 1){
                    temp = "0" + temp;
                }
                h = h + " "+ temp;
            }

            return h;

        }
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
                mHandler.obtainMessage(MainActivity.MESSAGE_WRITE, -1, -1,buffer).sendToTarget();// Share the sent message back to the UI Activity
            } catch (IOException e) {}
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {}
        }
    }

    public void printHexString( byte[] b) {
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            Log.i("xqxinfobtye",hex.toUpperCase());
        }

    }
}
