package com.softsquared.android.corona.src.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.kakao.adfit.ads.AdListener;
import com.kakao.adfit.ads.ba.BannerAdView;
import com.mobon.sdk.AdItem;
import com.mobon.sdk.BannerType;
import com.mobon.sdk.InterstitialDialog;
import com.mobon.sdk.Key;
import com.mobon.sdk.MobonSDK;
import com.mobon.sdk.RectBannerView;
import com.mobon.sdk.callback.iMobonAdCallback;
import com.mobon.sdk.callback.iMobonBannerCallback;
import com.mobon.sdk.callback.iMobonInterstitialAdCallback;
import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseActivity;
import com.softsquared.android.corona.src.BaseFragment;
import com.softsquared.android.corona.src.main.community.PostDetailActivity;
import com.softsquared.android.corona.src.main.faq.FaqFragment;
import com.softsquared.android.corona.src.main.info.InfoFragment;
import com.softsquared.android.corona.src.main.interfaces.MainActivityView;
import com.softsquared.android.corona.src.main.map.MapViewFragment;
import com.softsquared.android.corona.src.main.news.NewsFragment;
import com.softsquared.android.corona.src.main.community.CommunityFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

import static com.softsquared.android.corona.src.ApplicationClass.PUSH_ON_OFF;
import static com.softsquared.android.corona.src.ApplicationClass.sSharedPreferences;

public class MainActivity extends BaseActivity implements MainActivityView {
    private TextView mTextViewTitle;
    public static int IS_WEBVIEW_MODE = 0;

    final int MAP_FRAGMENT = 0;
    final int COMMUNITY_FRAGMENT = 1;
    final int NEWS_FRAGMENT = 3;
    final int INFO_FRAGMENT = 2;
    final int FAQ_FRAGMENT = 4;

    private long backPressedTime = 0;
    private final long FINISH_INTERVAL_TIME = 2000;

    public static MainViewPager mViewPagerMain;
    BaseFragment mMapFragments;
    NewsFragment mNewsFragments;
    BaseFragment mCommunityFragment;
    BaseFragment mInfoFragment;
    BaseFragment mFaqFragment;
    MainFragmentPagerAdapter mMainFragmentPagerAdapter;

    private RelativeLayout mRelativeTopBar;
    private LinearLayout mLinearNoti;
    private ImageView mImageViewNoti;


    public static NavigationTabBar mNavigationTabBar;
    private ArrayList<NavigationTabBar.Model> mNavigationTabBarModels;
    private InterstitialAd mInterstitialAd;

    private AdView mAdView;
    private LinearLayout mLinear;

    private BannerAdView adView;


    public static final String AD_TEST_KEY_BANNER = "ca-app-pub-3940256099942544/6300978111";

    public static ArrayList<Integer> selectedInfectedArr = new ArrayList<>();
    public static boolean isFirstMapLoad = true;

    LinearLayout banner_container;
    RectBannerView rectBannerView;
    InterstitialDialog interstitialDialog;
    MobonSDK mMobonSDK;
    Context mContext;
//    public static final String AD_TEST_KEY_INTERESTITIAL = "ca-app-pub-3940256099942544/1033173712";

//      [주의] 실제키로 빌드하면안됨
//    public static final String AD_REAL_KEY_BANNER = "ca-app-pub-2165488373168832/8270923170";
//    public static final String AD_REAL_KEY_INTERESTITIAL = "ca-app-pub-2165488373168832/3881904411";
//      [주의] 실제키로 빌드하면안됨

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mMobonSDK = new MobonSDK(this, "coronadocter"); //두번째 인자에 발급받은 미디어코드로 수정하세요.
        MobonSDK.init(getApplication());
//        setMobonAdJson();

//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId(AD_TEST_KEY_INTERESTITIAL);
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                // Load the next interstitial.
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//
//        });

