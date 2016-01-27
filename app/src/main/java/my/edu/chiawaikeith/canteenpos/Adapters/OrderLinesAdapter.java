package my.edu.chiawaikeith.canteenpos.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import my.edu.chiawaikeith.canteenpos.Domains.OrderLines;
import my.edu.chiawaikeith.canteenpos.R;

/**
 * Created by win77 on 2/1/2016.
 */
public class OrderLinesAdapter extends RecyclerView.Adapter<OrderLinesAdapter.ViewHolder>{
    private List<OrderLines> orderLines;
    //private List<Foods> foods;
    private Context context;
    private int itemLayout;

    public OrderLinesAdapter(Context context, List<OrderLines> orderLines,int itemLayout){
        this.context = context;
        this.orderLines = orderLines;
        //this.foods = foods;
        this.itemLayout = itemLayout;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_orderline_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.foodId.setText(String.valueOf(orderLines.get(position).getFood_id()));
        //holder.foodName.setText(foods.get(position).getFood_name());
        holder.itemqty.setText(String.valueOf(orderLines.get(position).getItem_qty()));
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView foodId,itemqty,foodName;

//        ImageView personPhoto;

        public ViewHolder(View itemView) {
            super(itemView);

            foodId = (TextView)itemView.findViewById(R.id.foodID);
            itemqty = (TextView)itemView.findViewById(R.id.itemQty);
            //foodName = (TextView)itemView.findViewById(R.id.foodName);

            //personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }
    }

    @Override
    public int getItemCount() {
        return orderLines.size();
    }
}
