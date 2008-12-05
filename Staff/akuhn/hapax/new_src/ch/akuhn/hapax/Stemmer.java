package ch.akuhn.hapax;

public interface Stemmer {

    public static final Stemmer NULL = new Stemmer() {
        @Override
        public CharSequence stem(CharSequence string) {
            return string;
        }
    };

    public CharSequence stem(CharSequence string);
    
}
