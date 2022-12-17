package org.remast.baralga.gui.panels;

import com.google.common.eventbus.Subscribe;
import info.clearthought.layout.TableLayout;
import org.remast.baralga.gui.BaralgaMain;
import org.remast.baralga.gui.events.BaralgaEvent;
import org.remast.baralga.gui.model.PresentationModel;
import org.remast.baralga.gui.panels.report.AccummulatedActitvitiesPanel;
import org.remast.baralga.gui.panels.report.AllActitvitiesPanel;
import org.remast.baralga.gui.panels.report.HoursByDayPanel;
import org.remast.baralga.gui.panels.report.HoursByMonthPanel;
import org.remast.baralga.gui.panels.report.HoursByProjectChartPanel;
import org.remast.baralga.gui.panels.report.HoursByProjectPanel;
import org.remast.baralga.gui.panels.report.HoursByQuarterPanel;
import org.remast.baralga.gui.panels.report.HoursByWeekPanel;
import org.remast.baralga.gui.settings.UserSettings;
import org.remast.util.TextResourceBundle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author remast
 */
@SuppressWarnings("serial")//$NON-NLS-1$
public class FilteredActivitiesPane extends JPanel {

    /** The bundle for internationalized texts. */
    private static final TextResourceBundle textBundle = TextResourceBundle.getBundle(BaralgaMain.class);

    /** The model. */
    private PresentationModel model;

    /** The category that's shown right now. */
    private String shownCategory;

    /** The tab container for the categorized tabs. */
    private JTabbedPane tabs = new JTabbedPane();

    /** All categorized tabs. */
    private List<CategorizedTab> categorizedTabs = new ArrayList<>();

    // ------------------------------------------------
    // Tabs with their panels
    // ------------------------------------------------

    private transient AccummulatedActitvitiesPanel accummulatedActitvitiesPanel;
    private transient CategorizedTab accummulatedActitvitiesTab;

    private transient HoursByWeekPanel hoursByWeekPanel;
    private transient CategorizedTab hoursByWeekTab;

    private transient HoursByDayPanel hoursByDayPanel;
    private transient CategorizedTab hoursByDayTab;

    private transient HoursByMonthPanel hoursByMonthPanel;
    private transient CategorizedTab hoursByMonthTab;

    private transient HoursByQuarterPanel hoursByQuarterPanel;
    private transient CategorizedTab hoursByQuarterTab;

    private transient HoursByProjectPanel hoursByProjectPanel;
    private transient CategorizedTab hoursByProjectTab;

    private transient HoursByProjectChartPanel hoursByProjectChartPanel;
    private transient CategorizedTab hoursByProjectChartTab;

    private transient AllActitvitiesPanel filteredActitvitiesPanel;
    private transient CategorizedTab filteredActitvitiesTab;

    private transient DescriptionPanel descriptionPanel;
    private transient CategorizedTab descriptionTab;

    // ------------------------------------------------
    // Toggle buttons for tab categories
    // ------------------------------------------------

    private JPanel categoryButtonPanel = new JPanel();

	private JToggleButton generalButton = new JToggleButton(new AbstractAction(textBundle.textFor("Category.General"), new ImageIcon(getClass().getResource("/icons/gtk-dnd-multiple.png"))) {

	@Override
	public void actionPerformed(final ActionEvent event) {
	    FilteredActivitiesPane.this.toggleCategory("General"); //$NON-NLS-1$
	}

    });
    {
	generalButton.setBorder(null);
	generalButton.setToolTipText(textBundle.textFor("Category.General.ToolTipText"));
    }

	private JToggleButton timeButton = new JToggleButton(new AbstractAction(textBundle.textFor("Category.Time"), new ImageIcon(getClass().getResource("/icons/clock.png"))) {

	@Override
	public void actionPerformed(final ActionEvent event) {
	    FilteredActivitiesPane.this.toggleCategory("Time"); //$NON-NLS-1$
	}

    });
    {
	timeButton.setToolTipText(textBundle.textFor("Category.Time.ToolTipText"));
    }

