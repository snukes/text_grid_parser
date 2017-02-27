package com.mitchellsheaffer.textgridparser

import groovy.transform.AutoClone

/**
 * Created by mitchell2 on 2/22/2017.
 */
@AutoClone
class Record {

    LinkedHashMap<Integer, ArrayList<ReportGridField>> recordFields = [:]

    Record(LinkedHashMap<Integer, ArrayList<ReportGridField>> columnHeaders) {
        this.recordFields = columnHeaders
    }

    /*
     * Loops through all columns and returns the max number of grid rows per record.
     */
    def maxGridRows() {
        def maxRow = 0

        recordFields.each { key, ArrayList<ReportGridField> column ->
            println column.size()
            //maxRow = column.size() > maxRow ? column.size() : maxRow
        }
        maxRow
    }

    @Override
    public String toString() {
        return "Record{" +
                "recordFields=" + recordFields +
                '}';
    }
}
