package kdd.xinghuangxu.parse.html.dataStruc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class Metadata implements Writable {

	/**
	 * A map of all metadata attributes. Map contains <Name,Values> pairs
	 */
	private Map<String, String[]> metadata = null;

	/**
	 * Constructs a new, empty metadata.
	 */
	public Metadata() {
		metadata = new HashMap<String, String[]>();
	}

	/**
	 * Returns true if named value is multivalued.
	 * 
	 * @param name
	 *            name of metadata
	 * @return true is named value is multivalued, false if single value or null
	 */
	public boolean isMultiValued(final String name) {
		return metadata.get(name) != null && metadata.get(name).length > 1;
	}

	/**
	 * Returns an array of the names contained in the metadata.
	 * 
	 * @return Metadata names
	 */
	public String[] names() {
		return metadata.keySet().toArray(new String[metadata.keySet().size()]);
	}

	/**
	 * Get the value associated to a metadata name. If many values are
	 * assiociated to the specified name, then the first one is returned.
	 * 
	 * @param name
	 *            of the metadata.
	 * @return the value associated to the specified metadata name.
	 */
	public String get(final String name) {
		String[] values = metadata.get(name);
		if (values == null) {
			return null;
		} else {
			return values[0];
		}
	}

	/**
	 * Get the values associated to a metadata name.
	 * 
	 * @param name
	 *            of the metadata.
	 * @return the values associated to a metadata name.
	 */
	public String[] getValues(final String name) {
		return _getValues(name);
	}

	private String[] _getValues(final String name) {
		String[] values = metadata.get(name);
		if (values == null) {
			values = null;
		}
		return values;
	}

	/**
	 * Add a metadata name/value mapping. Add the specified value to the list of
	 * values associated to the specified metadata name.
	 * 
	 * @param name
	 *            the metadata name.
	 * @param value
	 *            the metadata value.
	 */
	public void add(final String name, final String value) {
		String[] values = metadata.get(name);
		if (values == null) {
			set(name, value);
		} else {
			String[] newValues = new String[values.length + 1];
			System.arraycopy(values, 0, newValues, 0, values.length);
			newValues[newValues.length - 1] = value;
			metadata.put(name, newValues);
		}
	}

	/**
	 * Set metadata name/value. Associate the specified value to the specified
	 * metadata name. If some previous values were associated to this name, they
	 * are removed.
	 * 
	 * @param name
	 *            the metadata name.
	 * @param value
	 *            the metadata value.
	 */
	public void set(String name, String value) {
		metadata.put(name, new String[] { value });
	}

	/**
	 * Copy All key-value pairs from properties.
	 * 
	 * @param properties
	 *            properties to copy from
	 */
	public void setAll(Properties properties) {
		Enumeration<?> names = properties.propertyNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			metadata.put(name, new String[] { properties.getProperty(name) });
		}
	}

	/**
	 * Remove a metadata and all its associated values.
	 * 
	 * @param name
	 *            metadata name to remove
	 */
	public void remove(String name) {
		metadata.remove(name);
	}

	/**
	 * Returns the number of metadata names in this metadata.
	 * 
	 * @return number of metadata names
	 */
	public int size() {
		return metadata.size();
	}

	/** Remove all mappings from metadata. */
	public void clear() {
		metadata.clear();
	}

	/**
	 * Comparing if two metadata is equal to each other.
	 */
	public boolean equals(Object o) {

		if (o == null) {
			return false;
		}

		Metadata other = null;
		try {
			other = (Metadata) o;
		} catch (ClassCastException cce) {
			return false;
		}

		if (other.size() != size()) {
			return false;
		}

		String[] names = names();
		for (int i = 0; i < names.length; i++) {
			String[] otherValues = other._getValues(names[i]);
			String[] thisValues = _getValues(names[i]);
			if (otherValues.length != thisValues.length) {
				return false;
			}
			for (int j = 0; j < otherValues.length; j++) {
				if (!otherValues[j].equals(thisValues[j])) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Override toString method
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		String[] names = names();
		for (int i = 0; i < names.length; i++) {
			String[] values = _getValues(names[i]);
			for (int j = 0; j < values.length; j++) {
				buf.append(names[i]).append("=").append(values[j]).append(" ");
			}
		}
		return buf.toString();
	}

	/**
	 * For Writable Interface
	 * 
	 * Calls Text.readString(DataInput in) here which does: The function of
	 * hadoop Text: This class stores text using standard UTF8 encoding. It
	 * provides methods to serialize, deserialize, and compare texts at byte
	 * level. The type of length is integer and is serialized using
	 * zero-compressed format.
	 * 
	 * In addition, it provides methods for string traversal without converting
	 * the byte array to a string.
	 * 
	 * Also includes utilities for serializing/deserialing a string,
	 * coding/decoding a string, checking if a byte array contains valid UTF8
	 * code, calculating the length of an encoded string.
	 */
	@Override
	public void readFields(DataInput in) throws IOException {
		// 1. Read the Size of the Map
		int keySize = in.readInt();
		String key;
		for (int i = 0; i < keySize; i++) {
			// 2.Read the name of the pair
			key = Text.readString(in);
			// 3.Read the size of the value
			int valueSize = in.readInt();
			for (int j = 0; j < valueSize; j++) {
				// 4.Read the Value
				add(key, Text.readString(in));
			}
		}

	}

	/**
	 * For Writable Interface In this method we call Text.writeString() to write
	 * out the data in
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		// 1. First Write the Size of the Mapper Pairs
		out.writeInt(size());
		String[] values = null;
		String[] names = names();
		for (int i = 0; i < names.length; i++) {
			// 2. Write out the name of the pair
			Text.writeString(out, names[i]);
			values = _getValues(names[i]);
			// Count the number of Values in this pair
			int cnt = 0;
			for (int j = 0; j < values.length; j++) {
				if (values[j] != null)
					cnt++;
			}
			// 3. Write out the number of Values in this pair
			out.writeInt(cnt);

			for (int j = 0; j < values.length; j++) {
				if (values[j] != null) {
					// 4. Writ out all the values in the pair
					Text.writeString(out, values[j]);
				}
			}
		}

	}

}
