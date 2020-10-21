package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentBaseResponse {

    @SerializedName("code")
    @Expose
    private int Code;

    @SerializedName("text")
    @Expose
    private String Text;

    @SerializedName("type")
    @Expose
    private int Type;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        this.Code = code;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        this.Text = text;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        this.Type = type;
    }

}
