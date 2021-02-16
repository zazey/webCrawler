package com.blake;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Base;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HttpHandler {

	private static int currentTopic = 0;

	// This should be it's own method -- we get this from running topics.size();
	private static int maxTopics = 36;
	static Connection.Response loginForm;

	public static int getCurrentTopic() {
		return currentTopic;
	}

	public static void setCurrentTopic(int currentTopic) {
		HttpHandler.currentTopic = currentTopic;
	}

	public static int getMaxTopics() {
		return maxTopics;
	}

	public static void setMaxTopics(int maxTopics) {
		HttpHandler.maxTopics = maxTopics;
	}

	// Session to establish cookie
	public static Document establishSession() {
		String loginUrl = "https://www.cochranelibrary.com/c/portal/login";
		String topicPageURL = "https://www.cochranelibrary.com/browse-by-topic";

		try {
			loginForm = Jsoup.connect(topicPageURL).method(Connection.Method.GET).execute();
			Connection.Response mainPage = Jsoup.connect(loginUrl).cookies(loginForm.cookies()).execute();
			Map<String, String> cookies = mainPage.cookies();
			Document evaluationPage = Jsoup.connect(topicPageURL).cookies(cookies).execute().parse();

			return evaluationPage;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	// Find current topic and max topics
	public static Element findTopic(Document startingPage) {
		Elements allTopics = startingPage.getElementsByClass("browse-by-list-item");
		Element topicPosition = allTopics.get(currentTopic);
		maxTopics = allTopics.size();
		currentTopic++;

		return topicPosition;
	}

	// Obtaining each search result by using login cookies to remember previous
	// result
	public static Document getResultsPage(String resultURL) {
		try {
			Document result = Jsoup.connect(resultURL).cookies(loginForm.cookies()).execute().parse();
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	public static ArrayList<String> getNextPagesUrl(Document resultPage) {
		ArrayList<String> nextResult = new ArrayList<>();
		Elements allNextResults = resultPage.getElementsByClass("pagination-page-list-item");

		for (Element e : allNextResults) {
			nextResult.add(e.select("a[href]").attr("href").toString());
		}

		return nextResult;
	}

}
