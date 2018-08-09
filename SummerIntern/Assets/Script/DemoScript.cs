using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Runtime.InteropServices;


public class DemoScript : MonoBehaviour {


	public void OnClick()
    {
#if UNITY_EDITOR
        Debug.Log("UnityEditorでは使用できません");
#elif UNITY_ANDROID
        // パッケージ名 + クラス名を引数にする
        using (AndroidJavaClass javaClass = new AndroidJavaClass("com.example.unitylib_.Serial"))
        {
            // そのクラスのメソッドを指定する.
            // 引数があるの場合は、第2引数以降がそのメソッドの第1引数に該当する.
            javaClass.CallStatic("ShowToast");
        }
#endif
    }
    public void Serial()
    {
#if UNITY_EDITOR
        Debug.Log("UnityEditorでは使用できません");
#elif UNITY_ANDROID
        AndroidJavaClass plugin = new AndroidJavaClass("com.example.unitylib_.Serial");
        AndroidJavaObject jo = plugin.CallStatic<AndroidJavaObject>("instance");
		jo.Call("StartSerial");
#endif
    }
}
