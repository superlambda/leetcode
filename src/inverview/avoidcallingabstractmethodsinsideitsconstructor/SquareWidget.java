package inverview.avoidcallingabstractmethodsinsideitsconstructor;

public class SquareWidget extends Widget {
    private final int size;
	
    public SquareWidget(int size) {
        this.size = size;
    }

    @Override
    protected int width() {
        return size;
    }

    @Override
    protected int height() {
        return size;
    }
    
    public static void main(String[] args) {
    	SquareWidget sw=new SquareWidget(5);
    	System.out.println("cachedWidth: " + sw.getCachedHeight());
    }

}
