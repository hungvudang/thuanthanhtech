package com.thuanthanhtech.entities;

import java.util.Optional;

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
}
