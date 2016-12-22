package icynote.plugins;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveFolder.DriveFileResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import icynote.exporters.HTMLExporter;
import icynote.note.Note;
import icynote.ui.R;
import icynote.ui.login_activities.GoogleClient;

public class GoogleDriveExport implements Plugin {
    private static String TAG = GoogleDriveExport.class.getSimpleName();
    private static final int EXPORT_REQUEST = 100;
    private boolean pluginEnabled = false;
    private static Note<SpannableString> note = null;

    @Override
    public boolean canHandle(int requestCode) {
        return requestCode == EXPORT_REQUEST;
    }

    @Override
    public void handle(int requestCode, int resultCode, Intent data, PluginData state) {
        log("Handling code " + requestCode);

        if (!canHandle(requestCode)) {
            log("aborting: unknown request code " + requestCode);
            return;
        }

        if (requestCode == EXPORT_REQUEST) {
            log("Export request, result code is: " + resultCode);
        } else {
            log("Plugin cannot handle code " + requestCode + " (but declared it could handle it).");
        }
    }

    @Override
    public Iterable<View> getMetaButtons(final PluginData state) {
        final Activity a = state.getActivity();
        Button gdriveExport = new Button(a);
        gdriveExport.setBackgroundResource(R.drawable.plugin_drive);
        gdriveExport.setAlpha(0.5f);
        gdriveExport.setContentDescription("Export this note to Google Drive");
        gdriveExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity a = state.getActivity();
                Intent gdrive = new Intent(a, GoogleDriveCreateFileActivity.class);
                if (gdrive.resolveActivity(a.getPackageManager()) == null) {
                    log("Couldn't find an activity to start for Google Drive export");
                } else {
                    log("Starting activity for google drive");
                    note = state.getLastOpenedNote();
                    a.startActivityForResult(gdrive, EXPORT_REQUEST);
                }
            }
        });
        ArrayList<View> buttons = new ArrayList<>();
        buttons.add(gdriveExport);
        return buttons;
    }

    private static void log(String message, Throwable error) {
        Log.e(TAG, message, error);
    }
    private static void log(String message) {
        Log.i(TAG, message);
    }

    @Override
    public String getName() {
        return "Google Drive export";
    }

    @Override
    public void setEnabled(boolean enabled) {
        pluginEnabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return pluginEnabled;
    }

    public static final class GoogleDriveCreateFileActivity extends GoogleClient {
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onConnected(Bundle connectionHint) {
            super.onConnected(connectionHint);
            // create new contents resource
            GoogleApiClient api = apiClient();
            Drive.DriveApi.newDriveContents(api)
                    .setResultCallback(driveContentsCallback);
        }

        private void showMessage(String message) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

        final private ResultCallback<DriveContentsResult> driveContentsCallback = new
                ResultCallback<DriveContentsResult>() {
                    @Override
                    public void onResult(DriveContentsResult result) {
                        if (!result.getStatus().isSuccess()) {
                            log("Unsuccessful connection to drive. Code: " + result.getStatus().getStatusCode()
                                    + " Has resolution: " + result.getStatus().hasResolution());
                            showMessage("Error while trying to create new file contents");
                            try {
                                result.getStatus().startResolutionForResult(GoogleDriveCreateFileActivity.this, EXPORT_REQUEST);
                            } catch (IntentSender.SendIntentException e) {
                                log("Couldn't resolve problem", e);
                            }

                            return;
                        }
                        final DriveContents driveContents = result.getDriveContents();

                        // Perform I/O off the UI thread.
                        new Thread() {
                            @Override
                            public void run() {
                                // write content to DriveContents
                                OutputStream outputStream = driveContents.getOutputStream();
                                try {
                                    outputStream.write(new HTMLExporter().export(note, getApplicationContext()).getBytes());
                                    outputStream.close();
                                } catch (IOException e) {
                                    log("Couldn't write to gdrive", e);
                                }

                                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                        .setTitle(note.getTitle() + ".html")
                                        .setMimeType("text/html")
                                        .setStarred(true).build();

                                // create a file on root folder
                                Drive.DriveApi.getRootFolder(apiClient())
                                        .createFile(apiClient(), changeSet, driveContents)
                                        .setResultCallback(fileCallback);
                                finish();
                            }
                        }.start();
                    }
                };

        final private ResultCallback<DriveFileResult> fileCallback = new
                ResultCallback<DriveFileResult>() {
                    @Override
                    public void onResult(DriveFileResult result) {
                        if (!result.getStatus().isSuccess()) {
                            showMessage("Error while trying to create the file");
                            return;
                        }
                        showMessage("Note successfully exported to Google Drive!");
                    }
                };
    }
}
