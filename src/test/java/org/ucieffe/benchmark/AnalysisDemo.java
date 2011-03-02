package org.ucieffe.benchmark;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;

public class AnalysisDemo {
    private static final String[] strings = {
        "The quick brown fox jumped over the lazy dogs",
        "XY&Z Corporation - xyz@example.com"
    };

    private static final Analyzer[] analyzers = new Analyzer[]{
        new WhitespaceAnalyzer(),
        new SimpleAnalyzer(),
        new StopAnalyzer(Version.LUCENE_30),
        new StandardAnalyzer(Version.LUCENE_30)
    };

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < strings.length; i++) {
            analyze(strings[i]);
        }
    }

    private static void analyze(String text) throws IOException {
        System.out.println("Analzying \"" + text + "\"");
        for (int i = 0; i < analyzers.length; i++) {
            Analyzer analyzer = analyzers[i];
            System.out.println("\t" + analyzer.getClass().getName() + ":");
            System.out.print("\t\t");
            TokenStream stream = analyzer.tokenStream("contents", new StringReader(text));
            OffsetAttribute offsetAttribute = stream.getAttribute(OffsetAttribute.class);
            TermAttribute termAttribute = stream.getAttribute(TermAttribute.class);

            while (stream.incrementToken()) {
                int startOffset = offsetAttribute.startOffset();
                int endOffset = offsetAttribute.endOffset();
                String term = termAttribute.term();

                System.out.print("[" + term + "] ");
            }            
            System.out.println("\n");
        }
    }

}