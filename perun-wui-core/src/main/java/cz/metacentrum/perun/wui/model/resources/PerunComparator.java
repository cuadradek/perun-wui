package cz.metacentrum.perun.wui.model.resources;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.model.GeneralObject;
import cz.metacentrum.perun.wui.model.beans.Facility;
import cz.metacentrum.perun.wui.model.beans.Owner;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Comparator for any Perun's object - it makes a GeneralObject from them.
 *
 * @author Vaclav Mach <374430@mail.muni.cz>
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunComparator<T extends JavaScriptObject> implements Comparator<T> {

	private PerunColumnType column;

	/**
	 * Creates a new Comparator with specified column/property to sort by
	 *
	 * @param column
	 */
	public PerunComparator(PerunColumnType column) {
		this.column = column;
	}

	/**
	 * Safely compares two strings using browser's locale settings.
	 *
	 * @param o1 string to compare
	 * @param o2 string to compare with
	 * @return comparison result used in comparators
	 */
	public static final native int nativeCompare(String o1, String o2) /*-{
        if (o1 == null) return -1;
        if (o2 == null) return 1;
        return o1.localeCompare(o2);
    }-*/;

	/**
	 * Return Comparator<String> which uses browser's localeCompare().
	 *
	 * @return localized Comparator<String>
	 */
	public static final Comparator<String> getNativeComparator() {
		return new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				if (o1 == null) return -1;
				if (o2 == null) return 1;
				return nativeCompare(o1, o2);
			}
		};
	}

	/**
	 * Compares the two objects
	 *
	 * @param obj1 First object
	 * @param obj2 Second object
	 */
	@Override
	public int compare(T obj1, T obj2) {

		if (obj1 == null) return -1;
		if (obj2 == null) return 1;

		GeneralObject o1 = obj1.cast();
		GeneralObject o2 = obj2.cast();

		if (PerunColumnType.ID.equals(this.column)) return this.compareById(o1, o2);
		if (PerunColumnType.NAME.equals(this.column)) return this.compareByName(o1, o2);
		if (PerunColumnType.DESCRIPTION.equals(this.column)) return this.compareByDescription(o1, o2);

		// VO columns
		if (PerunColumnType.SHORT_NAME.equals(this.column)) return this.compareByShortName(o1, o2);

		// Facility columns
		if (PerunColumnType.OWNERS.equals(this.column)) return this.compareByOwnersNames(o1, o2);

		return 0;

	}

	/**
	 * Compares GeneralObjects by ID.
	 *
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareById(GeneralObject o1, GeneralObject o2) {
		return o1.getId() - o2.getId();
	}

	/**
	 * Compares GeneralObjects by the name
	 *
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareByName(GeneralObject o1, GeneralObject o2) {
		return nativeCompare(o1.getName(), o2.getName());
	}

	/**
	 * Compares GeneralObjects by the description
	 *
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareByDescription(GeneralObject o1, GeneralObject o2) {
		return nativeCompare(o1.getDescription(), o2.getDescription());
	}

	/**
	 * Compares VirtualOrganizations by ShortName
	 *
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareByShortName(GeneralObject o1, GeneralObject o2) {

		Vo vo1 = o1.cast();
		Vo vo2 = o2.cast();
		return nativeCompare(vo1.getShortName(), vo2.getShortName());

	}

	/**
	 * Compares Facilities by their Owner's names (only technical)
	 *
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareByOwnersNames(GeneralObject o1, GeneralObject o2) {

		Facility facility1 = o1.cast();
		Facility facility2 = o2.cast();

		ArrayList<String> result = new ArrayList<>();
		for (Owner o : facility1.getOwners()) {
			if (o.getType().equals("technical")) result.add(o.getName());
		}

		ArrayList<String> result2 = new ArrayList<>();
		for (Owner o : facility2.getOwners()) {
			if (o.getType().equals("technical")) result2.add(o.getName());
		}

		Collections.sort(result, getNativeComparator());
		Collections.sort(result2, getNativeComparator());

		return nativeCompare(Utils.join(result, ", "), Utils.join(result2, ", "));

	}

}