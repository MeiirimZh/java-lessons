package com.example.javalessons;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

public class Lecture extends AppCompatActivity {
    private ImageView pdfImageView;
    private Button prevButton, nextButton;
    private TextView pageText;
    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page currentPage;
    private ParcelFileDescriptor fileDescriptor;
    private int currentPageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lecture);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.createDatabaseIfNotExists();
        List<String> lectures = dbHelper.getLectureTitles();

        pdfImageView = findViewById(R.id.pdfImageView);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        pageText = findViewById(R.id.pageText);

        try {
            openPdfFromAssets("example.pdf");
            showPage(currentPageIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }

        prevButton.setOnClickListener(v -> showPage(currentPageIndex - 1));
        nextButton.setOnClickListener(v -> showPage(currentPageIndex + 1));

        int lecture_id = getIntent().getIntExtra("lecture_id", -1);
        TextView lecture_title_text = findViewById(R.id.lecture_title_text);

        if (lecture_id != -1) {
            lecture_title_text.setText(lectures.get(lecture_id));
        }
        else {
            lecture_title_text.setText("Лекция не найдена");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (currentPage != null) currentPage.close();
            if (pdfRenderer != null) pdfRenderer.close();
            if (fileDescriptor != null) fileDescriptor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void openPdfFromAssets(String fileName) throws Exception {
        File file = new File(getCacheDir(), fileName);
        if (!file.exists()) {
            InputStream asset = getAssets().open(fileName);
            FileOutputStream output = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int size;
            while ((size = asset.read(buffer)) != -1) {
                output.write(buffer, 0, size);
            }
            output.close();
            asset.close();
        }

        fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        pdfRenderer = new PdfRenderer(fileDescriptor);
    }

    private void showPage(int index) {
        if (pdfRenderer == null) return;
        if (index < 0 || index >= pdfRenderer.getPageCount()) return;

        if (currentPage != null) currentPage.close();

        currentPageIndex = index;
        currentPage = pdfRenderer.openPage(currentPageIndex);

        Bitmap bitmap = Bitmap.createBitmap(
                currentPage.getWidth(),
                currentPage.getHeight(),
                Bitmap.Config.ARGB_8888
        );
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        pdfImageView.setImageBitmap(bitmap);

        pageText.setText((currentPageIndex + 1) + " / " + pdfRenderer.getPageCount());
    }

}