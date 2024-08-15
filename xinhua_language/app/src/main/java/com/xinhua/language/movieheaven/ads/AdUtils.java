package com.xinhua.language.movieheaven.ads;


import static com.xinhua.language.movieheaven.ads.AdConfig.AppID;
import static com.xinhua.language.movieheaven.ads.AdConfig.TakuAppKey;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.anythink.banner.api.ATBannerExListener;
import com.anythink.banner.api.ATBannerView;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.ATNetworkConfirmInfo;
import com.anythink.core.api.ATSDK;
import com.anythink.core.api.AdError;
import com.anythink.interstitial.api.ATInterstitial;
import com.anythink.interstitial.api.ATInterstitialListener;
import com.anythink.nativead.api.ATNative;
import com.anythink.rewardvideo.api.ATRewardVideoAd;
import com.anythink.rewardvideo.api.ATRewardVideoListener;
import com.anythink.splashad.api.ATSplashAd;
import com.anythink.splashad.api.ATSplashAdExtraInfo;
import com.anythink.splashad.api.ATSplashExListener;

public class AdUtils {

    private  ATSplashAd splashAd;
    private  ATRewardVideoAd mRewardVideoAd;
    private  ATInterstitial mInterstitialAd;
    private  ATBannerView mBannerView;
    private static AdUtils adUtils;

    private AdUtils() {
    }

    public static AdUtils getInstance(){
        if (adUtils==null){
            adUtils = new AdUtils();
        }
        return adUtils;
    }
    //初始化
    public  void init(Application app){
//初始化SDK该接口不会采集用户信息
        ATSDK.init(app, AppID, TakuAppKey);
//调用init后请再调用start，否则可能影响广告填充，造成收入下降
        ATSDK.start();
        ATSDK.integrationChecking(app);

    }
    public void initSplashAdd(Activity mContext){
        splashAd = new ATSplashAd(mContext, AdConfig.开屏, new ATSplashExListener() {
            @Override
            public void onDeeplinkCallback(ATAdInfo atAdInfo, boolean b) {

            }

            @Override
            public void onDownloadConfirm(Context context, ATAdInfo atAdInfo, ATNetworkConfirmInfo atNetworkConfirmInfo) {

            }

            @Override
            public void onAdLoaded(boolean b) {

            }

            @Override
            public void onAdLoadTimeout() {

            }

            @Override
            public void onNoAdError(AdError adError) {

            }

            @Override
            public void onAdShow(ATAdInfo atAdInfo) {

            }

            @Override
            public void onAdClick(ATAdInfo atAdInfo) {

            }

            @Override
            public void onAdDismiss(ATAdInfo atAdInfo, ATSplashAdExtraInfo atSplashAdExtraInfo) {

            }
        }, 4000);
        splashAd.loadAd();
    }

    //开屏广告
    public  void splashAd(Activity mContext,ViewGroup container, AdListener listener) {
        if (splashAd!=null&&splashAd.isAdReady()) {
            splashAd.setAdListener( new ATSplashExListener() {
                @Override
                public void onDeeplinkCallback(ATAdInfo atAdInfo, boolean b) {

                }

                @Override
                public void onDownloadConfirm(Context context, ATAdInfo atAdInfo, ATNetworkConfirmInfo atNetworkConfirmInfo) {

                }

                @Override
                public void onAdLoaded(boolean b) {

                }

                @Override
                public void onAdLoadTimeout() {
                    listener.onClose();
                }

                @Override
                public void onNoAdError(AdError adError) {
                    listener.onClose();
                }

                @Override
                public void onAdShow(ATAdInfo atAdInfo) {
                    listener.onShow();
                }

                @Override
                public void onAdClick(ATAdInfo atAdInfo) {

                }

                @Override
                public void onAdDismiss(ATAdInfo atAdInfo, ATSplashAdExtraInfo atSplashAdExtraInfo) {
                    listener.onClose();
                }
            });
            //container大小至少占屏幕75%
            splashAd.show(mContext, container);
        }else{
            //重新加载
            initSplashAdd(mContext);
            listener.onClose();
        }
    }

    public  void initRewardVideo(Activity activity){
            mRewardVideoAd = new ATRewardVideoAd(activity.getApplicationContext(), AdConfig.激励视频);
            mRewardVideoAd.setAdListener(new ATRewardVideoListener() {
                @Override
                public void onRewardedVideoAdLoaded() {
                    Log.e("reword","onRewardedVideoAdLoaded");
                }

                @Override
                public void onRewardedVideoAdFailed(AdError adError) {
                    Log.e("reword","onRewardedVideoAdFailed");
                }

                @Override
                public void onRewardedVideoAdPlayStart(ATAdInfo atAdInfo) {

                }

                @Override
                public void onRewardedVideoAdPlayEnd(ATAdInfo atAdInfo) {

                }

                @Override
                public void onRewardedVideoAdPlayFailed(AdError adError, ATAdInfo atAdInfo) {

                }

                @Override
                public void onRewardedVideoAdClosed(ATAdInfo atAdInfo) {

                }

                @Override
                public void onRewardedVideoAdPlayClicked(ATAdInfo atAdInfo) {

                }

                @Override
                public void onReward(ATAdInfo atAdInfo) {

                }
            });
            mRewardVideoAd.load();
    }
    //激励视频
    public  void rewardVideo(Activity activity,AdListener listener){
        if (mRewardVideoAd!=null&&mRewardVideoAd.isAdReady()) {
            mRewardVideoAd.setAdListener(new ATRewardVideoListener(){
                @Override
                public void onRewardedVideoAdLoaded() {

                }

                @Override
                public void onRewardedVideoAdFailed(AdError adError) {

                }

                @Override
                public void onRewardedVideoAdPlayStart(ATAdInfo atAdInfo) {
                    listener.onShow();
                }

                @Override
                public void onRewardedVideoAdPlayEnd(ATAdInfo atAdInfo) {

                }

                @Override
                public void onRewardedVideoAdPlayFailed(AdError adError, ATAdInfo atAdInfo) {

                }

                @Override
                public void onRewardedVideoAdClosed(ATAdInfo atAdInfo) {
                    listener.onClose();
                    initRewardVideo(activity);
                }

                @Override
                public void onRewardedVideoAdPlayClicked(ATAdInfo atAdInfo) {

                }

                @Override
                public void onReward(ATAdInfo atAdInfo) {
                    listener.reword(true);
                    initRewardVideo(activity);
                }
            });
            mRewardVideoAd.show(activity);
        }else{
            //重新加载
            listener.reword(false);
            listener.onClose();
            initRewardVideo(activity);
        }
    }

