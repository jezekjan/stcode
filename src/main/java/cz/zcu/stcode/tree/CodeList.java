package cz.zcu.stcode.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import cz.zcu.stcode.coder.Cube;

public class CodeList {

	List<Set<String>> arr ;
	public final int level;
	public CodeList(int level){
		this.level = level;
		arr = new ArrayList<Set<String>>(level);		
		for(int i = 0;  i< level; i++){
			arr.add(i, new TreeSet<String>());
		}
	}
	public void insertCode(String s){
		Cube c = new Cube(s);
		for(int i = level-1;  i>0 ; i--){
			arr.get(i).add(c.getCode());
			c=c.genParentCube();
		}
		
	}
	
	public Set<String> getLevel(int level){
		return arr.get(level);
	}
}
