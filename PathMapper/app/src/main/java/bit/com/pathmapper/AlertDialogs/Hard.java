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

public class Hard extends DialogFragment
{
    public Hard() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder hardBuilder = new AlertDialog.Builder(getActivity());

        hardBuilder.setIcon(R.drawable.red);
        hardBuilder.setTitle("Hard Tracks");
        hardBuilder.setItems(R.array.hardArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        Dialog customDialog = hardBuilder.create();
        customDialog.setCanceledOnTouchOutside(true);

        return customDialog;
    }
}
