package aadithyabharadwaj.cs.niu.covid19.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aadithyabharadwaj.cs.niu.covid19.EachStateDataActivity;
import aadithyabharadwaj.cs.niu.covid19.Models.StateWiseModel;
import aadithyabharadwaj.cs.niu.covid19.R;

import static aadithyabharadwaj.cs.niu.covid19.Constants.STATE_ACTIVE;
import static aadithyabharadwaj.cs.niu.covid19.Constants.STATE_CONFIRMED;
import static aadithyabharadwaj.cs.niu.covid19.Constants.STATE_CONFIRMED_NEW;
import static aadithyabharadwaj.cs.niu.covid19.Constants.STATE_NAME;
import static aadithyabharadwaj.cs.niu.covid19.Constants.STATE_DEATH;
import static aadithyabharadwaj.cs.niu.covid19.Constants.STATE_DEATH_NEW;
import static aadithyabharadwaj.cs.niu.covid19.Constants.STATE_RECOVERED;
import static aadithyabharadwaj.cs.niu.covid19.Constants.STATE_RECOVERED_NEW;
import static aadithyabharadwaj.cs.niu.covid19.Constants.STATE_LAST_UPDATE;

public class StateWiseAdapter extends RecyclerView.Adapter<StateWiseAdapter.ViewHolder>
{

    Context mContext;
    private ArrayList<StateWiseModel> stateWiseModelArrayList;
    private String searchText = "";
    private SpannableStringBuilder spannableStringBuilder;

    public StateWiseAdapter(Context mContext, ArrayList<StateWiseModel> stateWiseModelArrayList)
    {
        this.mContext = mContext;
        this.stateWiseModelArrayList = stateWiseModelArrayList;
    } // End StateWiseAdapter Constructor Class

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.statewise_layout, parent, false);
        return new ViewHolder(view);
    } // End onCreateViewHolder

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {
        StateWiseModel currentItem = stateWiseModelArrayList.get(position);
        String stateName = currentItem.getState();
        String stateTotal = currentItem.getConfirmed();
        holder.stateTotalCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(stateTotal)));
        //holder.stateTotalCasesTextView.setText(stateTotal);
        holder.stateNameTextView.setText(stateName);
        if(searchText.length()>0)
        {
            //color your text here
            spannableStringBuilder = new SpannableStringBuilder(stateName);
            Pattern word = Pattern.compile(searchText.toLowerCase());
            Matcher match = word.matcher(stateName.toLowerCase());
            while(match.find())
            {
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(52, 195, 235)); //specify color here
                spannableStringBuilder.setSpan(fcs, match.start(), match.end(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            } // End while
            holder.stateNameTextView.setText(spannableStringBuilder);

        } // End if
        else
        {
            holder.stateNameTextView.setText(stateName);
        } // End else
        holder.stateWiseLinearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                StateWiseModel clickedItem = stateWiseModelArrayList.get(position);
                Intent perStateIntent = new Intent(mContext, EachStateDataActivity.class);
                perStateIntent.putExtra(STATE_NAME, clickedItem.getState());
                perStateIntent.putExtra(STATE_CONFIRMED, clickedItem.getConfirmed());
                perStateIntent.putExtra(STATE_CONFIRMED_NEW, clickedItem.getConfirmed_new());
                perStateIntent.putExtra(STATE_ACTIVE, clickedItem.getActive());
                perStateIntent.putExtra(STATE_DEATH, clickedItem.getDeath());
                perStateIntent.putExtra(STATE_DEATH_NEW, clickedItem.getDeath_new());
                perStateIntent.putExtra(STATE_RECOVERED, clickedItem.getRecovered());
                perStateIntent.putExtra(STATE_RECOVERED_NEW, clickedItem.getRecovered_new());
                perStateIntent.putExtra(STATE_LAST_UPDATE, clickedItem.getLastupdate());

                mContext.startActivity(perStateIntent);

            } // End onClick
        });
    } // End onBindViewHolder

    public void filterList(ArrayList<StateWiseModel> filteredList, String text)
    {
        stateWiseModelArrayList = filteredList;
        this.searchText = text;
        notifyDataSetChanged();
    } // End filterList

    @Override
    public int getItemCount()
    {
        return stateWiseModelArrayList==null ? 0 : stateWiseModelArrayList.size();
    } // End getItemsCount

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView stateNameTextView, stateTotalCasesTextView;
        LinearLayout stateWiseLinearLayout;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            stateNameTextView = itemView.findViewById(R.id.statewiseLayoutNameTextView);
            stateTotalCasesTextView = itemView.findViewById(R.id.statewiseLayoutConfirmedTextView);
            stateWiseLinearLayout = itemView.findViewById(R.id.statewiseLinearLayout);
        } // End ViewHolder
    } // End ViewHolder extends RecyclerView.ViewHolder
} // End StateWiseAdapter extends RecyclerView.Adapter<StateWiseAdapter.ViewHolder>
