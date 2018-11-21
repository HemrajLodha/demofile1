package com.pws.pateast.enums;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        FeedCategory.ALL,
        FeedCategory.MINE,
        FeedCategory.APPROVED,
        FeedCategory.APPROVAL
})
@Retention(RetentionPolicy.SOURCE)
public @interface FeedCategory {
    int ALL = 1;
    int MINE = 2;
    int APPROVED = 3;
    int APPROVAL = 4;
}
