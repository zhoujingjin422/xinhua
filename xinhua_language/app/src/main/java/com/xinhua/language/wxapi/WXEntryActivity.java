package com.xinhua.language.wxapi;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.xinhua.language.wanbang.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.errCode==0){
            //支付成功。去后台查询订单
            EventBus.getDefault().post(new MessageEvent("SUCCESS",null));
        }else if (baseResp.errCode==-1){
            //出现错误
            EventBus.getDefault().post(new MessageEvent("ERROR",null));
        }else if (baseResp.errCode==-2){
            //用户取消支付
            EventBus.getDefault().post(new MessageEvent("CANCEL",null));
        }
        if (baseResp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){

        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
