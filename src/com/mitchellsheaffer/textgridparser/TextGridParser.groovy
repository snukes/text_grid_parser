package com.mitchellsheaffer.textgridparser

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Grid-based text parsing tool. Dynamically handles information laid out in grid form.
 */
class TextGridParser {

    def holdFileName
    def gridCellEndings
    def records = []
    boolean singleLine // report can have either a single line per record or multiple records in a grid shape. This parser should be able to handle both.
    Record columnHeaderRecord
    def final static Pattern RECORD_START_PATTERN = Pattern.compile("")

    TextGridParser(String holdFileName, boolean singleLine) {
        this.holdFileName = holdFileName
        this.singleLine = singleLine
    }

    def generateReport() {
        parseFile(holdFileName)
    }

    def parseFile(String holdFileName) {

        File holdFile = new File(holdFileName)
        def foundColumnHeaders = false
        def startingLines = []

        // multi-line specific variables
        def recordLines = []
        def lineCount = 0

        if(!holdFile.exists()) {
            println "File does not exist"
        } else {
            // Step through each line in the file
            holdFile.eachLine { line ->

                if (!foundColumnHeaders) {
                    if (line.startsWith('----')) {
                        foundColumnHeaders = true
                        findGridEndings(line)
                        generateColumnHeaders(startingLines)
                    } else {
                        startingLines.add(line)
                    }
                } else {
                    // Once we've reached this else, we will have created the column headers and can now parse records
                    if (singleLine) {
                        if(isLineRecordStart(line)) {
                            parseSingleLineRecord(line)
                        }
                    } else {
                        if (lineCount == columnHeaderRecord.maxGridRows()) {
                            parseMultiLineRecord(recordLines)
                            recordLines = []    // reset
                            lineCount = 0       // reset
                        } else {
                            recordLines.add(line)
                            lineCount++
                        }
                    }
                }
            }
        }

        records.each { record ->
            println record
        }

    }

    /*
     * Since the format is based on a grid layout, the right side of all cells lines up. This method produces a list
     * of indices that are used later on to break split the report column data out.
     */
    def findGridEndings(String line) {
        gridCellEndings = new ArrayList<Integer>()
        String pattern = '- '
        int index = line.indexOf(pattern)
        while (index >= 0) {
            //System.out.println(index)
            if (index > 0) {
                gridCellEndings.add(index + 1)
            }
            index = line.indexOf(pattern, index + 1)
        }
    }

    /*
     * Generates the columns and stores them in a Record in a format similar to their layout in the text file
     *
     * @param startingLines - List of all strings that start out the record. Contains the column headings
     */
    def generateColumnHeaders(ArrayList<String> startingLines) {

        Collections.reverse(startingLines) // starting from the bottom and moving up.
        def columnHeadersMap = [:]

        int i = 0
        String line = startingLines.get(i)
        while (line) {  // Reports typically have a blank like in between the title info and column headings. This loop ensures we stop once we read the headings.
            def splitLine = splitStringOnGridEndings(line)
            splitLine.eachWithIndex{ String column, int index ->
                if (column.trim()) {
                    def rowNum = singleLine ? 0 : i

                    // if records are returned on a single line, column headers are typically more verbose and may be present on multiple lines
                    // The first chunk of this if statement should only be called by multiple row single line reports
                    if (columnHeadersMap[index] && columnHeadersMap[index][rowNum]) {
                        ReportGridField curReportGridField = columnHeadersMap[index].get(rowNum)
                        curReportGridField.name = column + ' ' + curReportGridField.name
                        columnHeadersMap[index].set(rowNum, curReportGridField)
                    } else {
                        ReportGridField reportGridField = new ReportGridField(column, 0, index)
                        if (!columnHeadersMap[index]) {
                            columnHeadersMap[index] = []
                        }
                        columnHeadersMap[index].add(reportGridField)
                    }
                }
            }
            line = startingLines.get(++i)
        }
        columnHeaderRecord = new Record(columnHeadersMap)
        println columnHeaderRecord
    }

    /*
     * Splits a string based on grid column endings and trims the resulting whitespace off
     *
     */
    def splitStringOnGridEndings(String line) {

        def splitString = []
        def startIndex = 0

        for (endingIndex in gridCellEndings) {
            def substr = line.substring(startIndex, endingIndex).trim()
            startIndex = endingIndex
            splitString.add(substr)
        }

        return splitString
    }

    /*
     * Parses a single line into a Record
     */
    def parseSingleLineRecord(String line) {

        def splitLine = splitStringOnGridEndings(line)
        Record record = columnHeaderRecord.clone() //starting off with a clone of column headings. Everything should be in the right format

        splitLine.eachWithIndex{ String column, int index ->
            if (column.trim()) {
                ReportGridField reportGridField = record.recordFields[index].get(0)
                reportGridField.value = column
                record.recordFields[index].set(0, reportGridField)
            }
        }
        records.add(record)
    }

    def parseMultiLineRecord(ArrayList<String> lines) {

    }

    def isLineRecordStart(String line) {
        def recordStart = false
        switch (line) {
            case RECORD_START_PATTERN :
                recordStart = true
        }
        recordStart
    }



}
