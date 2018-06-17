package com.projects.juan.journeys.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projects.juan.journeys.R;
import com.projects.juan.journeys.models.Transaction;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by juan on 13/02/18.
 */

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Transaction> transactions;
    private int layout;
    private OnClickListener clickListener;

    public TransactionsAdapter(ArrayList<Transaction> transactions, int layout, OnClickListener clickListener) {
        this.transactions = transactions;
        this.layout = layout;
        this.clickListener = clickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(transactions.get(position), clickListener);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView coins;
        public TextView transaction_code;
        public TextView status;
        public TextView kind;


        public ViewHolder(View itemView) {
            super(itemView);
            transaction_code = (TextView) itemView.findViewById(R.id.code_transaction);
            coins = (TextView) itemView.findViewById(R.id.coins_transaction);
            status = (TextView) itemView.findViewById(R.id.status_transaction);
            kind = (TextView) itemView.findViewById(R.id.kind_transaction);
        }

        public void bind(final Transaction transaction, final OnClickListener clickListener){

            transaction_code.setText(transaction.getTransaction_code());
            coins.setText("Coins: " + transaction.getCoins());
            status.setText("Status: " + transaction.getStatus());
            kind.setText("Kind: " + transaction.getKind());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(transaction);
                }
            });
        }
    }

    public interface OnClickListener{
        void onClick(Transaction transaction);
    }

}
