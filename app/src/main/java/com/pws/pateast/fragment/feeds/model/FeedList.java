package com.pws.pateast.fragment.feeds.model;

import com.pws.pateast.api.model.Feeds;

import java.util.ArrayList;

public class FeedList extends ArrayList<Feeds> {
    @Override
    public int size() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Feeds get(int index) {
        Feeds feeds = super.get(index);
        feeds.setIndex(index);
        return feeds;
    }

    @Override
    public int indexOf(Object o) {
        return o instanceof Feeds ? ((Feeds) o).getIndex() : super.indexOf(o);
    }
}
