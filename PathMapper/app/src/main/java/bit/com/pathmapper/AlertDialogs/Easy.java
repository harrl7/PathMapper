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

public class Easy extends DialogFragment
{
    public Easy() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder easyBuilder = new AlertDialog.Builder(getActivity());

        easyBuilder.setIcon(R.drawable.green);
        easyBuilder.setTitle("Easy Tracks");
        easyBuilder.setItems(R.array.easyArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        Dialog customDialog = easyBuilder.create();
        customDialog.setCanceledOnTouchOutside(true);

        return customDialog;
    }
}
