
public class DLList {
	private DLNode head = null;
	private DLNode tail = null;
	private int size;
	
	public void add(int value){
		DLNode toAdd = new DLNode(value);
		
		if(head==null){
			this.head=toAdd;
			this.tail=toAdd;
		}
		else{
			toAdd.setPrevious(tail);
			this.tail.setNext(toAdd);
			this.tail=toAdd;
		}
		size++;	
	}
	
	public void add(int index, int value){
		DLNode toAdd = new DLNode(value);
		DLNode current = head;
		if(head==null){
			this.head=toAdd;
			this.tail=toAdd;
		}
		for(int i=0;i<index;i++){
				current=current.getNext();
		}
		size++;
		toAdd.setNext(current);
		toAdd.setPrevious(current.getPrevious());
		current.setPrevious(toAdd);
		toAdd.getPrevious().setNext(toAdd);
	}
	
	public void clear(){
		this.head=null;
		this.tail=null;
	}
	
	public boolean contains(int value){
		Boolean hasValue = false;
		DLNode current=head;
		for(int i=0;i<size;i++){
			if(current.getValue()==value){
				hasValue=true;
				break;
			}
			current=current.getNext();
		}
		return hasValue;
	}
	
	public boolean equals(Object o){
		boolean isEqual=true;
		
		if(!(o instanceof DLList))
			return false;
		else{
			DLList toTest=(DLList)o;
			if(!(toTest.size==this.size)){
				return false;
			}
			DLNode node1=this.head;
			DLNode node2=toTest.head;
			for(int i=0;i<this.size;i++){
				if(node1.getValue()==node2.getValue()){
					node1=node1.getNext();
					node2=node2.getNext();
				}
				else
					return false;
			}
			return true;
		}
	}
	
	public int get(int index){
		DLNode current = this.head;
		for(int i=0;i<index;i++){
				current=current.getNext();
		}
		return current.getValue();
	}
	
	public int indexOf(int value){
		DLNode current = this.head;
		for(int i=0;i<size;i++){
			if(current.getValue()==value)
				return i;
			current = current.getNext();
		}
		return -1;
	}

	public boolean isEmpty(){
		if(this.head==null)
		return false;
		return true;
	}
	
	public int lastIndexOf(int value){
		DLNode current = tail;
		for(int i=size;i>-1;i--){
			if(current.getValue()==value)
				return i;
			current=current.getPrevious();
		}
		return -1;
	}
	
	public boolean remove(int index){
		DLNode current = this.head;
		for(int i=0;i<size;i++){
			if(i==index){
				current.getPrevious().setNext(current.getPrevious());
				current.getNext().setPrevious(current.getNext());
				size--;
				return true;
			}
			current=current.getNext();
		}
		return false;
	}
	
	public boolean removeV(int value){
		DLNode current=this.head;
		for(int i=0;i<size;i++){
			if(current.getValue()==value){
				current.getPrevious().setNext(current.getPrevious());
				current.getNext().setPrevious(current.getNext());
				size--;
				return true;
			}
			current=current.getNext();
		}
		return false;
	}
	
	public boolean set(int index, int value){
		DLNode current = this.head;
		DLNode newNode = new DLNode(value);
		for(int i=0;i<size;i++){
			if(i==index){
				current.getPrevious().setNext(newNode);
				current.getNext().setPrevious(newNode);
				newNode.setNext(current.getNext());
				newNode.setPrevious(current.getPrevious());
			}
			current=current.getNext();
		}
		return false;
	}
	
	public int size(){
		return size;
	}
	
	public String toString() {
		String toReturn = "[";
		DLNode current = head;
		while(current != null){
			toReturn += current.getValue();
			toReturn += " ";
			current = current.getNext();
		}
		toReturn += "]";
		return toReturn;
	}

}
