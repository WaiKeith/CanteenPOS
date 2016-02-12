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

    public OrderLinesAdapter(Context context, List<OrderLines> orderLines, int itemLayout){
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_cart_line, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        double qty,single,sub;

        qty = Double.parseDouble(String.valueOf(orderLines.get(position).getItem_qty()));
        single = Double.parseDouble(String.valueOf(orderLines.get(position).getSingle_price()));
        sub = qty * single;

        holder.foodName.setText(String.valueOf(orderLines.get(position).getFood_name()));
        holder.singleprice.setText("RM " + String.valueOf(orderLines.get(position).getSingle_price()));
        holder.itemqty.setText(String.valueOf(orderLines.get(position).getItem_qty()));
        holder.subtotal.setText("RM " + String.valueOf(sub));
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView foodName,itemqty,singleprice,subtotal;

//        ImageView personPhoto;

        public ViewHolder(View itemView) {
            super(itemView);

            foodName = (TextView)itemView.findViewById(R.id.foodName);
            itemqty = (TextView)itemView.findViewById(R.id.itemQty);
            singleprice = (TextView)itemView.findViewById(R.id.single);
            subtotal = (TextView)itemView.findViewById(R.id.subTotal);
        }
    }

    @Override
    public int getItemCount() {
        return orderLines.size();
    }
}
