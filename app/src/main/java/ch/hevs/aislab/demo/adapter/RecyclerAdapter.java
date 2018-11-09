package ch.hevs.aislab.demo.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.database.entity.ComputerEntity;
import ch.hevs.aislab.demo.database.entity.RoomEntity;
import ch.hevs.aislab.demo.database.entity.StudentEntity;
import ch.hevs.aislab.demo.util.RecyclerViewItemClickListener;

public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;
    private List<T> mData;
    // private List<T> mData;
    private RecyclerViewItemClickListener mListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        View itemView;

        private TextView itemTitle;
        private TextView itemDetail;
        private ImageView itemImage;

        ViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemDetail = itemView.findViewById(R.id.item_detail);
            itemImage = itemView.findViewById(R.id.item_image);
        }
    }

    public RecyclerAdapter(RecyclerViewItemClickListener listener, Context context) {
        mListener = listener;
        this.context = context;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(view -> mListener.onItemClick(view, viewHolder.getAdapterPosition()));
        v.setOnLongClickListener(view -> {
            mListener.onItemLongClick(view, viewHolder.getAdapterPosition());
            return true;
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        T item = mData.get(position);

        if (item.getClass().equals(RoomEntity.class)) {
            String label = "Room: " + ((RoomEntity) item).getLabel();
            holder.itemTitle.setText(label);
            String nbOfPlaces = "Places: " + Integer.toString(((RoomEntity) item).getNbOfPlaces());
            holder.itemDetail.setText(nbOfPlaces);
            holder.itemImage.setImageDrawable(context.getDrawable(R.drawable.ic_room));
        } else if (item.getClass().equals(StudentEntity.class)) {
            holder.itemTitle.setText(((StudentEntity) item).getLastName());
            holder.itemDetail.setText(((StudentEntity) item).getFirstName());
            holder.itemImage.setImageDrawable(context.getDrawable(R.drawable.ic_student_round));
        } else if (item.getClass().equals(ComputerEntity.class)) {
            holder.itemTitle.setText(((ComputerEntity) item).getLabel());
            holder.itemDetail.setText(((ComputerEntity) item).getDescription());
            holder.itemImage.setImageDrawable(context.getDrawable(R.drawable.ic_computer_round));
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }

        public class ItemInfo { // TODO: Get from model
        protected String itemTitle;
        protected String itemDetail;
        protected int itemImage;
        protected String id;

        public String getId() {
            return this.id;
        }
    }

    public void setData(final List<T> data) {

        if (mData == null) {
            mData = data;
            notifyItemRangeInserted(0, data.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mData.size();
                }

                @Override
                public int getNewListSize() {
                    return data.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    if (mData instanceof RoomEntity) {
                        return ((ItemInfo) mData.get(oldItemPosition)).getId().equals(((ItemInfo) data.get(newItemPosition)).getId());
                    }
                    return false;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    if (mData instanceof RoomEntity) {
                        RoomEntity newRoom = (RoomEntity) data.get(newItemPosition);
                        RoomEntity oldRoom = (RoomEntity) mData.get(newItemPosition);
                        return newRoom.getId().equals(oldRoom.getId())
                                && Objects.equals(newRoom.getLabel(), oldRoom.getLabel())
                                && Objects.equals(newRoom.getNbOfPlaces(), oldRoom.getNbOfPlaces());
                    }
                    return false;
                }
            });
            mData = data;
            result.dispatchUpdatesTo(this);
        }
    }
}
