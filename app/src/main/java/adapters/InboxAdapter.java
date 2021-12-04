package adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hrapps.R;

import java.util.ArrayList;
import java.util.HashMap;

import DB.DbUtil;
import Model.AttachmentModel;
import Model.ConfigModel;
import Model.FeedsDataModel;
import Utility.Configuration;
import Utility.MaterialImageView;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ParentViewHolder> {
    private ArrayList<FeedsDataModel> _feedsList = new ArrayList<FeedsDataModel>();
    private ArrayList<FeedsDataModel> originalList = new ArrayList<FeedsDataModel>();
    private ArrayList<FeedsDataModel> itemsPendingRemoval = new ArrayList<FeedsDataModel>();


    private LayoutInflater _linflater = null;
    String perspective = "";
    private Context _context = null;

    private boolean undoOn;
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<FeedsDataModel, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be
    private static final int PENDING_REMOVAL_TIMEOUT = 5000; // 3sec


    public InboxAdapter(Context context, ArrayList<FeedsDataModel> stateList, String perspect) {
        _context = context;
        _feedsList.clear();
        _feedsList = stateList;
        originalList.addAll(_feedsList);
        perspective = perspect;
        _linflater = LayoutInflater.from(context);
    }


    @Override
    public ParentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_type, parent, false);
        return new ParentViewHolder(itemLayoutView);
    }

    ;

    @Override
    public void onBindViewHolder(ParentViewHolder parentHolder, int position) {


        //Get configured Card for type and subtype
        ConfigModel card = new DbUtil().getConfiguredCard(_feedsList.get(position).type, _feedsList.get(position).subtype, _context);
        ViewHolder viewHolder = null;
        View childView = null;
        boolean childExists = false;
        int xml = 0;
        Configuration config = Configuration.getConfigInstance();
        config.setperspective(perspective);
        xml = config.getXml(card.id);
        if (_feedsList.get(position).type.equalsIgnoreCase("Champion Score Card")) {
            xml = config.getXml("csc_approval");
            config.approvalType("csc");
        } else if (_feedsList.get(position).type.equalsIgnoreCase("Performance Management System") && _feedsList.get(position).subtype.equalsIgnoreCase("Appraisal Template Approve")) {
            xml = config.getXml("csc_approval");
            config.approvalType("pms");
        } else if (_feedsList.get(position).type.equalsIgnoreCase("Performance Management System") && _feedsList.get(position).subtype.equalsIgnoreCase("Goal Setting")) {
//            xml = config.getXml("pms_goalSetting_approval");
//            config.approvalType("goalsetting");
        } else if (_feedsList.get(position).type.equalsIgnoreCase("Recruitment") && _feedsList.get(position).subtype.equalsIgnoreCase("Requisition")) {
            xml = config.getXml("recruitment_requisition_approval");
        } else if (_feedsList.get(position).type.equalsIgnoreCase("Recruitment") && _feedsList.get(position).subtype.equalsIgnoreCase("updateRequisition")) {
            xml = config.getXml("update_requisition");
        } else if (_feedsList.get(position).type.equalsIgnoreCase("Recruitment") && _feedsList.get(position).subtype.equalsIgnoreCase("jobApplicationException")) {
            xml = config.getXml("recruitment_requisition_approval");
        } else if (_feedsList.get(position).type.equalsIgnoreCase("Recruitment") && _feedsList.get(position).subtype.equalsIgnoreCase("offerRollout")) {
            xml = config.getXml("recruitment_requisition_approval");
        } else if (_feedsList.get(position).type.equalsIgnoreCase("Recruitment") && _feedsList.get(position).subtype.equalsIgnoreCase("payRefferalBonus")) {
            xml = config.getXml("recruitment_requisition_approval");
        } else if (_feedsList.get(position).type.equalsIgnoreCase("Curriculum Approval Template")) {
            xml = config.getXml("lms_curriculum_approval");
            if (card.id.isEmpty()) {
                card.id = "Curriculum Approval Template";
            }
        }


        for (int childCount = 0; childCount < parentHolder.parent.getChildCount(); childCount++) {
            View child = parentHolder.parent.getChildAt(childCount);
            if (((ViewHolder) child.getTag()).cardId.equalsIgnoreCase(card.id) && ((ViewHolder) child.getTag()).perspective.equalsIgnoreCase(perspective)) {
                childExists = true;
                child.setVisibility(View.VISIBLE);
                childView = child;
                viewHolder = (ViewHolder) child.getTag();

            } else {
                child.setVisibility(View.GONE);
            }

        }
        if (!childExists) {
            if (card != null) {
                xml = config.getXml(card.id);
                if (xml != 0) {
                    childView = _linflater.inflate(xml, null);

                } else if (_feedsList.get(position).type.equalsIgnoreCase("Leave") && _feedsList.get(position).subtype.equalsIgnoreCase("Approve")) {

                    xml = config.getXml("Leave_LeaveHistory_002");
                    if (xml != 0) {
                        childView = _linflater.inflate(xml, null);

                    } else {
                        childView = _linflater.inflate(R.layout.no_type, null);
                    }
                } else if (_feedsList.get(position).subtype.equalsIgnoreCase("BPM Task")) {

                    xml = config.getXml("po_pr_001");
                    if (xml != 0) {
                        childView = _linflater.inflate(xml, null);

                    } else {
                        childView = _linflater.inflate(R.layout.no_type, null);
                    }
                } else if (_feedsList.get(position).type.equalsIgnoreCase("Champion Score Card")) {

                    xml = config.getXml("csc_approval");
                    if (xml != 0) {
                        childView = _linflater.inflate(xml, null);

                    } else {
                        childView = _linflater.inflate(R.layout.no_type, null);
                    }
                } else if (_feedsList.get(position).type.equalsIgnoreCase("Performance Management System") && _feedsList.get(position).subtype.equalsIgnoreCase("Appraisal Template Approve")) {

                    xml = config.getXml("csc_approval");
                    if (xml != 0) {
                        childView = _linflater.inflate(xml, null);

                    } else {
                        childView = _linflater.inflate(R.layout.no_type, null);
                    }
                } else if (_feedsList.get(position).type.equalsIgnoreCase("Recruitment") && _feedsList.get(position).subtype.equalsIgnoreCase("Requisition")) {
                    xml = config.getXml("recruitment_requisition_approval");
                    if (xml != 0) {
                        childView = _linflater.inflate(xml, null);
                    } else {
                        childView = _linflater.inflate(R.layout.no_type, null);
                    }
                } else if (_feedsList.get(position).type.equalsIgnoreCase("Recruitment") && _feedsList.get(position).subtype.equalsIgnoreCase("updateRequisition")) {
                    xml = config.getXml("update_requisition");
                    if (xml != 0) {
                        childView = _linflater.inflate(xml, null);
                    } else {
                        childView = _linflater.inflate(R.layout.no_type, null);
                    }
                } else if (_feedsList.get(position).type.equalsIgnoreCase("Recruitment") && _feedsList.get(position).subtype.equalsIgnoreCase("jobApplicationException")) {
                    xml = config.getXml("recruitment_requisition_approval");
                    if (xml != 0) {
                        childView = _linflater.inflate(xml, null);
                    } else {
                        childView = _linflater.inflate(R.layout.no_type, null);
                    }
                } else if (_feedsList.get(position).type.equalsIgnoreCase("Recruitment") && _feedsList.get(position).subtype.equalsIgnoreCase("offerRollout")) {
                    xml = config.getXml("recruitment_requisition_approval");
                    if (xml != 0) {
                        childView = _linflater.inflate(xml, null);
                    } else {
                        childView = _linflater.inflate(R.layout.no_type, null);
                    }
                } else if (_feedsList.get(position).type.equalsIgnoreCase("Recruitment") && _feedsList.get(position).subtype.equalsIgnoreCase("payRefferalBonus")) {
                    xml = config.getXml("recruitment_requisition_approval");
                    if (xml != 0) {
                        childView = _linflater.inflate(xml, null);
                    } else {
                        childView = _linflater.inflate(R.layout.no_type, null);
                    }
                } else if (_feedsList.get(position).type.equalsIgnoreCase("Curriculum Approval Template") && _feedsList.get(position).subtype.equalsIgnoreCase("Curriculum Approval Template")) {
                    // LMS - change xml
                    xml = config.getXml("lms_curriculum_approval");
                    if (xml != 0) {
                        childView = _linflater.inflate(xml, null);
                    } else {
                        childView = _linflater.inflate(R.layout.no_type, null);
                    }
                } else {
                    childView = _linflater.inflate(R.layout.no_type, null);
                }
            }

            viewHolder = new ViewHolder(card.id, perspective, childView);
            childView.setTag(viewHolder);
            parentHolder.parent.addView(childView);


        }


        if (xml != 0 && viewHolder != null && childView != null)
            config.configureXml(xml, _context, childView, _feedsList.get(position), card, viewHolder);

        if (itemsPendingRemoval.contains(_feedsList.get(position))) {
            parentHolder.parent.setVisibility(View.VISIBLE);
//            parentHolder.swipeLayout.setVisibility(View.VISIBLE);
//            parentHolder.undo.setOnClickListener(new UndoClicked( _feedsList.get(position)));

        } else {
            parentHolder.parent.setVisibility(View.VISIBLE);
            parentHolder.swipeLayout.setVisibility(View.GONE);
        }


    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public int getItemCount() {
        return _feedsList.size();
    }

   /* @Override

    public View getView(final int position, View view, ViewGroup parent) {


        //Get configured Card for type and subtype
        ConfigModel card = new DbUtil().getConfiguredCard(_feedsList.get(position).type, _feedsList.get(position).subtype, _context);
        ViewHolder viewHolder = null;
        ParentViewHolder parentHolder;
        View childView = null;
        boolean childExists=false;
        int xml = 0;
        Configuration config = Configuration.getConfigInstance();
        config.setperspective(perspective);
        xml = config.getXml(card.id);
        if (_feedsList.get(position).type.equalsIgnoreCase("Leave") && _feedsList.get(position).subtype.equalsIgnoreCase("Approve")) {

            xml = config.getXml("Leave_LeaveHistory_002");
        }

        if(view==null) {
            view = _linflater.inflate(R.layout.no_type, null);
            parentHolder = new ParentViewHolder(view);
            parentHolder.parent= (LinearLayout)view.findViewById(R.id.parent_layout);
            parentHolder.swipeLayout =(LinearLayout)view.findViewById(R.id.swiped_layout);
            view.setTag(parentHolder);
        }
        else{
            parentHolder = (ParentViewHolder) view.getTag();
        }

        for(int childCount=0;childCount<parentHolder.parent.getChildCount();childCount++){
            View child=parentHolder.parent.getChildAt(childCount);
            if(((ViewHolder)child.getTag()).cardId.equalsIgnoreCase(card.id)&&((ViewHolder)child.getTag()).perspective.equalsIgnoreCase(perspective)){
                childExists=true;
                child.setVisibility(View.VISIBLE);
                childView = child;
                viewHolder = (ViewHolder)child.getTag();

            }
            else{
                child.setVisibility(View.GONE);
            }

        }
        if(!childExists){
            if (card != null) {
                xml = config.getXml(card.id);
                if (xml != 0) {
                    childView = _linflater.inflate(xml, null);

                } else if (_feedsList.get(position).type.equalsIgnoreCase("Leave") && _feedsList.get(position).subtype.equalsIgnoreCase("Approve")) {

                    xml = config.getXml("Leave_LeaveHistory_002");
                    if (xml != 0) {
                        childView = _linflater.inflate(xml, null);

                    } else {
                        childView = _linflater.inflate(R.layout.no_type, null);
                    }
                } else {
                    childView = _linflater.inflate(R.layout.no_type, null);
                }
            }

            viewHolder=new ViewHolder(card.id,perspective,childView);
            childView.setTag(viewHolder);
            parentHolder.parent.addView(childView);



        }


        if(xml != 0&&viewHolder!=null&&childView!=null)
            config.configureXml(xml, _context, childView, _feedsList.get(position), card,viewHolder);

        if(position == swipedPosition){
            parentHolder.parent.setVisibility(View.GONE);
            parentHolder.swipeLayout.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipedPosition = -1;
                    _feedsList.remove(position);
                    notifyDataSetChanged();
                }
            }, 3000);
        }
        else{
            parentHolder.parent.setVisibility(View.VISIBLE);
            parentHolder.swipeLayout.setVisibility(View.GONE);
        }

        return view;
    }
*/

    /* @Override
     public void notifyDataSetChanged() {
         // TODO Auto-generated method stub
         super.notifyDataSetChanged();
     }*/
    public void setPerspective(String perspect) {
        this.perspective = perspect;

    }

    public class ViewHolder {
        public LinearLayout action3Linear;
        public ArrayList<AttachmentModel> attachmentsList;
        public TextView title, description, action, action_time, sourceDate, source, name, time, commentCount, commentedBy, comment, firstName, createdDate, likeCount, exit_from_feed;
        public RelativeLayout taskLayout, userMessageLayout, newsImageLayout, header, commentLayout, latestActivity, newsLayout, likeRelative, newUserLayout;
        public MaterialImageView image;
        public EditText taskType;
        public MaterialImageView commentPic;
        public ImageView likeImage;
        public LinearLayout commentLinear;
        public Spinner moreOptions;
        public TextView attachmentCount, action1, action3, likeText, attach5text;
        public ImageView attach1, attach2, attach3, line2, single, publicFeed, newsImage;
        public ImageView attach5;
        public LinearLayout galleryLay, action1Linear, likeLinear;
        public String cardId = "";
        String perspective = "";

        public ViewHolder(String cardId, String perspective, View view) {
            this.cardId = cardId;
            this.perspective = perspective;
            switch (cardId) {
                case "recruitment_requisition_approval":

                    commentedBy = (TextView) view.findViewById(R.id.FirstNameComment);
                    comment = (TextView) view.findViewById(R.id.SecondNameComment);
                    commentPic = (MaterialImageView) view.findViewById(R.id.imageView1);
                    commentLayout = (RelativeLayout) view.findViewById(R.id.commentLayout);
                    commentLinear = (LinearLayout) view.findViewById(R.id.CommentButton);

            }
        }

        public ViewHolder(View itemLayoutView) {

        }
    }

    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public void pendingRemoval(int position) {
        final FeedsDataModel item = _feedsList.get(position);
        if (!itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.add(item);
            // this will redraw row in "undo" state
            // notifyItemChanged(position);
            notifyDataSetChanged();
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(_feedsList.indexOf(item));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(_feedsList.get(position), pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        FeedsDataModel item = _feedsList.get(position);
        if (itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.remove(item);
        }
        if (_feedsList.contains(item)) {
            _feedsList.remove(position);

            if (originalList.contains(item))
                originalList.remove(item);

            notifyDataSetChanged();

        }
//        Actions delegate = new Actions(_context, "ARCHIVE", item, item.id);
//        delegate.onClick(null);
    }

    public boolean isPendingRemoval(int position) {
        FeedsDataModel item = _feedsList.get(position);
        return itemsPendingRemoval.contains(item);
    }

    public class ParentViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout parent;
        LinearLayout swipeLayout;
        TextView undo;

        public ParentViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            parent = (RelativeLayout) itemLayoutView.findViewById(R.id.parent_layout);
            swipeLayout = (LinearLayout) itemLayoutView.findViewById(R.id.swiped_layout);
            undo = (TextView) itemLayoutView.findViewById(R.id.txt_undo);


        }
    }


    private class UndoClicked implements View.OnClickListener {
        FeedsDataModel item;

        public UndoClicked(FeedsDataModel item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            // user wants to undo the removal, let's cancel the pending task
            Runnable pendingRemovalRunnable = pendingRunnables.get(item);
            pendingRunnables.remove(item);
            if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
            itemsPendingRemoval.remove(item);
            // this will rebind the row in "normal" state
            notifyDataSetChanged();
        }
    }


}