package com.example.administrator.expandablelistviewdemo1;

import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import com.example.administrator.expandablelistviewdemo1.adapter.MainAdapter;
import com.example.administrator.expandablelistviewdemo1.bean.FirstBean;
import com.example.administrator.expandablelistviewdemo1.bean.SecondBean;
import com.example.administrator.expandablelistviewdemo1.bean.ThirdBean;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ExpandableListView.OnGroupExpandListener,
        MainAdapter.OnExpandClickListener{
    private ExpandableListView mList;
    private ArrayList<FirstBean> mDatas = new ArrayList<FirstBean>();
    private MainAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        for(int i=0;i<4;i++){
            FirstBean firstBean = new FirstBean();
            ArrayList<SecondBean> mArrlistSecondBean = new ArrayList<SecondBean>();
            if(i==0){
                firstBean.setScore("80分");
                firstBean.setTitle("KPI  关键能力");
            }else if(i==1){
                firstBean.setScore("10分");
                firstBean.setTitle("API  工作态度");
            }else if(i==2){
                firstBean.setScore("10分");
                firstBean.setTitle("LPI  团队建设");
            }else if(i==3){
                firstBean.setScore("5分");
                firstBean.setTitle("WPI  特殊事件");
            }
            for(int j=0;j<3;j++){
                SecondBean secondBean = new SecondBean();
                secondBean.setTitle("第"+i+"个二级标题");
                ArrayList<ThirdBean> mArrlistBean = new ArrayList<ThirdBean>();
                for(int k=0;k<2;k++){
                    ThirdBean thirdBean = new ThirdBean();
                    thirdBean.setTitle("第"+k+"个三级标题");
                    mArrlistBean.add(thirdBean);
                }
                secondBean.setSecondBean(mArrlistBean);
                mArrlistSecondBean.add(secondBean);
            }
            firstBean.setFirstData(mArrlistSecondBean);
            mDatas.add(firstBean);

            Log.e("xxx",mDatas.get(i).getTitle());
        }
    }

    private void initView(){
        mList = (ExpandableListView) findViewById(R.id.expand_list);
        mAdapter = new MainAdapter(this,mDatas);
        mList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        //设置点击父控件的监听
        mList.setOnGroupExpandListener(this);
        //点击最里面的菜单的点击事件
        mAdapter.setOnChildListener(this);
    }

    /**
     * 保证listview只展开一项
     * @param groupPosition
     */
    @Override
    public void onGroupExpand(int groupPosition) {
        Log.e("xxx","onGroupExpand>>"+groupPosition);
        for (int i = 0; i < mDatas.size(); i++) {
            if (i != groupPosition) {
                mList.collapseGroup(i);
            }
        }
    }

    /***
     * 点击最次级菜单的点击事件
     * @param parentPosition
     * @param childPosition
     * @param childIndex
     */
    @Override
    public void onclick(int parentPosition, int childPosition, int childIndex) {
        Log.e("xxx","点了"+"parentPosition>>"+"childPosition>>"+childPosition+
        "childIndex>>"+childIndex);
    }
}
