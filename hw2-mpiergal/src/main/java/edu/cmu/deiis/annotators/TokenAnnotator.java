/* TokenAnnotator.java
 *  @author Mario Piergallini
 */

package edu.cmu.deiis.annotators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;
import edu.cmu.deiis.types.*;

//Annotator that identifies tokens using Java regular expressions

public class TokenAnnotator extends JCasAnnotator_ImplBase {
//create regular expression pattern for tokens, excluding the Q/A columns
 private Pattern tokenPattern = Pattern.compile("(?<!(\\A|\\n(A )?))[\\w']+");

 public void process(JCas aJCas) {
   
   System.out.println("TokenAnnotator called");
   
   // get document text from JCas
   String docText = aJCas.getDocumentText();
   
   // search for tokens
   Matcher matcher = tokenPattern.matcher(docText);
   int pos = 0;
   while (matcher.find(pos)) {
     // match found - create the match as annotation in the JCas
     Token token = new Token(aJCas);
     token.setBegin(matcher.start());
     token.setEnd(matcher.end());
     token.addToIndexes();
     pos = matcher.end();
   }
 }
}
