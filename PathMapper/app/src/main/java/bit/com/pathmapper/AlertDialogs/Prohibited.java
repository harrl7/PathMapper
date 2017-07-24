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

public class Prohibited extends DialogFragment
{
    public Prohibited() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder prohibitedBuilder = new AlertDialog.Builder(getActivity());

        prohibitedBuilder.setIcon(R.drawable.info);
        prohibitedBuilder.setTitle("Prohibited Items ");
        prohibitedBuilder.setItems(R.array.prohibitedArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        Dialog customDialog = prohibitedBuilder.create();
        customDialog.setCanceledOnTouchOutside(true);

        return customDialog;
    }
}
