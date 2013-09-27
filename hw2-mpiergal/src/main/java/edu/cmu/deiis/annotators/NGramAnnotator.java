/** NGramAnnotator.java
 *  This annotates all the unigrams, bigrams and trigrams
 *  in the JCas. Unigrams are equivalent to tokens in this
 *  case. Bigrams and trigrams are detected using a regular
 *  expression that finds words separated only by spaces,
 *  commas, colons or semicolons.
 *  @author Mario Piergallini
 */

package edu.cmu.deiis.annotators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;
import java.util.ArrayList;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.JCas;
import edu.cmu.deiis.types.*;

//Annotator that identifies tokens using Java regular expressions

public class NGramAnnotator extends JCasAnnotator_ImplBase {
//create regular expression pattern for bigrams and trigrams
 private Pattern bigramPattern = Pattern.compile("(?<!(\\A|\\n(A )?))[\\w']+ ?[,;:]? [\\w']+");
 private Pattern trigramPattern = Pattern.compile("(?<!(\\A|\\n(A )?))[\\w']+ ?[,;:]? [\\w']+ ?[,;:]? [\\w']+");

 public void process(JCas aJCas) {
   
   //load tokens into an ArrayList
   FSIndex tokenIndex = aJCas.getAnnotationIndex(Token.type);
   Iterator tokenIter = tokenIndex.iterator();
   ArrayList<Token> tokens = new ArrayList<Token>();
   while (tokenIter.hasNext()) {
     Token t = (Token)tokenIter.next();
     tokens.add(t);
   }
   
   // get document text from JCas
   String docText = aJCas.getDocumentText();
   
   // make matchers for different NGrams
   Matcher bigramMatcher = bigramPattern.matcher(docText);
   Matcher trigramMatcher = trigramPattern.matcher(docText);
   
   //iterate through tokens
   int pos = 0;
   for(int i=0; i<tokens.size(); i++) {
     pos = tokens.get(i).getBegin();
     if (bigramMatcher.find(pos) && bigramMatcher.start()==pos) {
       // bigram match found - create the match as annotation in the JCas
       NGram bigram = new NGram(aJCas);
       bigram.setBegin(pos);
       bigram.setEnd(bigramMatcher.end());
       bigram.setElementType("bigram");
       bigram.setElements(new FSArray(aJCas, 2));
       bigram.setElements(0,tokens.get(i));
       bigram.setElements(1,tokens.get(i+1));
       bigram.addToIndexes();
     }
     if (trigramMatcher.find(pos) && trigramMatcher.start()==pos) {
       // trigram match found - create the match as annotation in the JCas
       NGram trigram = new NGram(aJCas);
       trigram.setBegin(pos);
       trigram.setEnd(trigramMatcher.end());
       trigram.setElementType("trigram");
       trigram.setElements(new FSArray(aJCas, 3));
       trigram.setElements(0,tokens.get(i));
       trigram.setElements(1,tokens.get(i+1));
       trigram.setElements(2,tokens.get(i+2));
       trigram.addToIndexes();
     }
     // add token as unigram annotation in the JCas
     NGram unigram = new NGram(aJCas);
     unigram.setBegin(pos);
     unigram.setEnd(tokens.get(i).getEnd());
     unigram.setElementType("unigram");
     unigram.setElements(new FSArray(aJCas, 1));
     unigram.setElements(0,tokens.get(i));
     unigram.addToIndexes();
   }
   
 }
}
