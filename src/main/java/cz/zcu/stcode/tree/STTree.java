package cz.zcu.stcode.tree;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

public class STTree {
	static int p = 0;

	String ln = "\n";
	LinkedList<Node> qeue = new LinkedList<Node>();	
	
	String s = "";
	
	TreeSet<Node> tree = new TreeSet<Node>(
			new CharComparator());

	char[] code;

	public STTree() {

	}

	public void insertCode(String code) {
		//System.out.println("inserting "+code);
		this.code = code.toCharArray();
		insert(0, tree);

	}

	private void insert(int i, TreeSet<Node> set) {

		if (!set.contains(new Node(code[i]))) {
			insertAll(i, set);
		} else {
			if (i >= code.length-1) {
				return;
			}
			insert(i+1, set.ceiling(new Node(code[i])).getChilds());
		}
	}

	private void insertAll(int i, TreeSet<Node> map){
		for (int j = i; j< code.length;j++){
			Node n = new Node(code[j]);
			p++;
			s = s + code[j];
			//System.out.println("level: "+j+" adding "+ code[j]);
			map.add(n);
			map = n.getChilds();
		}			
	}
		
	/**
	 * Lever order traversal
	 * @param writer
	 * @throws IOException 
	 */
	public void  printLevelOrder(Writer writer) throws IOException{
		ln = (String.valueOf(tree.size()));
		for (Node n : tree){			
			qeue.add(n);
		}
		int i =0;
		while (!qeue.isEmpty()){
			Node n = qeue.getFirst();
			qeue.removeFirst();
			writer.write(n.toString());
			i++;
			if (i > 100) { 
				writer.write("\n");  i = 0;}
			if (n.getChilds().size() > 0){
				qeue.addAll(n.getChilds());
				ln = ln + String.valueOf("\n"+n.getChilds().size());
			} 
		}	
		writer.write(ln);
		//writer.close();
	}
			
	
	public TreeSet<Node> getTree(){
		return (TreeSet<Node>)tree.clone();
	}	
	
	/**
	 * Generates a string for latex visualisation
	 *  
	 */
	public String getLaTex(Node n){
		String tree =  "\\pstree[levelsep=35pt]{\\Tcircle{"+n.getChar()+"}}{ \n";
		Set<Node> childs = n.getChilds();		
		for (Node ch : childs){
			tree = tree + getLaTex(ch) +" \n";
		}
		tree = tree + "}";
		return tree;
	}
}