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

public class Season extends DialogFragment {

    public Season() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder seasonBuilder = new AlertDialog.Builder(getActivity());

        seasonBuilder.setIcon(R.drawable.info);
        seasonBuilder.setTitle("Seasonal Attractions");
        seasonBuilder.setItems(R.array.seasonalArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        Dialog customDialog = seasonBuilder.create();
        customDialog.setCanceledOnTouchOutside(true);

        return customDialog;
    }
}
