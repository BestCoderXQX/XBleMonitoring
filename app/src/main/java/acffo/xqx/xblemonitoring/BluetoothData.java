/**
 * Created by Admin on 2016/6/28.
 */
package acffo.xqx.xblemonitoring;

public class BluetoothData {
//     public static  final  String SENSOR_UP_ADRESS="20:16:05:25:32:22";//肩膀处
//     public static final  String SENSOR_DOWN_ADRESS="20:16:05:25:32:31";//手腕处
//     public static final  String SENSOR_CENTER_ADRESS="20:16:06:15:78:79";//胸脯处
//
//
    public static String SENSOR_UP_ADRESS="88:4A:EA:8D:55:AF";//肩膀处
//    public static final  String SENSOR_DOWN_ADRESS="20:16:05:25:32:31";//手腕处t
   // public static final  String SENSOR_DOWN_ADRESS="20:16:05:25:32:22";//手腕处t
   public static String SENSOR_DOWN_ADRESS="88:4A:EA:8D:55:AF";//手腕处t
    //public static final  String SENSOR_DOWN_ADRESS="20:16:06:15:78:42";//手腕处t
    public static String SENSOR_CENTER_ADRESS="88:4A:EA:8D:55:AF";//胸脯处
    static String[]  list_adr={SENSOR_UP_ADRESS,SENSOR_DOWN_ADRESS,
            SENSOR_CENTER_ADRESS};
    public static String GetNextAddress(int current_pos)
    {
        if(current_pos<=list_adr.length)
        {
            return list_adr[current_pos-1];
        }
        return  null;
    }

}
