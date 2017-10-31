package ngram;

import java.util.LinkedList;

public class getNgram {
	
	public static LinkedList<String> bld (String str, int n){
        try{
            NGramExtractor extractor = new NGramExtractor();
            extractor.extract(str, n, false, true);
            LinkedList<String> ngrams = extractor.getNGrams();
            return ngrams;
            //for (String s : ngrams){
            	//int x = extractor.getNGramFrequency(s);
                //System.out.println("Ngram '" + s + "' occurs " + x + " times");
            //}
        }
        catch (Exception e){
            System.err.println(e.toString());
        }
		return null;
    }

}
