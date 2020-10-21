package amaz.objects.TwentyfourSeven.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.CancelationReason;
import amaz.objects.TwentyfourSeven.utilities.Fonts;

public class CancelationReasonsAdapter extends ArrayAdapter<CancelationReason> {

    private TextView reasonTv;
    private View separatorV;

    private Context context;
    private ArrayList<CancelationReason> cancelationReasons;
    private Fonts fonts;

    public CancelationReasonsAdapter(@NonNull Context context, @NonNull ArrayList<CancelationReason> cancelationReasons) {
        super(context, R.layout.item_cancelation_reason, cancelationReasons);
        this.context = context;
        this.cancelationReasons = cancelationReasons;
        fonts = MApplication.getInstance().getFonts();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, parent, false);
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, parent, true);
    }

    public View getCustomView(int position, ViewGroup parent, boolean isDropDown) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_cancelation_reason, parent, false);
        reasonTv = v.findViewById(R.id.tv_reason);
        reasonTv.setTypeface(fonts.customFont());
        if (position == 0) {
            reasonTv.setTextColor(ContextCompat.getColor(context, R.color.line_gray));

        } else {
            reasonTv.setTextColor(ContextCompat.getColor(context, R.color.gray));
        }
        separatorV = v.findViewById(R.id.v_separator);
        if (isDropDown){
            reasonTv.setSingleLine(false);
            separatorV.setVisibility(View.VISIBLE);
        }
        else {
            reasonTv.setSingleLine(true);
            reasonTv.setEllipsize(TextUtils.TruncateAt.END);
            separatorV.setVisibility(View.GONE);
        }
        reasonTv.setText(cancelationReasons.get(position).getTitle());
        return v;

    }
}
