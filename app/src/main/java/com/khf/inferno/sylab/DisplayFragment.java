package com.khf.inferno.sylab;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.pdfview.PDFView;

import java.io.File;

public class DisplayFragment extends Fragment {

    private static final String ARG_PARAM1 = "fileName";
    private static final String ARG_PARAM2 = "url";

    private Button openBtn;
    private Button refBtn;

    private String myFileName;
    private String url;

    private FileDownloader fileDownloader = null;
    
    private File file = null;

    //private static long ID = 0;

    public static DisplayFragment newInstance(String url, String fileName) {

        DisplayFragment fragment = new DisplayFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, fileName);
        args.putString(ARG_PARAM2, url);

        fragment.setArguments(args);
        return fragment;

    }

    public DisplayFragment() {}

    public void refreshFragment() {
        getFragmentManager().beginTransaction().replace(R.id.content_frame, this).detach(this).attach(this).commit();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            myFileName = getArguments().getString(ARG_PARAM1);
            url = getArguments().getString(ARG_PARAM2);
        }
        openBtn = (Button) getActivity().findViewById(R.id.open_btn);
        refBtn = (Button) getActivity().findViewById(R.id.refresh_btn);

        fileDownloader = new FileDownloader(); 
        if (!fileDownloader.isFilePresent("/sylab/" + myFileName + ".pdf")) openBtn.setVisibility(View.INVISIBLE);

        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(Uri.fromFile(file), "application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                Intent intent = Intent.createChooser(target, "Open File");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "No application(s) for viewing PDFs", Toast.LENGTH_SHORT).show();
                }
            }
        });

        refBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshFragment();
            }
        });



    }

    @Override
    public void onDetach() {
        super.onDetach();
        openBtn.setVisibility(View.INVISIBLE);
        refBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        openBtn.setVisibility(View.INVISIBLE);
        refBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(getArguments() == null){
            return inflater.inflate(R.layout.comingsoon, null);
        }

        View rootView;
        rootView = inflater.inflate(R.layout.fragment_display, null);

        file = new File(fileDownloader.getFilePath("/sylab/" + myFileName + ".pdf"));

        PDFView pdfView = (PDFView) rootView.findViewById(R.id.pdfView);

        if (fileDownloader.isFilePresent("/sylab/" + myFileName + ".pdf")) {
            openBtn.setVisibility(View.VISIBLE);
            refBtn.setVisibility(View.INVISIBLE);
            pdfView.fromFile(file).defaultPage(1).enableSwipe(true).load();
            return rootView;
        }

        if (fileDownloader.isReadyForDownload(this)) {
            rootView = inflater.inflate(R.layout.comingsoon, null);
            TextView error = (TextView) rootView.findViewById(R.id.textView2);
            error.setText("You haven't downloaded this file yet");
            fileDownloader.DownloadFile(getActivity().getApplicationContext(), url, "/sylab/", myFileName, ".pdf");
            Toast.makeText(getActivity().getApplicationContext(), "File is being downloaded. Refresh after a few moments to see changes.", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refBtn.setVisibility(View.VISIBLE);
                }
            }, 1500);
            return rootView;
        }
        if (!fileDownloader.isReadyForDownload(this)) {
            rootView = inflater.inflate(R.layout.comingsoon, null);
            Toast.makeText(getActivity().getApplicationContext(), "Network error. Check your network connections to download the file.", Toast.LENGTH_SHORT).show();
            return rootView;
        }
        return rootView;
    }
}
