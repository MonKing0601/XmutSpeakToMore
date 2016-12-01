package com.monking.xmutspeaktomore;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.monking.xmutspeaktomore.bean.ListenBean;
import com.monking.xmutspeaktomore.global.LanguageConstants;
import com.monking.xmutspeaktomore.global.XmutXunFei;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button listenBtn, backBtn, speakBtn;//底部栏三个空间绑定
    private TextView show_tv, itemFont;//字符串输出控件,标题栏
    private String strAll = "";//用于储存相关的字符串输出数据
    private String Str_speak = "", changedLanguage = "xiaoyan";//用于储存相关的语音合成字符串数据,changedLanguage方言选择变量
    private ImageButton selectBtn;
    private XmutXunFei mXmutXunFei;
    private EditText mEditText;
    private int edit_changed_num=0;    //用于判断edit是否为空
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化讯飞语音配置对象
        mXmutXunFei = new XmutXunFei(this, "5833f00b");
//        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5833f00b");
        //绑定控件
        listenBtn = (Button) findViewById(R.id.listen_btn);
        speakBtn = (Button) findViewById(R.id.speak_btn);
        backBtn = (Button) findViewById(R.id.back_btn);
        show_tv = (TextView) findViewById(R.id.show_text);
        selectBtn = (ImageButton) findViewById(R.id.select_language_btn);
        itemFont = (TextView) findViewById(R.id.item_font);
        mEditText= (EditText) findViewById(R.id.Edit_text_put);
        //绑定监听事件
        itemFont.setText(LanguageConstants.STRINGS[0]);
        listenBtn.setOnClickListener(this);
        speakBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        selectBtn.setOnClickListener(this);
        //监听edittext变化
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!mEditText.getText().toString().equals("")) {
                    listenBtn.setVisibility(View.INVISIBLE);
                    backBtn.setVisibility(View.VISIBLE);
                    speakBtn.setVisibility(View.VISIBLE);
                }else{
                    listenBtn.setVisibility(View.VISIBLE);
                    backBtn.setVisibility(View.INVISIBLE);
                    speakBtn.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.listen_btn:
                //封装后的录音方法
                mXmutXunFei.XmutSpeechRecognizer(mRecoListener);
                //更换控件显示
                listenBtn.setVisibility(View.INVISIBLE);
                backBtn.setVisibility(View.VISIBLE);
                speakBtn.setVisibility(View.VISIBLE);
                break;
            /**
             * 语音合成输出的代码
             */
            case R.id.speak_btn:
                //封装后的语音输出代码，传入录音和语种选择的字符串
                if (!mEditText.getText().toString().equals("")){
                    Str_speak=mEditText.getText().toString();
                }
                mXmutXunFei.XmutSpeechSynthesizer(Str_speak, changedLanguage);
                show_tv.setText(Str_speak);
                break;
            /**
             * 返回键代码
             */
            case R.id.back_btn:
                //还原初始状态
                listenBtn.setVisibility(View.VISIBLE);
                backBtn.setVisibility(View.INVISIBLE);
                speakBtn.setVisibility(View.INVISIBLE);
                Str_speak = "";
                strAll = "";
                mEditText.setText("");
                show_tv.setText("输入框输入或者语音输入");
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
    private int mCurrentSize = 0; //设置默认选项
    private int mTempWhich;//获得item位置值

    private void selectLanguageChanged() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择播放的方言");
        builder.setSingleChoiceItems(LanguageConstants.STRINGS, mCurrentSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mTempWhich = i;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (mTempWhich) {
                    case 0:
                        itemFont.setText(LanguageConstants.STRINGS[mTempWhich]);
                        changedLanguage = LanguageConstants.MANDARIN_COMMON_LADY;
                        break;
                    case 1:
                        itemFont.setText(LanguageConstants.STRINGS[mTempWhich]);
                        changedLanguage = LanguageConstants.MANDARIN_COMMON_MAN;
                        break;
                    case 2:
                        itemFont.setText(LanguageConstants.STRINGS[mTempWhich]);
                        changedLanguage = LanguageConstants.MANDARIN_YUEYU_LADY;
                        break;
                    case 3:
                        itemFont.setText(LanguageConstants.STRINGS[mTempWhich]);
                        changedLanguage = LanguageConstants.MANDARIN_TAIWAN_LADY;
                        break;
                    case 4:
                        itemFont.setText(LanguageConstants.STRINGS[mTempWhich]);
                        changedLanguage = LanguageConstants.MANDARIN_SICHUAN_LADY;
                        break;
                    case 5:
                        itemFont.setText(LanguageConstants.STRINGS[mTempWhich]);
                        changedLanguage = LanguageConstants.MANDARIN_DONGBEI_LADY;
                        break;
                    case 6:
                        itemFont.setText(LanguageConstants.STRINGS[mTempWhich]);
                        changedLanguage = LanguageConstants.MANDARIN_HENAN_MAN;
                        break;
                    case 7:
                        itemFont.setText(LanguageConstants.STRINGS[mTempWhich]);
                        changedLanguage = LanguageConstants.MANDARIN_HUNAN_MAN;
                        break;
                    case 8:
                        itemFont.setText(LanguageConstants.STRINGS[mTempWhich]);
                        changedLanguage = LanguageConstants.MANDARIN_SHANXI_LADY;
                        break;
                }
                //将默认值改成修改后的值
                mCurrentSize = mTempWhich;
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
            //如果语音录入完毕，则显示文字，并且将字符串赋值给用于发音的字符串Str_speak上。
            if (b = true) {
                show_tv.setText(strAll);
                Str_speak = strAll;
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
        int SpeakStringSize = listenBean.ws.size();
        for (int i = 0; i < SpeakStringSize; i++) {
            jsonstr = jsonstr + listenBean.ws.get(i).cw.get(0).w;
        }
        return jsonstr;
    }
}