        initView();
        moveToCommunityTab();
        initInterstitialDialog();
        iniMobonBanner();

/*        mAdView = new AdView(this);
        mAdView.setAdUnitId(AD_TEST_KEY_BANNER);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });


        mLinear = findViewById(R.id.activity_main_linear);
        // Step 1 - Create AdView and set the ad unit ID on it.
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.gravity = Gravity.TOP;ㅌ
        mLinear.addView(mAdView, params);
        loadBanner();*/

//        adView = findViewById(R.id.adView);  // 배너 광고 뷰
//        adView.setClientId("DAN-us3hebllllk2");  // 할당 받은 광고 단위(clientId) 설정
//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
////                showCustomToast("Banner is loaded");
//
//            }
//
//            @Override
//            public void onAdFailed(int i) {
////                showCustomToast("Failed to load banner :: errorCode = " + i);
//
//            }
//
//            @Override
//            public void onAdClicked() {
////                showCustomToast("Banner is clicked");
//
//            }
//        });
//        adView.loadAd();  // 광고 요청
//
//
//        getLifecycle().addObserver(new LifecycleObserver() {
//
//            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
//            public void onResume() {
//                if (adView == null) return;
//                adView.resume();
//            }
//
//            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
//            public void onPause() {
//                if (adView == null) return;
//                adView.pause();
//            }
//
//            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//            public void onDestroy() {
//                if (adView == null) return;
//                adView.destroy();
//                adView = null;
//            }
//
//        });
    }


    private void moveToCommunityTab() {
        int postNo = getIntent().getIntExtra("postNo", 0);
        if (getIntent().getBooleanExtra("moveCommunityTab", false)) {
            mNavigationTabBar.setViewPager(mViewPagerMain, 1);
            Log.d("로그1111", postNo + "");
        } else if (postNo > 0) {
            mNavigationTabBar.setViewPager(mViewPagerMain, 1);
            Log.d("로그", postNo + "");
            Intent intent = new Intent(this, PostDetailActivity.class);
            intent.putExtra("postNo", postNo);
            startActivity(intent);

        }
    }

    void setMobonAdJson() {
        //두번째 인자 : 받을 광고의 개수
        //세번째 인자 : 발급받은 UnitId 로 교체하세요.
        //네번째 인자 : 광고 호출 callback Listener
        mMobonSDK.getMobonAdData(this, 1, "unitId", new iMobonAdCallback() {
            @Override
            public void onLoadedMobonAdData(boolean result, JSONObject objData, String errorStr) {
                if (result) {
                    try {
                        JSONObject jObj = objData.getJSONArray("client").getJSONObject(0);
                        JSONArray jArray = jObj.getJSONArray("data");
                        int length = jObj.getInt("length");
                        String AdType = jObj.getString("target");
                        for (int i = 0; i < length; i++) {
                            AdItem item = new AdItem(mContext, AdType, jArray.getJSONObject(i));
                            //광고 데이터 처리...
                        }


                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    if (errorStr.equals(Key.NOFILL)) {//광고 없음
                    } else {//통신 오류
                    }
                }
            }
        });
    }

    void iniMobonBanner() {
// 각 광고 뷰 당 발급받은 UNIT_ID 값을 필수로 넣어주어야 합니다.
        rectBannerView = new RectBannerView(this, BannerType.BANNER_FILLx60).setBannerUnitId("287942");
// 배너뷰의 리스너를 등록합니다.
        rectBannerView.setAdListener(new iMobonBannerCallback() {
            @Override
            public void onLoadedAdInfo(boolean result, String errorcode) {
                if (result) {
                    //배너 광고 로딩 성공
//                    showCustomToast("배너 광고로딩");
                    // 광고를 띄우고자 하는 layout 에 배너뷰를 삽입합니다.
                    banner_container.addView(rectBannerView);
                } else {
//                    showCustomToast("광고실패 : " + errorcode);
                    rectBannerView.destroyAd();
                    rectBannerView = null;
                }
            }

            @Override
            public void onAdClicked() {
                System.out.println("광고클릭");
            }
        });
        // 광고를 호출합니다.
        rectBannerView.loadAd();
    }

    void initInterstitialDialog() {
        //전면 배너를 선언하시고 발급받은 UnitId 로 교체하세요.
        interstitialDialog = new InterstitialDialog(this).setType(Key.INTERSTITIAL_TYPE.SMALL).setUnitId("287941").build();
        //전면 배너 리스너를 등록합니다.
        interstitialDialog.setAdListener(new iMobonInterstitialAdCallback() {
            @Override
            public void onLoadedAdInfo(boolean result, final String errorStr) {
                if (result) {
                    //전면 광고를 띄웁니다.
                    if (interstitialDialog.isLoaded())
                        interstitialDialog.show();
                    //광고 성공
                } else {
                    //광고 실패
//                    showCustomToast("onLoadedAdInfo fail" + errorStr);
                }
            }

            @Override
            public void onClickEvent(Key.INTERSTITIAL_KEYCODE event_code) {
                if (event_code == Key.INTERSTITIAL_KEYCODE.CLOSE) {
//                    if (interstitialDialog != null)
//                        interstitialDialog.loadAd();
                } else if (event_code == Key.INTERSTITIAL_KEYCODE.ADCLICK) {
//                    showCustomToast("Interstitial Ad Click");
                    if (interstitialDialog != null)
                        interstitialDialog.close();
                }
            }

            @Override
            public void onOpened() {

            }

            @Override
            public void onClosed() {

            }
        });

        //광고를 호출합니다
//        interstitialDialog.loadAd();

    }

    private void initView() {
        mMapFragments = new MapViewFragment();
        mCommunityFragment = new CommunityFragment();
        mInfoFragment = new InfoFragment();
        mNewsFragments = new NewsFragment();
        mFaqFragment = new FaqFragment();

        mTextViewTitle = findViewById(R.id.activity_main_tv);
        mRelativeTopBar = findViewById(R.id.activity_main_relative_top);
        mLinearNoti = findViewById(R.id.activity_main_linear_noti);
        mImageViewNoti = findViewById(R.id.activity_main_iv_noti);

        banner_container = findViewById(R.id.activity_main_linear);

//        mTextViewTitle = findViewById(R.id.activity_main_tv);
//        mImageViewTitle = findViewById(R.id.activity_main_iv_title);

        mMainFragmentPagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        mMainFragmentPagerAdapter.addFragment(mMapFragments, "1");
        mMainFragmentPagerAdapter.addFragment(mCommunityFragment, "2");
        mMainFragmentPagerAdapter.addFragment(mInfoFragment, "3");
        mMainFragmentPagerAdapter.addFragment(mNewsFragments, "4");
        mMainFragmentPagerAdapter.addFragment(mFaqFragment, "5");

        mViewPagerMain = findViewById(R.id.vpMainViewPager);
        mViewPagerMain.setAdapter(mMainFragmentPagerAdapter);
        mViewPagerMain.setPagingEnabled(false);

        mNavigationTabBar = findViewById(R.id.ntb);
        mNavigationTabBarModels = new ArrayList<>();
        mNavigationTabBarModels.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_tab_map),
                        getResources().getColor(R.color.colorWhite)
                )
                        .title("")
                        .build()
        );

        mNavigationTabBarModels.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_tab_community),
                        getResources().getColor(R.color.colorWhite)
                )
                        .title("")
                        .build()
        );

        mNavigationTabBarModels.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_tab_info),
                        getResources().getColor(R.color.colorWhite)
                )
                        .title("")
                        .build()
        );

        mNavigationTabBarModels.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_tab_news),
                        getResources().getColor(R.color.colorWhite)
                )
                        .title("")
                        .build()
        );

        mNavigationTabBarModels.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_tab_faq),
                        getResources().getColor(R.color.colorWhite)
                )
                        .title("")
                        .build()
        );


        mNavigationTabBar.setModels(mNavigationTabBarModels);
        mNavigationTabBar.setIsBadged(true);
        mNavigationTabBar.setViewPager(mViewPagerMain, 0); // 처음으로 0번째 Fragment 를 보여줍니다

        mViewPagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case MAP_FRAGMENT:
                        mRelativeTopBar.setVisibility(View.GONE);
                        mTextViewTitle.setVisibility(View.VISIBLE);
                        mLinearNoti.setVisibility(View.GONE);
                        mTextViewTitle.setText("지도");
                        IS_WEBVIEW_MODE = 0;
                        if (adView != null) {
                            adView.setVisibility(View.VISIBLE);
                        }
                        break;

                    case NEWS_FRAGMENT:
                        mRelativeTopBar.setVisibility(View.VISIBLE);
                        mTextViewTitle.setVisibility(View.VISIBLE);
                        mLinearNoti.setVisibility(View.GONE);
                        mTextViewTitle.setText("뉴스");
                        IS_WEBVIEW_MODE = 1;
                        if (adView != null) {
                            adView.setVisibility(View.GONE);
                        }
                        break;

                    case COMMUNITY_FRAGMENT:
                        mRelativeTopBar.setVisibility(View.VISIBLE);
                        mTextViewTitle.setVisibility(View.VISIBLE);
                        mLinearNoti.setVisibility(View.VISIBLE);
                        mTextViewTitle.setText("커뮤니티");
                        IS_WEBVIEW_MODE = 0;
                        if (adView != null) {
                            adView.setVisibility(View.VISIBLE);
                        }

                        break;
                    case INFO_FRAGMENT:
                        mRelativeTopBar.setVisibility(View.VISIBLE);
                        mTextViewTitle.setVisibility(View.VISIBLE);
                        mLinearNoti.setVisibility(View.VISIBLE);
                        mTextViewTitle.setText("감염 정보");
                        IS_WEBVIEW_MODE = 0;
                        if (adView != null) {
                            adView.setVisibility(View.VISIBLE);
                        }
                        break;
                    case FAQ_FRAGMENT:
                        mRelativeTopBar.setVisibility(View.VISIBLE);
                        mTextViewTitle.setVisibility(View.VISIBLE);
                        mLinearNoti.setVisibility(View.GONE);
                        mTextViewTitle.setText("질문 답변");
                        IS_WEBVIEW_MODE = 0;
                        if (adView != null) {
                            adView.setVisibility(View.GONE);
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mNavigationTabBar.setBadgeTitleColor(Color.WHITE);
        mNavigationTabBar.setIsSwiped(true);

        setNotiIcon();
        mImageViewNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences spf = sSharedPreferences;
                boolean isPushOn = spf.getBoolean(PUSH_ON_OFF, true);
                final SharedPreferences.Editor editor = sSharedPreferences.edit();
                if (isPushOn) {
                    editor.putBoolean(PUSH_ON_OFF, false);
                } else {
                    editor.putBoolean(PUSH_ON_OFF, true);
                }
                editor.apply();
                setNotiIcon();
            }
        });
    }


    private void tryGetTest() {
        showProgressDialog();

        final MainService mainService = new MainService(this);
        mainService.getTest();
    }

    @Override
    public void validateSuccess(String text) {
        hideProgressDialog();
        mTextViewTitle.setText(text);
    }

    @Override
    public void validateFailure(@Nullable String message) {
        hideProgressDialog();
        showCustomToast(message == null || message.isEmpty() ? getString(R.string.network_error) : message);
    }

    // 뒤로가기 버튼을 눌렀을 때의 오버라이드 메소드
    // 뒤로가기 버튼을 눌렀을 때의 오버라이드 메소드
    @Override
    public void onBackPressed() {
//        Log.d("뒤로가기", " ");

        if (IS_WEBVIEW_MODE == 1) {
            try {
                int backResult = mNewsFragments.webViewBack();
                if (backResult == 1) { //웹뷰 뒤로가기 상공
                } else {//뒤로갈곳 없음
                    closeApp();
                }
            } catch (Exception e) {
                //back-key code of another fragment
            }
        } else {
//            Log.d("앱 뒤로가기", " ");
            closeApp();
        }
    }

    void closeApp() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
