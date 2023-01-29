package org.jamali.announcement.exchange;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jamali.announcement.utils.Client;
import org.jamali.announcement.utils.Model;
import org.jamali.announcement.utils.Util;

/**
 * 
 * @author Ibrahim Jamali 
 * this class is the super class of all exchanges in this program
 */
public abstract class Exchange implements Runnable {

	private static final Logger logger = LogManager.getLogger(Exchange.class);
	/**
	 * this is the http client pbject
	 */
	private Client client;
	private Properties properties;
	private String name;
	/**
	 * condition for special alert about some coins or tokens
	 */
	private String condition;

	private String url;
	private String announcementFile;
	private Pattern pattern;
	private HashSet<Model> oldAnnouncements;
	private String referralLink;
	private String keywords;

	/**
	 * 
	 * @param client
	 * @param properties
	 */
	public Exchange(Client client, Properties properties) {
		this.name = this.getClass().getSimpleName().toLowerCase();
		this.setProperties(properties);
		this.setUrl(properties.getProperty(this.name + "_announcement_url"));
		this.setPattern(Pattern.compile(properties.getProperty(this.name + "_keywords"), Pattern.CASE_INSENSITIVE));
		this.setAnnouncementFile(properties.getProperty(this.name + "_storage_file"));
		this.setReferralLink(properties.getProperty(this.name + "_refferal"));
		this.setOldAnnouncements(Util.getHashSetFromFile(this.getAnnouncementFile()).stream().map(Model::new)
				.collect(Collectors.toCollection(HashSet::new)));
		this.setKeywords(properties.getProperty("special_keywords", ""));
		this.setClient(client);
	}

	/**
	 * 
	 * @param oldAnnouncements
	 */
	public void setOldAnnouncements(HashSet<Model> oldAnnouncements) {
		if (oldAnnouncements != null)
			this.oldAnnouncements = oldAnnouncements;
	}

	/**
	 * 
	 * @param keywords
	 */
	public void setPattern(Pattern keywords) {
		if (keywords != null)
			this.pattern = keywords;
		else
			throw new RuntimeException(this.name + "_keywords must be not null");
	}

	/**
	 * 
	 * @param keywords
	 */
	public void setKeywords(String keywords) {
		if (keywords != null)
			this.keywords = keywords;
		else
			throw new RuntimeException("keywords must be not null");
	}

	/**
	 * 
	 * @return
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * 
	 * @return
	 */
	public Pattern getPattern() {
		return pattern;
	}

	/**
	 * 
	 * @return old announcements
	 */
	public HashSet<Model> getOldAnnouncements() {
		return oldAnnouncements;
	}

	/**
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		if (url != null)
			this.url = url;
		else
			throw new RuntimeException("URL must be not null");
	}

	/**
	 * 
	 * @param url
	 */
	public void setReferralLink(String url) {
		if (url != null)
			this.referralLink = url;
		else
			throw new RuntimeException("referralLink must be not null");
	}

	/**
	 * 
	 * @return
	 */
	public String getReferralLink() {
		return referralLink;
	}

	/**
	 * 
	 * @param client
	 */
	public void setClient(Client client) {
		if (url != null)
			this.client = client;
		else
			throw new RuntimeException("client must be not null");

	}

	/**
	 * 
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 
	 * @param announcementFile
	 */
	public void setAnnouncementFile(String announcementFile) {
		if (announcementFile != null)
			this.announcementFile = announcementFile;
		else
			throw new RuntimeException("announcementFile must be not null");
	}

	/**
	 * 
	 * @return
	 */
	public String getAnnouncementFile() {
		return announcementFile;
	}

	/**
	 * 
	 * @param properties
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * 
	 * @return
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * 
	 * @return
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * 
	 * @param announcements
	 * @return
	 */
	public abstract HashSet<Model> parse(String announcements);

	/**
	 * 
	 * @return
	 */
	public abstract String request();

	/**
	 * 
	 * @param url
	 * @param message notification method for telegram only you could add twitter
	 *                notification
	 */
	public void notify(String url, String message) {
		boolean priorityNotification = Pattern.compile(this.keywords, Pattern.CASE_INSENSITIVE).matcher(message).find();
		boolean condition = priorityNotification || pattern.matcher(message).find();
		if (this.pattern.toString().equals("special_keywords"))
			condition = priorityNotification;

		if (condition) {
			StringBuffer body = new StringBuffer(
					"🪙️ <a href=\"" + referralLink + "\">" + name.toUpperCase() + "</a> : ");
			if (priorityNotification)
				body.append("🚨🚨🚨🚨🚨🚨🚨🚨\n");
			body.append(message.replace("%return%", "\n")).append("\n📢 For more details click on the link bellow \n\n")
					.append("🔗 <a href=\"" + url + "\"><b>click here to read more ...</b></a>");
//			System.out.println(body);
			Util.telegram(this.client, body.toString());
		}
	}

	/**
	 * 
	 * @param condition
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

	/**
	 * 
	 * @return
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * the main method to be run every timeUnit defined in subclasses
	 */
	@Override
	public void run() {
		Pattern pattern = Pattern.compile(this.condition, Pattern.CASE_INSENSITIVE);
		String announcements = this.request();
		if (announcements != null && pattern.matcher(announcements).find()) {
			HashSet<Model> newAnnouncements = this.parse(announcements);
			if (newAnnouncements != null && newAnnouncements.size() > 0
					&& !newAnnouncements.equals(this.getOldAnnouncements())) {
				Set<Model> differences = new HashSet<Model>(newAnnouncements);
				differences.removeAll(this.getOldAnnouncements());
				this.setOldAnnouncements(newAnnouncements);
				Util.putHashSetInFile(this.getAnnouncementFile(), newAnnouncements.parallelStream().map(Model::toString)
						.collect(Collectors.toCollection(HashSet::new)));
				for (Model model : differences) {
					notify(model.getCode(), model.getTitle());
				}
			}
		} else {
			logger.error("There is a problem with api endpoint of " + this.name);
		}

	}

	/**
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}
}
