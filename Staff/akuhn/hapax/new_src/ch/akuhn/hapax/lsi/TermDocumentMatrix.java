package ch.akuhn.hapax.lsi;

import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.corpus.Index;
import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.hapax.linalg.SparseMatrix;
import ch.akuhn.util.Bag.Count;

public class TermDocumentMatrix
        extends SparseMatrix {

    private Index<Document> documents; // columns
    private Index<CharSequence> terms; // rows

    public TermDocumentMatrix() {
        super(0, 0);
        this.terms = new Index<CharSequence>();
        this.documents = new Index<Document>();
    }

    public int addDocument(Document document) {
        int index = documents.add(document);
        if (index == columnSize()) addColumn();
        return index;
    }

    @Override
    public String toString() {
        return String.format("TermDocumentMatrix (%d terms, %d documents)",
                rowSize(), columnSize());
    }

    public int addTerm(CharSequence term) {
        int index = terms.add(term);
        if (index == rowSize()) addRow();
        return index;
    }

    public void addCorpus(Corpus corpus) {
        for (Document document : corpus.documents()) {
            int column = addDocument(document);
            Terms terms = corpus.get(document);
            for (Count<CharSequence> each : terms.counts()) {
                int row = addTerm(each.element);
                add(row, column, each.count);
            }
        }
    }

}
