package my.edu.chiawaikeith.canteenpos.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import my.edu.chiawaikeith.canteenpos.Activities.FoodDetails;
import my.edu.chiawaikeith.canteenpos.Domains.Foods;
import my.edu.chiawaikeith.canteenpos.Domains.Transactions;
import my.edu.chiawaikeith.canteenpos.R;

/**
 * Created by win77 on 29/12/2015.
 */


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    private List<Foods> foods;
    private Context context;
    private int itemLayout;
    private String transacID;
    //public SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    public static final String KEY_FOOD = "food";
    public Transactions transactions;
    public static final String KEY_TRANSAC = "transaction";

    public FoodAdapter(Context context, List<Foods> foods, int itemLayout) {
        this.context = context;
        this.foods = foods;
        this.itemLayout = itemLayout;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_food_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.food_name.setText(foods.get(position).getFood_name());
        holder.food_price.setText("RM " + String.valueOf(foods.get(position).getFood_price()));
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView food_name,food_price;
        Button viewInfo;

//        ImageView personPhoto;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);

            food_name = (TextView)itemView.findViewById(R.id.foodName);
            food_price  = (TextView)itemView.findViewById(R.id.foodPrice);
            viewInfo = (Button)itemView.findViewById(R.id.btnView);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Foods food = new Foods();
            food.setFood_id(foods.get(getAdapterPosition()).getFood_id());
            food.setFood_name(foods.get(getAdapterPosition()).getFood_name());
            food.setFood_category(foods.get(getAdapterPosition()).getFood_category());
            food.setFood_price(foods.get(getAdapterPosition()).getFood_price());
            food.setFood_gst_price(foods.get(getAdapterPosition()).getFood_gst_price());
            food.setStall_id(foods.get(getAdapterPosition()).getStall_id());
            food.setFood_image_path(foods.get(getAdapterPosition()).getFood_image_path());

            Intent intent = new Intent(context, FoodDetails.class);
            intent.putExtra(KEY_FOOD, food);
//
//            transacID = String.valueOf(transactions.getTransac_id());
//            intent.putExtra(KEY_TRANSAC, transacID);
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }
}
