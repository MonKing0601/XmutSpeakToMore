package com.monking.xmutspeaktomore;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.monking.xmutspeaktomore.global.LanguageConstants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button listenBtn,backBtn,speakBtn;//底部栏三个空间绑定
    private TextView show_tv,itemFont;//字符串输出控件,标题栏
    private String strAll = "";//用于储存相关的字符串输出数据
    private String Str_speak="",changedLanguage="xiaoyan";//用于储存相关的语音合成字符串数据,changedLanguage方言选择变量
    private ImageButton selectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化讯飞语音配置对象
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5833f00b");
        //绑定控件
        listenBtn = (Button) findViewById(R.id.listen_btn);
        speakBtn= (Button) findViewById(R.id.speak_btn);
        backBtn= (Button) findViewById(R.id.back_btn);
        show_tv = (TextView) findViewById(R.id.show_text);
        selectBtn = (ImageButton) findViewById(R.id.select_language_btn);
        itemFont= (TextView) findViewById(R.id.item_font);
        //绑定监听事件
        itemFont.setText(LanguageConstants.STRINGS[0]);
        listenBtn.setOnClickListener(this);
        speakBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        selectBtn.setOnClickListener(this);
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
            /**
             * 语音合成输出的代码
             */
            case R.id.speak_btn:
                //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
                SpeechSynthesizer mTts= SpeechSynthesizer.createSynthesizer(this, null);
                //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
                mTts.setParameter(SpeechConstant.VOICE_NAME, changedLanguage);//设置发音人
                mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
                mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
                mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
                //3.开始合成
                mTts.startSpeaking(Str_speak, null);
                break;
            /**
             * 返回键代码
             */
            case R.id.back_btn:
                //还原初始状态
                listenBtn.setVisibility(View.VISIBLE);
                backBtn.setVisibility(View.INVISIBLE);
                speakBtn.setVisibility(View.INVISIBLE);
                Str_speak="";
                strAll = "";
                break;
            /**
             * 方言选择按钮控件
             */
            case R.id.select_language_btn:
                selectLanguageChanged();
                break;
        }
    }

    /**
     * 方言修改的方法 跳出alertDialog
     */
    private int mCurrentSize=0;
    private int mTempWhich;
    private void selectLanguageChanged() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("选择播放的方言");
        builder.setSingleChoiceItems(LanguageConstants.STRINGS, mCurrentSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mTempWhich=i;
            }
        });
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (mTempWhich){
                    case 0:
                        itemFont.setText("当前选择语言是"+LanguageConstants.STRINGS[mTempWhich]);
                        changedLanguage=LanguageConstants.MANDARIN_COMMON_LADY;
                        break;
                    case 1:
                        itemFont.setText("当前选择语言是"+LanguageConstants.STRINGS[mTempWhich]);
                        changedLanguage=LanguageConstants.MANDARIN_COMMON_MAN;
                        break;
                    case 2:
                        itemFont.setText("当前选择语言是"+LanguageConstants.STRINGS[mTempWhich]);
                        changedLanguage=LanguageConstants.MANDARIN_YUEYU_LADY;
                        break;
                    case 3:
                        itemFont.setText("当前选择语言是"+LanguageConstants.STRINGS[mTempWhich]);
                        changedLanguage=LanguageConstants.MANDARIN_TAIWAN_LADY;
                        break;
                    case 4:
                        itemFont.setText("当前选择语言是"+LanguageConstants.STRINGS[mTempWhich]);
                        changedLanguage=LanguageConstants.MANDARIN_SICHUAN_LADY;
                        break;
                    case 5:
                        itemFont.setText("当前选择语言是"+LanguageConstants.STRINGS[mTempWhich]);
                        changedLanguage=LanguageConstants.MANDARIN_DONGBEI_LADY;
                        break;
                    case 6:
                        itemFont.setText("当前选择语言是"+LanguageConstants.STRINGS[mTempWhich]);
                        changedLanguage=LanguageConstants.MANDARIN_HENAN_MAN;
                        break;
                    case 7:
                        itemFont.setText("当前选择语言是"+LanguageConstants.STRINGS[mTempWhich]);
                        changedLanguage=LanguageConstants.MANDARIN_HUNAN_MAN;
                        break;
                    case 8:
                        itemFont.setText("当前选择语言是"+LanguageConstants.STRINGS[mTempWhich]);
                        changedLanguage=LanguageConstants.MANDARIN_SHANXI_LADY;
                        break;
                }
                mCurrentSize=mTempWhich;
            }
        });
        builder.show();
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