//            if (mInterstitialAd.isLoaded()) {
//                mInterstitialAd.show();
//            } else {
//                Log.d("TAG", "The interstitial wasn't loaded yet.");
//            }
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Snackbar.make(findViewById(R.id.activity_main_tv),
                    " 뒤로가기를 한번 더 누르면 앱을 종료합니다", Snackbar.LENGTH_LONG).show();
        }
    }

    public void setNotiIcon() {
        SharedPreferences spf = sSharedPreferences;
        boolean isPushOn = spf.getBoolean(PUSH_ON_OFF, true);
        final SharedPreferences.Editor editor = sSharedPreferences.edit();
        if (isPushOn) {
            mImageViewNoti.setImageResource(R.drawable.img_noti_on);
            editor.putBoolean(PUSH_ON_OFF, false);
        } else {
            mImageViewNoti.setImageResource(R.drawable.img_noti_off);
            editor.putBoolean(PUSH_ON_OFF, true);
        }
    }

    public void customOnClick(View view) {
        switch (view.getId()) {
            case R.id.main_btn_hello_world:
                tryGetTest();
                break;
            default:
                break;
        }
    }

    private void loadBanner() {
        // Create an ad request. Check your logcat output for the hashed device ID
        // to get test ads on a physical device, e.g.,
        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this
        // device."
        AdRequest adRequest =
                new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();

        AdSize adSize = getAdSize();
        // Step 4 - Set the adaptive ad size on the ad view.
        mAdView.setAdSize(adSize);

        // Step 5 - Start loading the ad in the background.
        mAdView.loadAd(adRequest);
    }


    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    @Override
    protected void onDestroy() {
        hideProgressDialog();
        super.onDestroy();
    }
}
