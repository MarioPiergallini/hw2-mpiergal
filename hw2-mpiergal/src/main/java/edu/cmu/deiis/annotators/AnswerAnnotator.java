/* AnswerAnnotator.java
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

//Annotator that identifies answers using Java regular expressions

public class AnswerAnnotator extends JCasAnnotator_ImplBase {
//create regular expression pattern for questions
 private Pattern answerPattern = Pattern.compile("(?<=\\n)A [01] [\\w' ]+");

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
   Matcher ansMatch = answerPattern.matcher(docText);
   
   //iterate over answer matches in text
   int pos = 0;
   while (ansMatch.find(pos)) {
     //found an answer, create answer annotation
     Answer answer = new Answer(aJCas);
     answer.setBegin(ansMatch.start());
     answer.setEnd(ansMatch.end()+1);
     if (ansMatch.group().charAt(2)=='0') {
       answer.setIsCorrect(false);
     } else if (ansMatch.group().charAt(2)=='1') {
       answer.setIsCorrect(true);
     }
     int numWords = ansMatch.group().split(" ").length - 2;
     answer.setUnigrams(new FSArray(aJCas,numWords));
     answer.setBigrams(new FSArray(aJCas,numWords-1));
     if (numWords>1) {answer.setTrigrams(new FSArray(aJCas,numWords-2));}
     else {answer.setTrigrams(new FSArray(aJCas,0));}
     //set indices for uni-,bi-,trigrams for adding them to FSArrays
     int unidex = 0;
     int bidex = 0;
     int tridex = 0;
     //iterate over ngrams and add them to the question's FSArrays
     for(int i=0;i<ngrams.size();i++) {
       NGram ng = ngrams.get(i);
       //check if ngram is contained within the question
       if (ng.getBegin() >= answer.getBegin() && ng.getEnd() <= answer.getEnd()) {
         //check ngram type
         if (ng.getElementType() == "unigram") {
           answer.setUnigrams(unidex,ng);
           unidex++;
         } else if (ng.getElementType() == "bigram") {
           answer.setBigrams(bidex,ng);
           bidex++;
         } else if (ng.getElementType() == "trigram") {
           answer.setTrigrams(tridex,ng);
           tridex++;
         }
       }
     }
     answer.addToIndexes();
     pos = ansMatch.end();
   }
   
 }
}