	private JToggleButton projectButton = new JToggleButton(new AbstractAction(textBundle.textFor("Category.Project"), new ImageIcon(getClass().getResource("/icons/stock_calendar-view-day.png"))) {

	@Override
	public void actionPerformed(final ActionEvent event) {
	    FilteredActivitiesPane.this.toggleCategory("Project"); //$NON-NLS-1$
	}

    });
    {
	projectButton.setToolTipText(textBundle.textFor("Category.Project.ToolTipText"));
    }

    public FilteredActivitiesPane(final PresentationModel model) {
	super();
	this.model = model;
	this.model.getEventBus().register(this);

	initialize();
    }

    /**
     * Set up GUI components.
     */
    private void initialize() {
		final double[][] size = {
				{ TableLayout.FILL}, // Columns
		{ TableLayout.PREFERRED, TableLayout.FILL } }; // Rows
	this.setLayout(new TableLayout(size));

	int border = 5;
		categoryButtonPanel.setLayout(new TableLayout(new double [][] {{0, TableLayout.FILL, border, TableLayout.FILL, border, TableLayout.FILL}, {border, TableLayout.PREFERRED, border - 3}}));
	categoryButtonPanel.add(generalButton, "1, 1"); //$NON-NLS-1$
	categoryButtonPanel.add(timeButton, "3, 1"); //$NON-NLS-1$
	categoryButtonPanel.add(projectButton, "5, 1"); //$NON-NLS-1$
	this.add(categoryButtonPanel, "0, 0"); //$NON-NLS-1$

	shownCategory = UserSettings.instance().getShownCategory();

	filteredActitvitiesTab = new CategorizedTab("General");
	filteredActitvitiesPanel = new AllActitvitiesPanel(model);
		filteredActitvitiesTab.setComponent(
				textBundle.textFor("FilteredActivitiesPane.Tab.AllActivities"),  //$NON-NLS-1$
		null,
				//				new ImageIcon(getClass().getResource("/icons/gtk-dnd-multiple.png")),  //$NON-NLS-1$
				filteredActitvitiesPanel, 
				textBundle.textFor("FilteredActivitiesPane.Tab.AllActivities.Tooltip") //$NON-NLS-1$
	);
	categorizedTabs.add(filteredActitvitiesTab);

	accummulatedActitvitiesTab = new CategorizedTab("General"); //$NON-NLS-1$
	accummulatedActitvitiesPanel = new AccummulatedActitvitiesPanel(model.getFilteredReport());
		accummulatedActitvitiesTab.setComponent(
				textBundle.textFor("FilteredActivitiesPane.Tab.AccumulatedActivities"),  //$NON-NLS-1$
		null,
				//				new ImageIcon(getClass().getResource("/icons/gnome-calculator.png")),  //$NON-NLS-1$
		accummulatedActitvitiesPanel,
		textBundle.textFor("FilteredActivitiesPane.Tab.AccumulatedActivities.Tooltip") //$NON-NLS-1$
	);
	categorizedTabs.add(accummulatedActitvitiesTab);

	descriptionTab = new CategorizedTab("General");
	descriptionPanel = new DescriptionPanel(model);
		descriptionTab.setComponent(
				textBundle.textFor("FilteredActivitiesPane.Tab.Descriptions"),  //$NON-NLS-1$
		null,
		// new ImageIcon(getClass().getResource("/icons/gnome-mime-text-x-readme.png")),
				descriptionPanel, 
				textBundle.textFor("FilteredActivitiesPane.Tab.Descriptions.Tooltip") //$NON-NLS-1$
	);
	categorizedTabs.add(descriptionTab);

	hoursByWeekTab = new CategorizedTab("Time");
	hoursByWeekPanel = new HoursByWeekPanel(model.getHoursByWeekReport());
		hoursByWeekTab.setComponent(
				textBundle.textFor("FilteredActivitiesPane.Tab.HoursByWeek"),  //$NON-NLS-1$
		null,
				//				new ImageIcon(getClass().getResource("/icons/stock_calendar-view-work-week.png")),  //$NON-NLS-1$
				hoursByWeekPanel, 
				textBundle.textFor("FilteredActivitiesPane.Tab.HoursByWeek.Tooltip") //$NON-NLS-1$
	);
	categorizedTabs.add(hoursByWeekTab);

	hoursByDayTab = new CategorizedTab("Time");
	hoursByDayPanel = new HoursByDayPanel(model.getHoursByDayReport());
		hoursByDayTab.setComponent(
				textBundle.textFor("FilteredActivitiesPane.Tab.HoursByDay"),  //$NON-NLS-1$
		null,
				//				new ImageIcon(getClass().getResource("/icons/stock_calendar-view-day.png")),  //$NON-NLS-1$
				hoursByDayPanel, 
				textBundle.textFor("FilteredActivitiesPane.Tab.HoursByDay.Tooltip") //$NON-NLS-1$
	);
	categorizedTabs.add(hoursByDayTab);

	hoursByMonthTab = new CategorizedTab("Time");
	hoursByMonthPanel = new HoursByMonthPanel(model.getHoursByMonthReport());
		hoursByMonthTab.setComponent(
				textBundle.textFor("FilteredActivitiesPane.Tab.HoursByMonth"),  //$NON-NLS-1$
				null,
				hoursByMonthPanel, 
				textBundle.textFor("FilteredActivitiesPane.Tab.HoursByMonth.Tooltip") //$NON-NLS-1$
	);
	categorizedTabs.add(hoursByMonthTab);

	hoursByQuarterTab = new CategorizedTab("Time");
	hoursByQuarterPanel = new HoursByQuarterPanel(model.getHoursByQuarterReport());
	hoursByQuarterTab.setComponent(textBundle.textFor("FilteredActivitiesPane.Tab.HoursByQuarter"), //$NON-NLS-1$
		null, hoursByQuarterPanel, textBundle.textFor("FilteredActivitiesPane.Tab.HoursByQuarter.Tooltip") //$NON-NLS-1$
	);
	categorizedTabs.add(hoursByQuarterTab);

	hoursByProjectTab = new CategorizedTab("Project");
	hoursByProjectPanel = new HoursByProjectPanel(model.getHoursByProjectReport());
		hoursByProjectTab.setComponent(
				textBundle.textFor("FilteredActivitiesPane.Tab.HoursByProject"),  //$NON-NLS-1$
		null,
				//				new ImageIcon(getClass().getResource("/icons/stock_calendar-view-day.png")),  //$NON-NLS-1$
				hoursByProjectPanel, 
				textBundle.textFor("FilteredActivitiesPane.Tab.HoursByProject.Tooltip") //$NON-NLS-1$
	);
	categorizedTabs.add(hoursByProjectTab);

	hoursByProjectChartTab = new CategorizedTab("Project");
	hoursByProjectChartPanel = new HoursByProjectChartPanel(model.getHoursByProjectReport());
		hoursByProjectChartTab.setComponent(
				textBundle.textFor("FilteredActivitiesPane.Tab.HoursByProjectChart"),  //$NON-NLS-1$
		null,
				//				new ImageIcon(getClass().getResource("/icons/stock_calendar-view-day.png")),  //$NON-NLS-1$
				hoursByProjectChartPanel, 
				textBundle.textFor("FilteredActivitiesPane.Tab.HoursByProjectChart.Tooltip") //$NON-NLS-1$
	);
	categorizedTabs.add(hoursByProjectChartTab);

	this.initCategorizedTabs();

	this.initToggleButtons();

	this.add(tabs, "0, 1"); //$NON-NLS-1$
    }

