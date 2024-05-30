package com.cpdtlp.xinhuaandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.SubscribeMessage;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelbiz.WXOpenBusinessView;
import com.tencent.mm.opensdk.modelbiz.WXOpenBusinessWebview;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xinhua.language.R;
import com.xinhua.language.wanbang.bean.MessageEvent;
import com.xinhua.language.wanbang.utils.Constant;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
    private static String TAG = "MicroMsg.WXEntryActivity";
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, false);

        try {
            Intent intent = getIntent();
            api.handleIntent(intent, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Log.e("WXPayEntryActivity",String.valueOf(baseResp.errCode));
            if (baseResp.errCode==0){
                //支付成功。去后台查询订单
                EventBus.getDefault().post(new MessageEvent("SUCCESS",null));
                Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
            }else if (baseResp.errCode==-1){
                //出现错误
                EventBus.getDefault().post(new MessageEvent("ERROR",null));
            }else if (baseResp.errCode==-2){
                //用户取消支付
                EventBus.getDefault().post(new MessageEvent("CANCEL",null));
            }
        }
        finish();//这里需要关闭该页面
    }
}