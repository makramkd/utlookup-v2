package org.lag.utlookup.interfaces;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.lag.utlookup.back.StGeorgeCrawler;

/**
 * Crawler base class that contains functionality that will be used in all it's subclasses.
 * This includes getting and parsing the HTML document upon construction. As such, an internet
 * connection is required.
 * @author Makram
 *
 */
public abstract class Crawler {

	/**
	 * This is the link to the new course finder which is a utility that can be used to search for course 
	 * metadata and availability information (there is no timetable information here).
	 * 
	 * @deprecated this URL is actually useless. Use either {@link StGeorgeCrawler#TIMETABLE_URL_FW} or {@link StGeorgeCrawler#CALENDAR_URL} for St. George.
	 */
	@Deprecated
	public static final String COURSE_FINDER_URL = "http://coursefinder.utoronto.ca/course-search/search/courseSearch?viewId=CourseSearch-FormView&methodToCall=start"; // will redirect to a more specific url
	
	protected Document document;
	private String url;
	
	/**
	 * Construct a crawler with the given URL. The crawler will then parse the HTML page and 
	 * construct a tree.
	 * @param url the URL you want to crawl.
	 */
	public Crawler(String url)
	{
		try {
			document = Jsoup.connect(url).get();
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
		this.url = url;
	}

	/**
	 * Get the url that this crawler has crawled.
	 * @return the url that this crawler has crawled.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Set the URL for the crawler to crawl. If this is the same as the previous URL,
	 * there is no effect.
	 * @param url the URL for the crawler to crawl
	 */
	public void setUrl(String url) {
		if (url.equals(this.url)) {
			return;
		}
		this.url = url;
		try {
			document = Jsoup.connect(url).timeout(0).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialize this crawler. The purpose of this function is to get all the required HTML documents and parse
	 * them, as well as keeping track internally of the used URLs. Typically, since the crawler really depends on the
	 * website that you are crawling (if you want to get any useful information), the caller must use the crawler that 
	 * is best suited for what they want to achieve.
	 * 
	 * For example, if you want to crawl the St. George campus's course information and timetable, you have to use the
	 * St. George crawler. Since there is more than one link to keep track of, the crawler is the one that manages these
	 * links and not the caller.
	 */
	public abstract void initialize();
	
	/**
	 * Begin the crawl. This method will tell the crawler to start fulfilling its responsibilities, depending on what it is
	 * supposed to crawl. 
	 * 
	 * For example, if this is the crawler of the St. George campus, it will do the following: access the calendar, to get all
	 * the currently offered courses in the university. Then, it will access the timetable, and get all the meeting sections for 
	 * all the offered courses. Some courses may not even be in the timetable. This is possible because not all courses that are
	 * listed in the calendar are offered during the year. Finally, it will populate its inner database with all this scraped information
	 * and through another interface (not the Crawler interface) provide access to it.
	 */
	public abstract void crawl();
}
