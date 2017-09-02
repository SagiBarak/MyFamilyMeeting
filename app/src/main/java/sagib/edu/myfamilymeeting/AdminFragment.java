package sagib.edu.myfamilymeeting;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class AdminFragment extends BottomSheetDialogFragment {

    private static final int FILE_SELECT_CODE = 11;
    private static final int RC_WRITE = 1122;
    Button btnUpload;
    Button btnSync;
    TextInputLayout tilPassword;
    EditText etPassword;
    FirebaseStorage storage;
    FirebaseDatabase database;
    SharedPreferences prefs;
    private String datesParsed;
    ArrayList<String> dates = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin, container, false);

        prefs = getContext().getSharedPreferences("Data", MODE_PRIVATE);

        btnSync = (Button) v.findViewById(R.id.btnSync);
        btnUpload = (Button) v.findViewById(R.id.btnUpload);
        tilPassword = (TextInputLayout) v.findViewById(R.id.tilPassword);
        etPassword = (EditText) v.findViewById(R.id.etPassword);

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        datesParsed = prefs.getString("datesParsed", "");

        FirebaseDatabase.getInstance().getReference("DatesParsed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                datesParsed = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("DatesParsedAuto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    dates.add(snapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnSync.setVisibility(View.GONE);
        btnUpload.setVisibility(View.GONE);

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etPassword.getText().toString().equals("123456")) {
                    tilPassword.setVisibility(View.GONE);
                    etPassword.setVisibility(View.GONE);
                    btnSync.setVisibility(View.VISIBLE);
                    btnUpload.setVisibility(View.VISIBLE);
                    checkStoragePermission();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkStoragePermission()) return;
                ExecutorService service = Executors.newSingleThreadExecutor();
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        for (String date : dates) {
                            if (datesParsed == null || !datesParsed.contains(date)) {
                                storage.getReference().child("docs").child(date).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Log.d("SagiB Here1", uri.toString());
                                        String fileNameDate = uri.toString().substring(uri.toString().indexOf("%2F") + 3, uri.toString().indexOf("?alt"));
                                        parseThisDoc(String.valueOf(uri), fileNameDate);
                                        datesParsed = datesParsed + "\n" + fileNameDate;
                                        prefs.edit().remove("datesParsed").commit();
                                        prefs.edit().putString("datesParsed", datesParsed).commit();
                                        FirebaseDatabase.getInstance().getReference("DatesParsed").setValue(datesParsed);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Log.d("SagiB", "error");
                                    }
                                });
                            }
                        }

                    }
                });
            }
        };
        btnSync.setOnClickListener(onClickListener);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "בחר קובץ"),
                            FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    private void parseThisDoc(final String link, final String date) {
        final ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                URLConnection con = null;
                try {
                    url = new URL(link);
                    Log.d("SagiB link", link);
                    con = url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("SagiB coonnull", String.valueOf(con == null));
                if (con != null) {
                    try {
                        BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
                        XWPFDocument document = new XWPFDocument(bis);
                        final XWPFWordExtractor extractor = new XWPFWordExtractor(document);
                        int count = 0;
                        String text = extractor.getText();
                        String header = text.substring(text.indexOf("כותרת:") + 7, text.indexOf("תאריך:"));
                        String textDate = text.substring(text.indexOf("תאריך:") + 7, text.indexOf("פתיח:"));
                        String opening = text.substring(text.indexOf("פתיח:") + 6, text.indexOf("טקסט:"));
                        String body = text.substring(text.indexOf("טקסט:") + 6, text.indexOf("טקסט סיום:"));
                        String endingText = text.substring(text.indexOf("טקסט סיום:") + 11, text.indexOf("חידות:"));
                        String quiz = text.substring(text.indexOf("חידות:") + 7, text.indexOf("פתיח אוכל:"));
                        String openMenu = text.substring(text.indexOf("פתיח אוכל:") + 11, text.indexOf("תפריט:"));
                        String textBeforeMenu = text.substring(text.indexOf("תפריט:") + 7, text.indexOf("התכנסות:"));
                        String firstFood = text.substring(text.indexOf("התכנסות:") + 9, text.indexOf("ראשונות:"));
                        String secondFood = text.substring(text.indexOf("ראשונות:") + 9, text.indexOf("עיקרית:"));
                        String primaryFood = text.substring(text.indexOf("עיקרית:") + 8, text.indexOf("תוספות:"));
                        String topingsFood = text.substring(text.indexOf("תוספות:") + 8, text.indexOf("קינוחים:"));
                        String lastFood = text.substring(text.indexOf("קינוחים:") + 9, text.indexOf("תמונות פתיחה("));
                        String openImages = text.substring(text.indexOf("תמונות פתיחה(") + 20).substring(0, 1);
                        String bodyImages = text.substring(text.indexOf("תמונות(") + 14).substring(0, 1);
                        String foodImages = text.substring(text.indexOf("תמונות תפריט(") + 20).substring(0, 1);
                        String allRecipes = text.substring(text.indexOf("מתכון:"));
                        FirebaseDatabase.getInstance().getReference("Data").child(date.replace(".", "-")).child("text").child("header").setValue(header);
                        FirebaseDatabase.getInstance().getReference("Data").child(date.replace(".", "-")).child("text").child("textDate").setValue(textDate);
                        FirebaseDatabase.getInstance().getReference("Data").child(date.replace(".", "-")).child("text").child("opening").setValue(opening);
                        FirebaseDatabase.getInstance().getReference("Data").child(date.replace(".", "-")).child("text").child("body").setValue(body);
                        FirebaseDatabase.getInstance().getReference("Data").child(date.replace(".", "-")).child("text").child("endingText").setValue(endingText);
                        FirebaseDatabase.getInstance().getReference("Data").child(date.replace(".", "-")).child("text").child("quiz").setValue(quiz);
                        FirebaseDatabase.getInstance().getReference("Data").child(date.replace(".", "-")).child("text").child("openMenu").setValue(openMenu);
                        FirebaseDatabase.getInstance().getReference("Data").child(date.replace(".", "-")).child("text").child("textBeforeMenu").setValue(textBeforeMenu);
                        FirebaseDatabase.getInstance().getReference("Data").child(date.replace(".", "-")).child("text").child("firstFood").setValue(firstFood);
                        FirebaseDatabase.getInstance().getReference("Data").child(date.replace(".", "-")).child("text").child("secondFood").setValue(secondFood);
                        FirebaseDatabase.getInstance().getReference("Data").child(date.replace(".", "-")).child("text").child("primaryFood").setValue(primaryFood);
                        FirebaseDatabase.getInstance().getReference("Data").child(date.replace(".", "-")).child("text").child("topingsFood").setValue(topingsFood);
                        FirebaseDatabase.getInstance().getReference("Data").child(date.replace(".", "-")).child("text").child("lastFood").setValue(lastFood);
                        FirebaseDatabase.getInstance().getReference("Data").child(date.replace(".", "-")).child("text").child("allRecipes").setValue(allRecipes);
                        FirebaseDatabase.getInstance().getReference("Data").child(date.replace(".", "-")).child("imageCount").child("openImages").setValue(openImages);
                        FirebaseDatabase.getInstance().getReference("Data").child(date.replace(".", "-")).child("imageCount").child("bodyImages").setValue(bodyImages);
                        FirebaseDatabase.getInstance().getReference("Data").child(date.replace(".", "-")).child("imageCount").child("foodImages").setValue(foodImages);
                        for (XWPFParagraph p : document.getParagraphs()) {
                            Log.d("SagiB", p.getText());
                            for (XWPFRun run : p.getRuns()) {
                                for (XWPFPicture pic : run.getEmbeddedPictures()) {
                                    byte[] img = pic.getPictureData().getData();
                                    count++;
                                    StorageReference storageReference = storage.getReference().child("images").child(String.valueOf(date)).child("image" + count + ".jpg");
                                    UploadTask uploadTask = storageReference.putBytes(img);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            Log.d("SagiB", exception.getMessage());
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                            Log.d("SagiB", downloadUrl.toString());
                                            FirebaseDatabase.getInstance().getReference("Data").child(date.replace(".", "-")).child("images").push().setValue(downloadUrl.toString());
                                        }
                                    });
                                }
                            }
                        }
                        bis.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        service.shutdown();
    }

    private boolean checkStoragePermission() {
        int resultcode = ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean granted = resultcode == PackageManager.PERMISSION_GRANTED;
        if (!granted) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_WRITE);
        }
        return granted;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    btnSync.setClickable(false);
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("SagiB", "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;
                    try {
                        path = FileUtils.getPath(getContext(), uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Log.d("SagiB", "File Path: " + path);
                    // Get the file instance
                    final File file = new File(String.valueOf(uri));
                    // Initiate the upload
                    String fileName = uri.toString().substring(uri.toString().lastIndexOf("%2F") + 3).replace(".docx", "");
                    StorageReference storageRef = storage.getReference("docs/" + fileName);
                    storageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String name = taskSnapshot.getMetadata().getName();
                            FirebaseDatabase.getInstance().getReference("DatesParsedAuto").push().setValue(name).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Upload Completed!", Toast.LENGTH_SHORT).show();
                                    btnSync.setClickable(true);
                                }
                            });
                        }
                    });
                    break;
                }
                super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
