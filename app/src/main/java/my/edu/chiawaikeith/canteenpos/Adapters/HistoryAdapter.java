package my.edu.chiawaikeith.canteenpos.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import my.edu.chiawaikeith.canteenpos.Activities.OrderDetails;
import my.edu.chiawaikeith.canteenpos.Domains.Transactions;
import my.edu.chiawaikeith.canteenpos.R;

/**
 * Created by win77 on 2/1/2016.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    private List<Transactions> orders;
    private Context context;
    private int itemLayout;
    public SimpleDateFormat mySqlDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    public static final String KEY_HISTORY = "history";

    public HistoryAdapter(Context context, List<Transactions> orders,int itemLayout){
        this.context = context;
        this.orders = orders;
        this.itemLayout = itemLayout;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_order_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.paymentAmt.setText(String.valueOf(orders.get(position).getPayment_amount()));
        holder.orderDate.setText(mySqlDateFormat.format(orders.get(position).getOrder_date_time()));
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView paymentAmt,orderDate;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);

            paymentAmt = (TextView)itemView.findViewById(R.id.paymentAmt);
            orderDate = (TextView)itemView.findViewById(R.id.orderDateTime);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Transactions transaction = new Transactions();

            transaction.setTransac_id(orders.get(getAdapterPosition()).getTransac_id());
            transaction.setAcc_id(orders.get(getAdapterPosition()).getAcc_id());
            transaction.setPayment_amount(orders.get(getAdapterPosition()).getPayment_amount());
            transaction.setTotal_gst(orders.get(getAdapterPosition()).getTotal_gst());
            transaction.setOrder_date_time(orders.get(getAdapterPosition()).getOrder_date_time());
            transaction.setOrder_status(orders.get(getAdapterPosition()).getOrder_status());

            Intent intent = new Intent(context, OrderDetails.class);
            intent.putExtra(KEY_HISTORY, transaction);
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void clearAdapter() {
        orders.clear();
        notifyDataSetChanged();
    }
}
