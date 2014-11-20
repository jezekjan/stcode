package cz.zcu.stcode.tree;

import java.util.TreeSet;

public class Node {

	char c;	
	
	TreeSet<Node> childs = new TreeSet<Node>(new CharComparator());	
	
	
	public Node(char c) {
		this.c = c;
	}
	
	public Character getChar(){
		return c;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		Node n = (Node)obj;
		return c==n.getChar().charValue();
	}
	
	public TreeSet<Node> getChilds(){
		return childs;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new Character(c).toString();
	}
	
	
	
}
