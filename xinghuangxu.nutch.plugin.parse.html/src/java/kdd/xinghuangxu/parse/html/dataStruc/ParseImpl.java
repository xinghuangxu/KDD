package kdd.xinghuangxu.parse.html.dataStruc;



import java.io.*;
import org.apache.hadoop.io.*;


/** The result of parsing a page's raw content.
 * @see Parser#getParse(Content)
 */
public class ParseImpl implements Parse, Writable {
  private ParseText text;
  private ParseData data;
  private boolean isCanonical;

  public ParseImpl() {}

  public ParseImpl(Parse parse) {
    this(new ParseText(parse.getText()), parse.getData(), true);
  }

  public ParseImpl(String text, ParseData data) {
    this(new ParseText(text), data, true);
  }
  
  public ParseImpl(ParseText text, ParseData data) {
    this(text, data, true);
  }

  public ParseImpl(ParseText text, ParseData data, boolean isCanonical) {
    this.text = text;
    this.data = data;
    this.isCanonical = isCanonical;
  }

  public String getText() { return text.getText(); }

  public ParseData getData() { return data; }

  public boolean isCanonical() { return isCanonical; }
  
  public final void write(DataOutput out) throws IOException {
    out.writeBoolean(isCanonical);
    text.write(out);
    data.write(out);
  }

  public void readFields(DataInput in) throws IOException {
    isCanonical = in.readBoolean();
    text = new ParseText();
    text.readFields(in);

    data = new ParseData();
    data.readFields(in);
  }

  public static ParseImpl read(DataInput in) throws IOException {
    ParseImpl parseImpl = new ParseImpl();
    parseImpl.readFields(in);
    return parseImpl;
  }

}
