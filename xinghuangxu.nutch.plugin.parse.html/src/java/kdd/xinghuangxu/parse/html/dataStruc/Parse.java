package kdd.xinghuangxu.parse.html.dataStruc;




/** The result of parsing a page's raw content.
 * @see Parser#getParse(Content)
 */
public interface Parse {
  
  /** The textual content of the page. This is indexed, searched, and used when
   * generating snippets.*/ 
  String getText();

  /** Other data extracted from the page. */
  ParseData getData();
  
  /** Indicates if the parse is coming from a url or a sub-url */
  boolean isCanonical();
}
