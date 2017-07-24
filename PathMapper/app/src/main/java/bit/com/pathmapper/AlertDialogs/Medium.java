package bit.com.pathmapper.AlertDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import bit.com.pathmapper.R;

/**
 * Created by Nikaela on 21/10/2016.
 */

public class Medium extends DialogFragment
{
    public Medium() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder mediumBuilder = new AlertDialog.Builder(getActivity());

        mediumBuilder.setIcon(R.drawable.orange);
        mediumBuilder.setTitle("Medium Tracks");
        mediumBuilder.setItems(R.array.mediumArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        Dialog customDialog = mediumBuilder.create();
        customDialog.setCanceledOnTouchOutside(true);

        return customDialog;
    }
}
