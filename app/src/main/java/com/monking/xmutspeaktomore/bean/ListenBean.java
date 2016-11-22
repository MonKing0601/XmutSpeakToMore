package com.monking.xmutspeaktomore.bean;

import java.util.ArrayList;

/**
 * 录音的json数据Bean
 * Created by MonKing on 2016/11/22.
 */

public class ListenBean {
    public ArrayList<wsData> ws;

    public class wsData {
        public ArrayList<cwData> cw;
    }

    public class cwData {
        public String w;
    }
}
