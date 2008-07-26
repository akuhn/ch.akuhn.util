package magic;

/**
 * Separates elements of a collection while printing it. Does not require
 * special case treatment of first or last element. For example, the
 * following program prints a list of its arguments separated by commas,
 * without using any conditionals.
 * 
 * <pre>Separator s = new Separator(", ");
 *for (String a : args) {
 *    System.out.println(s + a);
 *}</pre>
 *
 * The implementation of class <code>Separator</code> is straight forward.
 * It wraps a string that is returned on every call of toString() except
 * for the first call, which returns an empty string.
 * 
 * @author Yossil Gil
 * 
 */
public class Separator {

    private final String value;
    private boolean omitNext;
    
    public Separator() {
        this(", ");
    }
    
    public Separator(String value) {
        this.value = value;
        this.omitNext = true;
    }
    
    public String toString() {
        String $ = omitNext ? "" : value;
        omitNext = false;
        return $;
    }
    
    public void reset() {
        omitNext = true;
    }
    
}