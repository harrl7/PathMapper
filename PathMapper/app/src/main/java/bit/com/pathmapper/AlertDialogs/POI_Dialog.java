package bit.com.pathmapper.AlertDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import bit.com.pathmapper.Models.PointOfInterest;
import bit.com.pathmapper.R;
import bit.com.pathmapper.Utilities.DB_Handler;

/**
 * Created by jacksct1 on 30/10/2016.
 */

public class POI_Dialog extends DialogFragment {


    public POI_Dialog()
    {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        int id = getArguments().getInt("key");
        AlertDialog.Builder poiBuilder = new AlertDialog.Builder(getActivity());

        DB_Handler db = new DB_Handler(getActivity());
        PointOfInterest poi = db.getPOI(id);

        poiBuilder.setIcon(R.drawable.collection);
        poiBuilder.setTitle(poi.getName());
        poiBuilder.setMessage(poi.getScientificName() + ": " + poi.getDescription());

        Dialog customDialog = poiBuilder.create();
        customDialog.setCanceledOnTouchOutside(true);

        return customDialog;
    }
}
