package com.example.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args ) throws IOException
    {
        URL url = new URL("http://localhost:8080/");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        Scanner sc = new Scanner(conn.getInputStream());
        while (sc.hasNext()) {
            System.out.println(sc.nextLine());
        }
        sc.close();
    }
}
