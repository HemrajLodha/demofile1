package com.pws.pateast.activity.parent.ward;

import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.base.AppView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by planet on 8/11/2017.
 */

public interface WardSelectionView extends AppView {
    void setWardAdapter(ArrayList<Ward> wardList);
}
