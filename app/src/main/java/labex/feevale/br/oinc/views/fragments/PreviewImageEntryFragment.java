package labex.feevale.br.oinc.views.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.model.Entry;
import labex.feevale.br.oinc.tasks.CartoonTask;
import labex.feevale.br.oinc.views.activities.GoalsAndEntryDataActivity;

/**
 * Created by 0126128 on 19/03/2015.
 */
public class PreviewImageEntryFragment extends Fragment {

    private Entry entry;
    private ImageView imageView;

    public PreviewImageEntryFragment(Entry entry) {
        this.entry = entry;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            setRetainInstance(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview_image_entry, container, false);
        imageView = (ImageView) view.findViewById(R.id.entryPreviewImageView);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Display display = getActivity().getWindowManager().getDefaultDisplay();

        CartoonTask.bitmap = BitmapFactory.decodeFile(entry.getPicturePath(), options);

        int percentResize = (display.getWidth() * 100)/CartoonTask.bitmap.getWidth();
        int newHeight = (CartoonTask.bitmap.getHeight() * percentResize)/100;
        imageView.setImageBitmap(Bitmap.createScaledBitmap(CartoonTask.bitmap, display.getWidth(), newHeight, false));

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((GoalsAndEntryDataActivity)getActivity()).setFragment(this);
    }


}
