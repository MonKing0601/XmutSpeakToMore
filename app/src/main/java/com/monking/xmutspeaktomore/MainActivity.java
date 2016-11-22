package com.monking.xmutspeaktomore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.monking.xmutspeaktomore.bean.ListenBean;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button listenBtn,backBtn,speakBtn;
    private TextView show_tv;
    private String strAll = "";
    private String Str_speak="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化讯飞语音配置对象
        // 将“12345678”替换成您申请的APPID，申请地址：http://open.voicecloud.cn
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5833f00b");

        listenBtn = (Button) findViewById(R.id.listen_btn);
        speakBtn= (Button) findViewById(R.id.speak_btn);
        backBtn= (Button) findViewById(R.id.back_btn);
        show_tv = (TextView) findViewById(R.id.show_text);
        listenBtn.setOnClickListener(this);
        speakBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.listen_btn:
                System.out.println("sssssss");
                //1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
                SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(this, null);
                //2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
                mIat.setParameter(SpeechConstant.DOMAIN, "iat");
                mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
                mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
                //3.开始听写
                mIat.startListening(mRecoListener);
                listenBtn.setVisibility(View.INVISIBLE);
                backBtn.setVisibility(View.VISIBLE);
                speakBtn.setVisibility(View.VISIBLE);
                break;

            case R.id.speak_btn:
                //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
                SpeechSynthesizer mTts= SpeechSynthesizer.createSynthesizer(this, null);
                //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
                mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
                mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
                mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
                mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
                //3.开始合成
                mTts.startSpeaking(Str_speak, null);
                break;
            case R.id.back_btn:
                //还原初始状态
                listenBtn.setVisibility(View.VISIBLE);
                backBtn.setVisibility(View.INVISIBLE);
                speakBtn.setVisibility(View.INVISIBLE);
                Str_speak="";
                strAll = "";
                break;
        }
    }

    /**
     * 录音监听的控件
     */
    private RecognizerListener mRecoListener = new RecognizerListener() {
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            //获得语音识别返回的json字符串
            String result = recognizerResult.getResultString();
            //解析json字符串
            String str = analysisJson(result);
            //将字符串放进成员变量String里面储存 用于显示
            strAll = strAll + str;

            System.out.println(str);
            System.out.println("strAll:"+strAll);
            //如果语音录入完毕，则显示文字，并且将字符串赋值给用于发音的字符串Str_speak上。
            if (b = true) {
                show_tv.setText(strAll);
                Str_speak=strAll;
                System.out.println("Str_speak:"+Str_speak);
            }
        }

        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        @Override
        public void onBeginOfSpeech() {
            show_tv.setText("开始说话");
        }

        @Override
        public void onEndOfSpeech() {
            System.out.println("结束说话");
        }


        @Override
        public void onError(SpeechError speechError) {
            show_tv.setText("录音失败");
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };



    /**
     * 用于解析录音中的Json文字数据
     *
     * @return
     */
    private String analysisJson(String result) {
        Gson gson = new Gson();
        String jsonstr = "";
        ListenBean listenBean = gson.fromJson(result, ListenBean.class);
        jsonstr = listenBean.ws.get(0).cw.get(0).w;
        return jsonstr;
    }
}
