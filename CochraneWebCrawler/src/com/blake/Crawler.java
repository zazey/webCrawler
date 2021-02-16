package com.blake;


import java.util.ArrayList;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Crawler {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("Crawling...");

		// Establish session
		Document landingPage = HttpHandler.establishSession();

		// Main loop
		while (HttpHandler.getCurrentTopic() < HttpHandler.getMaxTopics()) {

			// Obtain the current review topic
			Element currentTopic = HttpHandler.findTopic(landingPage);

			// Obtain the first result from the first topic
			Document currentReview = HttpHandler.getResultsPage(currentTopic.select("a[href]").attr("href").toString());

			// Get a list of every results from the current topic
			ArrayList<String> allResultLinks = HttpHandler.getNextPagesUrl(currentReview);

			// Holder for the returned results
			ArrayList<Elements> allResults = new ArrayList<>();

			// Checking all next pages for additional results
			for (String pageUrl : allResultLinks) {
				Document page = HttpHandler.getResultsPage(pageUrl);
				Elements result = page.getElementsByClass("search-results-item-body");
				allResults.add(result);
			}

			// Save result data
			if (OutputWriter.writeToFile(allResults, currentTopic.text())) {
				System.out.println("Finished Current Topic");
				System.out.println((HttpHandler.getMaxTopics() - HttpHandler.getCurrentTopic()) + " topics to go." );
			}
			
		}

	}
}
