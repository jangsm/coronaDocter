package com.softsquared.android.corona.src.main.community;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseFragment;
import com.softsquared.android.corona.src.main.community.interfaces.CommunityView;
import com.softsquared.android.corona.src.main.community.model.Post;
import com.softsquared.android.corona.src.main.info.CaringInfo;
import com.softsquared.android.corona.src.main.info.NewsAdapter;

import java.util.ArrayList;


public class CommunityFragment extends BaseFragment implements CommunityView {

    ViewPager mViewPager;
    NewsAdapter newsAdapter;
    TabLayout mTabLayout;
    Button mBtnCancel;
    ImageView mImageViewMask, mImageViewHand;
    LinearLayout mLinearHeader, mLinearContent;


    ArrayList<CaringInfo> mArrayListCaringInfos = new ArrayList<>();
    boolean mIsExpanded = false;

    ArrayList<Post> mPosts = new ArrayList<>();
    HotPostAdapter mHotPostAdapter;
    RecyclerView mRecyclerView;

    private int mPage = 0;
    private static int mSize = 20;

    boolean mNoMoreItem = false;
    boolean mLoadLock = false;

    ArrayList<Post> mNewPosts = new ArrayList<>();
    NewPostAdapter mNewPostAdapter;
    RecyclerView mNewRv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getHotPost();
        getNewPost(mPage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_community, container, false);
        setComponentView(view);
        return view;
    }

    @Override
    public void setComponentView(View v) {
        mTabLayout = v.findViewById(R.id.home_tab_indicator);
        mLinearHeader = v.findViewById(R.id.fragment_community_linear_header);
        mLinearContent = v.findViewById(R.id.fragment_community_linear_content);
        mBtnCancel = v.findViewById(R.id.dialog_infect_select_btn_cancel);
        mIsExpanded = false;
        mArrayListCaringInfos.clear();
        mArrayListCaringInfos.add(new CaringInfo(R.drawable.banner_invite, "", 1));
        mArrayListCaringInfos.add(new CaringInfo(R.drawable.banner_tip, "", 2));

        newsAdapter = new NewsAdapter(getChildFragmentManager(), mArrayListCaringInfos);

        mViewPager = v.findViewById(R.id.fragment_info_vp);
//       뷰페이저 미리보기 설정//
        mViewPager.setClipToPadding(false);
//        int dpValue = 25;
//        float d = getResources().getDisplayMetrics().density;
//        int margin = (int) (dpValue * d);
//        mViewPager.setPadding(margin/2, 0, margin, 0);
//        mViewPager.setPageMargin(margin/3);
        mViewPager.setAdapter(newsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

//        mImageViewMask.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent3 = new Intent(getContext(), OrderWebViewActivity.class);
//                intent3.putExtra("keyword", "KF94마스크");
//                startActivity(intent3);
//            }
//        });
//        mImageViewHand.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent3 = new Intent(getContext(), OrderWebViewActivity.class);
//                intent3.putExtra("keyword", "손세정제");
//                startActivity(intent3);
//            }
//        });
        mLinearHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mIsExpanded){
                    showWrite();
                    mIsExpanded = true;
                }
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideWrite();
                mIsExpanded = false;
            }
        });

        mRecyclerView = v.findViewById(R.id.hot_post_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        mHotPostAdapter = new HotPostAdapter(getContext(), mPosts, new HotPostAdapter.HotPostAdapterListener() {
            @Override
            public void Click() {

            }

            @Override
            public void likeClick() {

            }
        });
        mRecyclerView.setAdapter(mHotPostAdapter);

        mNewRv = v.findViewById(R.id.new_post_rv);
        mNewRv.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });

        mNewPostAdapter = new NewPostAdapter(getContext(), mNewPosts, new NewPostAdapter.NewPostAdapterListener() {
            @Override
            public void click() {

            }

            @Override
            public void likeClick() {

            }
        });

        mNewRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisiblePosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = mRecyclerView.getAdapter().getItemCount();

                if (lastVisiblePosition > itemTotalCount * 0.7) {
                    if (!mLoadLock) {
                        mLoadLock = true;
                        if (!mNoMoreItem) {
                            getNewPost(mPage);
                        }
                    }
                }
            }
        });

        mNewRv.setAdapter(mNewPostAdapter);
    }

    private void showWrite() {
        ValueAnimator anim2 = ValueAnimator.ofInt(0,700);
        anim2.setDuration(500); // duration 5 seconds
//        anim2.setRepeatCount(ValueAnimator.INFINITE);
        anim2.setRepeatMode(ValueAnimator.REVERSE);
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                mLinearContent.getLayoutParams().height = value.intValue();
                mLinearContent.requestLayout();
            }
        });
        anim2.start();
        mIsExpanded = true;
    }


    private void hideWrite() {
        ValueAnimator anim2 = ValueAnimator.ofInt(800,0);
        anim2.setDuration(500); // duration 5 seconds
//        anim2.setRepeatCount(ValueAnimator.INFINITE);
        anim2.setRepeatMode(ValueAnimator.REVERSE);
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                mLinearContent.getLayoutParams().height = value.intValue();
                mLinearContent.requestLayout();
            }
        });
        anim2.start();
//        mLinearHeader.setVisibility(View.VISIBLE);
    }

    void getHotPost(){
        showProgressDialog(getActivity());
        final CommunityService communityService = new CommunityService(this);
        communityService.getHotPost();
    }

    void getNewPost(int mPage){
        showProgressDialog(getActivity());
        final CommunityService communityService = new CommunityService(this);
        communityService.getNewPost(mPage);
    }

    @Override
    public void getHotPostSuccess(ArrayList<Post> arrayList) {
        hideProgressDialog();
        mPosts.addAll(arrayList);
        mHotPostAdapter.notifyDataSetChanged();
    }

    @Override
    public void getNewPostSuccess(ArrayList<Post> arrayList) {
        hideProgressDialog();
        if(arrayList.size()%mSize!=0||arrayList.size()==0){
            mNoMoreItem = true;
        }

        if(arrayList!=null){
            mNewPosts.addAll(arrayList);
            mNewPostAdapter.notifyDataSetChanged();
            mPage += arrayList.size();
            mLoadLock = false;
        }
    }

    @Override
    public void validateFailure(String message) {
        hideProgressDialog();
        showCustomToast( message == null || message.isEmpty() ? getString(R.string.network_error) : message);
    }
}