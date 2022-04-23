
public class Node {
	int x;
	int y;
	public Node(int a, int b) {
		this.x = a;
		this.y = b;
	}
	
	public boolean equals(Node a) {
		return (a.x==this.x && a.y==this.y);
	}
	
	@Override
	public String toString() {
		return("(" + this.x + "," + this.y + ")");
	}
}
