package com.gauchoshout.android.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * Utility class to show an error dialog with a customized message
 * from any screen. The static method takes an Activity so that it
 * can be called from any activity or fragment.
 *
 * *************************
 * * Some text about stuff *
 * *					   *
 * *        **OK**         *
 * *************************
 *  ^This is what this does in the app.
 */
public final class DialogHelper {
	
	public static final String ERROR_DIALOG_TAG = "error_dialog";
	
	private DialogHelper() {}

	public static void showErrorDialog(SherlockFragmentActivity activity, String message) {
		ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
		fragment.show(activity.getSupportFragmentManager(), ERROR_DIALOG_TAG);
	}
	
	public static class ErrorDialogFragment extends SherlockDialogFragment {
		
		private String message;
		
		public static ErrorDialogFragment newInstance(String message) {
			ErrorDialogFragment frag = new ErrorDialogFragment();
			Bundle bundle = new Bundle();
			bundle.putString("message", message);
			frag.setArguments(bundle);
			return frag;
		}
		
		@Override
		public void onCreate(Bundle state) {
			super.onCreate(state);
			message = getArguments().getString("message");
		}
		
		@Override
		public Dialog onCreateDialog(Bundle state) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Error")
				.setMessage(message)
				.setNeutralButton("Dismiss", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dismiss();
					}
				});
			return builder.create();
		}
	}
	
}
