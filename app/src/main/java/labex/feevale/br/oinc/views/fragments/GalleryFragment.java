package labex.feevale.br.oinc.views.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.actions.Observer;
import labex.feevale.br.oinc.actions.Subject;
import labex.feevale.br.oinc.views.actions.CameraActions;
import labex.feevale.br.oinc.views.actions.LayoutChanges;

/**
 * Created by 0126128 on 30/01/2015.
 */
public class GalleryFragment extends Fragment implements CameraActions, Observer{

    private String imagePath;
    private String commentPicture;
    private ImageView imageView;
    private LayoutChanges layoutChanges;
    private TextView commentTextView;
    private LinearLayout backgroundComment;

    private Subject subject;

    public GalleryFragment() {}

    public GalleryFragment(String imagePath, String commentPicture, LayoutChanges layoutChanges) {
        this.imagePath = imagePath;
        this.commentPicture = commentPicture;
        this.layoutChanges = layoutChanges;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            setRetainInstance(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_fragment, container, false);
        imageView = (ImageView)view.findViewById(R.id.galleryItem);
        if(imagePath.length() > 4)
            imageView.setImageURI(Uri.fromFile(new File(imagePath)));
        else
            imageView.setImageResource(R.drawable.background_gallery);

        backgroundComment = (LinearLayout) view.findViewById(R.id.background_comment);
        showComment(commentPicture);

        commentTextView = (TextView) view.findViewById(R.id.commentTextView);
        commentTextView.setText(commentPicture);
        return view;
    }

    private void showComment(String commentToValidae){
        if(commentToValidae.length()>0)
            backgroundComment.setVisibility(View.VISIBLE);
        else
            backgroundComment.setVisibility(View.INVISIBLE);
    }

    @Override
    public void changePicture(String imagePath, String comment) {
        this.imageView.setImageURI(Uri.fromFile(new File(imagePath)));
        this.commentTextView.setText(comment);
        showComment(comment);
        layoutChanges.changeLayout();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void update() {
        this.commentTextView.setText(subject.getUpdate(this).toString());
        showComment(subject.getUpdate(this).toString());
    }

    @Override
    public void setSubject(Subject sub) {
        this.subject = sub;
    }
}
