package amaz.objects.TwentyfourSeven.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.List;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.OrdersSection;
import amaz.objects.TwentyfourSeven.listeners.OnOrderClickListener;
import amaz.objects.TwentyfourSeven.viewholders.OrderChildViewHolder;
import amaz.objects.TwentyfourSeven.viewholders.OrderSectionViewHolder;

/**
 * Created by objects on 09/07/18.
 */

public class OrdersAdapter extends SectioningAdapter {

    private Context context;
    private List<OrdersSection> sections;

    private final int SECTION_VIEW_TYPE = 1;
    private final int EMPTY_VIEW_TYPE = 2;
    private OnOrderClickListener onOrderClickListener;

    public OrdersAdapter(Context context, List<OrdersSection> sections,OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.sections = sections;
        this.onOrderClickListener = onOrderClickListener;
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerUserType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        HeaderViewHolder vh;

    //  if (headerUserType == SECTION_VIEW_TYPE){
            view = inflater.inflate(R.layout.item_order_section_view,parent,false);
            vh = new OrderSectionViewHolder(view,context,onOrderClickListener);
            return vh;
//       }
//        else {
//            view = inflater.inflate(R.layout.item_empty_cell,parent,false);
//            vh = new EmptySectionViewHolder(view);
//            return vh;
//        }
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemUserType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;

            view = inflater.inflate(R.layout.item_my_order, parent, false);
            OrderChildViewHolder vh = new OrderChildViewHolder(view,context,onOrderClickListener);
            return vh;

    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int sectionIndex, int headerUserType) {
        if (!sections.isEmpty()){
            ((OrderSectionViewHolder)viewHolder).setData(sections.get(sectionIndex).getSectionTitle(),sections.get(sectionIndex).getChildItems().size(),sections.size());
        }
    }

    @Override
    public void onBindItemViewHolder(ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemUserType) {
        if (!sections.isEmpty()){
            if (sections.get(sectionIndex).getChildItems().size() > 0) {

                ((OrderChildViewHolder) viewHolder).setData(sections.get(sectionIndex).getChildItems().get(itemIndex));

            }else{
                ((OrderChildViewHolder) viewHolder).setData(sections.get(sectionIndex).getSectionTitle());

            }
        }

    }

    @Override
    public int getNumberOfSections() {

        return sections.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        if (!sections.isEmpty()){
            if (sections.get(sectionIndex).getChildItems().size() == 0) {
                return 1;
            } else {
                if (sectionIndex == 0) {
                    if (sections.get(sectionIndex).getChildItems().size() > 2) {
                        return 2;
                    } else {
                        return sections.get(sectionIndex).getChildItems().size();

                    }
                }
                return sections.get(sectionIndex).getChildItems().size();
            }
        }
        else {
            return 0;
        }
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    @Override
    public GhostHeaderViewHolder onCreateGhostHeaderViewHolder(ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_order_section_view, parent, false);
        return new GhostHeaderViewHolder(v);
    }


    @Override
    public int getSectionHeaderUserType(int sectionIndex) {

                  return SECTION_VIEW_TYPE;

    }

    @Override
    public int getItemViewUserType(int adapterPosition) {
        if (sections.get(adapterPosition).getChildItems().size() == 0) {
            return EMPTY_VIEW_TYPE;

        }else {
            return SECTION_VIEW_TYPE;
        }
    }
}
