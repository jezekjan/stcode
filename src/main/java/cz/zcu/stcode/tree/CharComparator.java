package cz.zcu.stcode.tree;

import java.util.Comparator;

public class CharComparator implements Comparator<Node> {

	@Override
	public int compare(Node n1, Node n2) {
	
		return n1.toString().compareTo(n2.toString());
	}

}
