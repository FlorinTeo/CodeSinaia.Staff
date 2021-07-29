
public abstract class Phone {
    private String _os;
    private int _memory;
    
    public Phone(String os, int memory) {
        _os = os;
        _memory = memory;
    }
    
    public String toString() {
        return String.format("%s phone [%dGb]", _os, _memory);
    }
}
