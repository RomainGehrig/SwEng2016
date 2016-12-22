/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * Apache 2.0 licence: http://www.apache.org/licenses/LICENSE-2.0
 */
package icynote.ui;

import android.app.ProgressDialog;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;


/**
 * The activity with progress dialog.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class ActivityWithProgressDialog extends AppCompatActivity {

    @VisibleForTesting
    private ProgressDialog mProgressDialog;

    /**
     * Show progress dialog.
     */
    protected void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    /**
     * Hide progress dialog.
     */
    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

}
