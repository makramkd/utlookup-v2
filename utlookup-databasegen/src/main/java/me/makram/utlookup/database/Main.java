/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.makram.utlookup.database;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author admin
 */
public class Main {

    public static void main(String[] args) {
        DatabaseHelper instance = DatabaseHelper.instance();
        boolean init = instance.isInitialized();
        System.out.println("Database is initialized ? " + init);

        StGeorgeCrawler crawler = new StGeorgeCrawler(
                StGeorgeCrawler.CALENDAR_URL);
        List<Instructor> instructorList = crawler.getInstructorList();
        int[] insertions = instance.insertInstructors(instructorList);
        for (int i : insertions) {
            System.out.println(i);
        }
    }

    public static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
