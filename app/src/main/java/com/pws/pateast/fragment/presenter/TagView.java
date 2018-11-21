package com.pws.pateast.fragment.presenter;

import com.pws.pateast.api.model.Tag;
import com.pws.pateast.base.AppView;

import java.util.ArrayList;

/**
 * Created by planet on 8/29/2017.
 */

public interface TagView extends AppView {
    void setTagData(ArrayList<Tag> data);
}
