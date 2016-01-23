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

import my.edu.chiawaikeith.canteenpos.Activities.EditReminder;
import my.edu.chiawaikeith.canteenpos.Domains.Reminders;
import my.edu.chiawaikeith.canteenpos.R;

/**
 * Created by win77 on 29/12/2015.
 */


public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    private List<Reminders> reminders;
    private Context context;
    private int itemLayout;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    public static final String KEY_REMINDER = "reminder";

    public ReminderAdapter(Context context, List<Reminders> reminders, int itemLayout) {
        this.context = context;
        this.reminders = reminders;
        this.itemLayout = itemLayout;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_reminder_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.reminder_Title.setText(reminders.get(position).getReminder_title());
        holder.reminder_Date.setText(dateFormat.format(reminders.get(position).getReminder_date()));
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView reminder_Title,reminder_Date;

//        ImageView personPhoto;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);

            reminder_Title = (TextView)itemView.findViewById(R.id.reminderTitle);
            reminder_Date = (TextView)itemView.findViewById(R.id.reminderDate);

            itemView.setOnClickListener(this);
            //personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }

        @Override
        public void onClick(View v) {
            Reminders reminder = new Reminders();
            reminder.setReminder_id(reminders.get(getAdapterPosition()).getReminder_id());
            reminder.setAcc_id(reminders.get(getAdapterPosition()).getAcc_id());
            reminder.setReminder_desc(reminders.get(getAdapterPosition()).getReminder_desc());
            reminder.setReminder_title(reminders.get(getAdapterPosition()).getReminder_title());
            reminder.setReminder_date(reminders.get(getAdapterPosition()).getReminder_date());
            reminder.setReminder_time(reminders.get(getAdapterPosition()).getReminder_time());

            Intent intent = new Intent(context, EditReminder.class);
            intent.putExtra(KEY_REMINDER, reminder);
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }
}
