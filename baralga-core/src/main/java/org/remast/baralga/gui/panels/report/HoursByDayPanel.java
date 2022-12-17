package org.remast.baralga.gui.panels.report;

import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import com.google.common.eventbus.Subscribe;
import info.clearthought.layout.TableLayout;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.joda.time.format.DateTimeFormat;
import org.remast.baralga.gui.model.report.HoursByDay;
import org.remast.baralga.gui.model.report.HoursByDayReport;
import org.remast.baralga.gui.panels.table.HoursByDayTableFormat;
import org.remast.baralga.gui.panels.table.HoursByDayTextFilterator;
import org.remast.swing.JSearchField;
import org.remast.swing.table.JHighligthedTable;
import org.remast.text.DurationFormat;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Panel for displaying the report of working hours by day.
 * @see HoursByDayReport
 * @author remast
 */
@SuppressWarnings("serial") //$NON-NLS-1$
public class HoursByDayPanel extends JXPanel {
	
	/** Format for one day in report. */
	private final DateFormat DAY_FORMAT = newDayFormat();

    /**
     * The report displayed by this panel.
     */
    private transient HoursByDayReport report;
    
    /**
     * The table model.
     */
    private DefaultEventTableModel<HoursByDay> tableModel;
    
    /**
     * Creates a new panel for the given report of hours by day.
     * @param report the report with hours by day
     */
    public HoursByDayPanel(final HoursByDayReport report) {
        this.report = report;
        this.setLayout(new BorderLayout());
        
        this.report.getEventBus().register(this);
        
        initialize();
    }

    /**
     * Set up GUI components.
     */
    private void initialize() {
		// Init search field and a list filtered list for the quick search
		final JSearchField searchField = new JSearchField();
		final MatcherEditor<HoursByDay> textMatcherEditor = new TextComponentMatcherEditor<>(searchField, new HoursByDayTextFilterator());
		final FilterList<HoursByDay> textFilteredIssues = new FilterList<>(this.report.getHoursByDay(), textMatcherEditor);

        tableModel = new DefaultEventTableModel<>(textFilteredIssues, new HoursByDayTableFormat());

        final JTable table = new JHighligthedTable(tableModel);
		TableComparatorChooser.install(
				table, 
				this.report.getHoursByDay(), 
				TableComparatorChooser.MULTIPLE_COLUMN_MOUSE
		);
        
        table.getColumn(table.getColumnName(0)).setCellRenderer(new DefaultTableRenderer(new FormatStringValue(DAY_FORMAT)));
        table.getColumn(table.getColumnName(1)).setCellRenderer(new DefaultTableRenderer(new FormatStringValue(new DurationFormat())));
        
        JScrollPane tableScrollPane = new JScrollPane(table);

		int border = 5;
		final double[][] size = {
				{ border, TableLayout.FILL, border}, // Columns
				{ border, TableLayout.PREFERRED, border, TableLayout.FILL } }; // Rows
		this.setLayout(new TableLayout(size));

		this.add(searchField, "1, 1");
		this.add(tableScrollPane, "1, 3");
    }

   @Subscribe
   public void update(final Object obj) {
        if (obj instanceof HoursByDayReport) {
            tableModel.fireTableDataChanged();
        }
    }

    public static DateFormat newDayFormat() {
        return new SimpleDateFormat(DateTimeFormat.patternForStyle("S-", Locale.getDefault()) + " EEEEEEEEE");
    }

}
