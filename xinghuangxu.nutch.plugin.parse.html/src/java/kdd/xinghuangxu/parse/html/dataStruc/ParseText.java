package kdd.xinghuangxu.parse.html.dataStruc;


import java.io.*;
import org.apache.hadoop.io.*;

/* The text conversion of page's content, stored using gzip compression.
 * @see Parse#getText()
 */
public final class ParseText implements Writable {
  public static final String DIR_NAME = "parse_text";

  private final static byte VERSION = 2;

  public ParseText() {}
  private String text;
    
  public ParseText(String text){
    this.text = text;
  }

  public void readFields(DataInput in) throws IOException {
    byte version = in.readByte();
    switch (version) {
    case 1:
      text = WritableUtils.readCompressedString(in);
      break;
    case VERSION:
      text = Text.readString(in);
      break;
    default:
      throw new VersionMismatchException(VERSION, version);
    }
  }

  public final void write(DataOutput out) throws IOException {
    out.write(VERSION);
    Text.writeString(out, text);
  }

  public final static ParseText read(DataInput in) throws IOException {
    ParseText parseText = new ParseText();
    parseText.readFields(in);
    return parseText;
  }

  //
  // Accessor methods
  //
  public String getText()  { return text; }

  public boolean equals(Object o) {
    if (!(o instanceof ParseText))
      return false;
    ParseText other = (ParseText)o;
    return this.text.equals(other.text);
  }

  public String toString() {
    return text;
  }

  public static void main(String argv[]) throws Exception {
//    String usage = "ParseText (-local | -dfs <namenode:port>) recno segment";
//
//    if (argv.length < 3) {
//      System.out.println("usage:" + usage);
//      return;
//    }
//    Options opts = new Options();
//    Configuration conf = NutchConfiguration.create();
//    
//    GenericOptionsParser parser =
//      new GenericOptionsParser(conf, opts, argv);
//    
//    String[] remainingArgs = parser.getRemainingArgs();
//    
//    FileSystem fs = FileSystem.get(conf);
//    try {
//      int recno = Integer.parseInt(remainingArgs[0]);
//      String segment = remainingArgs[1];
//      String filename = new Path(segment, ParseText.DIR_NAME).toString();
//
//      ParseText parseText = new ParseText();
//      ArrayFile.Reader parseTexts = new ArrayFile.Reader(fs, filename, conf);
//
//      parseTexts.get(recno, parseText);
//      System.out.println("Retrieved " + recno + " from file " + filename);
//      System.out.println(parseText);
//      parseTexts.close();
//    } finally {
//      fs.close();
//    }
  }
}

