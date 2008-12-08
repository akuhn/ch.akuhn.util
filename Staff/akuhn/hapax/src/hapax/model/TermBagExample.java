package hapax.model;

import static org.junit.Assert.assertEquals;

import jexample.Depends;
import jexample.JExample;

import ch.akuhn.util.Strings;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.akuhn.fame.*;

@RunWith(JExample.class)
public class TermBagExample {

    private static final String EMPTY_DOCUMENT = "( (TEST.Document (id: 1)))";
    private static final String SOME_DOCUMENT = "( (TEST.Document (id: 1) (terms 2 'be' 'to' 1 'or' 'not')))";

    @Test
    public Tower tower() {
        Tower t = new Tower();
        t.metamodel.with(Document.class);
        assert t.metamodel.allPackageDescriptions().size() == 1;
        assert t.metamodel.allClassDescriptions().size() == 1;
        assert t.metamodel.allPropertyDescriptions().size() == 2;
        return t;
    }

    @Test
    @Depends("tower")
    public Repository emptyModel(Tower t) {
        return t.model;
    }

    @Test
    public Document emptyDocument() {
        return new Document();
    }

    @Test
    @Depends("emptyModel;emptyDocument")
    public Repository modelWithEmptyDocument(Repository m, Document d) {
        m.add(d);
        assert m.size() == 1;
        return m;
    }

    @Test
    @Depends("modelWithEmptyDocument")
    public void exportModelWithEmptyDocument(Repository r) {
        String s = r.exportMSE();
        assertEquals(EMPTY_DOCUMENT, normalizeWhitespace(s));
    }

    @Test
    public Document someDocument() {
        Document d = new Document();
        for (String each : Strings.letters("to be or not to be")) {
            d.terms.add(each);
        }
        assert d.terms.size() == 6;
        assert d.terms.occurrences("be") == 2;
        assert d.terms.occurrences("to") == 2;
        assert d.terms.occurrences("or") == 1;
        assert d.terms.occurrences("not") == 1;
        return d;
    }

    @Test
    @Depends("emptyModel;someDocument")
    public Repository modelWithSomeDocument(Repository m, Document d) {
        m.add(d);
        assert m.size() == 1;
        return m;
    }

    @Test
    @Depends("modelWithSomeDocument")
    public void exportModelWithSomeDocument(Repository r) {
        // TODO fragile test, order of terms may differ if hash key of interned
        // strings diffs or if Bag impl diffs
        // String s = r.exportMSE();
        // assertEquals(SOME_DOCUMENT, normalizeWhitespace(s));
    }

    @Test
    @Depends("tower")
    public Repository importModelWithSomeDocument(Tower t) {
        assert t.model.size() == 0;
        t.model.importMSE(SOME_DOCUMENT);
        assert t.model.size() == 1;
        Document d = (Document) t.model.getElements().iterator().next();
        assert d.terms.size() == 6;
        assert d.terms.occurrences("be") == 2;
        assert d.terms.occurrences("to") == 2;
        assert d.terms.occurrences("or") == 1;
        assert d.terms.occurrences("not") == 1;
        return t.model;
    }

    private static String normalizeWhitespace(String s) {
        StringBuilder $ = new StringBuilder();
        boolean wasWhitespace = false;
        for (int n = 0; n < s.length(); n++) {
            char ch = s.charAt(n);
            boolean isWhitespace = Character.isWhitespace(ch);
            if (!(isWhitespace && wasWhitespace)) $.append(isWhitespace ? ' ' : ch);
            wasWhitespace = isWhitespace;
        }
        return $.toString();
    }

}
