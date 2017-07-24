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

public class Statistics extends DialogFragment
{
    public Statistics() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder statisticsBuilder = new AlertDialog.Builder(getActivity());
        statisticsBuilder.setIcon(R.drawable.info);
        statisticsBuilder.setTitle("Garden Statistics");
        statisticsBuilder.setItems(R.array.statisticsArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        Dialog customDialog = statisticsBuilder.create();
        customDialog.setCanceledOnTouchOutside(true);

        return customDialog;
    }
}
