package com.thuanthanhtech.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import com.thuanthanhtech.repositories.CategoryRepository;
import com.thuanthanhtech.repositories.RecruitmentRepository;

public class RecruitmentHelper {
	
	public static boolean updateRecruitmentEntity(Integer id, Recruitment recruitment, RecruitmentRepository rRepository) {
		Optional<Recruitment> opRecruitment = rRepository.findById(id);
		if (opRecruitment.isPresent()) {
			Recruitment nRecruitment = opRecruitment.get();
			nRecruitment.setPosition(recruitment.getPosition());
			nRecruitment.setTitle(recruitment.getTitle());
			nRecruitment.setSlug(recruitment.getSlug());
			nRecruitment.setDeadline(recruitment.getDeadline());
			nRecruitment.setQuantity(recruitment.getQuantity());
			nRecruitment.setPub(recruitment.getPub());
			nRecruitment.setHot(recruitment.getHot());
			nRecruitment.setDescription(recruitment.getDescription());
			
			rRepository.saveAndFlush(nRecruitment);
			return true;
		}
		return false;
	}
	
	
	public static List<Category> getChilds(CategoryRepository cRepository, String recruitmentSlug) {
		List<Category> childs = new ArrayList<Category>();
		
		List<Category> categories = cRepository.findByPub(1, Sort.by(Sort.Direction.ASC, "parentId", "sort"));
		Item<Category> root = new Item<Category>();
		Category cate = new Category();
		cate.setId(0);
		cate.setName("Danh mục gốc");
		CategoryHelper.getCategoryTree(categories, root, cate);
		
		root.getChilds().stream().filter((item)->{
			return (item.getSelf().getSlug().equals(recruitmentSlug));
		}).forEach((item)->{
			item.getChilds().stream().forEach((it) -> {
				childs.add(it.getSelf());
			});
		});
		
		return childs;
	}
}
