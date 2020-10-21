package amaz.objects.TwentyfourSeven.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.Message;
import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.listeners.OnImageClickListener;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.viewholders.MessageViewHolder;

/**
 * Created by objects on 25/01/18.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Message> allMessages;
    private LocalSettings localSettings;
    private Order order;
    private Boolean fromCustomerOrders;
    private OnImageClickListener onImageClickListener;

    public ChatAdapter(Context context, ArrayList<Message> allMessages,Order order,boolean fromCustomerOrders,OnImageClickListener onImageClickListener){
        this.context = context;
        this.allMessages = allMessages;
        this.order = order;
        this.onImageClickListener =  onImageClickListener;
        this.fromCustomerOrders = fromCustomerOrders;
        localSettings = new LocalSettings(context);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        if(viewType==1){
            // change to sender
             view = inflater.inflate(R.layout.item_sender_chat,parent,false);

        }else if(viewType==3){
            view = inflater.inflate(R.layout.item_custom_chat,parent,false);

        }
        else{
             view = inflater.inflate(R.layout.item_receiver_chat,parent,false);

        }
        MessageViewHolder vh = new MessageViewHolder(view,context,onImageClickListener);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//       if (holder instanceof ImagesChatViewHolder){
//            ((ImagesChatViewHolder) holder).setData(allMessages.get(position));
//        }
//        else {
//        if (position == 0){
//            ((MessageViewHolder) holder).setData(order);
//
//        }else{
//            if (position > 0)
        if (position >0 && allMessages.get(position).getImages().size() >0){
            ((MessageViewHolder) holder).setImageData(allMessages.get(position),order,fromCustomerOrders);

        }else{
            ((MessageViewHolder) holder).setData(allMessages.get(position),order,fromCustomerOrders);

        }

      //  }
    //  }
    }

    @Override
    public int getItemCount() {
        return allMessages.size() ;
    }

    // types
    // images type 4
    // system type 3
    // text sender 1
    // text receiver 2
    @Override
    public int getItemViewType(int position) {
//        if (position == 0) {
//            if (localSettings.getUser().isDelegate()) {
//                return 2;
//            } else {
//                return 1;
//            }
//        } else {
        if (allMessages.get(position).getCreated_by() == 0){
            return 3;

        }else{
            if (allMessages.get(position ).getCreated_by() == localSettings.getUser().getId()) {
                return 1;
            } else {
                return 2;
            }
    }
    }


}
