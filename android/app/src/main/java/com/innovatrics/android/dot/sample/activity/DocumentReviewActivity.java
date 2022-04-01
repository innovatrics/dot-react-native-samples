package com.innovatrics.android.dot.sample.activity;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.AnyRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.innovatrics.android.dot.sample.R;
import com.innovatrics.android.dot.sample.documentcapture.DocumentSide;
import com.innovatrics.android.dot.sample.documentreview.DocumentItem;
import com.innovatrics.android.dot.sample.fragment.DemoDocumentReviewFragment;
import com.innovatrics.android.dot.sample.fragment.DocumentReviewFragment;
import com.innovatrics.android.dot.sample.documentcapture.Rect;

import java.util.ArrayList;
import java.util.List;

public class DocumentReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFragment();
    }

    private void setFragment() {
        if (getSupportFragmentManager().findFragmentById(android.R.id.content) != null) {
            return;
        }

        final Uri frontImageUri = getUri(R.drawable.document_sample_front);
        final Uri backImageUri = getUri(R.drawable.document_sample_back);
        final List<DocumentItem> visualInspectionZoneItemList = createDummyVisualInspectionZoneList();
        final List<DocumentItem> machineReadableZoneItemList = createDummyMachineReadableZoneList();

        final Bundle arguments = new Bundle();
        arguments.putParcelable(DocumentReviewFragment.ARG_FRONT_IMAGE_URI, frontImageUri);
        arguments.putParcelable(DocumentReviewFragment.ARG_BACK_IMAGE_URI, backImageUri);
        arguments.putSerializable(DocumentReviewFragment.ARG_VISUAL_INSPECTION_ZONE_ITEMS, new ArrayList<>(visualInspectionZoneItemList));
        arguments.putSerializable(DocumentReviewFragment.ARG_MACHINE_READABLE_ZONE_ITEMS, new ArrayList<>(machineReadableZoneItemList));

        final Fragment fragment = new DemoDocumentReviewFragment();
        fragment.setArguments(arguments);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }

    private final Uri getUri(@AnyRes final int drawableId) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(drawableId)
                + '/' + getResources().getResourceTypeName(drawableId)
                + '/' + getResources().getResourceEntryName(drawableId));
    }

    private List<DocumentItem> createDummyVisualInspectionZoneList() {
        final List<DocumentItem> list = new ArrayList<>();

        DocumentItem item;

        item = new DocumentItem();
        item.setDocumentSide(DocumentSide.FRONT);
        item.setFieldId("surname");
        item.setLine(0);
        item.setName("Surname");
        item.setValue("Specimen");
        item.setScore(0.99f);
        item.setRect(new Rect(290, 95, 435, 130));
        list.add(item);

        item = new DocumentItem();
        item.setDocumentSide(DocumentSide.FRONT);
        item.setFieldId("given_names");
        item.setLine(0);
        item.setName("Given Names");
        item.setValue("Vzorka");
        item.setScore(0.98f);
        item.setRect(new Rect(287, 143, 398, 181));
        list.add(item);

        item = new DocumentItem();
        item.setDocumentSide(DocumentSide.FRONT);
        item.setFieldId("sex");
        item.setLine(0);
        item.setName("Sex");
        item.setValue("F");
        item.setScore(0.99f);
        item.setRect(new Rect(289, 242, 322, 282));
        list.add(item);

        item = new DocumentItem();
        item.setDocumentSide(DocumentSide.FRONT);
        item.setFieldId("date_of_birth");
        item.setLine(0);
        item.setName("Date of Birth");
        item.setValue("11.11.1911");
        item.setScore(0.99f);
        item.setRect(new Rect(584, 191, 754, 232));
        list.add(item);

        item = new DocumentItem();
        item.setDocumentSide(DocumentSide.FRONT);
        item.setFieldId("personal_number");
        item.setLine(0);
        item.setName("Personal Number");
        item.setValue("111111/1111");
        item.setScore(0.86f);
        item.setRect(new Rect(585, 239, 778, 276));
        list.add(item);

        item = new DocumentItem();
        item.setDocumentSide(DocumentSide.FRONT);
        item.setFieldId("document_number");
        item.setLine(0);
        item.setName("Document Number");
        item.setValue("EA000000");
        item.setScore(0.83f);
        item.setRect(new Rect(282, 285, 485, 336));
        list.add(item);

        item = new DocumentItem();
        item.setDocumentSide(DocumentSide.FRONT);
        item.setFieldId("issued_by");
        item.setLine(0);
        item.setName("Issued by");
        item.setValue("Bratislava");
        item.setScore(0.99f);
        item.setRect(new Rect(284, 388, 438, 427));
        list.add(item);

        item = new DocumentItem();
        item.setDocumentSide(DocumentSide.FRONT);
        item.setFieldId("date_of_issue");
        item.setLine(0);
        item.setName("Date of Issue");
        item.setValue("01.03.2015");
        item.setScore(0.98f);
        item.setRect(new Rect(589, 385, 755, 422));
        list.add(item);

        item = new DocumentItem();
        item.setDocumentSide(DocumentSide.FRONT);
        item.setFieldId("date_of_expiry");
        item.setLine(0);
        item.setName("Date of Expiry");
        item.setValue("01.03.2025");
        item.setScore(0.98f);
        item.setRect(new Rect(586, 337, 753, 376));
        list.add(item);

        item = new DocumentItem();
        item.setDocumentSide(DocumentSide.BACK);
        item.setFieldId("address");
        item.setLine(0);
        item.setName("Address (1)");
        item.setValue("Vymyslena 34");
        item.setScore(0.83f);
        item.setRect(new Rect(375, 34, 535, 60));
        list.add(item);

        item = new DocumentItem();
        item.setDocumentSide(DocumentSide.BACK);
        item.setFieldId("address");
        item.setLine(1);
        item.setName("Address (2)");
        item.setValue("Krivan");
        item.setScore(0.99f);
        item.setRect(new Rect(371, 57, 456, 80));
        list.add(item);

        item = new DocumentItem();
        item.setDocumentSide(DocumentSide.BACK);
        item.setFieldId("surname_at_birth");
        item.setLine(0);
        item.setName("Surname at Birth");
        item.setValue("Povodna");
        item.setScore(0.99f);
        item.setRect(new Rect(371, 100, 475, 126));
        list.add(item);

        item = new DocumentItem();
        item.setDocumentSide(DocumentSide.BACK);
        item.setFieldId("place_of_birth");
        item.setLine(0);
        item.setName("Place of Birth");
        item.setValue("Banska Bystrica");
        item.setScore(0.99f);
        item.setRect(new Rect(374, 143, 550, 172));
        list.add(item);

        return list;
    }

    private List<DocumentItem> createDummyMachineReadableZoneList() {
        final List<DocumentItem> list = new ArrayList<>();

        DocumentItem item;

        item = new DocumentItem();
        item.setFieldId("surname");
        item.setLine(0);
        item.setName("Surname");
        item.setValue("SPECIMEN");
        list.add(item);

        item = new DocumentItem();
        item.setFieldId("given_names");
        item.setLine(0);
        item.setName("Given Names");
        item.setValue("VZORKA");
        list.add(item);

        item = new DocumentItem();
        item.setFieldId("sex");
        item.setLine(0);
        item.setName("Sex");
        item.setValue("F");
        list.add(item);

        item = new DocumentItem();
        item.setFieldId("date_of_birth");
        item.setLine(0);
        item.setName("Date of Birth");
        item.setValue("111111");
        list.add(item);

        item = new DocumentItem();
        item.setFieldId("nationality");
        item.setLine(0);
        item.setName("Nationality");
        item.setValue("SVK");
        list.add(item);

        item = new DocumentItem();
        item.setFieldId("document_code");
        item.setLine(0);
        item.setName("Document Code");
        item.setValue("ID");
        list.add(item);

        item = new DocumentItem();
        item.setFieldId("document_number");
        item.setLine(0);
        item.setName("Document Number");
        item.setValue("EA000000");
        list.add(item);

        item = new DocumentItem();
        item.setFieldId("country_code");
        item.setLine(0);
        item.setName("Country Code");
        item.setValue("SVK");
        list.add(item);

        item = new DocumentItem();
        item.setFieldId("date_expiry");
        item.setLine(0);
        item.setName("Date of Expiry");
        item.setValue("250301");
        list.add(item);

        return list;
    }

}
