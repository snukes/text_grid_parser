package com.mitchellsheaffer.textgridparser

/**
 * Created by mitchell2 on 2/22/2017.
 */
class Main {

    public static void main(String[] args) {
        def filename = ''
        def singleLine = true
        TextGridParser tgp = new TextGridParser(filename, singleLine)
        tgp.generateReport()
    }



}
