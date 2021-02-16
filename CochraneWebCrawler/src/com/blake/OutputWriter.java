package com.blake;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OutputWriter {

	public static boolean writeToFile(ArrayList<Elements> results, String topic) {
		String homePage = "https://www.cochranelibrary.com";
		String output = "outputResults/" + "Reviews.txt";

		try (FileWriter file = new FileWriter(output); BufferedWriter writer = new BufferedWriter(file)) {

			for (Elements result : results) {
				for (Element e : result) {
					writer.write(homePage + e.getElementsByClass("result-title").select("a[href]").attr("href") + "|"
							+ topic + "|" + e.getElementsByClass("result-title").text() + "|"
							+ e.getElementsByClass("search-result-authors").text() + "|"
							+ e.getElementsByClass("search-result-date").text());
					writer.newLine();
				}
			}

			file.flush();

		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
