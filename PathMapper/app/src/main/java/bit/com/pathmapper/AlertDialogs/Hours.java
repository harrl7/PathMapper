package bit.com.pathmapper.AlertDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import bit.com.pathmapper.R;


/**
 * Created by tsgar on 20/10/2016.
 */

public class Hours extends DialogFragment {

    public Hours(){}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder hourBuilder = new AlertDialog.Builder(getActivity());

        hourBuilder.setIcon(R.drawable.info);
        hourBuilder.setTitle("Information");
        hourBuilder.setItems(R.array.hoursArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        Dialog customDialog = hourBuilder.create();
        customDialog.setCanceledOnTouchOutside(true);

        return customDialog;
    }
}
