package kdd.xinghuangxu.parse.html.dataStruc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

/* An outgoing link from a page. */
public class Outlink implements Writable {

	private String toUrl;
	private String anchor;

	public Outlink() {
	}

	public Outlink(String toUrl, String anchor) throws MalformedURLException {
		this.toUrl = toUrl;
		if (anchor == null)
			anchor = "";
		this.anchor = anchor;
	}

	public void readFields(DataInput in) throws IOException {
		toUrl = Text.readString(in);
		anchor = Text.readString(in);
	}

	/** Skips over one Outlink in the input. */
	public static void skip(DataInput in) throws IOException {
		Text.skip(in); // skip toUrl
		Text.skip(in); // skip anchor
	}

	public void write(DataOutput out) throws IOException {
		Text.writeString(out, toUrl);
		Text.writeString(out, anchor);
	}

	public static Outlink read(DataInput in) throws IOException {
		Outlink outlink = new Outlink();
		outlink.readFields(in);
		return outlink;
	}

	public String getToUrl() {
		return toUrl;
	}

	public String getAnchor() {
		return anchor;
	}

	public void setUrl(String toUrl) {
		this.toUrl = toUrl;
	}

	public boolean equals(Object o) {
		if (!(o instanceof Outlink))
			return false;
		Outlink other = (Outlink) o;
		return this.toUrl.equals(other.toUrl)
				&& this.anchor.equals(other.anchor);
	}

	public String toString() {
		return "toUrl: " + toUrl + " anchor: " + anchor; // removed "\n".
															// toString, not
															// printLine... WD.
	}

}