    /**
     * Initializes the toggle buttons for the categories from the settings.
     */
    private void initToggleButtons() {
	// 1. Deselect all buttons
	generalButton.setSelected(false);
	timeButton.setSelected(false);
	projectButton.setSelected(false);

	// 2. Select shown button
		if (Objects.equals("General", shownCategory)) { //$NON-NLS-1$
	    generalButton.setSelected(true);
		} else if (Objects.equals("Time", shownCategory)) { //$NON-NLS-1$
	    timeButton.setSelected(true);
		} else if (Objects.equals("Project", shownCategory)) { //$NON-NLS-1$
	    projectButton.setSelected(true);
	}
    }

    /**
     * Initializes the categorized tabs from the settings.
     */
    private void initCategorizedTabs() {
	tabs.removeAll();

	// Show tabs of the shown category.
	for (CategorizedTab tab : categorizedTabs) {
	    if (Objects.equals(shownCategory, tab.getCategory())) {
		this.addCategorizedTab(tab);
	    }
	}

	// Update visibility depending on filter
	updateTabVisibility();
    }

    /**
     * Processes the action that the user toggles a category button.
	 * @param category the toggled category
     */
    private void toggleCategory(final String category) {
	// 1. Store category
	// a) internally
	shownCategory = category;

	// b) in user settings
	UserSettings.instance().setShownCategory(category);

	// 2. Set tab visibility
	initCategorizedTabs();

	// 3. Deselect all categoryToggleButtons except the one toggled
	initToggleButtons();
    }

