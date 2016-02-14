package my.edu.chiawaikeith.canteenpos.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
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
    public CircleImageView foodpic;
    public Transactions transactions;

    public FoodAdapter(Context context, List<Foods> foods, int itemLayout) {
        this.context = context;
        this.foods = foods;
        this.itemLayout = itemLayout;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public DisplayImageOptions options = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

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
        if(foods.get(position).getFood_image_path() != ""){
            ImageLoader.getInstance().displayImage(foods.get(position).getFood_image_path(), foodpic, options);}
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView food_name,food_price;

//        ImageView personPhoto;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);

            food_name = (TextView)itemView.findViewById(R.id.foodName);
            food_price  = (TextView)itemView.findViewById(R.id.foodPrice);
            foodpic = (CircleImageView)itemView.findViewById(R.id.image_food);

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

            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }
}
