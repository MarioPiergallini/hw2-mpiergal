/** QuestionAnnotator.java
 *  This annotator simply marks the question in the JCas
 *  and associates it with the NGrams contained within it.
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

//Annotator that identifies questions using Java regular expressions

public class QuestionAnnotator extends JCasAnnotator_ImplBase {
//create regular expression pattern for questions
 private Pattern questionPattern = Pattern.compile("\\AQ [\\w' ]+");

 public void process(JCas aJCas) {
   
   // get document text from JCas
   String docText = aJCas.getDocumentText();
   
   //load ngrams into an ArrayList
   FSIndex nGramIndex = aJCas.getAnnotationIndex(NGram.type);
   Iterator nGramIter = nGramIndex.iterator();
   ArrayList<NGram> ngrams = new ArrayList<NGram>();
   while (nGramIter.hasNext()) {
     NGram ng = (NGram)nGramIter.next();
     ngrams.add(ng);
   }
   
   // make matchers for different NGrams
   Matcher qMatch = questionPattern.matcher(docText);
   
   //set indices for uni-,bi-,trigrams for adding them to FSArrays
   int unidex = 0;
   int bidex = 0;
   int tridex = 0;
   //iterate over question matches in text
   int pos = 0;
   while (qMatch.find(pos)) {
     //found a question, create question annotation
     Question question = new Question(aJCas);
     question.setBegin(qMatch.start());
     question.setEnd(qMatch.end()+1);
     int numWords = qMatch.group().split(" ").length - 1;
     question.setUnigrams(new FSArray(aJCas,numWords));
     question.setBigrams(new FSArray(aJCas,numWords-1));
     if (numWords>1) {question.setTrigrams(new FSArray(aJCas,numWords-2));}
     else {question.setTrigrams(new FSArray(aJCas,0));}
     //iterate over ngrams and add them to the question's FSArrays
     for(int i=0;i<ngrams.size();i++) {
       NGram ng = ngrams.get(i);
       //check if ngram is contained within the question
       if (ng.getBegin() >= question.getBegin() && ng.getEnd() <= question.getEnd()) {
         //check ngram type
         if (ng.getElementType() == "unigram") {
           question.setUnigrams(unidex,ng);
           unidex++;
         } else if (ng.getElementType() == "bigram") {
           question.setBigrams(bidex,ng);
           bidex++;
         } else if (ng.getElementType() == "trigram") {
           question.setTrigrams(tridex,ng);
           tridex++;
         }
       }
     }
     question.addToIndexes();
     pos = qMatch.end();
   }
   
 }
}
