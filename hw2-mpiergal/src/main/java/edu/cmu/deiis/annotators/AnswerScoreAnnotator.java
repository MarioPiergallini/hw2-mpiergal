/* AnswerAnnotator.java
 *  @author Mario Piergallini
 */

package edu.cmu.deiis.annotators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import edu.cmu.deiis.types.*;

//Annotator that scores answers by comparing the Q and A NGrams

public class AnswerScoreAnnotator extends JCasAnnotator_ImplBase {
 
 private Pattern negPattern = Pattern.compile("n't\\b|\\bnot?\\b|\\bnever|no(where|thing|body)");
  
 public void process(JCas aJCas) {
   
   System.out.println("Answer score annotator called");
   
   //load question
   FSIndex qIndex = aJCas.getAnnotationIndex(Question.type);
   Iterator qIter = qIndex.iterator();
   Question question = null;
   while (qIter.hasNext()) {
     question = (Question)qIter.next();
     System.out.println("Question: " + question.getCoveredText());
   }
   
   //iterate over answers
   FSIndex ansIndex = aJCas.getAnnotationIndex(Answer.type);
   Iterator ansIter = ansIndex.iterator();
   int aCount = 0; 
   while (ansIter.hasNext()) {
     Answer ans = (Answer)ansIter.next();
     //set the begin and end of the score to be the same as the answer
     AnswerScore score = new AnswerScore(aJCas);
     score.setAnswer(ans);
     score.setBegin(ans.getBegin());
     score.setEnd(ans.getEnd());
     aCount++;
     System.out.println("Answer " + aCount + ": " + ans.getCoveredText());
     
     //set unigram/matches counts
     int numQUni = question.getUnigrams().size();
     int uniMatches = 0;
     for(int i=0;i<numQUni;i++){
       //check answer's unigrams for matches
       for(int j=0;j<ans.getUnigrams().size();j++){
         NGram qUni = question.getUnigrams(i);
         NGram ansUni = ans.getUnigrams(j);
         if (ansUni.getCoveredText().equals(qUni.getCoveredText())){
           System.out.println("UniMatch: " + ansUni.getCoveredText());
           uniMatches++;
         }
       }
     }
     //set bigram/matches counts
     int numQBi = question.getBigrams().size();
     int biMatches = 0;
     for(int i=0;i<numQBi;i++){
       //check answer's bigrams for matches
       for(int j=0;j<ans.getBigrams().size();j++){
         NGram qBi = question.getBigrams(i);
         NGram ansBi = ans.getBigrams(j);
         if (ansBi.getCoveredText().equals(qBi.getCoveredText())){
           System.out.println("BiMatch: " + ansBi.getCoveredText());
           biMatches++;
         }
       }
     }
     //set trigram/matches counts
     int numQTri = question.getTrigrams().size();
     int triMatches = 0;
     for(int i=0;i<numQTri;i++){
       //check answer's bigrams for matches
       for(int j=0;j<ans.getTrigrams().size();j++){
         NGram qTri = question.getTrigrams(i);
         NGram ansTri = ans.getTrigrams(j);
         if (ansTri.getCoveredText().equals(qTri.getCoveredText())){
           System.out.println("TriMatch: " + ansTri.getCoveredText());
           triMatches++;
         }
       }
     }
     int numQGrams = numQUni + numQBi + numQTri;
     int allMatches = uniMatches + biMatches + triMatches;
     double negPenalty = 1.0;
     
     String ansText = ans.getCoveredText();
     // make matcher for negation
     Matcher negMatch = negPattern.matcher(ansText);
     //check if the answer is a negative sentence and penalize it if so
     if (negMatch.find()){
       negPenalty = 0.25;
       System.out.println("Penalty! Negative sentence.");
     }
     
     double s = negPenalty*(double)allMatches/(double)numQGrams;
     System.out.println("Unweighted Score: " + (s*100) + "% out of " + numQGrams);
     System.out.println();
     
     score.setScore(s);
     score.addToIndexes();
   }
   
 }
}