    public  void  initInterstitialAd(Activity activity){
        mInterstitialAd = new ATInterstitial(activity, AdConfig.插屏);
        mInterstitialAd.setAdListener(new ATInterstitialListener() {
            @Override
            public void onInterstitialAdLoaded() {

            }

            @Override
            public void onInterstitialAdLoadFail(AdError adError) {

            }

            @Override
            public void onInterstitialAdClicked(ATAdInfo atAdInfo) {

            }

            @Override
            public void onInterstitialAdShow(ATAdInfo atAdInfo) {

            }

            @Override
            public void onInterstitialAdClose(ATAdInfo atAdInfo) {

            }

            @Override
            public void onInterstitialAdVideoStart(ATAdInfo atAdInfo) {

            }

            @Override
            public void onInterstitialAdVideoEnd(ATAdInfo atAdInfo) {

            }

            @Override
            public void onInterstitialAdVideoError(AdError adError) {

            }
        });
        mInterstitialAd.load();
    }
    //插屏
    public  void interstitialAd(Activity activity,AdListener adListener) {

        if (mInterstitialAd!=null&&mInterstitialAd.isAdReady()) {
            mInterstitialAd.setAdListener( new ATInterstitialListener() {
                @Override
                public void onInterstitialAdLoaded() {

                }

                @Override
                public void onInterstitialAdLoadFail(AdError adError) {
                    adListener.onClose();
                    initInterstitialAd(activity);
                }

                @Override
                public void onInterstitialAdClicked(ATAdInfo atAdInfo) {

                }

                @Override
                public void onInterstitialAdShow(ATAdInfo atAdInfo) {
                    adListener.onShow();
                }

                @Override
                public void onInterstitialAdClose(ATAdInfo atAdInfo) {
                    adListener.onClose();
                    initInterstitialAd(activity);
                }

                @Override
                public void onInterstitialAdVideoStart(ATAdInfo atAdInfo) {

                }

                @Override
                public void onInterstitialAdVideoEnd(ATAdInfo atAdInfo) {

                }

                @Override
                public void onInterstitialAdVideoError(AdError adError) {
                    adListener.onClose();
                }
            });
            mInterstitialAd.show(activity);
        }else{
            //重新加载
            initInterstitialAd(activity);
            adListener.onClose();
        }
    }
    //Banner
    public  void bannerAd(Activity activity,ViewGroup container) {
        mBannerView = new ATBannerView(activity);
        mBannerView.setPlacementId(AdConfig.横幅);
        mBannerView.setBannerAdListener(new ATBannerExListener() {
            @Override
            public void onDeeplinkCallback(boolean b, ATAdInfo atAdInfo, boolean b1) {

            }

            @Override
            public void onDownloadConfirm(Context context, ATAdInfo atAdInfo, ATNetworkConfirmInfo atNetworkConfirmInfo) {

            }

            @Override
            public void onBannerLoaded() {
                Log.e("banner","onBannerLoaded");

            }

            @Override
            public void onBannerFailed(AdError adError) {
                Log.e("banner","onBannerFailed");
            }

            @Override
            public void onBannerClicked(ATAdInfo atAdInfo) {

            }

            @Override
            public void onBannerShow(ATAdInfo atAdInfo) {

            }

            @Override
            public void onBannerClose(ATAdInfo atAdInfo) {

            }

            @Override
            public void onBannerAutoRefreshed(ATAdInfo atAdInfo) {

            }

            @Override
            public void onBannerAutoRefreshFail(AdError adError) {

            }
        });
            container.removeAllViews();
//        int width = activity.getResources().getDisplayMetrics().widthPixels;//定一个宽度值，比如屏幕宽度
//        int height = (int) (130*1.5);

//如果出现Banner有时高、有时低的情况，请使用此代码
            float ratio = 320 / 50f;//必须跟Taku后台配置的Banner广告源宽高比例一致，假设尺寸为320x50
            int width = activity.getResources().getDisplayMetrics().widthPixels;//定一个宽度值，比如屏幕宽度
            int height = (int) (width / ratio);
            mBannerView.setLayoutParams(new FrameLayout.LayoutParams(width, height));
            container.addView(mBannerView);
            mBannerView.loadAd();
    }
}
