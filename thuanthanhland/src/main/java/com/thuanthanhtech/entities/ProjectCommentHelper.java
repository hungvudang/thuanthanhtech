package com.thuanthanhtech.entities;

import java.util.ArrayList;
import java.util.List;

public class ProjectCommentHelper {

	public static void getProjectCommentTree(List<ProjectComment> projectComments, Item<ProjectComment> root) {
		List<Boolean> visited = new ArrayList<Boolean>();
		projectComments.stream().forEach((pComment)->{
			visited.add(false);
		});
		
		root.setId(null);
		root.setSelf(null);
		root.setChilds(new ArrayList<Item<ProjectComment>>());
		
		for (int i = 0; i< projectComments.size(); i++) {
			ProjectComment pComment = projectComments.get(i);
			if (!visited.get(i) && pComment.getParentId() == -1) {
				
				visited.set(i, true);
				Item<ProjectComment> child = new Item<ProjectComment>();
				child.setId(pComment.getId());
				child.setSelf(pComment);
				child.setChilds(new ArrayList<Item<ProjectComment>>());
				
				root.getChilds().add(child);
				
				recursive(projectComments, visited, child);
			}
		}
		
	}
	
	
	public static void recursive(List<ProjectComment> projectComments, List<Boolean> visited, Item<ProjectComment> root) {
		for (int i = 0; i < projectComments.size(); i++) {
			if (!visited.get(i) && root.getId() == projectComments.get(i).getParentId()) {
				visited.set(i, true);
				Item<ProjectComment> child = new Item<ProjectComment>(projectComments.get(i).getId(), projectComments.get(i), new ArrayList<Item<ProjectComment>>());
				root.getChilds().add(child);
//				recursive(projectComments, visited, child);
			}
		}
	}
}
