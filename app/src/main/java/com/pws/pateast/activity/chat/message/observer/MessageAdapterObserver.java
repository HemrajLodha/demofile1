package com.pws.pateast.activity.chat.message.observer;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.base.ui.view.BaseRecyclerView;
import com.pws.pateast.activity.chat.message.MessageAdapter;

/**
 * Created by intel on 09-Aug-17.
 */

public class MessageAdapterObserver extends RecyclerView.AdapterDataObserver
{
    private RecyclerView rvMessage;
    private MessageAdapter messageAdapter;
    public MessageAdapterObserver(RecyclerView rvMessage,MessageAdapter messageAdapter)
    {
        this.rvMessage = rvMessage;
        this.messageAdapter = messageAdapter;
    }


    @Override
    public void onChanged() {
        super.onChanged();
        int positionStart = messageAdapter.getItemCount();
        int friendlyMessageCount = messageAdapter.getItemCount();
        int lastVisiblePosition =
                ((LinearLayoutManager) rvMessage.getLayoutManager()).findLastCompletelyVisibleItemPosition();
        // If the recycler view is initially being loaded or the
        // user is at the bottom of the list, scroll to the bottom
        // of the list to show the newly added message.
        if (lastVisiblePosition == -1 ||
                (positionStart >= (friendlyMessageCount - 1) &&
                        lastVisiblePosition == (positionStart - 1))) {
            rvMessage.scrollToPosition(positionStart);
        }
    }
}
