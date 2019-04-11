package com.example.administrator.expandablelistviewdemo1.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.expandablelistviewdemo1.ContentActivity;
import com.example.administrator.expandablelistviewdemo1.R;
import com.example.administrator.expandablelistviewdemo1.bean.FirstBean;
import com.example.administrator.expandablelistviewdemo1.bean.SecondBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/15.
 */
public class MainAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<FirstBean> mData;
    ViewHolder holder = null;
    public MainAdapter(Context context,ArrayList<FirstBean> data){
        this.mContext = context;
        this.mData = data;
    }
    @Override
    public int getGroupCount() {
        return mData != null?mData.size():0;
    }

    @Override
    public int getChildrenCount(int parentPosition) {
        return mData.get(parentPosition).getFirstData().size();
    }

    @Override
    public Object getGroup(int parentPosition) {
        return mData.get(parentPosition);
    }

    @Override
    public SecondBean getChild(int parentPosition, int childPosition) {
        return mData.get(parentPosition).getFirstData().get(childPosition);
    }

    @Override
    public long getGroupId(int parentPosition) {
        return parentPosition;
    }

    @Override
    public long getChildId(int parentPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     *  第一级菜单适配器布局
     * @param parentPosition
     * @param isExpanded
     *
     * @param convertView
     * @param viewGroup
     * @return
     */
    @Override
    public View getGroupView(int parentPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.parent_group_item, null);
            holder = new ViewHolder();
            holder.upImg = (ImageView) convertView.findViewById(R.id.kpi_back_img);
            holder.score = (TextView) convertView.findViewById(R.id.kpi_score);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //区分箭头往上还是
        if(isExpanded){
            holder.upImg.setImageResource(R.drawable.dowm);
        }else{
            holder.upImg.setImageResource(R.drawable.up);
        }
        holder.title.setText(mData.get(parentPosition).getTitle());
        holder.score.setText(mData.get(parentPosition).getScore());
        return convertView;
    }
    class ViewHolder{
        private TextView title;
        private TextView score;
        private ImageView upImg;
    }
    public ExpandableListView getExpandableListView() {
        ExpandableListView mExpandableListView = new ExpandableListView(
                mContext);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) mContext
                .getResources().getDimension(
                        R.dimen.parent_list_height));
        mExpandableListView.setLayoutParams(lp);
        mExpandableListView.setDividerHeight(0);// 取消group项的分割线
        mExpandableListView.setChildDivider(null);// 取消child项的分割线
        mExpandableListView.setGroupIndicator(null);// 取消展开折叠的指示图标
        return mExpandableListView;
    }
    /**
     * 第二级菜单式配
     * @param parentPosition
     * @param childPosition
     * @param isExpanded
     * @param view
     * @param viewGroup
     * @return
     */
    @Override
    public View getChildView(final int parentPosition, final int childPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        final ExpandableListView childListView = getExpandableListView();
        //获取子菜单的数据
        final ArrayList<SecondBean> childData = new ArrayList<SecondBean>();
        final SecondBean bean = getChild(parentPosition,childPosition);
        childData.add(bean);
        ChildAdapter adapter = new ChildAdapter(mContext,childData,parentPosition);
        childListView.setAdapter(adapter);

        /**
         * 点击最小级菜单，调用该方法
         * */
        childListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView arg0, View arg1,
                                        int groupIndex, int childIndex, long arg4) {
                if(mListener != null){
                    mListener.onclick(parentPosition,childPosition, childIndex);
                    //点击三级菜单，跳转到编辑菜单界面
                    Intent intent = new Intent(mContext, ContentActivity.class);
                    intent.putExtra("content","你点的位置是:  "+"parentPosition>>"+parentPosition+
                    "childPosition>>"+childPosition+"childIndex>>"+childIndex);
                    mContext.startActivity(intent);
                }
                return false;
            }
        });
        /**
         *子ExpandableListView展开时，因为group只有一项，所以子ExpandableListView的总高度=
         * （子ExpandableListView的child数量 + 1 ）* 每一项的高度
         * */
        childListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Log.e("xxx",groupPosition+"onGroupExpand>>");
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (bean.getSecondBean().size() + 1)* (int) mContext
                        .getResources().getDimension(R.dimen.parent_list_height));
                childListView.setLayoutParams(lp);
            }
        });
        /**
         *子ExpandableListView关闭时，此时只剩下group这一项，
         * 所以子ExpandableListView的总高度即为一项的高度
         * */
        childListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.e("xxx",groupPosition+">>onGroupCollapse");
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, (int) mContext
                        .getResources().getDimension(R.dimen.parent_list_height));
                childListView.setLayoutParams(lp);
                holder.upImg.setImageResource(R.drawable.up);
            }
        });
        /**
         * 在这里对二级菜单的点击事件进行操作
         */
        childListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int Position, long id) {
//                if(isClick){
//                    holder.mUpImg.setImageResource(R.drawable.dowm);
//                    isClick = false;
//                }else{
//                    holder.mUpImg.setImageResource(R.drawable.up);
//                    isClick = true;
//                }
                Log.e("Xxx","恭喜你,点击了"+parentPosition+"childpos>>>"+childPosition);
                return false;
            }
        });
        return childListView;
    }
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    /*接口回调*/
    public interface OnExpandClickListener{
        void onclick(int parentPosition,int childPosition,int childIndex);
    }
    OnExpandClickListener mListener;
    public void setOnChildListener(OnExpandClickListener listener){
        this.mListener = listener;
    }
}
