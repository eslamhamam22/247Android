package amaz.objects.TwentyfourSeven.data.models;

import java.util.ArrayList;

/**
 * Created by objects on 09/05/18.
 */

public class Message {

    private int id = 0;
    private MessageChat msg ;
    private ArrayList<ImageChat> images = new ArrayList<>();
    private int recipient_type = 0;
    private int created_by = 0;
    private long created_at = 0;
   // private MessageChat chat;

    public MessageChat getMsg() {
        return msg;
    }

    public void setMsg(MessageChat msg) {
        this.msg = msg;
    }

    public ArrayList<ImageChat> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageChat> images) {
        this.images = images;
    }

    public Message(){

    }

    public int getRecipient_type() {
        return recipient_type;
    }

    public void setRecipient_type(int recipient_type) {
        this.recipient_type = recipient_type;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

//    public MessageChat getMessage() {
//        return msg;
//    }
//
//    public void setMessage(MessageChat message) {
//        this.msg = message;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
