package com.teampenguin.apps.notenote.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.teampenguin.apps.notenote.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryListAdapter extends ListAdapter<String, CategoryListAdapter.CategoryAdapterViewHold> {

    private static final String TAG = "CategoryListAdapter";
    private static final int PRESET_CATEGORY_NUMBER = 6;
    private String chosenCategory;

    private ArrayList<CardView> cardViews;

    private static DiffUtil.ItemCallback<String> DIFF_CALLBACK = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }
    };

    private CategoryAdapterCallBack callBackListener = null;

    public CategoryListAdapter(@Nullable String chosenCategory) {
        super(DIFF_CALLBACK);
        this.chosenCategory = chosenCategory;
        cardViews = new ArrayList<>();
    }

    public void setCallBackListener(CategoryAdapterCallBack listener)
    {
        this.callBackListener = listener;
    }

    @NonNull
    @Override
    public CategoryAdapterViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_category_list, parent,false);
        return new CategoryAdapterViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapterViewHold holder, int position) {

        if(position!=RecyclerView.NO_POSITION)
        {
            CardView cardView = (CardView) holder.itemView;
            if(!cardViews.contains(cardView))
            {
                cardViews.add(cardView);
            }

            holder.categoryNameTV.setText(getItem(position));

            if(getCurrentList().indexOf(getItem(position)) < PRESET_CATEGORY_NUMBER)
            {
                holder.deleteCategoryIV.setVisibility(View.GONE);
            }else
            {
                holder.deleteCategoryIV.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateCardViewsColours(CardView cardView)
    {
        for(CardView cv : cardViews)
        {
            if(cv.equals(cardView))
            {
                cv.setCardBackgroundColor(Color.RED);
            }else
            {
                cv.setCardBackgroundColor(Color.WHITE);
            }
        }
    }

    public class CategoryAdapterViewHold extends RecyclerView.ViewHolder{

        @BindView(R.id.adapter_category_list_category_tv)
        TextView categoryNameTV;
        @BindView(R.id.delete_category_iv)
        ImageView deleteCategoryIV;


        public CategoryAdapterViewHold(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            CardView cardView = (CardView) itemView;

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final int pos = getAdapterPosition();
                    chosenCategory = getItem(pos);
                    updateCardViewsColours(cardView);
                    if(callBackListener!=null)
                    {
                        callBackListener.onCategoryChosen(chosenCategory);
                    }
                }
            });

            deleteCategoryIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final int pos = getAdapterPosition();

                    if(callBackListener!=null)
                    {
                        callBackListener.onCategoryDelete(getItem(pos));
                    }
                }
            });
        }
    }

    public interface CategoryAdapterCallBack {
        void onCategoryDelete(String category);
        void onCategoryChosen(String category);
    }
}
