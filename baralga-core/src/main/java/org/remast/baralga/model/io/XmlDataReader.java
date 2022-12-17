//---------------------------------------------------------
// $Id$ 
// 
// (c) 2011 Cellent Finance Solutions AG 
//          Calwer Strasse 33 
//          70173 Stuttgart 
//          www.cellent-fs.de 
//--------------------------------------------------------- 
package org.remast.baralga.model.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.text.StringEscapeUtils;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.remast.baralga.model.Project;
import org.remast.baralga.model.ProjectActivity;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Reads data from an xml file.
 * @author remast
 */
public class XmlDataReader extends DefaultHandler {

	/** The imported projects. */
	private Collection<Project> projects = new ArrayList<>();

	/** The imported activites. */
	private Collection<ProjectActivity> activities = new ArrayList<>();
	
	/** Temporary string buffer. */
	private String currentBuffer;

	@SuppressWarnings("unused")
	private int version = -1;

	/** Temporary project. */
	private Project currentProject;
	
	/** Temporary activity. */
	private ProjectActivity currentActivity;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		currentBuffer = null;
		
		if ("baralga".equals(qName)) {
			version = Integer.parseInt(attributes.getValue("version"));
		} else if ("project".equals(qName)) {
			currentProject = new Project(attributes.getValue("id"), null, null);
			
			if (attributes.getValue("active") != null) {
			  currentProject.setActive(Boolean.parseBoolean(attributes.getValue("active")));
			}
		} else if ("activity".equals(qName)) {
			DateTime start = ISODateTimeFormat.dateHourMinute().parseDateTime(attributes.getValue("start"));
			DateTime end = ISODateTimeFormat.dateHourMinute().parseDateTime(attributes.getValue("end"));

			String projectId = attributes.getValue("projectReference");
			Project project = null;
			for (Project tmpProject : projects) {
				if (tmpProject.getId().equals(projectId)) {
					project = tmpProject;
					break;
				}
			}
			
			currentActivity = new ProjectActivity(start, end, project);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		if ("project".equals(qName)) {
			projects.add(currentProject);
			currentProject = null;
		} else if ("title".equals(qName)) {
			if (currentProject != null) {
				currentProject.setTitle(currentBuffer);
			}
		} else if ("description".equals(qName)) {
			if (currentActivity != null) {
				currentActivity.setDescription(StringEscapeUtils.unescapeHtml4(currentBuffer));
			}
		} else if ("activity".equals(qName)) {
			activities.add(currentActivity);
			currentActivity = null;
		}
		
	}

	@Override
	public void characters(char[] character, int start, int length) {
		final String currentCharacters = new String(character, start, length);
		if (currentBuffer == null) {
			currentBuffer = currentCharacters;
		} else {
			currentBuffer += currentCharacters;
		}
	}

	/**
	 * Actually read the data from file.
	 * @throws IOException
	 */
	public void read(final File file) throws IOException {
		try (InputStream fis = new FileInputStream(file)) {
			read(fis);
		} catch (IOException e) {
			throw new IOException("The file " + (file != null ? file.getName() : "<null>") + " does not contain valid Baralga data.", e);
		}
	}

	/**
	 * Read the data from an {@link InputStream}.
	 * @throws IOException
	 */
	public void read(final InputStream input) throws IOException {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(input, this);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	/**
	 * Getter for the projects of the data file.
	 */
	public Collection<Project> getProjects() {
		return projects;
	}

	/**
	 * Getter for the activities of the data file.
	 */
	public Collection<ProjectActivity> getActivities() {
		return activities;
	}

}
