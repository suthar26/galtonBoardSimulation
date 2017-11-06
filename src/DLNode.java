
public class DLNode {
	private DLNode next = null;
	private DLNode previous = null;
	private int value;
	
	public DLNode(int value) {
		this.value = value;
	}

	public void setNext(DLNode next) {
		this.next = next;
	}

	public void setPrevious(DLNode previous) {
		this.previous = previous;
	}

	public DLNode getNext() {
		return next;
	}

	public DLNode getPrevious() {
		return previous;
	}

	public int getValue() {
		return value;
	}
	
	public String toString(){
		return "" + value;
		
	}
}
