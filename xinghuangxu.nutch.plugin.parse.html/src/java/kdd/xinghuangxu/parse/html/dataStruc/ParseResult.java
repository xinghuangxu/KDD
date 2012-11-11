package kdd.xinghuangxu.parse.html.dataStruc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.io.Text;

/**
 * A utility class that stores result of a parse. Internally a ParseResult
 * stores &lt;{@link Text}, {@link Parse}&gt; pairs.
 * <p>
 * Parsers may return multiple results, which correspond to parts or other
 * associated documents related to the original URL.
 * </p>
 * <p>
 * There will be usually one parse result that corresponds directly to the
 * original URL, and possibly many (or none) results that correspond to derived
 * URLs (or sub-URLs).
 */
public class ParseResult implements Iterable<Map.Entry<Text, Parse>> {
	
	//Text and ParseImpl
	private Map<Text, Parse> parseMap;
	private String originalUrl;

	/**
	 * Create a container for parse results.
	 * 
	 * @param originalUrl
	 *            the original url from which all parse results have been
	 *            obtained.
	 */
	public ParseResult(String originalUrl) {
		parseMap = new HashMap<Text, Parse>();
		this.originalUrl = originalUrl;
	}

	/**
	 * Convenience method for obtaining {@link ParseResult} from a single
	 * <code>Parse</code> output.
	 * 
	 * @param url
	 *            canonical url.
	 * @param parse
	 *            single parse output.
	 * @return result containing the single parse output.
	 */
	public static ParseResult createParseResult(String url, Parse parse) {
		ParseResult parseResult = new ParseResult(url);
		parseResult.put(new Text(url), new ParseText(parse.getText()),
				parse.getData());
		return parseResult;
	}

	/**
	 * Checks whether the result is empty.
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return parseMap.isEmpty();
	}

	/**
	 * Return the number of parse outputs (both successful and failed)
	 */
	public int size() {
		return parseMap.size();
	}

	/**
	 * Retrieve a single parse output.
	 * 
	 * @param key
	 *            sub-url under which the parse output is stored.
	 * @return parse output corresponding to this sub-url, or null.
	 */
	public Parse get(String key) {
		return get(new Text(key));
	}

	/**
	 * Retrieve a single parse output.
	 * 
	 * @param key
	 *            sub-url under which the parse output is stored.
	 * @return parse output corresponding to this sub-url, or null.
	 */
	public Parse get(Text key) {
		return parseMap.get(key);
	}

	/**
	 * Store a result of parsing.
	 * 
	 * @param key
	 *            URL or sub-url of this parse result
	 * @param text
	 *            plain text result
	 * @param data
	 *            corresponding parse metadata of this result
	 */
	public void put(Text key, ParseText text, ParseData data) {
		put(key.toString(), text, data);
	}

	/**
	 * Store a result of parsing.
	 * 
	 * @param key
	 *            URL or sub-url of this parse result
	 * @param text
	 *            plain text result
	 * @param data
	 *            corresponding parse metadata of this result
	 * 
	 *
	 */
	public void put(String key, ParseText text, ParseData data) {
		parseMap.put(new Text(key),
				new ParseImpl(text, data, key.equals(originalUrl)));
	}

	/**
	 * Iterate over all entries in the &lt;url, Parse&gt; map.
	 */
	public Iterator<Entry<Text, Parse>> iterator() {
		return parseMap.entrySet().iterator();
	}

	/**
	 * Remove all results where status is not successful (as determined by
	 * </code>ParseStatus#isSuccess()</code>). Note that effects of this
	 * operation cannot be reversed.
	 */
	public void filter() {
		for (Iterator<Entry<Text, Parse>> i = iterator(); i.hasNext();) {
			Entry<Text, Parse> entry = i.next();
			if (!entry.getValue().getData().getStatus().isSuccess()) {
				System.out.println(entry.getKey()
						+ " is not parsed successfully, filtering");
				i.remove();
			}
		}

	}

	/**
	 * A convenience method which returns true only if all parses are
	 * successful. Parse success is determined by
	 * <code>ParseStatus#isSuccess()</code>.
	 */
	public boolean isSuccess() {
		for (Iterator<Entry<Text, Parse>> i = iterator(); i.hasNext();) {
			Entry<Text, Parse> entry = i.next();
			if (!entry.getValue().getData().getStatus().isSuccess()) {
				return false;
			}
		}
		return true;
	}
}
