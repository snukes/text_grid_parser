package com.mitchellsheaffer.textgridparser

/**
 * Grid-based text parsing tool. Dynamically handles information laid out in grid form.
 */
class TextGridParser {

    static void main(String[] args) {

        def recordInfo = []
        // Hold File Location
        def holdFileName = ''
        File holdFile = new File(holdFileName )
        def startingLines = []
        def fields = [:]
        def foundColumnHeaders = false
        def recordPrefix

        if(!holdFile.exists()) {
            println "File does not exist"
        } else {
            // Step through each line in the file
            holdFile.eachLine { line ->

                if (!foundColumnHeaders) {
                    if (line.startsWith('----')) {
                        foundColumnHeaders = true
                        initializeFieldsMap(startingLines)
                    } else {
                        startingLines.add(line)
                    }

                } else {
                    line.trim()
                    def record = line.split('\\b\\s').collect { it.trim() }

                    recordInfo.add(record)
                }
            }
        }

        recordInfo.each { line ->
            //println line
        }
    }

    /*
     * Initial method to generate a list of fields returned in the text file
     * Identifies the grid boundaries
     */
    def static initializeFieldsMap(ArrayList<String> startingLines) {
        def map = [:]
        for (line in startingLines) {
            println(line)
        }
        map.'ID' = '001'
    }
}
