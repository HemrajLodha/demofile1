package com.pws.pateast.fragment.fees;

import android.app.DownloadManager;

import com.pws.pateast.api.model.Fees;
import com.pws.pateast.base.AppView;

import java.util.List;

/**
 * Created by intel on 02-Feb-18.
 */

public interface FeesPaymentView extends AppView {
    int REQUEST_CODE = 12;


    void setFeesAdapter(List<Fees> fees);

    DownloadManager getDownloadManager();

    void downloadInvoice(String invoiceUrl, boolean isInvoice);
}
