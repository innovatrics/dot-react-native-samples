package com.innovatrics.android.dot.sample.dto;

import com.innovatrics.android.dot.sample.documentcapture.DocumentSide;
import com.innovatrics.android.dot.sample.documentreview.DocumentItem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class OnboardingDocumentData implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<DocumentSide, String> uriStringMap;
    private List<DocumentItem> visualInspectionZoneItemList;
    private List<DocumentItem> machineReadableZoneItemList;

    public Map<DocumentSide, String> getUriStringMap() {
        return uriStringMap;
    }

    public void setUriStringMap(final Map<DocumentSide, String> uriStringMap) {
        this.uriStringMap = uriStringMap;
    }

    public List<DocumentItem> getVisualInspectionZoneItemList() {
        return visualInspectionZoneItemList;
    }

    public void setVisualInspectionZoneItemList(final List<DocumentItem> visualInspectionZoneItemList) {
        this.visualInspectionZoneItemList = visualInspectionZoneItemList;
    }

    public List<DocumentItem> getMachineReadableZoneItemList() {
        return machineReadableZoneItemList;
    }

    public void setMachineReadableZoneItemList(final List<DocumentItem> machineReadableZoneItemList) {
        this.machineReadableZoneItemList = machineReadableZoneItemList;
    }

}
