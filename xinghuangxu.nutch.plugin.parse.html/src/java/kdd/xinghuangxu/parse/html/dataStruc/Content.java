package kdd.xinghuangxu.parse.html.dataStruc;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.InflaterInputStream;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.UTF8;
import org.apache.hadoop.io.VersionMismatchException;
import org.apache.hadoop.io.Writable;
import org.apache.nutch.metadata.Metadata;



/**The content need to be parsed
 * 
 * It will be write under the content directory
 * @author xinghuang
 *
 */
public class Content implements Writable{

	
	  public static final String DIR_NAME = "content";

	  private final static int VERSION = -1;

	  private int version;

	  private String url;

	  private String base;

	  private byte[] content;

	  private String contentType;

	  private Metadata metadata;


	  public Content() {
	    metadata = new Metadata();
	  }

	  public Content(String url, String base, byte[] content, String contentType,
	      Metadata metadata) {

	    if (url == null)
	      throw new IllegalArgumentException("null url");
	    if (base == null)
	      throw new IllegalArgumentException("null base");
	    if (content == null)
	      throw new IllegalArgumentException("null content");
	    if (metadata == null)
	      throw new IllegalArgumentException("null metadata");

	    this.url = url;
	    this.base = base;
	    this.content = content;
	    this.metadata = metadata;
	    
	    this.contentType=contentType;

	    //this.contentType = getContentType(contentType, url, content);
	  }

	  
	  private final void readFieldsCompressed(DataInput in) throws IOException {
	    byte oldVersion = in.readByte();
	    switch (oldVersion) {
	    case 0:
	    case 1:
	      url = UTF8.readString(in); // read url
	      base = UTF8.readString(in); // read base

	      content = new byte[in.readInt()]; // read content
	      in.readFully(content);

	      contentType = UTF8.readString(in); // read contentType
	      // reconstruct metadata
	      int keySize = in.readInt();
	      String key;
	      for (int i = 0; i < keySize; i++) {
	        key = UTF8.readString(in);
	        int valueSize = in.readInt();
	        for (int j = 0; j < valueSize; j++) {
	          metadata.add(key, UTF8.readString(in));
	        }
	      }
	      break;
	    case 2:
	      url = Text.readString(in); // read url
	      base = Text.readString(in); // read base

	      content = new byte[in.readInt()]; // read content
	      in.readFully(content);

	      contentType = Text.readString(in); // read contentType
	      metadata.readFields(in); // read meta data
	      break;
	    default:
	      throw new VersionMismatchException((byte)2, oldVersion);
	    }

	  }
	  
	  
	  //Required by the Writable Interface
	  @Override
	  public final void readFields(DataInput in) throws IOException {
	    metadata.clear();
	    int sizeOrVersion = in.readInt();
	    if (sizeOrVersion < 0) { // version
	      version = sizeOrVersion;
	      switch (version) {
	      case VERSION:
	        url = Text.readString(in);
	        base = Text.readString(in);

	        content = new byte[in.readInt()];
	        in.readFully(content);

	        contentType = Text.readString(in);
	        metadata.readFields(in);
	        break;
	      default:
	        throw new VersionMismatchException((byte)VERSION, (byte)version);
	      }
	    } else { // size
	      byte[] compressed = new byte[sizeOrVersion];
	      in.readFully(compressed, 0, compressed.length);
	      ByteArrayInputStream deflated = new ByteArrayInputStream(compressed);
	      DataInput inflater =
	        new DataInputStream(new InflaterInputStream(deflated));
	      readFieldsCompressed(inflater);
	    }
	  }

	  public final void write(DataOutput out) throws IOException {
	    out.writeInt(VERSION);

	    Text.writeString(out, url); // write url
	    Text.writeString(out, base); // write base

	    out.writeInt(content.length); // write content
	    out.write(content);

	    Text.writeString(out, contentType); // write contentType

	    metadata.write(out); // write metadata
	  }

	  public static Content read(DataInput in) throws IOException {
	    Content content = new Content();
	    content.readFields(in);
	    return content;
	  }

	  //
	  // Accessor methods
	  //

	  /** The url fetched. */
	  public String getUrl() {
	    return url;
	  }

	  /** The base url for relative links contained in the content.
	   * Maybe be different from url if the request redirected.
	   */
	  public String getBaseUrl() {
	    return base;
	  }

	  /** The binary content retrieved. */
	  public byte[] getContent() {
	    return content;
	  }

	  public void setContent(byte[] content) {
	    this.content = content;
	  }

	  /** The media type of the retrieved content.
	   * @see <a href="http://www.iana.org/assignments/media-types/">
	   *      http://www.iana.org/assignments/media-types/</a>
	   */
	  public String getContentType() {
	    return contentType;
	  }

	  public void setContentType(String contentType) {
	    this.contentType = contentType;
	  }

	  /** Other protocol-specific data. */
	  public Metadata getMetadata() {
	    return metadata;
	  }

	  /** Other protocol-specific data. */
	  public void setMetadata(Metadata metadata) {
	    this.metadata = metadata;
	  }

	  public boolean equals(Object o) {
	    if (!(o instanceof Content)) {
	      return false;
	    }
	    Content that = (Content) o;
	    return this.url.equals(that.url) && this.base.equals(that.base)
	        && Arrays.equals(this.getContent(), that.getContent())
	        && this.contentType.equals(that.contentType)
	        && this.metadata.equals(that.metadata);
	  }

	  public String toString() {
	    StringBuffer buffer = new StringBuffer();

	    buffer.append("Version: " + version + "\n");
	    buffer.append("url: " + url + "\n");
	    buffer.append("base: " + base + "\n");
	    buffer.append("contentType: " + contentType + "\n");
	    buffer.append("metadata: " + metadata + "\n");
	    buffer.append("Content:\n");
	    buffer.append(new String(content)); // try default encoding

	    return buffer.toString();

	  }

	  public static void main(String argv[]) throws Exception {

//	    String usage = "Content (-local | -dfs <namenode:port>) recno segment";
//
//	    if (argv.length < 3) {
//	      System.out.println("usage:" + usage);
//	      return;
//	    }
//	    Options opts = new Options();
//	    Configuration conf = NutchConfiguration.create();
//	    
//	    GenericOptionsParser parser =
//	      new GenericOptionsParser(conf, opts, argv);
//	    
//	    String[] remainingArgs = parser.getRemainingArgs();
//	    FileSystem fs = FileSystem.get(conf);
//	    
//	    try {
//	      int recno = Integer.parseInt(remainingArgs[0]);
//	      String segment = remainingArgs[1];
//
//	      Path file = new Path(segment, DIR_NAME);
//	      System.out.println("Reading from file: " + file);
//
//	      ArrayFile.Reader contents = new ArrayFile.Reader(fs, file.toString(),
//	          conf);
//
//	      Content content = new Content();
//	      contents.get(recno, content);
//	      System.out.println("Retrieved " + recno + " from file " + file);
//
//	      System.out.println(content);
//
//	      contents.close();
//	    } finally {
//	      fs.close();
//	    }
	  }

//	  private String getContentType(String typeName, String url, byte[] data) {
//	    return contentType;
//	  }

	}




