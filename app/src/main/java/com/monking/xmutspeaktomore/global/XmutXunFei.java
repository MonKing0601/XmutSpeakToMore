package com.monking.xmutspeaktomore.global;

import android.content.Context;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;

/**
 * 封装讯飞的类
 * Created by MonKing on 2016/11/29.
 */

public class XmutXunFei {
    private Context mContext;
    private String XunFeiid;
    /**
     * 构造方法传入上下文和讯飞的ID
     * @param context
     * @param ID
     */
    public XmutXunFei(Context context,String  ID) {
        mContext=context;
        XunFeiid=ID;
        SpeechUtility.createUtility(context, SpeechConstant.APPID +"="+ID);
    }

    /**
     * 录音监听的方法
     */
    public void XmutSpeechRecognizer(RecognizerListener mRecoListener){
        SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(mContext, null);
        //2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
        //3.开始听写
        mIat.startListening(mRecoListener);
    }
    /**
     * 录音播放的方法
     */
    public  void    XmutSpeechSynthesizer(String speakString, String changedLanguage){
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        SpeechSynthesizer mTts= SpeechSynthesizer.createSynthesizer(mContext, null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, changedLanguage);//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //3.开始合成
        mTts.startSpeaking(speakString, null);
    }
}
