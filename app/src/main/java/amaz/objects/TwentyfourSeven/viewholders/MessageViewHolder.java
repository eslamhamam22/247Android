package amaz.objects.TwentyfourSeven.viewholders;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.ChatImageAdapter;
import amaz.objects.TwentyfourSeven.api.APIURLs;
import amaz.objects.TwentyfourSeven.data.models.ImageChat;
import amaz.objects.TwentyfourSeven.data.models.ImageSizes;
import amaz.objects.TwentyfourSeven.data.models.Message;
import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.listeners.OnImageClickListener;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.ValidationsUtilities;


public class MessageViewHolder extends RecyclerView.ViewHolder {

    private TextView messageTv,messageDateTv;
    private LocalSettings localSettings;
    private ArrayList<ImageChat> images = new ArrayList<>();
    private ChatImageAdapter chatImageAdapter;
    private Context context;
    private RecyclerView images_list;
    private Fonts fonts;
    private ImageView profileImage,imageUpload;
    private OnImageClickListener onImageClickListener;
    private Message message = new Message();

    public MessageViewHolder(View itemView, Context context,OnImageClickListener onImageClickListener) {
        super(itemView);
        this.context = context;
        localSettings  =new  LocalSettings(context);
        this.onImageClickListener = onImageClickListener;
        initViews();
        setFonts();

    }

    private void initViews(){
        messageTv = itemView.findViewById(R.id.messageTxt);
        images_list = itemView.findViewById(R.id.images_list);
        messageDateTv = itemView.findViewById(R.id.time);
        profileImage = itemView.findViewById(R.id.profile_image);
        imageUpload = itemView.findViewById(R.id.image_upload);
        images_list.setLayoutManager(new GridLayoutManager(context,3));
        chatImageAdapter = new ChatImageAdapter(context,images,onImageClickListener);
        images_list.setAdapter(chatImageAdapter);
        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageSizes imageSizes = new ImageSizes();
                imageSizes.setBig(message.getImages().get(0).url);
                onImageClickListener.onImageClick(imageSizes);
            }
        });

    }
    private void setFonts(){
        fonts = MApplication.getInstance().getFonts();
        messageTv.setTypeface(fonts.customFont());
        messageDateTv.setTypeface(fonts.customFont());

    }

    public void setData(Message message,Order order,boolean fromCustomerOrders){


        this.message = message;

        if (message.getCreated_by() == 0){
            if (localSettings.getLocale().equals("ar")){
                if (message.getMsg() != null) {
                    messageTv.setText(message.getMsg().ar);
                }

            }else{
                if (message.getMsg() != null) {

                    messageTv.setText(message.getMsg().en);
                }

            }
        }else{
            if (message.getMsg() != null){
                messageTv.setText(message.getMsg().no_locale);
                messageTv.setVisibility(View.VISIBLE);


            }else{
                messageTv.setVisibility(View.GONE);

            }

            if (message.getImages().size() > 0){
                images.clear();
                images.addAll(message.getImages());
                chatImageAdapter.notifyDataSetChanged();
                images_list.setVisibility(View.VISIBLE);

            }else{
                images_list.setVisibility(View.GONE);
            }


            if (message.getCreated_by() == localSettings.getUser().getId()) {
                if (localSettings.getUser().getImage() != null) {

                    Picasso.with(context).load(localSettings.getUser().getImage().getMedium()).placeholder(R.drawable.avatar)
                            .error(R.drawable.avatar).into(profileImage);
                }
            }else{
                if (!fromCustomerOrders){
                    if (order.getCreatedBy().getImage() != null) {

                        Picasso.with(context).load(order.getCreatedBy().getImage().getMedium()).placeholder(R.drawable.avatar)
                                .error(R.drawable.avatar).into(profileImage);
                    }
                }else{
                    if (order.getDelegate().getImage() != null) {
                        Picasso.with(context).load(order.getDelegate().getImage().getMedium()).placeholder(R.drawable.avatar)
                                .error(R.drawable.avatar).into(profileImage);
                    }
                }

            }


        }
        imageUpload.setVisibility(View.GONE);
        messageDateTv.setText(ValidationsUtilities.getDateFromChat(message.getCreated_at()));

    }
    public void setImageData(Message message,Order order,boolean fromCustomerOrders){
        this.message = message;

        messageTv.setVisibility(View.GONE);
        images_list.setVisibility(View.GONE);
        if (message.getImages().size() > 0){
            if(itemView.getContext() != null && !((AppCompatActivity)itemView.getContext()).isDestroyed()
            && !((AppCompatActivity)itemView.getContext()).isFinishing()){
                Glide.with(itemView.getContext()).load(message.getImages().get(0).url)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.louding_img)
                                .dontAnimate())
                        .into(imageUpload);
                //.placeholder(R.drawable.louding_img).dontAnimate().into(imageUpload);
                imageUpload.setVisibility(View.VISIBLE);
            }
        }else{
            imageUpload.setVisibility(View.GONE);
        }
        if (message.getCreated_by() == localSettings.getUser().getId()) {
            if (localSettings.getUser().getImage() != null) {
                Picasso.with(context).load(localSettings.getUser().getImage().getMedium()).placeholder(R.drawable.avatar)
                        .error(R.drawable.avatar).into(profileImage);
            }
        }else{
            if (!fromCustomerOrders){
                if (order.getCreatedBy().getImage() != null) {

                    Picasso.with(context).load(order.getCreatedBy().getImage().getMedium()).placeholder(R.drawable.avatar)
                            .error(R.drawable.avatar).into(profileImage);
                }
            }else{
                if (order.getDelegate().getImage() != null) {
                    Picasso.with(context).load(order.getDelegate().getImage().getMedium()).placeholder(R.drawable.avatar)
                            .error(R.drawable.avatar).into(profileImage);
                }
            }

        }


        messageDateTv.setText(ValidationsUtilities.getDateFromChat(message.getCreated_at()));

    }
    public void setData(Order order){
        if (order.getImages().size() > 0){
            images.clear();
            for (int i = 0;i<order.getImages().size();i++) {
                ImageChat imageChat = new ImageChat();
                imageChat.url = order.getImages().get(i).getImage().getSmall();
                images.add(imageChat);
            }
            chatImageAdapter.notifyDataSetChanged();
            images_list.setVisibility(View.VISIBLE);

        }else{
            images_list.setVisibility(View.GONE);
        }

        messageTv.setText(order.getDescription());

        if((order.getCreatedBy() != null)) {
            if (order.getCreatedBy().getImage() != null) {
                Picasso.with(context).load(order.getCreatedBy().getImage().getMedium()).placeholder(R.drawable.avatar)
                        .error(R.drawable.avatar).into(profileImage);
            }
        }

    }
}
