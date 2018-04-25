package de.articdive;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.PropertyConfigurator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Created by Lukas Mansour on 4/22/18 10:45 AM
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

/**
 * Goal which allows you to call language files form PhraseApp.
 *
 * @goal phrase
 * @requiresOnline true
 * @phase process-resources
 * @since 1.0
 */
public class PhraseAppPlugin extends AbstractMojo {

	/**
	 * @parameter default-value="${project}"
	 * @required
	 * @readonly
	 * @since 1.0
	 */
	private MavenProject project = null;
	/**
	 * @parameter name="authToken"
	 * @required
	 * @since 1.0
	 */
	private String authToken = null;

	/**
	 * @parameter name="projectID"
	 * @required
	 * @since 1.0
	 */
	private String projectID = null;

	/**
	 * @parameter name="branch" default-value=""
	 * @since 1.0
	 */
	private String branch = null;

	/**
	 * @parameter name="ignoredLocales"
	 * @since 1.0
	 */
	private List<String> ignoredLocales = new ArrayList<String>();

	/**
	 * @parameter name="include_empty_translations"
	 * @since 2.0
	 */
	private boolean include_empty_translations = false;

	private HttpClient httpClient;

	public void execute() throws MojoFailureException {
		getLog().info("------------------------------------------------------------------------");
		getLog().info("PhraseApp | Maven Plugin | Start");
		getLog().info("------------------------------------------------------------------------");

		// Remove Log4J stupid annoying message
		Properties log4jProp = new Properties();
		log4jProp.setProperty("log4j.rootLogger", "WARN");
		PropertyConfigurator.configure(log4jProp);
		// Finish

		httpClient = HttpClientBuilder.create()
				.setDefaultRequestConfig(RequestConfig.custom()
						.setCookieSpec(CookieSpecs.STANDARD)
						.build())
				.build();

		getLog().info("Attempting connection to PhraseApp");

		String urlOverHttps = "https://api.phraseapp.com/api/v2/projects/" + projectID + "/locales" + "?access_token=" + authToken;
		if (branch != null) {
			urlOverHttps = urlOverHttps + "&branch=" + branch;
		}
		Header maintainerheader = new BasicHeader("User-Agent", "phraseapp-maven-plugin (articdive@gmail.com");
		Header userheader = new BasicHeader("User-Agent (End-User)", project.getArtifactId() + "(" + project.getOrganization() + ")");
		HttpGet getMethod = new HttpGet(urlOverHttps);
		getMethod.setHeader(maintainerheader);
		getMethod.addHeader(userheader);
		try {
			HttpResponse response = httpClient.execute(getMethod);
			if (handleStatusCodeRetrieveLocales(response.getStatusLine())) {
				String strresponse = new BasicResponseHandler().handleResponse(response);
				JSONArray jsonArray = new JSONArray(strresponse);
				for (int j = 0; j < jsonArray.length(); j++) {
					JSONObject jsonObject = jsonArray.getJSONObject(j);
					downloadLocale(jsonObject);
				}
				getLog().info("------------------------------------------------------------------------");
				getLog().info("PhraseApp | Maven Plugin | Finish");
				getLog().info("------------------------------------------------------------------------");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private boolean handleStatusCodeRetrieveLocales(StatusLine line) throws MojoFailureException {
		int statusCode = line.getStatusCode();
		if (statusCode == 200) {
			getLog().info("Successfully connected into PhraseApp API & Retrieved Locales");
			return true;
		} else {
			throw new MojoFailureException("Something went wrong: " + line.getReasonPhrase());
		}

	}

	private void downloadLocale(JSONObject jsonObject) throws MojoFailureException {
		String name = jsonObject.getString("name");
		String localeid = jsonObject.getString("id");
		if (ignoredLocales.contains(localeid)) {
			return;
		}
		getLog().info("Attempting to download locale with ID " + localeid + " ( " + name + " ) ");
		String urlOverHttps = "https://api.phraseapp.com/api/v2/projects/" + projectID + "/locales/" + localeid + "/download" + "?file_format=yml" + "&include_empty_translations=" + include_empty_translations + "&access_token=" + authToken;
		Header maintainerheader = new BasicHeader("User-Agent", "phraseapp-maven-plugin (articdive@gmail.com");
		Header userheader = new BasicHeader("User-Agent (End-User)", project.getArtifactId() + "(" + project.getOrganization() + ")");
		HttpGet getMethod = new HttpGet(urlOverHttps);
		getMethod.setHeader(maintainerheader);
		getMethod.addHeader(userheader);
		try {
			HttpResponse response = httpClient.execute(getMethod);
			if (handleStatusCodeDownloadLocale(response.getStatusLine(), localeid, name)) {
				String strresponse = new BasicResponseHandler().handleResponse(response);
				File file = new File(project.getBuild().getOutputDirectory() + "/" + name + ".yml");
				file.getParentFile().mkdirs();
				if (!file.exists()) {
					file.createNewFile();
				}
				BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath(), false));
				writer.write(strresponse);
				writer.flush();
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean handleStatusCodeDownloadLocale(StatusLine line, String localeid, String name) throws MojoFailureException {
		int statusCode = line.getStatusCode();
		if (statusCode == 200) {
			getLog().info("Successfully downloaded locale with ID " + localeid + " ( " + name + " ) ");
			return true;
		} else {
			throw new MojoFailureException("Something went wrong: " + line.getReasonPhrase());
		}
	}
}
