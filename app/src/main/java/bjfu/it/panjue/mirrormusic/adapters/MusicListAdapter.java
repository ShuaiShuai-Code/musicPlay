package bjfu.it.panjue.mirrormusic.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import bjfu.it.panjue.mirrormusic.R;
import bjfu.it.panjue.mirrormusic.activities.PlayMusicActivity;
import bjfu.it.panjue.mirrormusic.models.MusicModel;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {

    private Context mContext;
    private View mItemView;
    private RecyclerView mRv;
    private boolean isCalcaulationRyHeight;
    private List<MusicModel>mDataSource;

    public MusicListAdapter(Context context, RecyclerView recyclerView, List<MusicModel>dataSource){

        mContext = context;
        mRv = recyclerView;
        mDataSource = dataSource;
    }

    @NonNull
    @Override
    public MusicListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mItemView = LayoutInflater.from(mContext).inflate(R.layout.item_list_music, viewGroup, false);
        return new ViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicListAdapter.ViewHolder viewHolder, int i) {

        setRecycleViewHeight();

        final MusicModel musicModel = mDataSource.get(i);

        Glide.with(mContext)
                .load(musicModel.getPoster())
                .into(viewHolder.ivIcon);

        viewHolder.tvName.setText(musicModel.getName());
        viewHolder.tvAuthor.setText(musicModel.getAuthor());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mContext, PlayMusicActivity.class);
                intent.putExtra(PlayMusicActivity.MUSIC_ID, musicModel.getMusicId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    //1.获取ItemView的高度
    //2.ItemView的数量
    //3.使用ItemViewHeight * ItemViewNum = RecycleView的高度
    private void setRecycleViewHeight(){

        if(isCalcaulationRyHeight || mRv == null) return;

        isCalcaulationRyHeight = true;

        //获取ItemView的高度
        RecyclerView.LayoutParams itemViewLp =(RecyclerView.LayoutParams) mItemView.getLayoutParams();
        //获取ItemView的数量
        int itemCount = getItemCount();
        //计算出RecycleView的高度
        int recycleViewHeight = itemViewLp.height * itemCount;
        //设置RecycleView的高度
        LinearLayout.LayoutParams rvLp = (LinearLayout.LayoutParams) mRv.getLayoutParams();
        rvLp.height = recycleViewHeight;
        mRv.setLayoutParams(rvLp);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        View itemView;
        ImageView ivIcon;
        TextView tvName, tvAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAuthor = itemView.findViewById(R.id.tv_author);
        }
    }
}
