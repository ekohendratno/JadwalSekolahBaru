package id.kopas.berkarya.jadwalsekolah;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.kopas.berkarya.jadwalsekolah.database.Jadwal;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Jadwal> jadwalList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public void add(Jadwal item) {
        jadwalList.add(item);
        notifyItemInserted(jadwalList.size());
    }

    public void addAll(List<Jadwal> item) {
        for (Jadwal items : item) {
            add(items);
        }
    }

    public void remove(Jadwal item) {
        int position = jadwalList.indexOf(item);
        if (position > -1) {
            jadwalList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }


    public Jadwal getItem(int position) {
        return jadwalList.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragmen_listitem, parent, false);
        return new JadwalViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof JadwalViewHolder) {
            JadwalViewHolder jadwalViewHolder = (JadwalViewHolder) holder;

            final Jadwal item = jadwalList.get(position);

            jadwalViewHolder.itemTime.setText(item.getMulai() + " - " + item.getSelesai());
            jadwalViewHolder.itemTitle.setText(item.getJadwal());
            jadwalViewHolder.itemRoom.setText(item.getRuang());
        }
    }

    @Override
    public int getItemCount() {
        return jadwalList == null ? 0 : jadwalList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }

    static class JadwalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView itemTime;
        public TextView itemTitle;
        public TextView itemRoom;
        OnItemClickListener onItemClickListener;

        public JadwalViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            itemTime = itemView.findViewById(R.id.time);
            itemTitle = itemView.findViewById(R.id.title);
            itemRoom = itemView.findViewById(R.id.room);

            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
