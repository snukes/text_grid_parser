package com.mitchellsheaffer.textgridparser

import groovy.transform.AutoClone

/**
 * Created by mitchell2 on 2/22/2017.
 */
@AutoClone
class ReportGridField {

    def name
    def type
    def row
    def column
    def value

    ReportGridField(String name) {
        this.name = name
    }

    ReportGridField(String name, int row, int column) {
        this.name = name
        this.row = row
        this.column = column
    }

    ReportGridField(String name, FieldTypes type, int row, int column, String value) {
        this.name = name
        this.type = type
        this.row = row
        this.column = column
        this.value = value
    }

    @Override
    public String toString() {
        return "ReportGridField{" +
                "name=" + name +
                //", type=" + type +
                ", row=" + row +
                ", column=" + column +
                ", value=" + value +
                '}';
    }
}