    /**
     * Add a categorized tab to the tabs.
	 * @param tab the tab to add
     */
    private void addCategorizedTab(final CategorizedTab tab) {
	if (tab == null) {
	    return;
	}

	tabs.addTab(tab.getTitle(), tab.getIcon(), tab.getComponent(), tab.getTooltip());
    }

    @Subscribe 
    public void update(final Object eventObject) {
	if (!(eventObject instanceof BaralgaEvent)) {
	    return;
	}

	final BaralgaEvent event = (BaralgaEvent) eventObject;

	switch (event.getType()) {

	case BaralgaEvent.FILTER_CHANGED:
	    updateTabVisibility();
	    break;

		default: break;

	}
    }

    /**
     * Updates the visibility of tabs. E.g. if there is a filter for 
     * a day it does not make sense to display the hours by month.
     */
    private void updateTabVisibility() {
	if (!Objects.equals(shownCategory, "Time")) {
	    return;
	}

	// First remove hours by week and month.
	tabs.remove(hoursByWeekTab.getComponent());
	tabs.remove(hoursByMonthTab.getComponent());
	tabs.remove(hoursByQuarterTab.getComponent());

	switch (model.getFilter().getSpanType()) {
	case Day:
	    // Don't display hours by week or month.
	    break;

	case Week:
	    // Don't display hours by month.
	    addCategorizedTab(hoursByWeekTab);
	    break;

	case Month:
	    addCategorizedTab(hoursByWeekTab);
	    addCategorizedTab(hoursByMonthTab);
	    // Don't display hours by quarter.
	    break;

	case Quarter:
	case Year:
	    addCategorizedTab(hoursByWeekTab);
	    addCategorizedTab(hoursByMonthTab);
	    addCategorizedTab(hoursByQuarterTab);
	    break;
	}
    }

    /**
     * A tab belonging to a category.
     */
    private static class CategorizedTab {

	/** The category of the tab. */
	private String category;

	/** The title of the tab. */
	private String title;

	/** The icon of the tab. */
	private Icon icon;

	/** The tooltip of the tab. */
	private String tooltip;

	/** The component displayed in the tab. */
	private Component component;

	private CategorizedTab(final String category) {
	    this.category = category;
	}

	private void setComponent(final String title, final Icon icon, final Component component, final String tooltip) {
	    this.title = title;
	    this.icon = icon;
	    this.component = component;
	    this.tooltip = tooltip;
	}

	/**
	 * @return the category
	 */
	private String getCategory() {
	    return category;
	}

	/**
	 * @return the title
	 */
	private String getTitle() {
	    return title;
	}

	/**
	 * @return the icon
	 */
	private Icon getIcon() {
	    return icon;
	}

	/**
	 * @return the component
	 */
	private Component getComponent() {
	    return component;
	}

	/**
	 * @return the tip
	 */
	private String getTooltip() {
	    return tooltip;
	}
    }

}
