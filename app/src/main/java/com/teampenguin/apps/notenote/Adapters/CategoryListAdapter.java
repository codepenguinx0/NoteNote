package com.teampenguin.apps.notenote.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.teampenguin.apps.notenote.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryListAdapter extends ListAdapter<String, CategoryListAdapter.CategoryAdapterViewHold> {

    private static final int PRESET_CATEGORY_NUMBER = 6;

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

    public CategoryListAdapter() {
        super(DIFF_CALLBACK);
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
            holder.categoryNameTV.setText(getItem(position));
            if(position < PRESET_CATEGORY_NUMBER)
            {
                holder.deleteCategoryIV.setVisibility(View.GONE);
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
    }
}
