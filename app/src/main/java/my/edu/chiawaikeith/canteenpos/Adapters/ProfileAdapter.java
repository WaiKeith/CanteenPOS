package my.edu.chiawaikeith.canteenpos.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import my.edu.chiawaikeith.canteenpos.Activities.EditProfile;
import my.edu.chiawaikeith.canteenpos.Domains.Accounts;
import my.edu.chiawaikeith.canteenpos.Domains.OfflineLogin;
import my.edu.chiawaikeith.canteenpos.R;

/**
 * Created by win77 on 15/12/2015.
 */
public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {

    int itemLayout;
    Context context;
    List<Accounts> items;
    OfflineLogin offlineLogin;

    public ProfileAdapter(Context context, List<Accounts> items, int itemLayout) {
        this.context=context;
        this.items = items;
        this.itemLayout = itemLayout;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");

//        holder.userId.setText(items.get(position).getAcc_id() + "");
//        holder.userName.setText(items.get(position).getUser_name());
//        holder.accBalance.setText(items.get(position).getAcc_balance() + "");
//        holder.registerdate.setText(dateFormat.format(items.get(position).getRegister_date()));

        holder.userId.setText(offlineLogin.getCust_id());
        holder.userName.setText(offlineLogin.getUser_name());
        holder.accBalance.setText(String.valueOf(offlineLogin.getAcc_balance()));
        holder.registerdate.setText(String.valueOf(offlineLogin.getRegister_date()));
        Log.d("ProfileAdapter","a");
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView userId, userName, accBalance, registerdate;

        public MyViewHolder(final View itemView) {
            super(itemView);
            userId = (TextView) itemView.findViewById(R.id.cust_Id);
            userName = (TextView) itemView.findViewById(R.id.user_Name);
            accBalance = (TextView) itemView.findViewById(R.id.acc_Balance);
            registerdate = (TextView) itemView.findViewById(R.id.register_Date);


        }

        @Override
        public void onClick(View view) {
            Accounts accounts = new Accounts();
            accounts.setUser_name(items.get(getAdapterPosition()).getUser_name());
            accounts.setAcc_password(items.get(getAdapterPosition()).getAcc_password());

            // pass news data to view college news activity
            Intent intent = new Intent(context, EditProfile.class);
            //intent.putExtra(KEY_NEWS_DATA, accounts);
            context.startActivity(intent);


        }
    }
}