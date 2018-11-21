package com.pws.pateast.fragment.complaint;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.pws.pateast.api.model.Complaint;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.base.AppView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 09-Feb-18.
 */

public interface ComplaintView extends AppView, BaseRecyclerAdapter.OnItemClickListener {
    void setComplaintAdapter(List<Complaint> complaints);

    void setTags(ArrayList<Tag> tags);

    List<Tag> getTags();
}
